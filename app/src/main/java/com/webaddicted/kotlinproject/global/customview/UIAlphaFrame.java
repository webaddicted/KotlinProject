package com.webaddicted.kotlinproject.global.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import com.webaddicted.kotlinproject.R;

public class UIAlphaFrame {
    private final View center;
//    private final View bottom;
//    private final View top;
//    private final View start;
//    private final View end;
    private final View main;
    private final int duration;
    private int current = Color.WHITE;

    public UIAlphaFrame(View root) {
        center = root.findViewById(R.id.frame_c);
//        bottom = root.findViewById(R.id.frame_b);
//        top    = root.findViewById(R.id.frame_t);
//        start  = root.findViewById(R.id.frame_s);
//        end    = root.findViewById(R.id.frame_e);
        main   = root;
        duration = 1000;
    }

    public UIAlphaFrame(View root, int duration) {
        center = root.findViewById(R.id.frame_c);
        main   = root;
        this.duration = duration;
    }

    public void changeAlphaTo(int color) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), current, color);
        colorAnimation.setDuration(duration);

        colorAnimation.addUpdateListener(animator -> {
            ColorStateList tint = ColorStateList.valueOf((int) animator.getAnimatedValue());
            if (center != null) center.setBackgroundTintList(tint);
//            bottom.setBackgroundTintList(tint);
//            top.setBackgroundTintList(tint);
//            start.setBackgroundTintList(tint);
//            end.setBackgroundTintList(tint);
            main.setBackgroundTintList(tint);
        });

        colorAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                current = color;
            }
        });
        colorAnimation.start();
    }
}
