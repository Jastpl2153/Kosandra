package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerHelperDialog {
    public static void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> showDatePickerDialog(editText.getContext(), editText));
    }

    private static void showDatePickerDialog(Context context, EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedMonth = selectedMonth + 1;
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDayOfMonth , selectedMonth, selectedYear);
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public static LocalDate parseDateDataBase(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public static String parseDateOutput(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
