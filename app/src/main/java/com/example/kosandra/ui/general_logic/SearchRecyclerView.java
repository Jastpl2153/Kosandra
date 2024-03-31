package com.example.kosandra.ui.general_logic;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;

import com.example.kosandra.R;

public interface SearchRecyclerView extends MenuProvider, SearchView.OnQueryTextListener {

    @Override
    default void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    default boolean onMenuItemSelected(@NonNull MenuItem menuItem){
        return false;
    }

    @Override
    default boolean onQueryTextSubmit(String query){
        return false;
    }

    @Override
    default boolean onQueryTextChange(String newText){
        filter(newText);
        return true;
    }

    void filter(String text);
}
