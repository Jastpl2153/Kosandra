package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.kosandra.R;

public interface EmptyFields {

    boolean validateEmptyFields();

    void handleEmptyFields();
    default void isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            animateEmptyField(editText, editText.getContext());
        }
    }

    default void animateEmptyField(EditText editText, Context context) {
        if (editText instanceof AutoCompleteTextView){
            editText.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.red));
        editText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }
}
