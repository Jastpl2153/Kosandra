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
import com.example.kosandra.ui.records.record_listener_rv.OnClockItemCalendar;
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

public class RecordsMainFragment extends Fragment implements OnClickCalendar, OnClockItemCalendar {
    private FragmentMainRecordsBinding binding;
    private LocalDate selectedDate;
    private List<LocalDate> dayInMonth;
    private AdapterCalendar adapterCalendar;
    private AdapterInfoVisit adapterInfoVisit;
    private View prevCellCalendar;
    private RecordsViewModel recordsViewModel;

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

    private void showDialogAddRecord() {
        Bundle bundle = new Bundle();
        bundle.putString("logic", "add");
        AddEditRecordsDialog addEditRecordsDialog = new AddEditRecordsDialog();
        addEditRecordsDialog.setArguments(bundle);
        addEditRecordsDialog.show(getParentFragmentManager(), "DialogAddRecord");
    }

    private void nextMonth() {
        selectedDate = selectedDate.plusMonths(1);
        observeRecord();
    }

    private void prevMonth() {
        selectedDate = selectedDate.minusMonths(1);
        observeRecord();
    }

    private void initDate(){
        selectedDate = LocalDate.now();
    }

    private void initRecyclerView(){
        adapterCalendar = new AdapterCalendar( this, requireContext());
        adapterInfoVisit = new AdapterInfoVisit(getViewLifecycleOwner(), requireActivity(), requireContext(), this);
        RecyclerView.LayoutManager layoutManager =  new GridLayoutManager(requireActivity(), 7);
        RecyclerView.LayoutManager layoutManager1 =  new LinearLayoutManager(requireActivity());
        binding.includeCalendar.calendarRv.setLayoutManager(layoutManager);
        binding.visitDateRv.setLayoutManager(layoutManager1);
    }


    private List<LocalDate> dayInMonthList(){
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
        return  daysInMonthList;
    }

    private String monthTitle(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru"));
        String month = selectedDate.format(dateTimeFormatter);
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        return month;
    }

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

    private void viewInfoCell(LocalDate date){
        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            List<Record> cellRecord = new ArrayList<>();

            for (int i = 0; i < records.size(); i++) {
                if (date.isEqual(records.get(i).getVisitDate())) {
                    cellRecord.add(records.get(i));
                }
            }
            if (cellRecord.isEmpty()){
                binding.titleVisitRv.setVisibility(View.GONE);
            } else {
                binding.titleVisitRv.setVisibility(View.VISIBLE);
            }
            adapterInfoVisit.setRecords(cellRecord);
            binding.visitDateRv.setAdapter(adapterInfoVisit);
        });
    }

    @Override
    public void onEditClick(Record record) {
        Bundle bundle = new Bundle();
        bundle.putString("logic", "edit");
        bundle.putParcelable("record", record);
        AddEditRecordsDialog addEditRecordsDialog = new AddEditRecordsDialog();
        addEditRecordsDialog.setArguments(bundle);
        addEditRecordsDialog.show(getParentFragmentManager(), "DialogEditRecord");
    }

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

    private void deleteMaterial(Record record) {
        RecordsViewModel recordsViewModel = new ViewModelProvider(requireActivity()).get(RecordsViewModel.class);
        recordsViewModel.delete(record);
    }
}