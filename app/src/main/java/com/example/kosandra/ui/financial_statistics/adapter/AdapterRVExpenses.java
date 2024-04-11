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
import com.example.kosandra.entity.Expenses;
import com.example.kosandra.ui.financial_statistics.financial_statistics_listener_rv.RvFinanceClickListener;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * AdapterRVExpenses class is responsible for managing the RecyclerView adapter for displaying a list of expenses.
 * It binds the expense data to the corresponding views and handles click events on each item.
 */
public class AdapterRVExpenses extends RecyclerView.Adapter<AdapterRVExpenses.Holder> {
    private final List<Expenses> expensesList = new ArrayList<>();
    private final RvFinanceClickListener<Expenses> rvFinanceClickListener;

    /**
     * Constructor for AdapterRVExpenses that initializes the RvFinanceClickListener.
     */
    public AdapterRVExpenses(RvFinanceClickListener<Expenses> rvFinanceClickListener) {
        this.rvFinanceClickListener = rvFinanceClickListener;
    }

    @NonNull
    @Override
    public AdapterRVExpenses.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_financial_statistics_income_or_expenses, parent, false);
        return new AdapterRVExpenses.Holder(view, rvFinanceClickListener, expensesList);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRVExpenses.Holder holder, int position) {
        Expenses expenses = expensesList.get(position);
        holder.name.setText(expenses.getNameExpenses());
        holder.cost.setText(String.valueOf(expenses.getCost()));
        holder.date.setText((DatePickerHelperDialog.parseDateOutput(expenses.getDate())));
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    /**
     * Sets the list of expenses to be displayed in the RecyclerView.
     * Clears the current list, adds all items from the provided list, and notifies data set changes.
     *
     * @param expensesList The list of Expenses to set in the adapter
     */
    public void setExpensesList(List<Expenses> expensesList) {
        this.expensesList.clear();
        this.expensesList.addAll(expensesList);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cost;
        private TextView date;
        private ImageButton delete;
        private LinearLayout item_layout;

        public Holder(@NonNull View itemView, RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name_expenses_other);
            cost = itemView.findViewById(R.id.item_cost_expenses_other);
            date = itemView.findViewById(R.id.item_date_expenses_other);
            delete = itemView.findViewById(R.id.but_expenses_delete);
            item_layout = itemView.findViewById(R.id.item_rv_expenses_other);
            setupListeners(rvFinanceClickListener, expensesList);
        }


        /**
         * Sets up click listeners for handling different click events on the item.
         *
         * @param rvFinanceClickListener The click listener for handling item interactions
         * @param expensesList           The list of Expenses associated with the item
         */
        private void setupListeners(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList) {
            itemView.setOnLongClickListener(v -> handleClick(rvFinanceClickListener, expensesList, true));
            itemView.setOnClickListener(v -> handleClick(rvFinanceClickListener, expensesList, false));
            delete.setOnClickListener(v -> handleDeleteClick(rvFinanceClickListener, expensesList));
        }

        /**
         * Handles click event on the item based on the click type (long click or short click).
         *
         * @param rvFinanceClickListener The click listener for handling item interactions
         * @param expensesList           The list of Expenses associated with the item
         * @param isLongClick            A flag indicating the click type
         * @return true if the click event is handled, false otherwise
         */
        private boolean handleClick(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList, boolean isLongClick) {
            int position = getAdapterPosition();
            if (rvFinanceClickListener != null && position != RecyclerView.NO_POSITION) {
                Expenses expenses = expensesList.get(position);
                if (isLongClick) {
                    rvFinanceClickListener.onLongClick(item_layout, delete, expenses.getId());
                } else {
                    rvFinanceClickListener.onClick(item_layout, delete, expenses.getId());
                }
                return true;
            }
            return false;
        }

        /**
         * Handles the delete click event on the item.
         *
         * @param rvFinanceClickListener The click listener for handling item interactions
         * @param expensesList           The list of Expenses associated with the item
         */
        private void handleDeleteClick(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList) {
            int position = getAdapterPosition();
            if (rvFinanceClickListener != null && position != RecyclerView.NO_POSITION) {
                Expenses expenses = expensesList.get(position);
                rvFinanceClickListener.onDeleteClick(item_layout, delete, expenses);
                expensesList.remove(position);
                notifyItemRemoved(position);
            }
        }

    }
}