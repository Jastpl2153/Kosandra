package com.example.kosandra.ui.material.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * AdapterViewPager2 is a custom FragmentStateAdapter that is responsible for managing the fragments displayed in a ViewPager2.
 * <p>
 * It extends FragmentStateAdapter and overrides methods to handle fragment creation and retrieval.
 */
public class AdapterViewPager2 extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;

    /**
     * Constructs a new AdapterViewPager2 with the given fragment and list of fragments to display.
     *
     * @param fragment     The parent Fragment containing the ViewPager2.
     * @param fragmentList The list of Fragments to display in the ViewPager2.
     */
    public AdapterViewPager2(@NonNull Fragment fragment, List<Fragment> fragmentList) {
        super(fragment);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
