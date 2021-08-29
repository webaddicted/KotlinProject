package com.webaddicted.kotlinproject.global.blink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Encapsulates the core image scanning.
 * <p/>
 * As of 7/20/12, the flow should be:
 * <p/>
 * 1. CardIOActivity sets up the CardScanner, Preview and Overlay. 2. As each frame is received &
 * processed by the scanner, the scanner notifies the activity of any relevant changes. (e.g. edges
 * detected, scan complete etc.) 3. CardIOActivity passes on the information to the preview and
 * overlay, which can then update themselves as needed. 4. Once a result is reported, CardIOActivty
 * closes the scanner and launches the next activity.
 * <p/>
 * HOWEVER, at the moment, the CardScanner is directly communicating with the Preview.
 */

//TODO: implement autofocus of the camera

class FaceScanner implements Camera.PreviewCallback, Camera.AutoFocusCallback,
        SurfaceHolder.Callback {
    private static final String TAG = FaceScanner.class.getSimpleName();

    private static final float MIN_FOCUS_SCORE = 6; // TODO - parameterize this
    // value based on phone? or
    // change focus behavior?

    private static final int DEFAULT_UNBLUR_DIGITS = -1; // no blur per default

    private static final int CAMERA_CONNECT_TIMEOUT = 5000;
    private static final int CAMERA_CONNECT_RETRY_INTERVAL = 50;

    static final int ORIENTATION_PORTRAIT = 1;

    // these values MUST match those in dmz_constants.h
    static final int CREDIT_CARD_TARGET_WIDTH = 428; // kCreditCardTargetWidth
    static final int CREDIT_CARD_TARGET_HEIGHT = 270; // kCreditCardTargetHeight


    private Bitmap detectedBitmap;

    private static boolean manualFallbackForError;

    // member data
    protected WeakReference<CameraActivity> mScanActivityRef;
    private final boolean mSuppressScan = false;
    private boolean mScanExpiry;
    private final int mUnblurDigits = DEFAULT_UNBLUR_DIGITS;

    // read by CardIOActivity to set up Preview
    final int mPreviewWidth = 640;
    final int mPreviewHeight = 480;


    private boolean mFirstPreviewFrame = true;
    private long mAutoFocusStartedAt;
    private long mAutoFocusCompletedAt;

    private Camera mCamera;
    private byte[] mPreviewBuffer;

    // accessed by test harness subclass.
    protected boolean useCamera = true;

    private boolean isSurfaceValid;


    // ------------------------------------------------------------------------
    // STATIC INITIALIZATION
    // ------------------------------------------------------------------------

    /**
     * Custom loadLibrary method that first tries to load the libraries from the built-in libs
     * directory and if it fails, tries to use the alternative libs path if one is set.
     *
     * No checks are performed to ensure that the native libraries match the cardIO library version.
     * This needs to be handled by the consuming application.
     */
    FaceScanner(CameraActivity scanActivity, int currentFrameOrientation) {
        mScanActivityRef = new WeakReference<>(scanActivity);
    }

    /**
     * Connect or reconnect to camera. If fails, sleeps and tries again. Returns <code>true</code> if successful,
     * <code>false</code> if maxTimeout passes.
     */
//    aaaaaaaaaaaaaaaaaaaaaaaaaaa
    private Camera openFrontFacingCameraGingerbread(boolean isOpenFromCamera) {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {

            if (isOpenFromCamera) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (isOpenFromCamera && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }else {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (!isOpenFromCamera && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }
        }

        return cam;
    }

    private Camera connectToCamera(int checkInterval, int maxTimeout, boolean isOpenFromCamera) {
        long start = System.currentTimeMillis();
        if (useCamera) {
            do {
                try {
                    // Camera.open() will open the back-facing camera. Front cameras are not
                    // attempted.
                    return openFrontFacingCameraGingerbread(isOpenFromCamera);
                } catch (RuntimeException e) {
                    try {
                        Log.w(TAG,
                                "Wasn't able to connect to camera service. Waiting and trying again...");
                        Thread.sleep(checkInterval);
                    } catch (InterruptedException e1) {
                        Log.e(TAG, "Interrupted while waiting for camera", e1);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Unexpected exception. Please report it as a GitHub issue", e);
                    maxTimeout = 0;
                }

            } while (System.currentTimeMillis() - start < maxTimeout);
        }
        Log.w(TAG, "camera connect timeout");
        return null;
    }

    void prepareScanner(boolean isOpenFrontCamera) {
        mFirstPreviewFrame = true;
        mAutoFocusStartedAt = 0;
        mAutoFocusCompletedAt = 0;


        if (useCamera && mCamera == null) {
            mCamera = connectToCamera(CAMERA_CONNECT_RETRY_INTERVAL, CAMERA_CONNECT_TIMEOUT, isOpenFrontCamera);
            if (mCamera == null) {
                Log.e(TAG, "prepare scanner couldn't connect to camera!");
                return;
            }
//aaaaaaaaaaaaaaaaa
            setCameraDisplayOrientation(mCamera);

            Parameters parameters = mCamera.getParameters();

            List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            if (supportedPreviewSizes != null) {
                Size previewSize = null;
                for (Size s : supportedPreviewSizes) {
                    if (s.width == 640 && s.height == 480) {
                        previewSize = s;
                        break;
                    }
                }
                if (previewSize == null) {
                    Log.w(TAG,
                            "Didn't find a supported 640x480 resolution, so forcing");

                    previewSize = supportedPreviewSizes.get(0);

                    previewSize.width = mPreviewWidth;
                    previewSize.height = mPreviewHeight;
                }
            }

            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);

            mCamera.setParameters(parameters);
        } else if (!useCamera) {
            Log.w(TAG, "useCamera is false!");
        } else if (mCamera != null) {
            Log.v(TAG, "we already have a camera instance: " + mCamera);
        }
        if (detectedBitmap == null) {
            detectedBitmap = Bitmap.createBitmap(CREDIT_CARD_TARGET_WIDTH,
                    CREDIT_CARD_TARGET_HEIGHT, Bitmap.Config.ARGB_8888);
        }
    }

    public void pauseScanning() {
        setFlashOn(false);
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                Log.w(TAG, "can't stop preview display", e);
            }
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mPreviewBuffer = null;
            mCamera = null;
        }
    }

    public void endScanning() {
        if (mCamera != null) {
            pauseScanning();
        }

        mPreviewBuffer = null;
    }

    /*
     * --------------------------- SurfaceHolder callbacks
     */
    private boolean makePreviewGo(SurfaceHolder holder) {
        // method name from http://www.youtube.com/watch?v=-WmGvYDLsj4
        assert holder != null;
        assert holder.getSurface() != null;
        mFirstPreviewFrame = true;

        if (useCamera) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.e(TAG, "can't set preview display", e);
                return false;
            }
            try {
                mCamera.startPreview();
                mCamera.autoFocus(this);
            } catch (RuntimeException e) {
                Log.e(TAG, "startPreview failed on camera. Error: ", e);
                return false;
            }
        }
        return true;
    }

    /*
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder )
     */

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where to draw.
        if (mCamera != null || !useCamera) {
            isSurfaceValid = true;
            makePreviewGo(holder);
        } else {
            Log.wtf(TAG, "CardScanner.surfaceCreated() - camera is null!");
            return;
        }
    }

    /*
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder , int,
     * int, int)
     */

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, String.format("Preview.surfaceChanged(holder?:%b, f:%d, w:%d, h:%d )",
                (holder != null), format, width, height));
    }

    /*
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view. SurfaceHolder)
     */

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, "error stopping camera", e);
            }
        }
        isSurfaceValid = false;
    }

    /**
     * Handles processing of each frame.
     * <p/>
     * This method is called by Android, never directly by application code.
     */
    private static boolean processingInProgress = false;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (data == null) {
            Log.w(TAG, "frame is null! skipping");
            return;
        }

        if (processingInProgress) {
            Log.e(TAG, "processing in progress.... dropping frame");
            // return frame buffer to pool
            if (camera != null) {
                //camera.addCallbackBuffer(data);
            }
            return;
        }

        processingInProgress = true;

        // TODO: eliminate this foolishness and measure/layout properly.
        if (mFirstPreviewFrame) {
            mFirstPreviewFrame = false;
        }

        Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

        byte[] bytes = out.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 300, false);

        Log.d("time", SystemClock.uptimeMillis()+"");

        long time1 = SystemClock.currentThreadTimeMillis();

        CameraActivity.setBitMapImage(bitmap);

        Log.d("time", (SystemClock.currentThreadTimeMillis() - time1)+"");

        if (camera != null) {
            camera.addCallbackBuffer(data);
        }
        processingInProgress = false;

    }


    // ------------------------------------------------------------------------
    // CAMERA CONTROL & CALLBACKS
    // ------------------------------------------------------------------------

    /**
     * Invoked when autoFocus is complete
     * <p/>
     * This method is called by Android, never directly by application code.
     */
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        mAutoFocusCompletedAt = System.currentTimeMillis();
    }

    /**
     * True if autoFocus is in progress
     */
    boolean isAutoFocusing() {
        return mAutoFocusCompletedAt < mAutoFocusStartedAt;
    }

    void toggleFlash() {
        setFlashOn(!isFlashOn());
    }

    // ------------------------------------------------------------------------
    // MISC CAMERA CONTROL
    // ------------------------------------------------------------------------

    /**
     * Tell Preview's camera to trigger autofocus.
     *
     * @param isManual callback for when autofocus is complete
     */
    void triggerAutoFocus(boolean isManual) {
        if (useCamera && !isAutoFocusing()) {
            try {
                mAutoFocusStartedAt = System.currentTimeMillis();
                mCamera.autoFocus(this);
            } catch (RuntimeException e) {
                Log.w(TAG, "could not trigger auto focus: " + e);
            }
        }
    }

    /**
     * Check if the flash is on.
     *
     * @return state of the flash.
     */

    public boolean isFlashOn() {
        if (!useCamera) {
            return false;
        }
        Parameters params = mCamera.getParameters();
        return params.getFlashMode().equals(Parameters.FLASH_MODE_TORCH);
    }

    /**
     * Set the flash on or off
     *
     * @param b desired flash state
     * @return <code>true</code> if successful
     */

    public boolean setFlashOn(boolean b) {
        if (mCamera != null) {
            try {
                Parameters params = mCamera.getParameters();
                params.setFlashMode(b ? Parameters.FLASH_MODE_TORCH : Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(params);

                return true;
            } catch (RuntimeException e) {
                Log.w(TAG, "Could not set flash mode: " + e);
            }
        }
        return false;
    }
    boolean resumeScanning(SurfaceHolder holder, boolean isOpenFromCamera) {
        if (mCamera == null) {
            prepareScanner(isOpenFromCamera);
        }
        if (useCamera && mCamera == null) {
            // prepare failed!
            Log.i(TAG, "null camera. failure");
            return false;
        }

        assert holder != null;

        if (useCamera && mPreviewBuffer == null) {
            Parameters parameters = mCamera.getParameters();
            int previewFormat = parameters.getPreviewFormat();
            int bytesPerPixel = ImageFormat.getBitsPerPixel(previewFormat) / 8;
            int bufferSize = mPreviewWidth * mPreviewHeight * bytesPerPixel * 3;

            mPreviewBuffer = new byte[bufferSize];
            mCamera.addCallbackBuffer(mPreviewBuffer);
        }

        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (useCamera) {
            mCamera.setPreviewCallbackWithBuffer(this);
        }

        if (isSurfaceValid) {
            makePreviewGo(holder);
        }

        // Turn flash off
        setFlashOn(false);

        return true;
    }


    private void setCameraDisplayOrientation(Camera mCamera) {
        int result;

        /* check API level. If upper API level 21, re-calculate orientation. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Camera.CameraInfo info =
                    new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);
            int degrees = getRotationalOffset();
            int cameraOrientation = info.orientation;
            result = (cameraOrientation - degrees + 360) % 360;
        } else {
            /* if API level is lower than 21, use the default value */
            result = 90;
        }

        /*set display orientation*/
        mCamera.setDisplayOrientation(result);
    }

    /**
     * @see <a
     * href="http://stackoverflow.com/questions/12216148/android-screen-orientation-differs-between-devices">SO
     * post</a>
     */
    int getRotationalOffset() {
        final int rotationOffset;
        // Check "normal" screen orientation and adjust accordingly
        int naturalOrientation = ((WindowManager) mScanActivityRef.get().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();
        if (naturalOrientation == Surface.ROTATION_0) {
            rotationOffset = 0;
        } else if (naturalOrientation == Surface.ROTATION_90) {
            rotationOffset = 90;
        } else if (naturalOrientation == Surface.ROTATION_180) {
            rotationOffset = 180;
        } else if (naturalOrientation == Surface.ROTATION_270) {
            rotationOffset = 270;
        } else {
            // just hope for the best (shouldn't happen)
            rotationOffset = 0;
        }
        return rotationOffset;
    }
}
