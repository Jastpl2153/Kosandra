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

public class AdapterRVExpenses extends RecyclerView.Adapter<AdapterRVExpenses.Holder> {
    private final List<Expenses> expensesList = new ArrayList<>();

//    private final ExpensesClickItemRv expensesClickItemRv;
//    private final ClickListenerExpenses clickListenerExpenses;
        private final RvFinanceClickListener<Expenses> rvFinanceClickListener;

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

        public Holder(@NonNull View itemView, RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList ) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name_expenses_other);
            cost = itemView.findViewById(R.id.item_cost_expenses_other);
            date = itemView.findViewById(R.id.item_date_expenses_other);
            delete = itemView.findViewById(R.id.but_expenses_delete);
            item_layout = itemView.findViewById(R.id.item_rv_expenses_other);
            setupListeners(rvFinanceClickListener, expensesList);
        }


        private void setupListeners(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList ) {
            itemView.setOnLongClickListener(v -> handleClick(rvFinanceClickListener, expensesList, true));
            itemView.setOnClickListener(v -> handleClick(rvFinanceClickListener, expensesList, false));
            delete.setOnClickListener(v -> handleDeleteClick(rvFinanceClickListener, expensesList));
        }

        private boolean handleClick(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList , boolean isLongClick) {
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

        private void handleDeleteClick(RvFinanceClickListener<Expenses> rvFinanceClickListener, List<Expenses> expensesList ) {
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