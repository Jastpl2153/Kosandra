package com.example.kosandra.ui.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientAddBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.GalleryHandlerInterface;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.view_model.ClientViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientAddFragment extends Fragment implements GalleryHandlerInterface, EmptyFields {
    private FragmentClientAddBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupGalleryResult(launcher -> getContentLauncher = launcher, getContext(), this, binding.clientImageAdd);
        binding.clientImageAdd.setOnClickListener(v -> openGallery(getContentLauncher));
        DatePickerHelperDialog.setupDatePicker(binding.etClientBirthday);
        initMenu();
    }

    private void initMenu() {
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
        if (validateEmptyFields()) {
            try {
                ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
                clientViewModel.insert(initClient());
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.popBackStack();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
           handleEmptyFields();
        }
    }

    private Client initClient(){
        return new Client( getImageGallery(binding.clientImageAdd.getDrawable(), getResources()),
                binding.etClientName.getText().toString(),
                initEmptyFieldDate(binding.etClientBirthday.getText().toString()),
                initEmptyFieldString(binding.etClientPhone.getText().toString()),
                0,
                parseHairLength(),
                initEmptyFieldString(binding.etHairColor.getText().toString()),
                initEmptyFieldString(binding.etHairDensity.getText().toString()),
                initEmptyFieldString(binding.etConversationDetails.getText().toString()));
    }
    private int parseHairLength () {
        if(!binding.etHairLength.getText().toString().isEmpty()) {
            return Integer.parseInt(binding.etHairLength.getText().toString());
        }
        return 0;
    }

    private String initEmptyFieldString(String text){
        return text.isEmpty() ? "Не указано" : text;
    }

    private LocalDate initEmptyFieldDate(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return text.isEmpty() ? LocalDate.parse("01-01-1917", formatter) : DatePickerHelperDialog.parseDateDataBase(text);
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.etClientName.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.etClientName);
    }
}