package com.example.kosandra.ui.general_logic;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;

import com.example.kosandra.R;

/**
 * The SearchRecyclerView interface provides methods for handling menu creation, menu item selection,
 * and search query filtering in a RecyclerView.
 * The filter method is used to apply text filtering to the RecyclerView based on the input provided.
 */
public interface SearchRecyclerView extends MenuProvider, SearchView.OnQueryTextListener {

    /**
     * Called to inflate the menu items associated with the SearchRecyclerView.
     * @param menu         the Menu object to populate with menu items
     * @param menuInflater the MenuInflater used to inflate the menu
     */
    @Override
    void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater);

    @Override
    default boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    default boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when the query text is changed. This implementation filters the RecyclerView based on the new text.
     * @param newText the updated query text
     * @return true if the implementation handles the query change, false otherwise
     */
    @Override
    default boolean onQueryTextChange(String newText) {
        filter(newText);
        return true;
    }

    /**
     * Filters the RecyclerView based on the provided text.
     * @param text the text used to filter the RecyclerView items
     */
    void filter(String text);
}
