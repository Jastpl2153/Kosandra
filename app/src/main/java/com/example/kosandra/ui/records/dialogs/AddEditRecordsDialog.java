package com.example.kosandra.ui.records.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosandra.databinding.DialogRecordsAddEditBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.Record;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.TimePickerHelperDialog;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.RecordsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

/**
 * AddEditRecordsDialog class represents a dialog fragment that allows users to add or edit records.
 * <p>
 * It extends BottomSheetDialogFragment and implements EmptyFields interface.
 * <p>
 * The class contains various methods for initializing view models, setting up client lists, handling logic for
 * <p>
 * adding or editing records, validating empty fields, handling empty fields, and performing database operations for records.
 */
public class AddEditRecordsDialog extends BottomSheetDialogFragment implements EmptyFields {
    private DialogRecordsAddEditBinding binding;
    private int selectedClientId = -1;
    private Bundle arguments;
    private Record record;
    private ClientViewModel clientViewModel;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRecordsAddEditBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arguments = getArguments();
        initViewModel();
        DatePickerHelperDialog.setupDatePicker(binding.addEditRecordDate);
        TimePickerHelperDialog.setupDatePicker(binding.addEditRecordTime);
        initListClient();
        initLogic();
    }

    /**
     * initViewModel method initializes clientViewModel and recordsViewModel.
     */
    private void initViewModel() {
        clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
    }

    /**
     * initListClient method observes changes in the list of clients and sets up the client list in the UI.
     */
    private void initListClient() {
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), this::setupClientList);
    }

    /**
     * setupClientList method sets up the client list in the UI using the provided list of clients.
     *
     * @param clients The list of clients to be displayed.
     */
    private void setupClientList(List<Client> clients) {
        ArrayAdapter<Client> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, clients);
        binding.addEditRecordClient.setAdapter(adapter);
        binding.addEditRecordClient.setOnItemClickListener((parent, view, position, id) -> {
            Client selectedClient = adapter.getItem(position);
            if (selectedClient != null) {
                selectedClientId = selectedClient.getId();
            }
        });
        binding.addEditRecordClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (Client client : clients) {
                    if (client.getName().equals(binding.addEditRecordClient.getText().toString())) {
                        selectedClientId = client.getId();
                        break;
                    } else {
                        selectedClientId = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * This method initializes the logic based on the value of the "logic" key from the arguments.
     * <p>
     * It executes either the "addLogic()" method or the "editLogic()" method based on the value of the "logic" key.
     */
    private void initLogic() {
        String logic = arguments.getString("logic");

        switch (logic) {
            case "add":
                addLogic();
                break;
            case "edit":
                editLogic();
                break;
        }
    }

    /**
     * This method validates if all the required fields for adding or editing a record are not empty.
     *
     * @return true if all fields are not empty, false otherwise
     */
    @Override
    public boolean validateEmptyFields() {
        return !binding.addEditRecordDate.getText().toString().isEmpty() &&
                !binding.addEditRecordTime.getText().toString().isEmpty() &&
                !binding.addEditRecordClient.getText().toString().isEmpty() &&
                !binding.addEditRecordHairstyle.getText().toString().isEmpty() &&
                !binding.addEditRecordCost.getText().toString().isEmpty();
    }

    /**
     * This method handles empty fields by calling the "isEmpty()" method for each binding field.
     */
    @Override
    public void handleEmptyFields() {
        isEmpty(binding.addEditRecordDate);
        isEmpty(binding.addEditRecordTime);
        isEmpty(binding.addEditRecordClient);
        isEmpty(binding.addEditRecordHairstyle);
        isEmpty(binding.addEditRecordCost);
    }

    /**
     * This method validates if the selected client ID is valid.
     * If the client ID is invalid, it animates the empty field and displays a toast message.
     *
     * @return true if client ID is valid, false otherwise
     */
    private boolean validateClientDataBase() {
        if (selectedClientId == -1) {
            animateEmptyField(binding.addEditRecordClient, requireContext());
            Toast.makeText(requireContext(), "Клиент не найден", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * This method sets up the logic for adding a record.
     * It sets a click listener for the "save" button to add a new record.
     */
    private void addLogic() {
        binding.saveRecord.setOnClickListener(v -> addRecord());
    }

    /**
     * This method adds a new record if all fields are valid, otherwise it handles empty fields.
     */
    private void addRecord() {
        if (validateEmptyFields() && validateClientDataBase()) {
            try {
                recordsViewModel.insert(initRecord());
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            handleEmptyFields();
        }
    }

    /**
     * This method initializes a new record object based on the input from the user interface fields.
     *
     * @return a new Record object with the provided details
     */
    private Record initRecord() {
        return new Record(
                selectedClientId,
                DatePickerHelperDialog.parseDateDataBase(binding.addEditRecordDate.getText().toString()),
                TimePickerHelperDialog.parseTime(binding.addEditRecordTime.getText().toString()),
                binding.addEditRecordHairstyle.getText().toString(),
                Integer.parseInt(binding.addEditRecordCost.getText().toString()));
    }

    /**
     * This method sets up the logic for editing an existing record.
     * It retrieves the record from the arguments and initializes the fields with the record details.
     */
    private void editLogic() {
        record = arguments.getParcelable("record");
        initField();
        binding.saveRecord.setOnClickListener(v -> editRecord());
    }

    /**
     * This method initializes the edit fields with the details of the selected record.
     */
    private void initField() {
        binding.addEditRecordDate.setText(DatePickerHelperDialog.parseDateOutput(record.getVisitDate()));
        binding.addEditRecordTime.setText(record.getTimeSpent().toString());
        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        clientViewModel.getClient(record.getClientId()).observe(getViewLifecycleOwner(), client -> {
            binding.addEditRecordClient.setText(client.getName());
            selectedClientId = client.getId();
        });
        binding.addEditRecordHairstyle.setText(record.getHaircutName());
        binding.addEditRecordCost.setText(String.valueOf(record.getCost()));
    }

    /**
     * This method updates the record with the new values from the user interface fields.
     * It then checks if all fields are valid before updating the record.
     */
    private void editRecord() {
        setRecord();
        if (validateEmptyFields() && validateClientDataBase()) {
            try {
                recordsViewModel.update(record);
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            handleEmptyFields();
        }
    }

    /**
     * This method updates the fields of the record object with the new values from the user interface.
     */
    private void setRecord() {
        record.setClientId(selectedClientId);
        record.setVisitDate(DatePickerHelperDialog.parseDateDataBase(binding.addEditRecordDate.getText().toString()));
        record.setTimeSpent(TimePickerHelperDialog.parseTime(binding.addEditRecordTime.getText().toString()));
        record.setHaircutName(binding.addEditRecordHairstyle.getText().toString());
        record.setCost(Integer.parseInt(binding.addEditRecordCost.getText().toString()));
    }
}
