package com.example.kosandra.ui.records.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.Record;
import com.example.kosandra.ui.records.record_listener_rv.OnClickCalendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdapterCalendar extends RecyclerView.Adapter<AdapterCalendar.CalendarHolder> {
    private List<LocalDate> dayOfMonth = new ArrayList<>();
    private List<Record> recordsVisit = new ArrayList<>();
    private OnClickCalendar clickListener;
    private Context mContext;

    public AdapterCalendar(OnClickCalendar clickListener, Context mContext) {
        this.clickListener = clickListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterCalendar.CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_cell, parent, false);
        return new CalendarHolder(view, clickListener, dayOfMonth, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCalendar.CalendarHolder holder, int position) {
        holder.imageCell.setImageDrawable(null);
        LocalDate day = dayOfMonth.get(position);
        if (day == null) {
            holder.cell.setText("");
        } else {
            holder.cell.setText(String.valueOf(day.getDayOfMonth()));
            if (LocalDate.now().isEqual(day)) {
                holder.itemView.setBackgroundResource(R.drawable.ic_record_data_now_selected);
            }
        }
        if (!recordsVisit.isEmpty()) {
            for (int i = 0; i < recordsVisit.size(); i++) {
                if (day != null && day.isEqual(recordsVisit.get(i).getVisitDate())) {
                    holder.imageCell.setImageResource(R.drawable.ic_record_notifical_visit);
                    break;
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return dayOfMonth.size();
    }

    public void setDayOfMonth(List<LocalDate> dayOfMonth) {
        this.dayOfMonth.clear();
        this.dayOfMonth.addAll(dayOfMonth);
        notifyDataSetChanged();
    }

    public void setVisitClient(List<Record> recordsVisit) {
        this.recordsVisit = recordsVisit;
        notifyDataSetChanged();
    }

    public static class CalendarHolder extends RecyclerView.ViewHolder {
        private final TextView cell;
        private final ImageView imageCell;

        public CalendarHolder(@NonNull View itemView, OnClickCalendar clickListener, List<LocalDate> dayOfMonth, Context context) {
            super(itemView);
            cell = itemView.findViewById(R.id.tv_cell_calendar);
            imageCell = itemView.findViewById(R.id.notification_visit_cell);
            setHeight(context);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    LocalDate date = dayOfMonth.get(position);
                    clickListener.onClick(itemView, date);
                }
            });
        }

        private void setHeight(Context context){
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int cellSize = Math.min(screenWidth, screenHeight) / 7;
            params.width = cellSize;
            params.height = cellSize;
            itemView.setLayoutParams(params);
        }
    }
}
