package com.example.kosandra.ui.material.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class AdapterViewPager2 extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;

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
