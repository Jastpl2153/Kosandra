package com.example.kosandra.ui.records.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.Record;
import com.example.kosandra.ui.records.record_listener_rv.OnClockItemCalendar;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterInfoVisit extends RecyclerView.Adapter<AdapterInfoVisit.ViewHolder> {
    private List<Record> records = new ArrayList<>();
    private LifecycleOwner lifecycleOwner;
    private ViewModelStoreOwner viewModelStoreOwner;
    private Context context;
    private OnClockItemCalendar onClockItemCalendar;

    public AdapterInfoVisit(LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner, Context context, OnClockItemCalendar onClockItemCalendar) {
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.context = context;
        this.onClockItemCalendar = onClockItemCalendar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_visit_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<Record> records){
        this.records.clear();
        this.records.addAll(records);
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view, Record record) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.menu_record_visit);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_record_edit) {
                onClockItemCalendar.onEditClick(record);
                return true;
            } else if (itemId == R.id.menu_record_delete) {
                onClockItemCalendar.onDeleteClick(record);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView name;
        private TextView hairstyle;
        private TextView cost;
        private ImageButton but_menu;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.info_time_calendar);
            name = itemView.findViewById(R.id.info_name_calendar);
            hairstyle = itemView.findViewById(R.id.info_hairstyle_calendar);
            cost = itemView.findViewById(R.id.info_cost_calendar);
            but_menu = itemView.findViewById(R.id.but_menu_visit);
        }

        void bind(Record record) {
            time.setText(record.getTimeSpent().toString());
            ClientViewModel clientViewModel = new ViewModelProvider(viewModelStoreOwner).get(ClientViewModel.class);
            clientViewModel.getClient(record.getClientId()).observe(lifecycleOwner, client -> {
                name.setText(client.getName());
            });
            hairstyle.setText(record.getHaircutName());
            cost.setText(String.valueOf(record.getCost()));

            but_menu.setOnClickListener(v -> showPopupMenu(but_menu, record));
        }
    }
}
