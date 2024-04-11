package com.example.kosandra.ui.material.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMainMaterialsBinding;
import com.example.kosandra.ui.general_logic.SearchRecyclerView;
import com.example.kosandra.ui.material.adapter.AdapterViewPager2;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fragment class representing the main materials screen in the application.
 * <p>
 * Implements SearchRecyclerView interface for handling search functionality.
 */
public class MaterialMainFragment extends Fragment implements SearchRecyclerView {

    private FragmentMainMaterialsBinding binding;
    private List<Fragment> fragmentList = new ArrayList<>();
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainMaterialsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentMaterial();
        setAdapterMediator();
        binding.butAddMaterials.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_material_to_add_material_navigation));
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    /**
     * Initializes fragment materials with specific types.
     */
    private void initFragmentMaterial() {
        fragmentList.add(MaterialTabFragment.newInstance("Канекалон"));
        fragmentList.add(MaterialTabFragment.newInstance("Кудри"));
        fragmentList.add(MaterialTabFragment.newInstance("Термоволокно"));
    }

    /**
     * Sets up adapter mediator for ViewPager2 with provided fragments and corresponding tabs.
     */
    private void setAdapterMediator() {
        AdapterViewPager2 adapterViewPager2 = new AdapterViewPager2(this, fragmentList);
        binding.pagerFragmentMaterial.setAdapter(adapterViewPager2);
        new TabLayoutMediator(binding.tabTypeMaterials, binding.pagerFragmentMaterial, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Канекалон");
                    break;
                }
                case 1: {
                    tab.setText("Кудри");
                    break;
                }
                case 2: {
                    tab.setText("Термоволокно");
                    break;
                }
            }
        }).attach();
    }

    /**
     * Called when the Fragment is no longer visible to the user. Clears and sets the adapter to null.
     */
    @Override
    public void onPause() {
        super.onPause();
        binding.pagerFragmentMaterial.setAdapter(null);
    }

    /**
     * Called when the Fragment is no longer started. Collapses the search view.
     */
    @Override
    public void onStop() {
        super.onStop();
        searchView.onActionViewCollapsed();
    }

    /**
     * Creates the menu for the fragment using the menu inflater. Sets up the search view and query listener.
     *
     * @param menu         The options menu in which to place your items
     * @param menuInflater The MenuInflater object that can be used to inflate the menu
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_material_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_material_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    /**
     * Handles click events for menu items. Sorts materials based on selected criteria.
     *
     * @param menuItem The selected menu item
     * @return True if the event was handled, false otherwise
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        int currentItem = binding.pagerFragmentMaterial.getCurrentItem();
        Fragment fragment = fragmentList.get(currentItem);
        if (itemId == R.id.menu_sort_material_name_asc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getNameAllMaterialSortedByColorAscending();
            }
            return true;
        } else if (itemId == R.id.menu_sort_name_material_desc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getNameAllMaterialSortedByColorDescending();
            }
            return true;
        } else if (itemId == R.id.menu_sort_count_material_asc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getNameAllMaterialSortedByCountAscending();
            }
            return true;
        } else if (itemId == R.id.menu_sort_count_material_desc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getNameAllMaterialSortedByCountDescending();
            }
            return true;
        } else if (itemId == R.id.menu_sort_popular_material_asc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getAllMaterialSortedByRatingAscending();
            }
            return true;
        } else if (itemId == R.id.menu_sort_popular_material_desc) {
            if (fragment instanceof MaterialTabFragment) {
                ((MaterialTabFragment) fragment).getAllMaterialSortedByRatingDescending();
            }
            return true;
        }
        return false;
    }

    /**
     * Filters the data based on the provided text in the search view.
     *
     * @param text The text to filter by
     */
    @Override
    public void filter(String text) {
        int currentItem = binding.pagerFragmentMaterial.getCurrentItem();
        Fragment fragment = fragmentList.get(currentItem);
        if (fragment instanceof MaterialTabFragment) {
            ((MaterialTabFragment) fragment).filter(text);
        }
    }
}