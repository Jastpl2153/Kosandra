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

    private void initViewModel(){
        clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
    }

    private void initListClient() {
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), this::setupClientList);
    }

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
                    }
                    else {
                        selectedClientId = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

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

    @Override
    public boolean validateEmptyFields() {
        return !binding.addEditRecordDate.getText().toString().isEmpty() &&
                !binding.addEditRecordTime.getText().toString().isEmpty() &&
                !binding.addEditRecordClient.getText().toString().isEmpty() &&
                !binding.addEditRecordHairstyle.getText().toString().isEmpty() &&
                !binding.addEditRecordCost.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.addEditRecordDate);
        isEmpty(binding.addEditRecordTime);
        isEmpty(binding.addEditRecordClient);
        isEmpty(binding.addEditRecordHairstyle);
        isEmpty(binding.addEditRecordCost);
    }

    private boolean validateClientDataBase() {
        if (selectedClientId == -1) {
            animateEmptyField(binding.addEditRecordClient, requireContext());
            Toast.makeText(requireContext(), "Клиент не найден", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Методы добавления
    private void addLogic() {
        binding.saveRecord.setOnClickListener(v -> addRecord());
    }

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

    private Record initRecord() {
        return new Record(
                selectedClientId,
                DatePickerHelperDialog.parseDateDataBase(binding.addEditRecordDate.getText().toString()),
                TimePickerHelperDialog.parseTime(binding.addEditRecordTime.getText().toString()),
                binding.addEditRecordHairstyle.getText().toString(),
                Integer.parseInt(binding.addEditRecordCost.getText().toString()));
    }

    //методы изменения
    private void editLogic() {
        record = arguments.getParcelable("record");
        initField();
        binding.saveRecord.setOnClickListener(v -> editRecord());
    }

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

    private void setRecord() {
        record.setClientId(selectedClientId);
        record.setVisitDate(DatePickerHelperDialog.parseDateDataBase(binding.addEditRecordDate.getText().toString()));
        record.setTimeSpent(TimePickerHelperDialog.parseTime(binding.addEditRecordTime.getText().toString()));
        record.setHaircutName(binding.addEditRecordHairstyle.getText().toString());
        record.setCost(Integer.parseInt(binding.addEditRecordCost.getText().toString()));
    }
}
