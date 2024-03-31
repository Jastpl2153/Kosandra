package com.example.kosandra.ui.general_logic;

import android.content.Context;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerHelperDialog {
    public static void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> showTimePickerDialog(editText.getContext(), editText));
    }

    private static void showTimePickerDialog(Context context, EditText editText) {
        final int currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        final int currentMinute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);

        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(context, (view, hourOfDay, minute) -> {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editText.setText(selectedTime);
        }, currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }

    public static LocalTime parseTime(String dateString) {
        return LocalTime.parse(dateString);
    }
}
