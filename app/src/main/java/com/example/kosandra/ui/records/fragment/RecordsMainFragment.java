package com.example.kosandra.ui.records.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMainRecordsBinding;
import com.example.kosandra.entity.Record;
import com.example.kosandra.ui.general_logic.ConfirmationDialog;
import com.example.kosandra.ui.records.record_listener_rv.OnClickCalendar;
import com.example.kosandra.ui.records.record_listener_rv.OnClickItemCalendar;
import com.example.kosandra.ui.records.adapter.AdapterCalendar;
import com.example.kosandra.ui.records.adapter.AdapterInfoVisit;
import com.example.kosandra.ui.records.dialogs.AddEditRecordsDialog;
import com.example.kosandra.view_model.RecordsViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class represents the main fragment for managing records.
 * <p>
 * It contains functionality for displaying a calendar, adding/editing records,
 * <p>
 * navigating between months, and interacting with record items.
 */
public class RecordsMainFragment extends Fragment implements OnClickCalendar, OnClickItemCalendar {
    private FragmentMainRecordsBinding binding; // View binding for the fragment
    private LocalDate selectedDate; // The currently selected date
    private List<LocalDate> dayInMonth;// List of days in the selected month
    private AdapterCalendar adapterCalendar;// Adapter for the calendar view
    private AdapterInfoVisit adapterInfoVisit;// Adapter for displaying record information
    private View prevCellCalendar; // Previous cell selected in the calendar
    private RecordsViewModel recordsViewModel;// ViewModel for managing records

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.butAddRecord.setOnClickListener(v -> showDialogAddRecord());
        binding.includeCalendar.prevMonth.setOnClickListener(v -> prevMonth());
        binding.includeCalendar.nextMonth.setOnClickListener(v -> nextMonth());
        initDate();
        initRecyclerView();
        observeRecord();
    }

    /**
     * Shows the add record dialog.
     */
    private void showDialogAddRecord() {
        Bundle bundle = new Bundle();
        bundle.putString("logic", "add");
        AddEditRecordsDialog addEditRecordsDialog = new AddEditRecordsDialog();
        addEditRecordsDialog.setArguments(bundle);
        addEditRecordsDialog.show(getParentFragmentManager(), "DialogAddRecord");
    }

    /**
     * Moves to the next month in the calendar.
     */
    private void nextMonth() {
        selectedDate = selectedDate.plusMonths(1);
        observeRecord();
    }

    /**
     * Moves to the previous month in the calendar.
     */
    private void prevMonth() {
        selectedDate = selectedDate.minusMonths(1);
        observeRecord();
    }

    /**
     * Initializes the selected date to the current date.
     */
    private void initDate() {
        selectedDate = LocalDate.now();
    }

    /**
     * Initializes the RecyclerViews and their respective adapters.
     */
    private void initRecyclerView() {
        adapterCalendar = new AdapterCalendar(this, requireContext());
        adapterInfoVisit = new AdapterInfoVisit(getViewLifecycleOwner(), requireActivity(), requireContext(), this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireActivity(), 7);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(requireActivity());
        binding.includeCalendar.calendarRv.setLayoutManager(layoutManager);
        binding.visitDateRv.setLayoutManager(layoutManager1);
    }

    /**
     * Retrieves a list of days in the selected month.
     *
     * @return List of LocalDate objects representing days in the month.
     */
    private List<LocalDate> dayInMonthList() {
        ArrayList<LocalDate> daysInMonthList = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i < dayOfWeek; i++) {
            daysInMonthList.add(null);
        }

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate day = firstOfMonth.plusDays(i);
            daysInMonthList.add(day);
        }
        return daysInMonthList;
    }

    /**
     * Generates the title for the current selected month.
     *
     * @return The formatted title string for the month.
     */
    private String monthTitle() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru"));
        String month = selectedDate.format(dateTimeFormatter);
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        return month;
    }

    /**
     * Observes the record data and updates the UI accordingly.
     */
    private void observeRecord() {
        recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            binding.includeCalendar.titleCalendar.setText(monthTitle());
            dayInMonth = dayInMonthList();
            adapterCalendar.setDayOfMonth(dayInMonth);
            binding.includeCalendar.calendarRv.setAdapter(adapterCalendar);
            adapterCalendar.setVisitClient(records);
        });
    }

    /**
     * Handles click events on calendar cells.
     *
     * @param view The clicked view.
     * @param date The date associated with the clicked cell.
     */
    @Override
    public void onClick(View view, LocalDate date) {
        if (prevCellCalendar == null && date.isEqual(LocalDate.now())) {
        } else if (prevCellCalendar == null) {
            prevCellCalendar = view;
            view.setBackgroundResource(R.drawable.ic_record_data_now_selected);
        } else if (date.isEqual(LocalDate.now())) {
            prevCellCalendar.setBackground(null);
            prevCellCalendar = null;
        } else {
            prevCellCalendar.setBackground(null);
            prevCellCalendar = view;
            view.setBackgroundResource(R.drawable.ic_record_data_now_selected);
        }
        viewInfoCell(date);
    }

    /**
     * Displays information related to a specific date.
     *
     * @param date The date to display information for.
     */
    private void viewInfoCell(LocalDate date) {
        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            List<Record> cellRecord = new ArrayList<>();

            for (int i = 0; i < records.size(); i++) {
                if (date.isEqual(records.get(i).getVisitDate())) {
                    cellRecord.add(records.get(i));
                }
            }
            if (cellRecord.isEmpty()) {
                binding.titleVisitRv.setVisibility(View.GONE);
            } else {
                binding.titleVisitRv.setVisibility(View.VISIBLE);
            }
            adapterInfoVisit.setRecords(cellRecord);
            binding.visitDateRv.setAdapter(adapterInfoVisit);
        });
    }

    /**
     * Handles the edit action for a record item.
     *
     * @param record The record to edit.
     */
    @Override
    public void onEditClick(Record record) {
        Bundle bundle = new Bundle();
        bundle.putString("logic", "edit");
        bundle.putParcelable("record", record);
        AddEditRecordsDialog addEditRecordsDialog = new AddEditRecordsDialog();
        addEditRecordsDialog.setArguments(bundle);
        addEditRecordsDialog.show(getParentFragmentManager(), "DialogEditRecord");
    }

    /**
     * Handles the delete action for a record item.
     *
     * @param record The record to delete.
     */
    @Override
    public void onDeleteClick(Record record) {
        ConfirmationDialog dialog = new ConfirmationDialog(
                getContext(),
                "Подтверждение",
                "Вы уверены, что хотите удалить?",
                (dialogInterface, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        deleteMaterial(record);
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialogInterface.dismiss();
                    }
                });

        dialog.show();
    }

    /**
     * Deletes a record from the database.
     *
     * @param record The record to be deleted.
     */
    private void deleteMaterial(Record record) {
        RecordsViewModel recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
        recordsViewModel.delete(record);
    }
}