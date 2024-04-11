package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.kosandra.R;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * This interface, GalleryHandlerInterface, provides methods for setting up a gallery result, opening the gallery, retrieving an image from the gallery,
 * <p>
 * and validating the size of the photo. It includes default implementations for each method.
 */
public interface GalleryHandlerInterface {
    /**
     * Set up the gallery result by registering for activity result to get content from the gallery and display it in the provided image view.
     *
     * @param launcherConsumer The consumer for the activity result launcher.
     * @param context          The context of the application.
     * @param fragment         The fragment where the image view is located.
     * @param image            The view where the image will be displayed.
     */
    default void setupGalleryResult(Consumer<ActivityResultLauncher<String>> launcherConsumer, Context context, Fragment fragment, View image) {
        ActivityResultLauncher<String> getContentLauncher = fragment.registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Glide.with(context)
                                    .load(uri)
                                    .fitCenter()
                                    .override(758, 1138)
                                    .into((ImageView) image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        launcherConsumer.accept(getContentLauncher);
    }

    /**
     * Open the gallery using the provided activity result launcher to select an image.
     *
     * @param getContentLauncher The activity result launcher for getting content from the gallery.
     */
    default void openGallery(ActivityResultLauncher<String> getContentLauncher) {
        getContentLauncher.launch("image/*");
    }

    /**
     * Retrieve a byte array representation of an image from a drawable object with the specified image quality.
     *
     * @param drawable The drawable object representing the image.
     * @param res      The resources object to decode vector drawables.
     * @return The byte array representing the image.
     */
    default byte[] getImageGallery(Drawable drawable, Resources res) {
        return CompletableFuture.supplyAsync(() ->
        {
            int quality = 100;
            Bitmap bitmap = getDrawable(drawable, res);
            byte[] photo;

            do {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                photo = stream.toByteArray();
                quality -= 10;
            } while (!validatePhoto(photo) && quality >= 0);

            return photo;
        }).join();
    }

    /**
     * Convert a drawable object to a bitmap object for image processing.
     *
     * @param drawable The drawable object to convert to a bitmap.
     * @param res      The resources object used for decoding certain drawable types.
     * @return The bitmap object representing the drawable image.
     */
    default Bitmap getDrawable(Drawable drawable, Resources res) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            bitmap = BitmapFactory.decodeResource(res, R.drawable.logo);
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return bitmap;
    }

    /**
     * Validate the size of the photo by checking if the byte array representation is within the specified limit.
     *
     * @param photo The byte array representing the photo image.
     * @return True if the photo size is within limits, false otherwise.
     */
    default boolean validatePhoto(byte[] photo) {
        return photo.length < 1048576;
    }
}
