package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper class for setting up a DatePicker dialog for an EditText field.
 * Allows user to select a date from a calendar dialog and displays it in the EditText field.
 */
public class DatePickerHelperDialog {
    /**
     * Set up a DatePicker dialog for the provided EditText field.
     * When the EditText is clicked, it triggers the DatePicker dialog.
     *
     * @param editText the EditText field for date selection
     */
    public static void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> showDatePickerDialog(editText.getContext(), editText));
    }

    /**
     * Display a DatePicker dialog for selecting a date and update the EditText field with the selected date.
     *
     * @param context  the context in which the dialog is displayed
     * @param editText the EditText field to update with the selected date
     */
    private static void showDatePickerDialog(Context context, EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedMonth = selectedMonth + 1;
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDayOfMonth, selectedMonth, selectedYear);
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    /**
     * Parse the given date string in the format "dd-MM-yyyy" to a LocalDate object.
     *
     * @param dateString the date string to parse
     * @return the LocalDate object parsed from the input string
     */
    public static LocalDate parseDateDataBase(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Format the provided LocalDate object into a string with the format "dd-MM-yyyy".
     *
     * @param date the LocalDate object to format
     * @return the formatted date string
     */
    public static String parseDateOutput(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
