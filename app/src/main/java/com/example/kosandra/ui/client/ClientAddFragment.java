package com.example.kosandra.ui.client;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientAddBinding;
import com.example.kosandra.view_model.ClientViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
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
        listenerActivityPhotoResult();


        return root;
    }

    private void listenerActivityPhotoResult() {
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Picasso.get()
                                    .load(uri)
                                    .fit()
                                    .centerCrop()
                                    .into(binding.clientImageAdd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setupUI(){
        setupAddPhotoClient();
        setupNumberVisitPicker();
        setupDatePicker();
        setupSaveMenu();
    }

    private void setupAddPhotoClient(){
        binding.clientImageAdd.setOnClickListener(v -> openGalleryForImage());
    }

    private void openGalleryForImage() {
        getContentLauncher.launch("image/*");
    }

    private void setupNumberVisitPicker() {
        binding.numberVisitPicker.setMinValue(0);
        binding.numberVisitPicker.setMaxValue(10);

        binding.numberVisitPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {listenerPickerVisit(newVal);});
    }

    private void listenerPickerVisit (int newVal){
        for (int i = 0; i < newVal; i++) {
            ToggleButton toggleButton = (ToggleButton) binding.gridCountVisit.getChildAt(i);
            toggleButton.setChecked(true);
        }
        for (int i = newVal; i < binding.numberVisitPicker.getMaxValue(); i++) {
            ToggleButton toggleButton = (ToggleButton) binding.gridCountVisit.getChildAt(i);
            toggleButton.setChecked(false);
        }
    }

    private void setupDatePicker() {
        binding.etClientBirthday.setOnClickListener(v -> listenerDatePickerDialog());
    }

    private void listenerDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedMonth = selectedMonth + 1;
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDayOfMonth, selectedMonth, selectedYear);
            binding.etClientBirthday.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
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

    private byte[] getPhotoClient () {
        return CompletableFuture.supplyAsync(() ->
        {
            BitmapDrawable drawable = (BitmapDrawable) binding.clientImageAdd.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }).join();
    }

    private int parseHairLength () {
        if(!binding.etHairLength.getText().toString().isEmpty()) {
            return Integer.parseInt(binding.etHairLength.getText().toString());
        }
        return 0;
    }

    private LocalDate parseBirthday() {
        String birthdayString = binding.etClientBirthday.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(birthdayString, formatter);
    }

    private Client initClient(){
        return new Client( getPhotoClient(),
                binding.etClientName.getText().toString(),
                parseBirthday(),
                binding.etClientPhone.getText().toString(),
                binding.numberVisitPicker.getValue(),
                parseHairLength(),
                binding.etHairColor.getText().toString(),
                binding.etHairDensity.getText().toString(),
                binding.etConversationDetails.getText().toString());
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