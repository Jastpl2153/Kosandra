package com.example.kosandra.ui.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientAddBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.client.dialogs.DatePickerHelperDialog;
import com.example.kosandra.view_model.ClientViewModel;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class ClientAddFragment extends Fragment {
    private FragmentClientAddBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupUI();


        return root;
    }

    private void setupUI(){
        setupAddPhotoClient();
        setupDatePicker();
        setupSaveMenu();
        listenerActivityPhotoResult();
    }

    private void setupAddPhotoClient(){
        binding.clientImageAdd.setOnClickListener(v -> openGalleryForImage());
    }
    private void listenerActivityPhotoResult() {
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            loadImage(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void loadImage(Uri uri) {
        Glide.with(requireContext())
                .load(uri)
                .fitCenter()
                .override(758, 1138)
                .into(binding.clientImageAdd);
    }
    private void openGalleryForImage() {
        getContentLauncher.launch("image/*");
    }
    private void setupDatePicker() {
        DatePickerHelperDialog.setupDatePicker(binding.etClientBirthday);
    }

    private void setupSaveMenu() {
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_save, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_save) {
                    saveClient();
                    return true;
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());
    }

    private void saveClient() {
        if (validateClientFieldInFields()) {
            try {
                ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
                clientViewModel.insert(initClient());
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.popBackStack();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
           isEmptyField();
        }
    }

    private boolean validateClientFieldInFields (){
        return !binding.etClientName.getText().toString().isEmpty() &&
                !binding.etClientPhone.getText().toString().isEmpty() &&
                !binding.etClientBirthday.getText().toString().isEmpty();
    }

    private boolean validateClientPhoto(byte[] photo) {
        if (photo.length < 1048576) {
            return true;
        } else {
            return false;
        }
    }

    private byte[] getPhotoClient() {
        return CompletableFuture.supplyAsync(() ->
        {
            int quality = 100;
            Bitmap bitmap = getDrawable();
            byte[] photo;

            do {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                photo = stream.toByteArray();
                quality -= 10;
            } while (!validateClientPhoto(photo) && quality >= 0);

            return photo;
        }).join();
    }

    private Bitmap getDrawable () {
        Drawable drawable = binding.clientImageAdd.getDrawable();
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.clientImageAdd.getDrawable();
            bitmap = bitmapDrawable.getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return bitmap;
    }

    private int parseHairLength () {
        if(!binding.etHairLength.getText().toString().isEmpty()) {
            return Integer.parseInt(binding.etHairLength.getText().toString());
        }
        return 0;
    }
    private Client initClient(){
        return new Client( getPhotoClient(),
                binding.etClientName.getText().toString(),
                DatePickerHelperDialog.parseBirthday(binding.etClientBirthday.getText().toString()),
                binding.etClientPhone.getText().toString(),
                0,
                parseHairLength(),
                initEmptyFieldString(binding.etHairColor.getText().toString()),
                initEmptyFieldString(binding.etHairDensity.getText().toString()),
                initEmptyFieldString(binding.etConversationDetails.getText().toString()));
    }
    private String initEmptyFieldString(String text){
        return text.isEmpty() ? "Не указано" : text;
    }

    private void animationEmptyField(EditText text) {
        text.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.red_heart));
        Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        text.startAnimation(shakeAnimation);
    }

    private void isEmptyField(){
        if (binding.etClientName.getText().toString().isEmpty()){
            animationEmptyField(binding.etClientName);
        }
        if (binding.etClientPhone.getText().toString().isEmpty()){
            animationEmptyField(binding.etClientPhone);
        }
        if (binding.etClientBirthday.getText().toString().isEmpty()){
            animationEmptyField(binding.etClientBirthday);
        }
    }
}