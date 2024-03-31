package com.example.kosandra.ui.financial_statistics.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosandra.R;
import com.example.kosandra.databinding.DialogFinancialStatisticsAddBinding;
import com.example.kosandra.entity.Expenses;
import com.example.kosandra.entity.Income;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.view_model.ExpensesViewModel;
import com.example.kosandra.view_model.IncomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddFinancialStatisticsDialog extends BottomSheetDialogFragment implements EmptyFields {
    private DialogFinancialStatisticsAddBinding binding;
    private  boolean type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFinancialStatisticsAddBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getBoolean("type", false);
        typeIncomeExpenses();
        binding.saveFinance.setOnClickListener(v -> addExpenses());
        DatePickerHelperDialog.setupDatePicker(binding.addDateFinance);
    }

    private void typeIncomeExpenses(){
        if (type){
            initSpinner(binding.spinnerAddFinance, R.array.type_income);
            binding.titleDialog.setText(R.string.title_dialog_add_income);
            binding.addNameFinance.setHint(R.string.what_income);
        } else {
            initSpinner(binding.spinnerAddFinance, R.array.type_expenses);
            binding.titleDialog.setText(R.string.title_dialog_add_expenses);
            binding.addNameFinance.setHint(R.string.expenses_type);
        }
    }

    private void initSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void addExpenses(){
        if (validateEmptyFields()) {
            if (type){
                IncomeViewModel incomeViewModel = new ViewModelProvider(requireActivity()).get(IncomeViewModel.class);
                incomeViewModel.insert(initIncome());
            } else {
                ExpensesViewModel expensesViewModel = new ViewModelProvider(requireActivity()).get(ExpensesViewModel.class);
                expensesViewModel.insert(initExpenses());
            }
            dismiss();
        } else {
            handleEmptyFields();
        }
    }

    private Expenses initExpenses(){
        return new Expenses(
                binding.spinnerAddFinance.getSelectedItem().toString(),
                binding.addNameFinance.getText().toString(),
                Integer.parseInt(binding.addCostFinance.getText().toString()),
                DatePickerHelperDialog.parseDateDataBase(binding.addDateFinance.getText().toString())
        );
    }

    private Income initIncome(){
        return new Income(
                binding.spinnerAddFinance.getSelectedItem().toString(),
                binding.addNameFinance.getText().toString(),
                Integer.parseInt(binding.addCostFinance.getText().toString()),
                DatePickerHelperDialog.parseDateDataBase(binding.addDateFinance.getText().toString())
        );
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.addNameFinance.getText().toString().isEmpty() &&
                !binding.addCostFinance.getText().toString().isEmpty() &&
                !binding.addDateFinance.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.addNameFinance);
        isEmpty(binding.addCostFinance);
        isEmpty(binding.addDateFinance);
    }
}
