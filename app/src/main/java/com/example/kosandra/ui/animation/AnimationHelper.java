package com.example.kosandra.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.kosandra.R;

/**
 * A utility class for managing animations on item layouts.
 */
public class AnimationHelper {
    /**
     * Cancels animation on the item layout and resets its properties.
     *
     * @param itemLayout   The view representing the item layout to cancel animation on.
     * @param deleteButton The view representing the delete button associated with the item.
     */
    public static void cancelAnimation(View itemLayout, View deleteButton) {
        itemLayout.animate().translationX(0).setDuration(0).start();
        deleteButton.setVisibility(View.INVISIBLE);
        itemLayout.setBackgroundColor(ContextCompat.getColor(itemLayout.getContext(), android.R.color.white));
    }

    /**
     * Animates the selected state of an item layout.
     *
     * @param itemLayout   The view representing the item layout to animate.
     * @param deleteButton The view representing the delete button associated with the item.
     */
    public static void animateItemSelected(View itemLayout, View deleteButton) {
        int offset = itemLayout.getWidth() / 5;
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(itemLayout, "translationX", 0, -offset);
        setupAnimator(translationXAnimator, true, itemLayout, deleteButton);
        translationXAnimator.start();
    }

    /**
     * Animates the deselected state of an item layout.
     *
     * @param itemLayout   The view representing the item layout to animate.
     * @param deleteButton The view representing the delete button associated with the item.
     */
    public static void animateItemDeselected(View itemLayout, View deleteButton) {
        int offset = deleteButton.getWidth();
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(itemLayout, "translationX", -offset, 0);
        setupAnimator(translationXAnimator, false, itemLayout, deleteButton);
        translationXAnimator.start();
    }

    /**
     * Sets up the animator with duration and listener to handle animation events.
     *
     * @param animator     The ObjectAnimator to set up for the animation.
     * @param isSelected   A flag indicating if the item is selected or deselected.
     * @param itemLayout   The view representing the item layout.
     * @param deleteButton The view representing the delete button associated with the item.
     */
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
