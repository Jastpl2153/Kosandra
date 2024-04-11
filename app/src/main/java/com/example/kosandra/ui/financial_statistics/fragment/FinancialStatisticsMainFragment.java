package com.example.kosandra.ui.financial_statistics.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMainFinancialStatisticsBinding;
import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Expenses;
import com.example.kosandra.entity.Income;
import com.example.kosandra.ui.animation.AnimationHelper;
import com.example.kosandra.ui.financial_statistics.adapter.AdapterRVExpenses;
import com.example.kosandra.ui.financial_statistics.adapter.AdapterRVHairstyleIncome;
import com.example.kosandra.ui.financial_statistics.adapter.AdapterRVIncome;
import com.example.kosandra.ui.financial_statistics.colors_charts.ColorEnum;
import com.example.kosandra.ui.financial_statistics.dialog.AddFinancialStatisticsDialog;
import com.example.kosandra.ui.financial_statistics.financial_statistics_listener_rv.RvFinanceClickListener;
import com.example.kosandra.ui.general_logic.OnBarChartsListener;
import com.example.kosandra.view_model.ExpensesViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;
import com.example.kosandra.view_model.IncomeViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class representing the main fragment for financial statistics in the application.
 * <p>
 * This class extends Fragment and implements RvFinanceClickListener interface to handle finance item clicks.
 */
public class FinancialStatisticsMainFragment<T> extends Fragment implements RvFinanceClickListener<T> {
    private FragmentMainFinancialStatisticsBinding binding;// The binding for the fragment layout
    private HairstyleVisitViewModel hairstyleVisitViewModel;// View model for hairstyle visit data
    private IncomeViewModel incomeViewModel;// View model for income data
    private ExpensesViewModel expensesViewModel;// View model for expenses data
    private boolean incomeOrExpenses = true;// Indicates whether to display income or expenses
    private AdapterRVHairstyleIncome adapterRVHairstyleIncome;// Adapter for hairstyling income
    private AdapterRVExpenses adapterRVExpenses; // Adapter for expenses
    private AdapterRVIncome adapterRVIncome; // Adapter for income
    private MenuProvider provider;// Provider for menu items
    private int positionSelection = -1;// Position selection for item
    private View prevItemLayout = null;// Previous item layout view
    private View prevButDelete = null;// View for previous delete button

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainFinancialStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initRecyclerView();
        uiBarCharts();
        updateChartData();
        binding.barCharts.setOnChartGestureListener(setupBarCharts());
        binding.fromDate.setOnClickListener(v -> showDatePickerDialog(binding.fromDate));
        binding.beforeDate.setOnClickListener(v -> showDatePickerDialog(binding.beforeDate));
        binding.butNowPeriod.setOnClickListener(v -> nowPeriod30Day());
        initPopularProfitableHairstyle();
    }

    /**
     * Sets the date range to the last 30 days and updates the chart data accordingly.
     */
    private void nowPeriod30Day() {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        binding.fromDate.setText(LocalDate.now().minusDays(30).format(ofPattern));
        binding.beforeDate.setText(LocalDate.now().format(ofPattern));
        updateChartData();
    }

    /**
     * Initializes the view models for hairstyle, expenses, and income using ViewModelProvider.
     */
    private void initViewModel() {
        hairstyleVisitViewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        expensesViewModel = new ViewModelProvider(requireActivity()).get(ExpensesViewModel.class);
        incomeViewModel = new ViewModelProvider(requireActivity()).get(IncomeViewModel.class);
    }

    /**
     * Initializes the recycler views and their respective adapters for hairstyle, expenses, and income.
     */
    private void initRecyclerView() {
        adapterRVHairstyleIncome = new AdapterRVHairstyleIncome();
        adapterRVExpenses = new AdapterRVExpenses((RvFinanceClickListener<Expenses>) this);
        adapterRVIncome = new AdapterRVIncome((RvFinanceClickListener<Income>) this);
        binding.rvMainFinancialStatistics.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvIncomeOther.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    /**
     * Configures the bar chart UI elements including legend, description, and offsets.
     */
    private void uiBarCharts() {
        Legend legend = binding.barCharts.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        binding.barCharts.setExtraOffsets(0f, 0f, 0f, 25f);
        binding.barCharts.getDescription().setTextSize(16f);
        binding.barCharts.getDescription().setYOffset(-20);
    }

    /**
     * Updates the chart data based on the selected date range and income/expense type.
     * Parses the date inputs, observes corresponding data, and updates the UI accordingly.
     */
    private void updateChartData() {
        showProgressBar();
        LocalDate fromDate = parseDate(binding.fromDate.getText().toString(), LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), binding.fromDate);
        LocalDate beforeDate = parseDate(binding.beforeDate.getText().toString(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), binding.beforeDate);

        if (incomeOrExpenses) {
            uiVisible();
            observeIncomeCharts(fromDate, beforeDate);
            observeIncomeRv(fromDate, beforeDate);
            observeIncomeOtherRv(fromDate, beforeDate);
        } else {
            observeExpensesCharts(fromDate, beforeDate);
            observeExpensesRv(fromDate, beforeDate);
            uiVisible();
        }
    }

    /**
     * Parses the input date string into a LocalDate object.
     * Sets the parsed date on the provided EditText for display.
     *
     * @param dateString        The input date string
     * @param defaultDateString The default date string to parse if input is empty
     * @param text              The EditText to set the parsed date
     * @return The parsed LocalDate object
     */
    private LocalDate parseDate(String dateString, String defaultDateString, EditText text) {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateToParse = dateString.isEmpty() ? LocalDate.parse(defaultDateString, ofPattern) : LocalDate.parse(dateString, ofPattern);

        text.setText(dateToParse.format(ofPattern));
        return dateToParse;
    }

    /**
     * Updates the UI elements based on the income or expenses type.
     */
    private void uiVisible() {
        if (incomeOrExpenses) {
            setTitleRv(R.string.name_hairstyle_income, R.string.do_hair, R.string.avg_price);
            visibleViewIncome(View.VISIBLE);
            colorTextSum(R.color.green);
        } else {
            setTitleRv(R.string.expenses_type, R.string.spent, R.string.date_expenses);
            visibleViewIncome(View.GONE);
            colorTextSum(R.color.red);
        }
    }

    /**
     * Sets the titles of the RecyclerView based on provided resource IDs.
     *
     * @param title1 Resource ID for title 1
     * @param title2 Resource ID for title 2
     * @param title3 Resource ID for title 3
     */
    private void setTitleRv(int title1, int title2, int title3) {
        binding.titleFinanceRv1.setText(title1);
        binding.titleFinanceRv2.setText(title2);
        binding.titleFinanceRv3.setText(title3);
    }

    /**
     * Sets the visibility of income-related views based on the input.
     *
     * @param visible The visibility value
     */
    private void visibleViewIncome(int visible) {
        binding.linearIncome1.setVisibility(visible);
        binding.linearIncome2.setVisibility(visible);
        binding.lineIncome.setVisibility(visible);
        binding.linearLayoutTitleIncomeOther.setVisibility(visible);
        binding.rvIncomeOther.setVisibility(visible);
        binding.lineIncomeRv.setVisibility(visible);
    }

    /**
     * Sets the text color of the sum based on the provided color ID.
     *
     * @param idColor The color resource ID
     */
    private void colorTextSum(int idColor) {
        int color = ContextCompat.getColor(requireContext(), idColor);
        binding.barCharts.getDescription().setTextColor(color);
    }

    /**
     * Sets the data for the BarChart with provided entries, sum, and legend entries.
     *
     * @param visitor       List of entries for the chart
     * @param sumCost       Total sum value
     * @param legendEntries List of legend entries
     */
    private void setBarChartsData(List<BarEntry> visitor, int sumCost, List<LegendEntry> legendEntries) {
        BarDataSet dataSet = new BarDataSet(visitor, "");
        dataSet.setColors(ColorEnum.COLORS.getColors());
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12);

        Legend legend = binding.barCharts.getLegend();
        legend.setCustom(legendEntries);

        BarData barData = new BarData(dataSet);
        binding.barCharts.setData(barData);
        setSumText(sumCost);
        binding.barCharts.invalidate();
        hideProgressBar();
    }

    /**
     * Sets the text in the description of the bar chart with the total sum cost.
     *
     * @param sumCost The total sum cost as an AtomicInteger
     */
    private void setSumText(int sumCost) {
        binding.barCharts.getDescription().setText("Общая сумма " + sumCost);
    }

    /**
     * Sets up the listener for bar charts on the UI.
     * This method toggles between displaying income or expenses data based on the current state.
     * It updates the 'fromDate' and 'beforeDate' fields on the UI accordingly.
     * It then triggers the update of the chart data and animates the bar charts.
     *
     * @return A listener for handling bar chart gestures
     */
    private OnBarChartsListener setupBarCharts() {
        return (me, lastPerformedGesture) -> {
            incomeOrExpenses = !incomeOrExpenses;
            if (incomeOrExpenses) {
                binding.fromDate.setText(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                binding.beforeDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } else {
                binding.fromDate.setText(LocalDate.now().minusYears(5).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                binding.beforeDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
            updateChartData();
            binding.barCharts.animateY(700, Easing.EaseInOutCubic);
        };
    }

    /**
     * Initializes the menu options on the financial statistics page.
     */
    private void initMenu() {
        provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_financial_statistics_add, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.add_money) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("type", incomeOrExpenses);
                    AddFinancialStatisticsDialog addFinancialStatisticsDialog = new AddFinancialStatisticsDialog();
                    addFinancialStatisticsDialog.setArguments(bundle);
                    addFinancialStatisticsDialog.show(getParentFragmentManager(), "DialogExpensesAdd");
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(provider);
    }

    /**
     * Executes actions when the activity starts.
     * This method initializes the menu by calling the 'initMenu' method.
     * The 'super.onStart()' is also called to maintain the superclass behavior.
     */
    @Override
    public void onStart() {
        super.onStart();
        initMenu();
    }

    /**
     * Executes actions when the activity stops.
     * This method removes the menu provider from the activity by calling 'requireActivity().removeMenuProvider(provider)'.
     * The 'super.onStop()' is also called to preserve the superclass behavior.
     */
    @Override
    public void onStop() {
        super.onStop();
        requireActivity().removeMenuProvider(provider);
    }

    /**
     * Displays a date picker dialog to select a date and updates the chart data based on the selected date.
     *
     * @param editText The EditText view to display the selected date
     */
    private void showDatePickerDialog(EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedMonth = selectedMonth + 1;
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDayOfMonth, selectedMonth, selectedYear);
            editText.setText(selectedDate);
            updateChartData();
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    /**
     * Handles the onClick event on a specific item layout, deselecting the item upon a second click.
     *
     * @param item_layout The layout of the clicked item
     * @param but_delete  The delete button associated with the item
     * @param id          The ID of the clicked item
     */
    @Override
    public void onClick(View item_layout, View but_delete, int id) {
        if (positionSelection == id) {
            AnimationHelper.animateItemDeselected(item_layout, but_delete);
            positionSelection = -1;
        }
    }

    /**
     * Handle long click event on an item in the list.
     * If no item is currently selected, select the item and apply animation.
     * If an item is already selected, switch the selection to the new item,
     * apply animation to both the new and previous items.
     *
     * @param item_layout The layout of the item being clicked
     * @param but_delete  The delete button associated with the item
     * @param id          The unique identifier of the item
     */
    @Override
    public void onLongClick(View item_layout, View but_delete, int id) {
        if (positionSelection == -1) {
            positionSelection = id;
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null) {
            positionSelection = id;
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            AnimationHelper.animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }

    /**
     * Handle click event to delete an item from the list, based on its type.
     * Delete the item from the corresponding view model and cancel any ongoing animation.
     *
     * @param item_layout The layout of the item being deleted
     * @param but_delete  The delete button associated with the item
     * @param t           The item object to be deleted
     */
    @Override
    public void onDeleteClick(View item_layout, View but_delete, T t) {
        if (t instanceof Income) {
            incomeViewModel.delete((Income) t);
            AnimationHelper.cancelAnimation(item_layout, but_delete);
        } else if (t instanceof Expenses) {
            expensesViewModel.delete((Expenses) t);
            AnimationHelper.cancelAnimation(item_layout, but_delete);
        }
    }

    // Income methods

    /**
     * Observe income data and update the bar charts with combined results.
     *
     * @param fromDate   The start date for data observation
     * @param beforeDate The end date for data observation
     */
    private void observeIncomeCharts(LocalDate fromDate, LocalDate beforeDate) {
        incomeViewModel.getCombinedResults(fromDate, beforeDate).observe(getViewLifecycleOwner(), sqlBarCharts -> {
            List<LegendEntry> legendEntries = new ArrayList<>();
            List<BarEntry> visitor = new ArrayList<>();
            int sumCost = 0;
            int x = 1;
            for (int i = 0; i < sqlBarCharts.size(); i++) {
                if (i < ColorEnum.COLORS.size()) {
                    SqlBarCharts cost = sqlBarCharts.get(i);
                    visitor.add(new BarEntry(x, cost.getSumCost()));
                    legendEntries.add(new LegendEntry(cost.getType(), Legend.LegendForm.SQUARE, 10f, 5f, null, ColorEnum.COLORS.getColors()[i]));
                    x += 2;
                    sumCost += cost.getSumCost();
                } else {
                    Toast.makeText(requireContext(), "Слишком много данных! Показано не все", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            int finalSumCost = sumCost;
            requireActivity().runOnUiThread(() -> {
                setBarChartsData(visitor, finalSumCost, legendEntries);
            });
        });
    }

    private void observeIncomeRv(LocalDate fromDate, LocalDate beforeDate) {
        hairstyleVisitViewModel.getHairstyleIncome(fromDate, beforeDate).observe(getViewLifecycleOwner(), hairstyleIncomes -> {
            if (hairstyleIncomes != null) {
                adapterRVHairstyleIncome.setHairstyleIncomes(hairstyleIncomes);
                binding.rvMainFinancialStatistics.setAdapter(adapterRVHairstyleIncome);
            }
        });
    }

    /**
     * Observe income data and update the recycler view with hairstyle income.
     *
     * @param fromDate   The start date for data observation
     * @param beforeDate The end date for data observation
     */
    private void observeIncomeOtherRv(LocalDate fromDate, LocalDate beforeDate) {
        incomeViewModel.getAllIncomeByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), incomes -> {
            if (incomes != null) {
                adapterRVIncome.setIncomesList(incomes);
                binding.rvIncomeOther.setAdapter(adapterRVIncome);
            }
        });
    }

    /**
     * /**
     * <p>
     * Initialize and update the views with popular and profitable hairstyle data.
     */
    private void initPopularProfitableHairstyle() {
        hairstyleVisitViewModel.getMostPopularHairstyle().observe(getViewLifecycleOwner(), sqlIncomeHairstyle -> {
            if (sqlIncomeHairstyle != null) {
                binding.tvPopular.setText(sqlIncomeHairstyle.getHaircutName());
                binding.tvPopularCount.setText(String.valueOf(sqlIncomeHairstyle.getCost()));
            }
        });

        hairstyleVisitViewModel.getMostProfitableHairstyle().observe(getViewLifecycleOwner(), sqlIncomeHairstyle -> {
            if (sqlIncomeHairstyle != null) {
                binding.tvProfitable.setText(sqlIncomeHairstyle.getHaircutName());
                binding.tvProfitablePrice.setText(String.valueOf(sqlIncomeHairstyle.getCost()));
            }
        });
    }

    // Expenses methods

    /**
     * Observe expenses data and update the bar charts with grouped results.
     *
     * @param fromDate   The start date for data observation
     * @param beforeDate The end date for data observation
     */
    private void observeExpensesCharts(LocalDate fromDate, LocalDate beforeDate) {
        expensesViewModel.getGroupTypeByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), sqlBarCharts -> {
            List<LegendEntry> legendEntries = new ArrayList<>();
            List<BarEntry> visitor = new ArrayList<>();
            int sumCost = 0;
            int x = 1;
            for (int i = 0; i < sqlBarCharts.size(); i++) {
                if (i < ColorEnum.COLORS.size()) {
                    SqlBarCharts pieCharts = sqlBarCharts.get(i);
                    visitor.add(new BarEntry(x, pieCharts.getSumCost()));
                    x += 2;
                    legendEntries.add(new LegendEntry(pieCharts.getType(), Legend.LegendForm.SQUARE, 10f, 5f, null, ColorEnum.COLORS.getColors()[i]));
                    sumCost += pieCharts.getSumCost();
                } else {
                    Toast.makeText(requireContext(), "Слишком много данных! Показано не все", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            int finalSumCost = sumCost;
            requireActivity().runOnUiThread(() -> {
                setBarChartsData(visitor, finalSumCost, legendEntries);
            });

        });
    }

    /**
     * Observe expenses data and update the recycler view with all expenses data.
     *
     * @param fromDate   The start date for data observation
     * @param beforeDate The end date for data observation
     */
    private void observeExpensesRv(LocalDate fromDate, LocalDate beforeDate) {
        expensesViewModel.getAllExpensesByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), expenses -> {
            adapterRVExpenses.setExpensesList(expenses);
            binding.rvMainFinancialStatistics.setAdapter(adapterRVExpenses);
        });
    }

    /**
     * Displays the progress bar on the screen.
     * The progress bar will be set to visible, indicating that a process is currently ongoing.
     */
    private synchronized void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the progress bar from the screen.
     * The progress bar will be set to invisible, indicating that the ongoing process has been completed or stopped.
     */
    private synchronized void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}
