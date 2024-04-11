package com.example.kosandra.ui.general_logic;

import android.view.View;

/**
 * This interface defines methods to handle item click events within a RecyclerView.
 * <p>
 * These methods provide functionality to respond to regular click, long click, delete click,
 * <p>
 * and photo click actions on items displayed in the RecyclerView.
 *
 * @param <T> the type of data object associated with the item being clicked
 */
public interface RvItemClickListener<T> {
    /**
     * This method is invoked when a regular click event occurs on an item within the RecyclerView.
     *
     * @param item_layout the view representing the entire item in the RecyclerView
     * @param but_delete  the view representing the delete button in the item layout
     * @param t           the data object associated with the clicked item
     */
    void onClick(View item_layout, View but_delete, T t);

    /**
     * This method is invoked when a long click event occurs on an item within the RecyclerView.
     *
     * @param item_layout the view representing the entire item in the RecyclerView
     * @param but_delete  the view representing the delete button in the item layout
     * @param t           the data object associated with the clicked item
     */
    void onLongClick(View item_layout, View but_delete, T t);

    /**
     * This method is invoked when a click event occurs on the delete button of an item within the RecyclerView.
     *
     * @param item_layout the view representing the entire item in the RecyclerView
     * @param but_delete  the view representing the delete button in the item layout
     * @param t           the data object associated with the clicked item
     */
    void onDeleteClick(View item_layout, View but_delete, T t);

    /**
     * This method is invoked when a click event occurs on the photo associated with an item in the RecyclerView.
     *
     * @param t     the data object associated with the clicked item
     * @param image the view representing the photo associated with the item
     */
    void onPhotoClick(T t, View image);
}
