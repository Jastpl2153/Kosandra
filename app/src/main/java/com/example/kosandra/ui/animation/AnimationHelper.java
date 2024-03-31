package com.example.kosandra.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.kosandra.R;

public class AnimationHelper {
    public static void cancelAnimation(View itemLayout, View deleteButton) {
        itemLayout.animate().translationX(0).setDuration(0).start();
        deleteButton.setVisibility(View.INVISIBLE);
        itemLayout.setBackgroundColor(ContextCompat.getColor(itemLayout.getContext(), android.R.color.white));
    }

    public static void animateItemSelected(View itemLayout, View deleteButton) {
        int offset = itemLayout.getWidth() / 5;
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(itemLayout, "translationX", 0, -offset);
        setupAnimator(translationXAnimator, true, itemLayout, deleteButton);
        translationXAnimator.start();
    }

    public static void animateItemDeselected(View itemLayout, View deleteButton) {
        int offset = deleteButton.getWidth();
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(itemLayout, "translationX", -offset, 0);
        setupAnimator(translationXAnimator, false, itemLayout, deleteButton);
        translationXAnimator.start();
    }

    private static void setupAnimator(ObjectAnimator animator, boolean isSelected, View itemLayout, View deleteButton) {
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isSelected) {
                    itemLayout.setBackgroundColor(ContextCompat.getColor(itemLayout.getContext(), R.color.blue_200));
                } else {
                    deleteButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSelected) {
                    deleteButton.setVisibility(View.VISIBLE);
                } else {
                    itemLayout.setBackgroundColor(ContextCompat.getColor(itemLayout.getContext(), R.color.white));
                }
            }
        });
    }
}
