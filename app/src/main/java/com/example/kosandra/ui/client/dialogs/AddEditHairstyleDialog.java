package com.example.kosandra.ui.client.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosandra.R;
import com.example.kosandra.databinding.DialogClientAddEditHairstyleBinding;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.GalleryHandlerInterface;
import com.example.kosandra.ui.general_logic.TimePickerHelperDialog;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;
import com.example.kosandra.view_model.MaterialsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddEditHairstyleDialog extends BottomSheetDialogFragment implements GalleryHandlerInterface, EmptyFields {
    private DialogClientAddEditHairstyleBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;
    private MaterialsViewModel materialsViewModel;
    private HairstyleVisitViewModel hairstyleVisitViewModel;
    private Client client;
    private HairstyleVisit hairstyleVisit;
    private Bundle arguments;
    private int countFieldUseMaterial = 1;
    private List<AutoCompleteTextView> listCodeUseMaterials;
    private List<EditText> listCountUseMaterials;
    //
    private int[] prevCountMaterials;
    private String[] prevCodeMaterials;
    List<String> dateBaseListCodeMaterial;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogClientAddEditHairstyleBinding.inflate(inflater, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arguments = getArguments();
        initViewModel();
        getListCodeMaterials();
        initLogic();
        DatePickerHelperDialog.setupDatePicker(binding.etDateVisit);
        TimePickerHelperDialog.setupDatePicker(binding.etTimeCompleteHairstyle);
        binding.addEditPhotoHairstyle.setOnClickListener(v -> openGallery(getContentLauncher));
        setupGalleryResult(launcher -> getContentLauncher = launcher, getContext(), this, binding.addEditPhotoHairstyle);
        binding.butPlusUseMaterial.setOnClickListener(v -> addFieldUseMaterial());
        binding.butMinusUseMaterial.setOnClickListener(v -> minusFieldUseMaterial());
    }


    // Общие методы
    private void initViewModel(){
        materialsViewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        hairstyleVisitViewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
    }
    private void getListCodeMaterials(){
        dateBaseListCodeMaterial = new ArrayList<>();
        dateBaseListCodeMaterial = materialsViewModel.getAllMaterialsCode();
    }

    private void initLogic() {
        String logic = arguments.getString("logic");

        switch (logic) {
            case "add":
                addLogic();
                break;
            case "edit":
                editLogic();
                break;
        }
        buttonEnabled();
    }

    private void buttonEnabled() {
        binding.butMinusUseMaterial.setEnabled(!(countFieldUseMaterial == 1));
        binding.butMinusUseMaterial.setAlpha(countFieldUseMaterial == 1 ? 0.5f : 1f);

        binding.butPlusUseMaterial.setEnabled(!(countFieldUseMaterial == 5));
        binding.butPlusUseMaterial.setAlpha(countFieldUseMaterial == 5 ? 0.5f : 1f);
    }

    private void addLogic(){
        getClient();
        binding.saveHairstyle.setOnClickListener(v -> saveHairstyle());
        initFirstAutoCompleteInputSetCode();
    }

    private void editLogic() {
        getHairstyle();
        countFieldUseMaterial = hairstyleVisit.getCountMaterial().length;
        initField();
        binding.saveHairstyle.setOnClickListener(v -> editHairstyle());
    }

    private void initAutoCompleteInputSetCode(AutoCompleteTextView codeMaterial){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, dateBaseListCodeMaterial);
        codeMaterial.setAdapter(adapter);
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.etDateVisit.getText().toString().isEmpty() &&
                !binding.etNameHairstyle.getText().toString().isEmpty() &&
                !binding.etPriceHairstyle.getText().toString().isEmpty() &&
                !binding.etTimeCompleteHairstyle.getText().toString().isEmpty() &&
                !binding.etWeightMaterials.getText().toString().isEmpty() &&
                validateEmptyFieldsMaterials();
    }

    private boolean validateEmptyFieldsMaterials() {
        int countEmpty = 0;
        for (int i = 0; i < countFieldUseMaterial; i++) {
            if (!listCodeUseMaterials.get(i).getText().toString().isEmpty() && !listCountUseMaterials.get(i).getText().toString().isEmpty()) {
                countEmpty += 1;
            }
        }
        return countEmpty == countFieldUseMaterial;
    }

    private boolean validateCorrectnessMaterialCode(){
        int correctFields = 0;
        for (int i = 0; i < countFieldUseMaterial; i++) {
            boolean validate = false;
            for (String code: dateBaseListCodeMaterial) {
                if (code.trim().equals(listCodeUseMaterials.get(i).getText().toString().trim())){
                    listCodeUseMaterials.get(i).setTextColor(Color.BLACK);
                    correctFields +=1;
                    validate = true;
                    break;
                }
            }
            if (!validate){
                animateEmptyField(listCodeUseMaterials.get(i), requireContext());
            }
        }
        return correctFields == countFieldUseMaterial;
    }

    private void initFieldListMaterials() {
        listCodeUseMaterials = new ArrayList<>();
        listCountUseMaterials = new ArrayList<>();
        for (int i = 0; i < countFieldUseMaterial; i++) {
            LinearLayout linearLayout = (LinearLayout) binding.linearLayoutAddEditHairstyle.getChildAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 1 - (countFieldUseMaterial - i));
            AutoCompleteTextView codeMaterial = linearLayout.findViewById(R.id.use_code_materials);
            EditText countMaterial = linearLayout.findViewById(R.id.use_count_materials);

            listCodeUseMaterials.add(codeMaterial);
            listCountUseMaterials.add(countMaterial);
        }
    }

    private String[] initArrayCode() {
        String[] codeUseMaterials = new String[countFieldUseMaterial];

        for (int i = 0; i < countFieldUseMaterial; i++) {
            codeUseMaterials[i] = listCodeUseMaterials.get(i).getText().toString().trim();
        }
        return codeUseMaterials;
    }

    private int[] initArrayCount() {
        int[] countUseMaterials = new int[countFieldUseMaterial];

        for (int i = 0; i < countFieldUseMaterial; i++) {
            countUseMaterials[i] = Integer.parseInt(listCountUseMaterials.get(i).getText().toString().trim());
        }
        return countUseMaterials;
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.etDateVisit);
        isEmpty(binding.etNameHairstyle);
        isEmpty(binding.etPriceHairstyle);
        isEmpty(binding.etTimeCompleteHairstyle);
        isEmpty(binding.etWeightMaterials);
        handlerEmptyFieldUseMaterials();
    }

    private void handlerEmptyFieldUseMaterials(){
        for (int i = 0; i < countFieldUseMaterial; i++) {
            isEmpty(listCodeUseMaterials.get(i));
            isEmpty(listCountUseMaterials.get(i));
        }
    }

    private void addFieldUseMaterial() {
        if (countFieldUseMaterial < 5) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_client_add_hairstyle_materials, binding.linearLayoutAddEditHairstyle, false);
            binding.linearLayoutAddEditHairstyle.addView(view, binding.linearLayoutAddEditHairstyle.getChildCount() - 1);
            LinearLayout linearLayout = (LinearLayout) binding.linearLayoutAddEditHairstyle.getChildAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
            AutoCompleteTextView autoCompleteTextView = linearLayout.findViewById(R.id.use_code_materials);
            initAutoCompleteInputSetCode(autoCompleteTextView);
            countFieldUseMaterial++;
        }
        buttonEnabled();
    }

    private void minusFieldUseMaterial() {
        if (countFieldUseMaterial > 1) {
            binding.linearLayoutAddEditHairstyle.removeViewAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
            countFieldUseMaterial--;
        }
        buttonEnabled();
    }

    private Future<Integer> priceMaterials() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < countFieldUseMaterial; i++) {
            final int index = i;
            futures.add(executorService.submit(() -> {
                Materials materials = materialsViewModel.getMaterial(listCodeUseMaterials.get(index).getText().toString().trim());
                return materials.getCost() * Integer.parseInt(listCountUseMaterials.get(index).getText().toString());
            }));
        }

        return executorService.submit(() -> {
            int totalPrice = 0;
            for (Future<Integer> future : futures) {
                totalPrice += future.get();
            }
            executorService.shutdown();
            return totalPrice;
        });
    }

    // Методы для добавления прически
    private void getClient() {
        if (arguments != null) {
            client = arguments.getParcelable("client");
        }
    }

    private void saveHairstyle(){
        try {
            HairstyleVisit visit = initHairstyleVisit();
            if (visit != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    saveVisit(visit);
                    dismiss();
                });
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HairstyleVisit initHairstyleVisit() throws ExecutionException, InterruptedException {
        initFieldListMaterials();
        if (validateEmptyFields() && validateCorrectnessMaterialCode()) {
            return new HairstyleVisit(
                    DatePickerHelperDialog.parseDateDataBase(binding.etDateVisit.getText().toString()),
                    getImageGallery(binding.addEditPhotoHairstyle.getDrawable(), getResources()),
                    binding.etNameHairstyle.getText().toString(),
                    client.getId(),
                    Integer.parseInt(binding.etPriceHairstyle.getText().toString()),
                    Integer.parseInt(String.valueOf(priceMaterials().get())),
                    TimePickerHelperDialog.parseTime(binding.etTimeCompleteHairstyle.getText().toString()),
                    Integer.parseInt(binding.etWeightMaterials.getText().toString()),
                    initArrayCode(),
                    initArrayCount()
            );
        } else {
            handleEmptyFields();
            return null;
        }
    }

    private void saveVisit(HairstyleVisit visit) {
        KosandraDataBase.getInstance(requireContext()).runInTransaction(() -> {
            hairstyleVisitViewModel.insert(visit);
            updateClientVisitCount();
            updateMaterialsCount(visit);
        });
    }

    private void updateClientVisitCount() {
        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        client.setNumberOfVisits(client.getNumberOfVisits() + 1);
        clientViewModel.update(client);
    }

    private void updateMaterialsCount(HairstyleVisit visit) {
        for (int i = 0; i < countFieldUseMaterial; i++) {
            String materialCode = visit.getCodeMaterial()[i].trim();
            int countMaterial = visit.getCountMaterial()[i];
            Materials material = materialsViewModel.getMaterial(materialCode);
            if (material != null) {
                material.setCount(material.getCount() - countMaterial);
                materialsViewModel.update(material);
            }
        }
    }

    private void initFirstAutoCompleteInputSetCode(){
        LinearLayout linearLayout = (LinearLayout) binding.linearLayoutAddEditHairstyle.getChildAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
        AutoCompleteTextView autoCompleteTextView = linearLayout.findViewById(R.id.use_code_materials);
        initAutoCompleteInputSetCode(autoCompleteTextView);
    }

    //Методы для изменения прически
    private void getHairstyle() {
        if (arguments != null) {
            hairstyleVisit = arguments.getParcelable("hairstyle");
        }
    }

    private void initField() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(hairstyleVisit.getPhotoHairstyle(), 0, hairstyleVisit.getPhotoHairstyle().length);
        binding.addEditPhotoHairstyle.setImageBitmap(bitmap);
        binding.etNameHairstyle.setText(hairstyleVisit.getHaircutName());
        binding.etDateVisit.setText(DatePickerHelperDialog.parseDateOutput(hairstyleVisit.getVisitDate()));
        binding.etTimeCompleteHairstyle.setText(hairstyleVisit.getTimeSpent().toString());
        binding.etPriceHairstyle.setText(String.valueOf(hairstyleVisit.getHaircutCost()));
        binding.etWeightMaterials.setText(String.valueOf(hairstyleVisit.getMaterialWeight()));
        initFieldUseMaterials();
    }

    private void initFieldUseMaterials() {
        for (int i = 0; i < countFieldUseMaterial; i++) {
            LinearLayout linearLayout;
            if (i == 0) {
                linearLayout = (LinearLayout) binding.linearLayoutAddEditHairstyle.getChildAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
            } else {
                linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_client_add_hairstyle_materials, binding.linearLayoutAddEditHairstyle, false);
                binding.linearLayoutAddEditHairstyle.addView(linearLayout, binding.linearLayoutAddEditHairstyle.getChildCount() - 1);
            }

            AutoCompleteTextView codeMaterial = linearLayout.findViewById(R.id.use_code_materials);
            initAutoCompleteInputSetCode(codeMaterial);

            EditText countMaterial = linearLayout.findViewById(R.id.use_count_materials);

            codeMaterial.setText(hairstyleVisit.getCodeMaterial()[i]);
            countMaterial.setText(String.valueOf(hairstyleVisit.getCountMaterial()[i]));
        }
    }

    private void editHairstyle() {
        initFieldListMaterials();
        if (validateEmptyFields() && validateCorrectnessMaterialCode()) {
            savePrevCountUseMaterials();
            setHairstyle();
            editHairstyleDataBase();
        } else {
            handleEmptyFields();
        }
    }

    private void setHairstyle() {
        try {
            hairstyleVisit.setPhotoHairstyle(getImageGallery(binding.addEditPhotoHairstyle.getDrawable(), getResources()));
            hairstyleVisit.setHaircutName(binding.etNameHairstyle.getText().toString());
            hairstyleVisit.setVisitDate(DatePickerHelperDialog.parseDateDataBase(binding.etDateVisit.getText().toString()));
            hairstyleVisit.setTimeSpent(TimePickerHelperDialog.parseTime(binding.etTimeCompleteHairstyle.getText().toString()));
            hairstyleVisit.setHaircutCost(Integer.parseInt(binding.etPriceHairstyle.getText().toString()));
            hairstyleVisit.setMaterialCost(priceMaterials().get());
            hairstyleVisit.setMaterialWeight(Integer.parseInt(binding.etWeightMaterials.getText().toString()));
            hairstyleVisit.setCodeMaterial(initArrayCode());
            hairstyleVisit.setCountMaterial(initArrayCount());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void savePrevCountUseMaterials() {
        prevCountMaterials = hairstyleVisit.getCountMaterial();
        prevCodeMaterials = hairstyleVisit.getCodeMaterial();
    }

    private void editHairstyleDataBase(){
        Executors.newSingleThreadExecutor().execute(() -> {
            KosandraDataBase.getInstance(requireContext()).runInTransaction(() -> {
                hairstyleVisitViewModel.update(hairstyleVisit);
                updateMaterialCount();
                dismiss();
            });
        });
    }

    private void updateMaterialCount(){
        for (int i = 0; i < prevCodeMaterials.length; i++) {
            Materials prevMaterial = materialsViewModel.getMaterial(prevCodeMaterials[i]);
            prevMaterial.setCount(prevMaterial.getCount() + prevCountMaterials[i]);
            materialsViewModel.updateNoThread(prevMaterial);
        }
        for (int i = 0; i < countFieldUseMaterial; i++) {
            Materials newMaterials = materialsViewModel.getMaterial(hairstyleVisit.getCodeMaterial()[i]);
            newMaterials.setCount(newMaterials.getCount() - hairstyleVisit.getCountMaterial()[i]);
            materialsViewModel.updateNoThread(newMaterials);
        }
    }
}