package com.example.kosandra.ui.client.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosandra.databinding.DialogClientEditCardBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.GalleryHandlerInterface;
import com.example.kosandra.view_model.ClientViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditCardClientDialog extends BottomSheetDialogFragment implements GalleryHandlerInterface, EmptyFields {
    private DialogClientEditCardBinding binding;
    private Client client;
    private ActivityResultLauncher<String> getContentLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogClientEditCardBinding.inflate(inflater, container, false);;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getClient();
        if (client != null) {
            initFields();
            DatePickerHelperDialog.setupDatePicker(binding.etEditBirthdayClient);
            binding.saveEditClient.setOnClickListener(v -> saveClient());
            binding.editClientImage.setOnClickListener(v -> openGallery(getContentLauncher));
            setupGalleryResult(launcher -> getContentLauncher = launcher, getContext(), this, binding.editClientImage);
        }
    }

    private void getClient() {
        client = getArguments() != null ? getArguments().getParcelable("client") : null;
    }

    private void initFields() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
        binding.editClientImage.setImageBitmap(bitmap);
        binding.etEditNameClient.setText(client.getName());
        binding.etEditPhoneClient.setText(client.getNumberPhone());
        binding.etEditBirthdayClient.setText(DatePickerHelperDialog.parseDateOutput(client.getDateOfBirth()));
        binding.etEditLengthClient.setText(String.valueOf(client.getHairLength()));
        binding.etEditColorClient.setText(client.getHairColor());
        binding.etEditDensityClient.setText(client.getHairDensity());
        binding.etEditCountVisitClient.setText(String.valueOf(client.getNumberOfVisits()));
        binding.etEditTalkingClient.setText(client.getConversationDetails());
    }

    private void saveClient() {
        if (validateEmptyFields()) {
            setClient();
            saveClientData();
            this.dismiss();
        } else {
            handleEmptyFields();
        }
    }

    private void setClient() {
        client.setPhoto(getImageGallery(binding.editClientImage.getDrawable(), getResources()));
        client.setName(binding.etEditNameClient.getText().toString());
        client.setNumberPhone(binding.etEditPhoneClient.getText().toString());
        client.setDateOfBirth(DatePickerHelperDialog.parseDateDataBase(binding.etEditBirthdayClient.getText().toString()));
        client.setHairLength(Integer.parseInt(binding.etEditLengthClient.getText().toString()));
        client.setHairColor(binding.etEditColorClient.getText().toString());
        client.setHairDensity(binding.etEditDensityClient.getText().toString());
        client.setNumberOfVisits(Integer.parseInt(binding.etEditCountVisitClient.getText().toString()));
        client.setConversationDetails(binding.etEditTalkingClient.getText().toString());
    }

    private void saveClientData() {
        ClientViewModel viewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        viewModel.update(client);
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.etEditNameClient.getText().toString().isEmpty() &&
                !binding.etEditPhoneClient.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.etEditNameClient);
        isEmpty(binding.etEditPhoneClient);
    }
}
