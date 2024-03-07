package com.example.kosandra.ui.client;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentHairstyleBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.dialogs.AddEditHairstyleDialog;
import com.example.kosandra.ui.client.dialogs.ConfirmationDialog;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;


public class HairstyleFragment extends Fragment {
    private FragmentHairstyleBinding binding;
    private HairstyleVisit hairstyleVisit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHairstyleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getHairstyleVisit();
        initMenu();
        observeHairstyle();
        setupViewingImage();
    }

    private void setupViewingImage() {
        binding.imageCardHairstyle.setOnClickListener(v -> {
            Drawable drawable = binding.imageCardHairstyle.getDrawable();
            if (drawable != null) {
                showFullScreenImage(drawable);
            }
        });
    }

    private void showFullScreenImage(Drawable drawable) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_photo_client);
        SubsamplingScaleImageView scaleImageView = dialog.findViewById(R.id.photo);

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        scaleImageView.setImage(ImageSource.bitmap(bitmap));
        dialog.show();
    }

    private void observeHairstyle() {
        HairstyleVisitViewModel model = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        model.getHairstyleVisit(hairstyleVisit.getId()).observe(getViewLifecycleOwner(), hairstyleVisit1 -> initField());
    }

    private void getHairstyleVisit(){
        hairstyleVisit = getArguments() != null ? getArguments().getParcelable("visit") : null;
    }
    private void initField(){
        if (hairstyleVisit != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hairstyleVisit.getPhotoHairstyle(), 0, hairstyleVisit.getPhotoHairstyle().length);
            binding.imageCardHairstyle.setImageBitmap(bitmap);
            binding.nameCardHairstyle.setText(hairstyleVisit.getHaircutName());
            binding.dateCardHairstyle.setText(hairstyleVisit.getVisitDate().toString());
            binding.timeCardHairstyle.setText(hairstyleVisit.getTimeSpent().toString());
            binding.priceCardHairstyle.setText(String.valueOf(hairstyleVisit.getHaircutCost()));
            binding.priceMaterialCardHairstyle.setText(String.valueOf(hairstyleVisit.getMaterialCost()));
            binding.weightCardHairstyle.setText(String.valueOf(hairstyleVisit.getMaterialWeight()));
        }
    }

    private void initMenu() {
        MenuProvider provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_card_hairstyle, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_edit_hairstyle){
                    editHairstyle();
                    return true;
                }
                if (menuItem.getItemId() == R.id.menu_delete_hairstyle){
                    openConfirmationDialog();
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    private void openConfirmationDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog(
                getContext(),
                "Подтверждение",
                "Вы уверены, что хотите удалить посещение?",
                (dialogInterface, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        deleteHairstyle();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialogInterface.dismiss();
                    }
                });

        dialog.show();
    }

    private void deleteHairstyle() {
        HairstyleVisitViewModel model = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        model.delete(hairstyleVisit);
        minusCountVisitClient();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack(R.id.clientCardFragment, false);
    }

    private void minusCountVisitClient () {
        Client client = getArguments() != null ? getArguments().getParcelable("client") : null;

        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);

        assert client != null;
        if (client.getNumberOfVisits() > 0){
            client.setNumberOfVisits(client.getNumberOfVisits() - 1);
            clientViewModel.update(client);
        }
    }

    private void editHairstyle() {
        AddEditHairstyleDialog dialog = new AddEditHairstyleDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogAddHairstyle");
    }

    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("hairstyle", hairstyleVisit);
        arg.putString("logic", "edit");
        return arg;
    }
}