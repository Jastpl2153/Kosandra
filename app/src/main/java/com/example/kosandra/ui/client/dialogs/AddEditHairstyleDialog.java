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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class representing a dialog for adding or editing a client's hairstyle, extending BottomSheetDialogFragment.
 * Implements GalleryHandlerInterface and EmptyFields for handling gallery functions and empty field validation.
 */
public class AddEditHairstyleDialog extends BottomSheetDialogFragment implements GalleryHandlerInterface, EmptyFields {
    private DialogClientAddEditHairstyleBinding binding; // Binding for dialog layout
    private ActivityResultLauncher<String> getContentLauncher; // Launcher for gallery content
    private MaterialsViewModel materialsViewModel; // ViewModel for materials
    private HairstyleVisitViewModel hairstyleVisitViewModel; // ViewModel for hairstyle visits
    private Client client; // Current client
    private HairstyleVisit hairstyleVisit; // Current hairstyle visit
    private Bundle arguments; // Arguments bundle
    private int countFieldUseMaterial = 1; // Count of material fields
    private List<AutoCompleteTextView> listCodeUseMaterials; // List of code input fields
    private List<EditText> listCountUseMaterials; // List of count input fields
    private int[] prevCountMaterials; // Previous count of materials
    private String[] prevCodeMaterials; // Previous codes of materials
    private List<String> dateBaseListCodeMaterial; // List of all material codes from database

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

    /**
     * Initializes the view models for materials and hairstyle visits.
     */
    private void initViewModel() {
        materialsViewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        hairstyleVisitViewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
    }

    /**
     * Retrieves the list of material codes from the ViewModel.
     */
    private void getListCodeMaterials() {
        dateBaseListCodeMaterial = new ArrayList<>();
        dateBaseListCodeMaterial = materialsViewModel.getAllMaterialsCode();
    }

    /**
     * Initializes the logic based on the provided arguments.
     * It determines which logic function to call based on the "logic" string parameter.
     */
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

    /**
     * Enables or disables buttons based on the countFieldUseMaterial value.
     * Sets the alpha value of buttons based on their enabling status.
     */
    private void buttonEnabled() {
        binding.butMinusUseMaterial.setEnabled(!(countFieldUseMaterial == 1));
        binding.butMinusUseMaterial.setAlpha(countFieldUseMaterial == 1 ? 0.5f : 1f);

        binding.butPlusUseMaterial.setEnabled(!(countFieldUseMaterial == 5));
        binding.butPlusUseMaterial.setAlpha(countFieldUseMaterial == 5 ? 0.5f : 1f);
    }

    private void addLogic() {
        getClient();
        binding.saveHairstyle.setOnClickListener(v -> saveHairstyle());
        initFirstAutoCompleteInputSetCode();
    }

    /**
     * Executes logic for adding a new item.
     * Retrieves client information, sets click listener for saving, and initializes AutoCompleteTextView.
     */
    private void editLogic() {
        getHairstyle();
        countFieldUseMaterial = hairstyleVisit.getCountMaterial().length;
        initField();
        binding.saveHairstyle.setOnClickListener(v -> editHairstyle());
    }

    /**
     * Initializes the AutoCompleteTextView with an adapter of code material list.
     *
     * @param codeMaterial AutoCompleteTextView to be initialized
     */
    private void initAutoCompleteInputSetCode(AutoCompleteTextView codeMaterial) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, dateBaseListCodeMaterial);
        codeMaterial.setAdapter(adapter);
    }

    /**
     * Validates if all required fields are not empty for input.
     *
     * @return boolean value indicating validation result
     */
    @Override
    public boolean validateEmptyFields() {
        return !binding.etDateVisit.getText().toString().isEmpty() &&
                !binding.etNameHairstyle.getText().toString().isEmpty() &&
                !binding.etPriceHairstyle.getText().toString().isEmpty() &&
                !binding.etTimeCompleteHairstyle.getText().toString().isEmpty() &&
                !binding.etWeightMaterials.getText().toString().isEmpty() &&
                validateEmptyFieldsMaterials();
    }

    /**
     * Validates if all required material fields are not empty for input.
     *
     * @return boolean value indicating validation result
     */
    private boolean validateEmptyFieldsMaterials() {
        int countEmpty = 0;
        for (int i = 0; i < countFieldUseMaterial; i++) {
            if (!listCodeUseMaterials.get(i).getText().toString().isEmpty() && !listCountUseMaterials.get(i).getText().toString().isEmpty()) {
                countEmpty += 1;
            }
        }
        return countEmpty == countFieldUseMaterial;
    }

    /**
     * Validates if the material code inputs are correct based on the database code list.
     *
     * @return boolean value indicating validation result
     */
    private boolean validateCorrectnessMaterialCode() {
        int correctFields = 0;
        for (int i = 0; i < countFieldUseMaterial; i++) {
            boolean validate = false;
            for (String code : dateBaseListCodeMaterial) {
                if (code.trim().equals(listCodeUseMaterials.get(i).getText().toString().trim())) {
                    listCodeUseMaterials.get(i).setTextColor(Color.BLACK);
                    correctFields += 1;
                    validate = true;
                    break;
                }
            }
            if (!validate) {
                animateEmptyField(listCodeUseMaterials.get(i), requireContext());
            }
        }
        return correctFields == countFieldUseMaterial;
    }

    /**
     * Initializes the list of material code and count fields.
     */
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

    /**
     * Initializes an array of material code values based on user input.
     *
     * @return String array of material codes
     */
    private String[] initArrayCode() {
        String[] codeUseMaterials = new String[countFieldUseMaterial];
        for (int i = 0; i < countFieldUseMaterial; i++) {
            codeUseMaterials[i] = listCodeUseMaterials.get(i).getText().toString().trim();
        }
        return codeUseMaterials;
    }

    /**
     * Initializes an array of material count values based on user input.
     *
     * @return integer array of material counts
     */
    private int[] initArrayCount() {
        int[] countUseMaterials = new int[countFieldUseMaterial];

        for (int i = 0; i < countFieldUseMaterial; i++) {
            countUseMaterials[i] = Integer.parseInt(listCountUseMaterials.get(i).getText().toString().trim());
        }
        return countUseMaterials;
    }

    /**
     * Handles empty fields by checking and highlighting them.
     */
    @Override
    public void handleEmptyFields() {
        isEmpty(binding.etDateVisit);
        isEmpty(binding.etNameHairstyle);
        isEmpty(binding.etPriceHairstyle);
        isEmpty(binding.etTimeCompleteHairstyle);
        isEmpty(binding.etWeightMaterials);
        handlerEmptyFieldUseMaterials();
    }

    /**
     * Handles empty material fields by checking and highlighting them.
     */
    private void handlerEmptyFieldUseMaterials() {
        for (int i = 0; i < countFieldUseMaterial; i++) {
            isEmpty(listCodeUseMaterials.get(i));
            isEmpty(listCountUseMaterials.get(i));
        }
    }

    /**
     * Adds a new field for entering material.
     */
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

    /**
     * Deletes the field for entering the material
     */
    private void minusFieldUseMaterial() {
        if (countFieldUseMaterial > 1) {
            binding.linearLayoutAddEditHairstyle.removeViewAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
            countFieldUseMaterial--;
        }
        buttonEnabled();
    }

    /**
     * Calculates the total price of materials asynchronously.
     *
     * @return Future<Integer> representing the total price of materials
     */
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

    /**
     * Retrieves the client details from the arguments bundle.
     * If arguments is not null, sets the client object with the parcelable data.
     */
    private void getClient() {
        if (arguments != null) {
            client = arguments.getParcelable("client");
        }
    }

    /**
     * Saves the hairstyle visit by initializing the HairstyleVisit object
     * and executing the save operation in a background thread.
     * Handles exceptions related to asynchronous execution.
     */
    private void saveHairstyle() {
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

    /**
     * Initializes the HairstyleVisit object after validating input fields
     * and material codes. Handles empty fields and returns null if validation fails.
     *
     * @return The initialized HairstyleVisit object or null if validation fails.
     * @throws ExecutionException   if an execution error occurs
     * @throws InterruptedException if the execution is interrupted
     */
    private HairstyleVisit initHairstyleVisit() throws ExecutionException, InterruptedException {
        initFieldListMaterials();
        if (validateEmptyFields() && validateCorrectnessMaterialCode()) {
            return new HairstyleVisit(
                    DatePickerHelperDialog.parseDateDataBase(binding.etDateVisit.getText().toString()),
                    getImageGallery(binding.addEditPhotoHairstyle.getDrawable(), getResources()),
                    binding.etNameHairstyle.getText().toString().trim(),
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

    /**
     * Saves the HairstyleVisit object by executing DB transactions
     * to insert the visit, update client visit count, and update material counts.
     */
    private void saveVisit(HairstyleVisit visit) {
        KosandraDataBase.getInstance(requireContext()).runInTransaction(() -> {
            hairstyleVisitViewModel.insert(visit);
            updateClientVisitCount();
            updateMaterialsCount(visit);
        });
    }

    /**
     * Updates the client's visit count by incrementing it and updating the client object.
     */
    private void updateClientVisitCount() {
        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        client.setNumberOfVisits(client.getNumberOfVisits() + 1);
        clientViewModel.update(client);
    }

    /**
     * Updates the count and ratings of materials used in the HairstyleVisit.
     * Retrieves the material, updates count and rating, and persists the changes.
     *
     * @param visit The HairstyleVisit object containing material details
     */
    private void updateMaterialsCount(HairstyleVisit visit) {
        for (int i = 0; i < countFieldUseMaterial; i++) {
            String materialCode = visit.getCodeMaterial()[i].trim();
            int countMaterial = visit.getCountMaterial()[i];
            Materials material = materialsViewModel.getMaterial(materialCode);
            if (material != null) {
                material.setCount(material.getCount() - countMaterial);
                material.setRating(material.getRating() + 1);
                materialsViewModel.update(material);
            }
        }
    }

    /**
     * Initializes the first AutoCompleteTextView input with material code.
     * Retrieves the AutoCompleteTextView from the layout and sets the code input.
     */
    private void initFirstAutoCompleteInputSetCode() {
        LinearLayout linearLayout = (LinearLayout) binding.linearLayoutAddEditHairstyle.getChildAt(binding.linearLayoutAddEditHairstyle.getChildCount() - 2);
        AutoCompleteTextView autoCompleteTextView = linearLayout.findViewById(R.id.use_code_materials);
        initAutoCompleteInputSetCode(autoCompleteTextView);
    }

    /**
     * Retrieve the hairstyle details from the arguments bundle.
     * If arguments are not null, assign the Parcelable "hairstyle" to the hairstyleVisit variable.
     */
    private void getHairstyle() {
        if (arguments != null) {
            hairstyleVisit = arguments.getParcelable("hairstyle");
        }
    }

    /**
     * Initialize the fields with hairstyle details.
     * Set the bitmap, name, date, time, price, and weight of the hairstyle.
     * Initialize the materials used in the hairstyle.
     */
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

    /**
     * Initialize the materials used in the hairstyle by iterating over the countFieldUseMaterial.
     * Add the material layout dynamically and set the code and count for each material.
     */
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

    /**
     * Edit the existing hairstyle by validating fields and material codes.
     * Save previous material counts, update the hairstyle details, and update the database.
     * Handle empty fields if validation fails.
     */
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

    /**
     * Set the hairstyle details by retrieving data from UI components.
     * Update the hairstyleVisit object with the new values.
     */
    private void setHairstyle() {
        try {
            hairstyleVisit.setPhotoHairstyle(getImageGallery(binding.addEditPhotoHairstyle.getDrawable(), getResources()));
            hairstyleVisit.setHaircutName(binding.etNameHairstyle.getText().toString().trim());
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

    /**
     * Save the previous count of materials used in the hairstyle.
     */
    private void savePrevCountUseMaterials() {
        prevCountMaterials = hairstyleVisit.getCountMaterial();
        prevCodeMaterials = hairstyleVisit.getCodeMaterial();
    }

    /**
     * Edit the hairstyle details in the database using a background thread.
     * Update the hairstyle, update material counts, and dismiss the operation.
     */
    private void editHairstyleDataBase() {
        Executors.newSingleThreadExecutor().execute(() -> {
            KosandraDataBase.getInstance(requireContext()).runInTransaction(() -> {
                hairstyleVisitViewModel.update(hairstyleVisit);
                updateMaterialCount();
                dismiss();
            });
        });
    }

    /**
     * Update the material counts in the database based on the changes made in the hairstyle.
     * Adjust the material count and rating for both old and new materials.
     */
    private void updateMaterialCount() {
        for (int i = 0; i < prevCodeMaterials.length; i++) {
            Materials prevMaterial = materialsViewModel.getMaterial(prevCodeMaterials[i]);
            prevMaterial.setCount(prevMaterial.getCount() + prevCountMaterials[i]);
            prevMaterial.setRating(prevMaterial.getRating() - 1);
            materialsViewModel.updateNoThread(prevMaterial);
        }
        for (int i = 0; i < countFieldUseMaterial; i++) {
            Materials newMaterials = materialsViewModel.getMaterial(hairstyleVisit.getCodeMaterial()[i]);
            newMaterials.setCount(newMaterials.getCount() - hairstyleVisit.getCountMaterial()[i]);
            newMaterials.setRating(newMaterials.getRating() + 1);
            materialsViewModel.updateNoThread(newMaterials);
        }
    }
}