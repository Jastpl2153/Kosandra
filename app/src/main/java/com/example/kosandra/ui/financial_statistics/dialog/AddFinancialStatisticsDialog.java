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

/**
 * A class representing a dialog fragment for adding financial statistics, such as income or expenses.
 */
public class AddFinancialStatisticsDialog extends BottomSheetDialogFragment implements EmptyFields {
    private DialogFinancialStatisticsAddBinding binding;
    private boolean type;

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

    /**
     * Sets up the UI elements based on the type (income or expenses).
     */
    private void typeIncomeExpenses() {
        if (type) {
            initSpinner(binding.spinnerAddFinance, R.array.type_income);
            binding.titleDialog.setText(R.string.title_dialog_add_income);
            binding.addNameFinance.setHint(R.string.what_income);
        } else {
            initSpinner(binding.spinnerAddFinance, R.array.type_expenses);
            binding.titleDialog.setText(R.string.title_dialog_add_expenses);
            binding.addNameFinance.setHint(R.string.expenses_type);
        }
    }

    /**
     * Initializes a spinner with data from a specified array resource.
     *
     * @param spinner The Spinner to initialize
     * @param arrayId The resource ID of the array to populate the Spinner
     */
    private void initSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Adds expenses or income based on the type selected.
     */
    private void addExpenses() {
        if (validateEmptyFields()) {
            if (type) {
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

    /**
     * Initializes an Expenses object with the input data from UI elements.
     *
     * @return The initialized Expenses object
     */
    private Expenses initExpenses() {
        return new Expenses(
                binding.spinnerAddFinance.getSelectedItem().toString(),
                binding.addNameFinance.getText().toString(),
                Integer.parseInt(binding.addCostFinance.getText().toString()),
                DatePickerHelperDialog.parseDateDataBase(binding.addDateFinance.getText().toString())
        );
    }

    /**
     * Initializes an Income object with the input data from UI elements.
     *
     * @return The initialized Income object
     */
    private Income initIncome() {
        return new Income(
                binding.spinnerAddFinance.getSelectedItem().toString(),
                binding.addNameFinance.getText().toString(),
                Integer.parseInt(binding.addCostFinance.getText().toString()),
                DatePickerHelperDialog.parseDateDataBase(binding.addDateFinance.getText().toString())
        );
    }

    /**
     * Validates if all required fields are not empty.
     *
     * @return True if all fields are not empty, otherwise false
     */
    @Override
    public boolean validateEmptyFields() {
        return !binding.addNameFinance.getText().toString().isEmpty() &&
                !binding.addCostFinance.getText().toString().isEmpty() &&
                !binding.addDateFinance.getText().toString().isEmpty();
    }

    /**
     * Handles empty fields by applying visual cues to indicate required input.
     */
    @Override
    public void handleEmptyFields() {
        isEmpty(binding.addNameFinance);
        isEmpty(binding.addCostFinance);
        isEmpty(binding.addDateFinance);
    }
}
