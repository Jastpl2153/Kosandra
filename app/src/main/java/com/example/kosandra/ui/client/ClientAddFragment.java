package com.example.kosandra.ui.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientAddBinding;

import java.util.ArrayList;

public class ClientAddFragment extends Fragment {
    private FragmentClientAddBinding binding;
    private ArrayList<ToggleButton> heartButtons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.numberVisitPicker.setMinValue(1);
        binding.numberVisitPicker.setMaxValue(10);

        binding.numberVisitPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                for (int i = 0; i < newVal; i++) {
                    ToggleButton toggleButton = (ToggleButton) binding.gridCountVisit.getChildAt(i);
                    toggleButton.setChecked(true);
                }
                for (int i = newVal; i < picker.getMaxValue() ; i++) {
                    ToggleButton toggleButton = (ToggleButton) binding.gridCountVisit.getChildAt(i);
                    toggleButton.setChecked(false);
                }
            }
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_save, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        return root;
    }
}