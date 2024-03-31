package com.example.kosandra.ui.general_logic;

import android.view.View;

public interface RvItemClickListener<T> {
    void onClick(View item_layout, View but_delete, T t);
    void onLongClick(View item_layout, View but_delete, T t);
    void onDeleteClick(View item_layout, View but_delete, T t);
    void onPhotoClick(T t, View image);
}
