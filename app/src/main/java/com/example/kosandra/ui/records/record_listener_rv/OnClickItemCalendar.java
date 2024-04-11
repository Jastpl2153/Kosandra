package com.example.kosandra.ui.records.record_listener_rv;

import com.example.kosandra.entity.Record;

/**
 * This interface defines methods for handling edit and delete events on calendar items.
 */
public interface OnClickItemCalendar {
    /**
     * This method is called when the edit button on a calendar item is clicked.
     *
     * @param record The record object associated with the calendar item being edited.
     */
    void onEditClick(Record record);

    /**
     * This method is called when the delete button on a calendar item is clicked.
     *
     * @param record The record object associated with the calendar item being deleted.
     */
    void onDeleteClick(Record record);
}
