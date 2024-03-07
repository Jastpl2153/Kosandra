package com.example.kosandra.ui.client.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.kosandra.databinding.DialogEditClientBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.view_model.ClientViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class EditClientDialog extends BottomSheetDialogFragment {
    private DialogEditClientBinding binding;
    private Client client;
    private ActivityResultLauncher<String> getContentLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditClientBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        listenerActivityPhotoResult();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getClientFromArguments();
        if (client != null) {
            populateFieldsWithClientData();
            initImageViewClient();
            setupDatePicker();
            setupSaveButtonClickListener();
            setupAddPhotoClient();
        }
    }

    private void initImageViewClient() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
        if (binding != null){
           binding.editClientImage.setImageBitmap(bitmap);
        }
    }

    private void getClientFromArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            client = arguments.getParcelable("client");
        }
    }

    private void populateFieldsWithClientData() {
        binding.etEditName.setText(client.getName());
        binding.etEditPhone.setText(client.getNumberPhone());
        binding.etEditBirthday.setText(client.getDateOfBirth().toString());
        binding.etEditLength.setText(String.valueOf(client.getHairLength()));
        binding.etEditColor.setText(client.getHairColor());
        binding.etEditDensity.setText(client.getHairDensity());
        binding.etEditCountVisit.setText(String.valueOf(client.getNumberOfVisits()));
        binding.etEditTalkingClient.setText(client.getConversationDetails());
    }

    private void setupSaveButtonClickListener() {
        binding.saveEditClient.setOnClickListener(v -> {
            updateClientFromInputFields();
            saveClientData();
            dismissDialog();
        });
    }

    private void updateClientFromInputFields() {
        client.setPhoto(getPhotoClient());
        client.setName(binding.etEditName.getText().toString());
        client.setNumberPhone(binding.etEditPhone.getText().toString());
        client.setDateOfBirth(DatePickerHelperDialog.parseBirthday(binding.etEditBirthday.getText().toString()));
        client.setHairLength(Integer.parseInt(binding.etEditLength.getText().toString()));
        client.setHairColor(binding.etEditColor.getText().toString());
        client.setHairDensity(binding.etEditDensity.getText().toString());
        client.setNumberOfVisits(Integer.parseInt(binding.etEditCountVisit.getText().toString()));
        client.setConversationDetails(binding.etEditTalkingClient.getText().toString());
    }

    private boolean validateClientPhoto(byte[] photo) {
        if (photo.length < 1048576) {
            return true;
        } else {
            return false;
        }
    }

    private byte[] getPhotoClient () {
        return CompletableFuture.supplyAsync(() ->
        {
            int quality = 100;
            BitmapDrawable drawable = (BitmapDrawable) binding.editClientImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
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
    private void saveClientData() {
        ClientViewModel viewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        viewModel.update(client);
    }

    private void dismissDialog() {
        dismiss();
    }

    private void setupDatePicker() {
        DatePickerHelperDialog.setupDatePicker(binding.etEditBirthday);
    }

    private void listenerActivityPhotoResult() {
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Glide.with(requireContext())
                                    .load(uri)
                                    .fitCenter()
                                    .override(758, 1138)
                                    .into(binding.editClientImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void openGalleryForImage() {
        getContentLauncher.launch("image/*");
    }

    private void setupAddPhotoClient(){
        binding.editClientImage.setOnClickListener(v -> openGalleryForImage());
    }
}
