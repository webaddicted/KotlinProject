package com.webaddicted.kotlinproject.global.blink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.webaddicted.kotlinproject.global.common.AppApplication;
import com.webaddicted.kotlinproject.global.common.Lg;

/**
 * Created by Paul on 11/4/15.
 */

public class FaceOverlayView extends View {
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    private double leftEyeOpenProbability = -1.0;
    private double rightEyeOpenProbability = -1.0;
    private double leftopenRatio = 1;
//    private static int blinkCount = 0;

    private final FaceDetector detector = new FaceDetector.Builder(getContext())
            .setTrackingEnabled(false)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
            .build();

    public FaceOverlayView(Context context) {
        this(context, null);
    }

    public FaceOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean isBlinkEye = false;
    boolean isFrameInProcess = false;

    public void setBitmap(Bitmap bitmap, boolean isOpenFromCamera) {
        mBitmap = bitmap;

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            //Log.d("time1", SystemClock.currentThreadTimeMillis()+"");
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
        }
        if (!isOpenFromCamera && !isFrameInProcess && mFaces!=null &&mFaces.size() == 1) {
            Face face = mFaces.valueAt(0);
            Bitmap myBitmap = bitmap;
            isFrameInProcess = true;
            if (isEyeOpenDL(face)) {
                Log.d("TAG", "output -> " + mFaces.size());
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(AppApplication.context).build();
                if (!txtRecognizer.isOperational()) {
                    isFrameInProcess = false;
                    // Shows if your Google Play services is not up to date or OCR is not supported for the device
                    Log.d("TAG", "OCR output setBitmap: Detector dependencies are not yet available");
//                    txtView.setText("Detector dependencies are not yet available");
                } else {
                    // Set the bitmap taken to the frame to perform OCR Operations.
                    Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append("/");
                        // The following Process is used to show how to use lines & elements as well
                        for (int j = 0; j < items.size(); j++) {
                            TextBlock itemb = (TextBlock) items.valueAt(j);
                            strBuilder.append(itemb.getValue());
                            strBuilder.append("/");
                            for (Text line : itemb.getComponents()) {
                                //extract scanned text lines here
                                Log.d("lines", line.getValue());
                                for (Text element : line.getComponents()) {
                                    //extract scanned text words here
                                    Log.d("element", element.getValue());
                                }
                            }
                        }
                    }
                    Log.d("TAG", "OCR output setBitmap:   " + strBuilder.toString());
//                    if (strBuilder.toString().contains("DRIVER") && strBuilder.toString().contains("LICENSE") || (strBuilder.toString().contains("DOB") || strBuilder.toString().contains("EXP")))
                        CameraActivity.showDl(myBitmap);
//                    else isFrameInProcess = false;
                }
            } else isFrameInProcess = false;
            return;
        }


        if (isEyeBlinked())
            isBlinkEye = true;
        else {
            if (isBlinkEye) {
                if (isEyeOpen()) {
                    isBlinkEye = false;
//                    blinkCount++;
//                    CameraActivity.showScore(blinkCount);
                    CameraActivity.showScoreBitmap(mBitmap);
                }
//                CameraActivity.showScoreBitmap(blinkCount, mBitmap );
            }
        }

        invalidate();
    }

    private boolean isEyeBlinked() {

        if (mFaces==null || mFaces.size() == 0)
            return false;

        Face face = mFaces.valueAt(0);
        float currentLeftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float currentRightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        if (currentLeftEyeOpenProbability == -1.0 || currentRightEyeOpenProbability == -1.0)
            return false;


        if (leftEyeOpenProbability > 0.9 || rightEyeOpenProbability > 0.9) {
            boolean blinked = false;
            if (currentLeftEyeOpenProbability < 0.6 || rightEyeOpenProbability < 0.6)
                blinked = true;
            leftEyeOpenProbability = currentLeftEyeOpenProbability;
            rightEyeOpenProbability = currentRightEyeOpenProbability;
            return blinked;
        } else {
            leftEyeOpenProbability = currentLeftEyeOpenProbability;
            rightEyeOpenProbability = currentRightEyeOpenProbability;
            return false;
        }
    }

       private boolean isEyeOpen() {

        if (mFaces.size() == 0)
            return false;

        Face face = mFaces.valueAt(0);
        float currentLeftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float currentRightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        if (currentLeftEyeOpenProbability == -1.0 || currentRightEyeOpenProbability == -1.0)
            return false;
        if (leftEyeOpenProbability > 0.98 && rightEyeOpenProbability > 0.98) {
            boolean blinked = false;
//            if(currentLeftEyeOpenProbability<0.6 || rightEyeOpenProbability< 0.6)blinked = true;
            leftEyeOpenProbability = currentLeftEyeOpenProbability;
            rightEyeOpenProbability = currentRightEyeOpenProbability;
            return true;
        }
        return false;
    }
    private boolean isEyeOpenDL(Face face) {
        float currentLeftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float currentRightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        if (currentLeftEyeOpenProbability == -1.0 || currentRightEyeOpenProbability == -1.0)
            return false;
        if (currentLeftEyeOpenProbability > 0.98 && currentRightEyeOpenProbability > 0.98) {
            leftEyeOpenProbability = currentLeftEyeOpenProbability;
            rightEyeOpenProbability = currentRightEyeOpenProbability;
            return true;
        }
        return false;
    }

    private boolean isEyeToggled() {
        if (mFaces.size() == 0)
            return false;
        Face face = mFaces.valueAt(0);
        float currentLeftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float currentRightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        if (currentLeftEyeOpenProbability == -1.0 || currentRightEyeOpenProbability == -1.0) {
            return false;
        }

        double currentLeftOpenRatio = currentLeftEyeOpenProbability / currentRightEyeOpenProbability;
        if (currentLeftOpenRatio > 3) currentLeftOpenRatio = 3;
        if (currentLeftOpenRatio < 0.33) currentLeftOpenRatio = 0.33;

        Log.d("probs", currentLeftOpenRatio + " " + leftopenRatio);
        if (currentLeftOpenRatio == 0.33 || currentLeftOpenRatio == 3.0) {
            if (leftopenRatio == 1) {
                leftopenRatio = currentLeftOpenRatio;
            }

            if (leftopenRatio * currentLeftOpenRatio == 0.99) {
                leftopenRatio = currentLeftOpenRatio;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceLandmarks(canvas, scale);
        }
    }

    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    private void drawFaceBox(Canvas canvas, double scale) {
        //This should be defined as a member variable rather than
        //being created on each onDraw request, but left here for
        //emphasis.
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            left = (float) (face.getPosition().x * scale);
            top = (float) (face.getPosition().y * scale);
            right = (float) scale * (face.getPosition().x + face.getWidth());
            bottom = (float) scale * (face.getPosition().y + face.getHeight());

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }

    private void drawFaceLandmarks(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            for (Landmark landmark : face.getLandmarks()) {
                int cx = (int) (landmark.getPosition().x * scale);
                int cy = (int) (landmark.getPosition().y * scale);
                canvas.drawCircle(cx, cy, 10, paint);
            }
        }
    }

    private void logFaceData() {
        float smilingProbability;
        float leftEyeOpenProbability;
        float rightEyeOpenProbability;
        float eulerY;
        float eulerZ;
        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);
            smilingProbability = face.getIsSmilingProbability();
            leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
            rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
            eulerY = face.getEulerY();
            eulerZ = face.getEulerZ();
            Log.e("Tuts+ Face Detection", "Smiling: " + smilingProbability);
            Log.d("Tuts+ Face Detection", "Left eye open: " + leftEyeOpenProbability);
            Log.d("Tuts+ Face Detection", "Right eye open: " + rightEyeOpenProbability);
            Log.e("Tuts+ Face Detection", "Euler Y: " + eulerY);
            Log.e("Tuts+ Face Detection", "Euler Z: " + eulerZ);
        }
    }
}

