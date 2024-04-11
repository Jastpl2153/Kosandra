package com.example.kosandra.ui.records.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
import com.example.kosandra.ui.records.record_listener_rv.OnClickItemCalendar;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying visit records in a RecyclerView.
 * <p>
 * This class sets up the ViewHolder and binds the data to the views.
 * <p>
 * It also handles click events on the items in the RecyclerView.
 */
public class AdapterInfoVisit extends RecyclerView.Adapter<AdapterInfoVisit.ViewHolder> {
    private List<Record> records = new ArrayList<>();
    private LifecycleOwner lifecycleOwner;
    private ViewModelStoreOwner viewModelStoreOwner;
    private Context context;
    private OnClickItemCalendar onClickItemCalendar;

    /**
     * Constructor for AdapterInfoVisit.
     * Initializes the Adapter with necessary parameters.
     *
     * @param lifecycleOwner      The lifecycle owner for observing live data.
     * @param viewModelStoreOwner The ViewModel store owner for accessing ViewModels.
     * @param context             The context in which the Adapter is being used.
     * @param onClickItemCalendar Callback for item click events.
     */
    public AdapterInfoVisit(LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner, Context context, OnClickItemCalendar onClickItemCalendar) {
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.context = context;
        this.onClickItemCalendar = onClickItemCalendar;
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

    /**
     * Sets the list of records and notifies the Adapter of the data change.
     *
     * @param records The list of records to be displayed.
     */
    public void setRecords(List<Record> records) {
        this.records.clear();
        this.records.addAll(records);
        notifyDataSetChanged();
    }

    /**
     * Shows a PopupMenu for performing actions on a record item.
     *
     * @param view   The View to anchor the PopupMenu to.
     * @param record The record associated with the item.
     */
    private void showPopupMenu(View view, Record record) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.menu_record_visit);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_record_edit) {
                onClickItemCalendar.onEditClick(record);
                return true;
            } else if (itemId == R.id.menu_record_delete) {
                onClickItemCalendar.onDeleteClick(record);
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

        /**
         * Binds the record data to the views in the ViewHolder.
         * Also sets up click listener for the menu button.
         *
         * @param record The record object to bind data from.
         */
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
