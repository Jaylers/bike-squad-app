package com.jaylerrs.bikesquad.utility.manager;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by jaylerr on 24-Mar-17.
 */

public class AnimationManager {
    private View view;

    public AnimationManager(View view) {
        this.view = view;
    }

    private void setProfileAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        });

        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
    }
}
