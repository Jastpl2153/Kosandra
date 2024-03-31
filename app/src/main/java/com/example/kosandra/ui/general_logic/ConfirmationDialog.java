package com.example.kosandra.ui.general_logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationDialog extends AlertDialog {
    public ConfirmationDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        super(context);
        setTitle(title);
        setMessage(message);
        setButton(BUTTON_POSITIVE, "Да", listener);
        setButton(BUTTON_NEGATIVE, "Отмена", listener);
    }
}