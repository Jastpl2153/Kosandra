package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.kosandra.R;

/**
 * The EmptyFields interface defines methods to validate and handle empty fields in EditText fields.
 * <p>
 * It provides a generic mechanism to check for empty fields and visually indicate them to the user.
 */
public interface EmptyFields {
    /**
     * Validates if there are any empty fields among the EditText fields.
     *
     * @return true if there are empty fields, false otherwise
     */
    boolean validateEmptyFields();

    /**
     * Handles the empty fields by visually indicating the user about the empty fields.
     */
    void handleEmptyFields();

    /**
     * Checks if the provided EditText is empty and animates it to provide a visual cue to the user.
     *
     * @param editText the EditText to check and animate if empty
     */
    default void isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            animateEmptyField(editText, editText.getContext());
        }
    }

    /**
     * Animates the provided EditText to indicate it is empty.
     * If the EditText is an instance of AutoCompleteTextView, it changes the text color to red.
     *
     * @param editText the EditText to animate
     * @param context  the context used to load resources and animations
     */
    default void animateEmptyField(EditText editText, Context context) {
        if (editText instanceof AutoCompleteTextView) {
            editText.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.red));
        editText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }
}
