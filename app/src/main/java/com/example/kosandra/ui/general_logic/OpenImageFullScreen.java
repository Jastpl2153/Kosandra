package com.example.kosandra.ui.general_logic;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;

/**
 * Interface for opening an image in full-screen mode.
 */
public interface OpenImageFullScreen {
    /**
     * Method to open an image in full-screen mode.
     *
     * @param drawable the drawable representing the image to be displayed
     * @param context  the context in which the image should be displayed
     */
    default void openFullScreen(Drawable drawable, Context context) {
        if (drawable != null) {
            showFullScreenImage(drawable, context);
        }
    }

    /**
     * Private method to display the image in full-screen mode.
     *
     * @param drawable the drawable representing the image to be displayed
     * @param context  the context in which the image should be displayed
     */
    private void showFullScreenImage(Drawable drawable, Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_client_photo_card);
        SubsamplingScaleImageView scaleImageView = dialog.findViewById(R.id.photo);

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        scaleImageView.setImage(ImageSource.bitmap(bitmap));
        dialog.show();
    }
}
