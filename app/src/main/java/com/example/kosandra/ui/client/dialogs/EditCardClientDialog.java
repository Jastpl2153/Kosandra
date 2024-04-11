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

/**
 * EditCardClientDialog class represents a dialog fragment for editing client details.
 * <p>
 * This class extends BottomSheetDialogFragment and implements GalleryHandlerInterface and EmptyFields interfaces.
 * <p>
 * It provides functionality to edit and save client information.
 */
public class EditCardClientDialog extends BottomSheetDialogFragment implements GalleryHandlerInterface, EmptyFields {
    /**
     * Binding object for DialogClientEditCardBinding layout.
     */
    private DialogClientEditCardBinding binding;
    /**
     * Client object to store and manipulate client information.
     */
    private Client client;
    /**
     * ActivityResultLauncher to get content from external sources.
     */
    private ActivityResultLauncher<String> getContentLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogClientEditCardBinding.inflate(inflater, container, false);
        ;
        return binding.getRoot();
    }

    /**
     * Called immediately after onCreateView and ensures that the view is fully initialized.
     * It initializes client information, sets up date picker, and handles button clicks.
     *
     * @param view               The view returned by onCreateView
     * @param savedInstanceState The previous state of the fragment
     */
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

    /**
     * Retrieves client object from the arguments passed to the fragment.
     */
    private void getClient() {
        client = getArguments() != null ? getArguments().getParcelable("client") : null;
    }

    /**
     * Initializes fields in the layout based on client information.
     */
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

    /**
     * Saves edited client information to the database.
     * If fields are not empty, updates the client details, saves data, and dismisses the dialog.
     * If fields are empty, displays error messages for empty fields.
     */
    private void saveClient() {
        if (validateEmptyFields()) {
            setClient();
            saveClientData();
            this.dismiss();
        } else {
            handleEmptyFields();
        }
    }

    /**
     * Sets the client object with updated information from the UI elements.
     */
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

    /**
     * Saves the updated client data to the ViewModel.
     */
    private void saveClientData() {
        ClientViewModel viewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        viewModel.update(client);
    }

    /**
     * Validates if essential fields are not empty.
     *
     * @return True if name and phone fields are not empty, false otherwise
     */
    @Override
    public boolean validateEmptyFields() {
        return !binding.etEditNameClient.getText().toString().isEmpty() &&
                !binding.etEditPhoneClient.getText().toString().isEmpty();
    }

    /**
     * Shows error messages for empty fields by highlighting them in the UI.
     */
    @Override
    public void handleEmptyFields() {
        isEmpty(binding.etEditNameClient);
        isEmpty(binding.etEditPhoneClient);
    }
}
