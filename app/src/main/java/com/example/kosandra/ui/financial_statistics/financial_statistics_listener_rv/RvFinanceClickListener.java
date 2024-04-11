package com.example.kosandra.ui.financial_statistics.financial_statistics_listener_rv;

import android.view.View;

import com.example.kosandra.entity.Income;

/**
 * Interface for handling click events in the RecyclerView related to finance items.
 * <p>
 * Implementing classes should override the onClick, onLongClick, and onDeleteClick methods.
 */
public interface RvFinanceClickListener<T> {
    /**
     * Called when a regular click event occurs on an item in the RecyclerView.
     *
     * @param item_layout The layout of the clicked item.
     * @param but_delete  The delete button associated with the item.
     * @param id          The unique identifier of the item.
     */
    void onClick(View item_layout, View but_delete, int id);

    /**
     * Called when a long click event occurs on an item in the RecyclerView.
     *
     * @param item_layout The layout of the long-clicked item.
     * @param but_delete  The delete button associated with the item.
     * @param id          The unique identifier of the long-clicked item.
     */
    void onLongClick(View item_layout, View but_delete, int id);

    /**
     * Called when the delete button associated with an item in the RecyclerView is clicked.
     *
     * @param item_layout The layout of the item for which the delete button was clicked.
     * @param but_delete  The delete button that was clicked.
     * @param t           The data object associated with the item.
     */
    void onDeleteClick(View item_layout, View but_delete, T t);
}
