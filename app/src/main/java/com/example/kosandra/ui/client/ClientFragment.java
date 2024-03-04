package com.example.kosandra.ui.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.client.adapter.AdapterRVListClients;
import com.example.kosandra.ui.client.interface_recycle_view.ClientClickListener;
import com.example.kosandra.view_model.ClientViewModel;

public class ClientFragment extends Fragment implements ClientClickListener {
    private FragmentClientBinding binding;
    private AdapterRVListClients adapter;
    private ClientViewModel clientViewModel;
    private int positionSelection = -1;
    private View prevItemLayout= null;
    private View prevButDelete = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initRecyclerView();
        listenerAddClient();
        getAllClientAdapter();
        return root;
    }

    private void initRecyclerView() {
        clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        adapter = new AdapterRVListClients(this);
        binding.rvClient.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClient.setAdapter(adapter);
    }

    private void listenerAddClient(){
        binding.butAddClient.setOnClickListener(
                v -> Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_open_add_client));
    }

    private void getAllClientAdapter(){
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClientClicked(View item_layout, View but_delete, Client client) {
        if (positionSelection == client.getId()) {
            animateItemDeselected(item_layout, but_delete);
        } else {
            Bundle args = new Bundle();
            args.putParcelable("client", client);
            Navigation.findNavController(item_layout)
                    .navigate(R.id.action_navigation_open_card_client, args);
        }
    }

    @Override
    public void onClientLongClicked(View item_layout, View but_delete, Client client) {
        if (positionSelection == -1 || positionSelection == client.getId()) {
            positionSelection = client.getId();
            animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null ){
            positionSelection = client.getId();
            animateItemSelected(item_layout, but_delete);
            animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }
    @Override
    public void onViewPhotoClicked(Client client, View client_image) {
        if (positionSelection != client.getId()) {
            Dialog dialog = new Dialog(client_image.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
            dialog.setContentView(R.layout.dialog_photo_client);

            SubsamplingScaleImageView imageView = dialog.findViewById(R.id.photo);

            Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
            imageView.setImage(ImageSource.bitmap(bitmap));

            dialog.show();
        }
    }
    @Override
    public void onDeleteClicked(View item_layout, View but_delete, Client client) {
        clientViewModel.delete(client);
        cancelAnimation(item_layout, but_delete);
    }

    private void cancelAnimation(View item_layout, View but_delete) {
        item_layout.animate().translationX(0).setDuration(0).start();
        but_delete.setVisibility(View.INVISIBLE);
        item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), android.R.color.white));
        positionSelection = -1;
    }
    private void animateItemSelected(View item_layout, View but_delete) {
        int offset = item_layout.getWidth() / 5;

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(item_layout, "translationX", 0, -offset);
        setupAnimator(translationXAnimator, true, item_layout, but_delete);
        translationXAnimator.start();
    }
    private void animateItemDeselected(View item_layout, View but_delete) {
        int offset = but_delete.getWidth();

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(item_layout, "translationX", -offset, 0);
        setupAnimator(translationXAnimator, false, item_layout, but_delete);
        translationXAnimator.start();
    }
    private void setupAnimator(ObjectAnimator animator, boolean isSelected, View item_layout, View but_delete){
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isSelected){
                    item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.blue_200));
                } else {
                    but_delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSelected){
                    but_delete.setVisibility(View.VISIBLE);
                } else {
                    item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.white));
                }
            }
        });
    }
}