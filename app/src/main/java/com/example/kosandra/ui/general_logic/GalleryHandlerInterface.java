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

public interface GalleryHandlerInterface {
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

        default void openGallery(ActivityResultLauncher<String> getContentLauncher) {
                getContentLauncher.launch("image/*");
        }

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

        default Bitmap getDrawable(Drawable drawable, Resources res){
                Bitmap bitmap;

                if (drawable instanceof BitmapDrawable){
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
                        bitmap = BitmapFactory.decodeResource(res, R.drawable.logo);
                } else {
                        throw new IllegalArgumentException("Unsupported image type");
                }

                return bitmap;
        }

        default boolean validatePhoto(byte[] photo) {
                return photo.length < 1048576;
        }
}
