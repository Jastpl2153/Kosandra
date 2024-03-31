package com.example.kosandra.ui.financial_statistics.financial_statistics_listener_rv;

import android.view.View;

import com.example.kosandra.entity.Income;

public interface RvFinanceClickListener<T>{
    void onClick(View item_layout, View but_delete, int id);
    void onLongClick(View item_layout, View but_delete,  int id);
    void onDeleteClick(View item_layout, View but_delete, T t);
}
