package com.example.kosandra.ui.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kosandra.databinding.FragmentHairstyleBinding;
import com.example.kosandra.entity.HairstyleVisit;


public class HairstyleFragment extends Fragment {
    private FragmentHairstyleBinding binding;
    private HairstyleVisit hairstyleVisit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHairstyleBinding.inflate(inflater, container, false);

        getHairstyleVisit();
        initPagerView2();

        return binding.getRoot();
    }

    private void initPagerView2() {

    }

    private void getHairstyleVisit(){
        hairstyleVisit = getArguments() != null ? getArguments().getParcelable("visit") : null;
    }
}