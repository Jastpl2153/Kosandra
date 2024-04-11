package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * A helper class for setting up a TimePicker dialog for selecting time values.
 */
public class TimePickerHelperDialog {

    /**
     * Sets up a TimePicker dialog for the given EditText view.
     *
     * @param editText The EditText view which when clicked triggers the TimePicker dialog.
     */
    public static void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> showTimePickerDialog(editText.getContext(), editText));
    }

    /**
     * Displays a TimePicker dialog to allow the user to select a time.
     *
     * @param context  The context in which the dialog should be shown.
     * @param editText The EditText view in which the selected time will be displayed.
     */
    private static void showTimePickerDialog(Context context, EditText editText) {
        final int currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        final int currentMinute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);

        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(context, (view, hourOfDay, minute) -> {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editText.setText(selectedTime);
        }, currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }

    /**
     * Parses the given time string into a LocalTime object.
     *
     * @param dateString The time string to be parsed.
     * @return A LocalTime object representing the parsed time.
     */
    public static LocalTime parseTime(String dateString) {
        return LocalTime.parse(dateString);
    }
}
