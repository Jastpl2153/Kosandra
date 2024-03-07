package com.example.kosandra.ui.client.dialogs;

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
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, selectedMonth, selectedDayOfMonth);
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public static LocalDate parseBirthday(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}
