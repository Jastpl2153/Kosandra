package com.example.kosandra.ui.general_logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Represents a custom dialog box for confirmation with a title, message, and buttons.
 * <p>
 * Extends the AlertDialog class.
 */
public class ConfirmationDialog extends AlertDialog {
    /**
     * Constructs a new ConfirmationDialog with the given parameters.
     *
     * @param context  the context in which the dialog should be shown
     * @param title    the title of the dialog
     * @param message  the message to be displayed in the dialog
     * @param listener the OnClickListener to handle button clicks
     */
    public ConfirmationDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        super(context);
        setTitle(title);
        setMessage(message);
        setButton(BUTTON_POSITIVE, "Да", listener);
        setButton(BUTTON_NEGATIVE, "Отмена", listener);
    }
}