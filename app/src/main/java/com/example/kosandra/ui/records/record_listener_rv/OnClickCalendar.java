package com.example.kosandra.ui.records.record_listener_rv;

import android.view.View;

import com.example.kosandra.entity.Record;

import java.time.LocalDate;
import java.util.List;

public interface OnClickCalendar {
    void onClick(View view, LocalDate date);
}
