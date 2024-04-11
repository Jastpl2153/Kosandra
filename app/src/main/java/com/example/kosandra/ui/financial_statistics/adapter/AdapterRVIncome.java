package com.example.kosandra.ui.financial_statistics.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.Income;
import com.example.kosandra.ui.financial_statistics.financial_statistics_listener_rv.RvFinanceClickListener;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of Income items in a RecyclerView.
 * <p>
 * The AdapterRVIncome class is responsible for connecting the data of Income objects to the RecyclerView.
 * <p>
 * It also handles user interactions such as long-click and click events.
 * <p>
 * This adapter uses a custom listener interface RvFinanceClickListener to handle item clicks.
 */
public class AdapterRVIncome extends RecyclerView.Adapter<AdapterRVIncome.Holder> {
    private final List<Income> incomesList = new ArrayList<>();
    private final RvFinanceClickListener<Income> rvFinanceClickListener;

    /**
     * Constructor for AdapterRVIncome.
     * Initializes the adapter with the provided RvFinanceClickListener.
     *
     * @param rvFinanceClickListener The listener for item click events
     */
    public AdapterRVIncome(RvFinanceClickListener<Income> rvFinanceClickListener) {
        this.rvFinanceClickListener = rvFinanceClickListener;
    }

    @NonNull
    @Override
    public AdapterRVIncome.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_financial_statistics_income_or_expenses, parent, false);
        return new AdapterRVIncome.Holder(view, rvFinanceClickListener, incomesList);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRVIncome.Holder holder, int position) {
        Income income = incomesList.get(position);
        holder.name.setText(income.getNameIncome());
        holder.cost.setText(String.valueOf(income.getCost()));
        holder.date.setText(DatePickerHelperDialog.parseDateOutput(income.getDate()));
    }

    @Override
    public int getItemCount() {
        return incomesList.size();
    }

    /**
     * Sets the list of Income items to be displayed.
     *
     * @param incomesList The list of Income items to set
     */
    public void setIncomesList(List<Income> incomesList) {
        this.incomesList.clear();
        this.incomesList.addAll(incomesList);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cost;
        private TextView date;
        private ImageButton delete;
        private LinearLayout item_layout;

        public Holder(@NonNull View itemView, RvFinanceClickListener<Income> rvFinanceClickListener, List<Income> incomesList) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name_expenses_other);
            cost = itemView.findViewById(R.id.item_cost_expenses_other);
            date = itemView.findViewById(R.id.item_date_expenses_other);
            delete = itemView.findViewById(R.id.but_expenses_delete);
            item_layout = itemView.findViewById(R.id.item_rv_expenses_other);
            setupListeners(rvFinanceClickListener, incomesList);
        }

        /**
         * Sets up listeners for item clicks and long-clicks.
         *
         * @param rvFinanceClickListener The listener for item click events
         * @param incomesList            The list of Income items
         */
        private void setupListeners(RvFinanceClickListener<Income> rvFinanceClickListener, List<Income> incomesList) {
            itemView.setOnLongClickListener(v -> handleClick(rvFinanceClickListener, incomesList, true));
            itemView.setOnClickListener(v -> handleClick(rvFinanceClickListener, incomesList, false));
            delete.setOnClickListener(v -> handleDeleteClick(rvFinanceClickListener, incomesList));
        }

        /**
         * Handles the click events for the item.
         *
         * @param rvFinanceClickListener The listener for item click events
         * @param incomesList            The list of Income items
         * @param isLongClick            True if long-click, False if single click
         * @return True if event is handled, False otherwise
         */
        private boolean handleClick(RvFinanceClickListener<Income> rvFinanceClickListener, List<Income> incomesList, boolean isLongClick) {
            int position = getAdapterPosition();
            if (rvFinanceClickListener != null && position != RecyclerView.NO_POSITION) {
                Income income = incomesList.get(position);
                if (isLongClick) {
                    rvFinanceClickListener.onLongClick(item_layout, delete, income.getId());
                } else {
                    rvFinanceClickListener.onClick(item_layout, delete, income.getId());
                }
                return true;
            }
            return false;
        }

        /**
         * Handles the delete click event for an item.
         *
         * @param rvFinanceClickListener The listener for item click events
         * @param incomesList            The list of Income items
         */
        private void handleDeleteClick(RvFinanceClickListener<Income> rvFinanceClickListener, List<Income> incomesList) {
            int position = getAdapterPosition();
            if (rvFinanceClickListener != null && position != RecyclerView.NO_POSITION) {
                Income income = incomesList.get(position);
                rvFinanceClickListener.onDeleteClick(item_layout, delete, income);
                incomesList.remove(position);
                notifyItemRemoved(position);
            }
        }

    }
}