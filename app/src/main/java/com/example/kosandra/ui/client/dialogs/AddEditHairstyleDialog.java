package com.example.kosandra.ui.client.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.kosandra.R;
import com.example.kosandra.databinding.DialogAddEditHaersyleBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class AddEditHairstyleDialog extends BottomSheetDialogFragment {
    private DialogAddEditHaersyleBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;
    private Client client;
    private HairstyleVisit hairstyleVisit;
    private Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddEditHaersyleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arguments = getArguments();
        initLogic();
        setupDatePicker();
        setupTimePicker();
        setupGalleryResult();
        binding.addPhotoHairstyle.setOnClickListener(v -> openGallery());
    }

    private void initLogic () {
        String logic = arguments.getString("logic");
        if (logic.equals("add")){
            getClient();
            binding.saveHairstyle.setOnClickListener(v -> saveHairstyle());
        } else if (logic.equals("edit")){
            getHairstyle();
            initField();
            binding.saveHairstyle.setOnClickListener(v -> editHairstyle());
        }
    }

    private void getClient() {
        if (arguments != null) {
            client = arguments.getParcelable("client");
        }
    }

    private void getHairstyle() {
        if (arguments != null) {
            hairstyleVisit = arguments.getParcelable("hairstyle");
        }
    }

    private void initField () {
        Bitmap bitmap = BitmapFactory.decodeByteArray(hairstyleVisit.getPhotoHairstyle(), 0, hairstyleVisit.getPhotoHairstyle().length);
        binding.addPhotoHairstyle.setImageBitmap(bitmap);
        binding.etNameHairstyle.setText(hairstyleVisit.getHaircutName());
        binding.etDateVisit.setText(hairstyleVisit.getVisitDate().toString());
        binding.etTimeCompleteHairstyle.setText(hairstyleVisit.getTimeSpent().toString());
        binding.etPriceHairstyle.setText(String.valueOf(hairstyleVisit.getHaircutCost()));
        binding.etPriceMaterials.setText(String.valueOf(hairstyleVisit.getMaterialCost()));
        binding.etWeightMaterials.setText(String.valueOf(hairstyleVisit.getMaterialWeight()));
    }

    private void editHairstyle() {
        if (validateFields()) {
            editObjectHairstyle();
            HairstyleVisitViewModel viewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
            viewModel.update(hairstyleVisit);
            dismissDialog();
        } else {
            handleEmptyFields();
        }
    }

    private void editObjectHairstyle() {
        hairstyleVisit.setPhotoHairstyle(getPhotoClient());
        hairstyleVisit.setHaircutName(binding.etNameHairstyle.getText().toString());
        hairstyleVisit.setVisitDate(DatePickerHelperDialog.parseBirthday(binding.etDateVisit.getText().toString()));
        hairstyleVisit.setTimeSpent(TimePickerHelperDialog.parseTime(binding.etTimeCompleteHairstyle.getText().toString()));
        hairstyleVisit.setHaircutCost(Integer.parseInt(binding.etPriceHairstyle.getText().toString()));
        hairstyleVisit.setMaterialCost(Integer.parseInt(binding.etPriceMaterials.getText().toString()));
        hairstyleVisit.setMaterialWeight(Integer.parseInt(binding.etWeightMaterials.getText().toString()));
    }

    private void setupDatePicker() {
        DatePickerHelperDialog.setupDatePicker(binding.etDateVisit);
    }

    private void setupTimePicker() {
        TimePickerHelperDialog.setupDatePicker(binding.etTimeCompleteHairstyle);
    }

    private void saveHairstyle() {
        HairstyleVisit visit = initHairstyleVisit();
        if (visit != null) {
            HairstyleVisitViewModel viewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
            viewModel.insert(visit);
            addCountVisitClient();
            dismissDialog();
        }
    }

    private void addCountVisitClient (){
        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        client.setNumberOfVisits(client.getNumberOfVisits() + 1);
        clientViewModel.update(client);
    }

    private HairstyleVisit initHairstyleVisit() {
        if (validateFields()) {
            return new HairstyleVisit(
                    DatePickerHelperDialog.parseBirthday(binding.etDateVisit.getText().toString()),
                    getPhotoClient(),
                    binding.etNameHairstyle.getText().toString(),
                    client.getId(),
                    Integer.parseInt(binding.etPriceHairstyle.getText().toString()),
                    Integer.parseInt(binding.etPriceMaterials.getText().toString()),
                    TimePickerHelperDialog.parseTime(binding.etTimeCompleteHairstyle.getText().toString()),
                    Integer.parseInt(binding.etWeightMaterials.getText().toString()));
        } else {
            handleEmptyFields();
            return null;
        }
    }

    private boolean validateFields() {
        return !binding.etDateVisit.getText().toString().isEmpty() &&
                !binding.etNameHairstyle.getText().toString().isEmpty() &&
                !binding.etPriceHairstyle.getText().toString().isEmpty() &&
                !binding.etPriceMaterials.getText().toString().isEmpty() &&
                !binding.etTimeCompleteHairstyle.getText().toString().isEmpty() &&
                !binding.etWeightMaterials.getText().toString().isEmpty();
    }

    private void handleEmptyFields() {
        isEmpty(binding.etDateVisit);
        isEmpty(binding.etNameHairstyle);
        isEmpty(binding.etPriceHairstyle);
        isEmpty(binding.etPriceMaterials);
        isEmpty(binding.etTimeCompleteHairstyle);
        isEmpty(binding.etWeightMaterials);
    }

    private void isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            animateEmptyField(editText);
        }
    }

    private void animateEmptyField(EditText editText) {
        editText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.red_heart));
        editText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
    }

    private void dismissDialog() {
        dismiss();
    }

    private void setupGalleryResult() {
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Glide.with(requireContext())
                                    .load(uri)
                                    .fitCenter()
                                    .override(758, 1138)
                                    .into(binding.addPhotoHairstyle);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void openGallery() {
        getContentLauncher.launch("image/*");
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
        Drawable drawable = binding.addPhotoHairstyle.getDrawable();
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.addPhotoHairstyle.getDrawable();
            bitmap = bitmapDrawable.getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return bitmap;
    }

    private boolean validateClientPhoto(byte[] photo) {
        if (photo.length < 1048576) {
            return true;
        } else {
            return false;
        }
    }
}