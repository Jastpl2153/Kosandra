package com.example.kosandra.ui.financial_statistics.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyleRangeDate;

import java.util.ArrayList;
import java.util.List;

public class AdapterRVHairstyleIncome extends RecyclerView.Adapter<AdapterRVHairstyleIncome.Holder> {
    private final List<SqlIncomeHairstyleRangeDate> sqlIncomeHairstyleRangeDates = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_financial_statistics_income_hairstyle, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SqlIncomeHairstyleRangeDate sqlIncomeHairstyleRangeDate = sqlIncomeHairstyleRangeDates.get(position);
        holder.nameHairstyle.setText(sqlIncomeHairstyleRangeDate.getHaircutName());
        holder.countHairstyle.setText(String.valueOf(sqlIncomeHairstyleRangeDate.getCountHairstyle()));
        holder.priceHairstyle.setText(String.valueOf(sqlIncomeHairstyleRangeDate.getAvgPrice()));
    }

    @Override
    public int getItemCount() {
        return sqlIncomeHairstyleRangeDates.size();
    }

    public void setHairstyleIncomes(List<SqlIncomeHairstyleRangeDate> sqlIncomeHairstyleRangeDates) {
        this.sqlIncomeHairstyleRangeDates.clear();
        this.sqlIncomeHairstyleRangeDates.addAll(sqlIncomeHairstyleRangeDates);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView nameHairstyle;
        private TextView countHairstyle;
        private TextView priceHairstyle;

        public Holder(@NonNull View itemView) {
            super(itemView);
            nameHairstyle = itemView.findViewById(R.id.item_name_income_expenses);
            countHairstyle = itemView.findViewById(R.id.item_count_income_expenses);
            priceHairstyle = itemView.findViewById(R.id.item_price_income_expenses);
        }
    }
}
