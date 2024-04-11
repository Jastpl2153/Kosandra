package com.example.kosandra.ui.records.record_listener_rv;

import android.view.View;

import com.example.kosandra.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface defines a method to handle click events on a calendar.
 */
public interface OnClickCalendar {
    /**
     * This method is called when a date on the calendar is clicked.
     *
     * @param view The view that was clicked (in this case, the calendar view).
     * @param date The LocalDate object representing the date that was clicked.
     */
    void onClick(View view, LocalDate date);
}
