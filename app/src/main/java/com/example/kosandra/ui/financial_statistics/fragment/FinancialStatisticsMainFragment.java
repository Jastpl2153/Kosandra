package com.example.kosandra.ui.financial_statistics.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.example.kosandra.ui.general_logic.OnPieChartsListener;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FinancialStatisticsMainFragment<T> extends Fragment implements RvFinanceClickListener<T> {
    private FragmentMainFinancialStatisticsBinding binding;
    private HairstyleVisitViewModel hairstyleVisitViewModel;
    private IncomeViewModel incomeViewModel;
    private ExpensesViewModel expensesViewModel;
    private boolean incomeOrExpenses = true;
    private AdapterRVHairstyleIncome adapterRVHairstyleIncome;
    private AdapterRVExpenses adapterRVExpenses;
    private AdapterRVIncome adapterRVIncome;
    private MenuProvider provider;
    private int positionSelection = -1;
    private View prevItemLayout= null;
    private View prevButDelete = null;

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
        initMenu();
        binding.barCharts.setOnChartGestureListener(setupPieCharts());
        binding.fromDate.setOnClickListener(v -> showDatePickerDialog(binding.fromDate));
        binding.beforeDate.setOnClickListener(v -> showDatePickerDialog(binding.beforeDate));
        initPopularProfitableHairstyle();
    }

    // Общие методы
    private void initViewModel(){
        hairstyleVisitViewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        expensesViewModel = new ViewModelProvider(requireActivity()).get(ExpensesViewModel.class);
        incomeViewModel = new ViewModelProvider(requireActivity()).get(IncomeViewModel.class);
    }
    private void initRecyclerView() {
        adapterRVHairstyleIncome = new AdapterRVHairstyleIncome();
        adapterRVExpenses = new AdapterRVExpenses((RvFinanceClickListener<Expenses>) this);
        adapterRVIncome = new AdapterRVIncome((RvFinanceClickListener<Income>) this);
        binding.rvMainFinancialStatistics.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvIncomeOther.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void uiBarCharts(){
        Legend legend = binding.barCharts.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        binding.barCharts.setExtraOffsets(0f, 0f, 0f, 25f);
        binding.barCharts.getDescription().setTextSize(16f);
        binding.barCharts.getDescription().setYOffset(-20);
    }

    private void updateChartData() {
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
    private LocalDate parseDate(String dateString, String defaultDateString, EditText text) {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateToParse = dateString.isEmpty() ? LocalDate.parse(defaultDateString, ofPattern) : LocalDate.parse(dateString, ofPattern);

        text.setText(dateToParse.format(ofPattern));
        return dateToParse;
    }

    private void uiVisible(){
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

    private void setTitleRv(int title1, int title2, int title3){
        binding.titleFinanceRv1.setText(title1);
        binding.titleFinanceRv2.setText(title2);
        binding.titleFinanceRv3.setText(title3);
    }

    private void visibleViewIncome(int visible){
        binding.linearIncome1.setVisibility(visible);
        binding.linearIncome2.setVisibility(visible);
        binding.lineIncome.setVisibility(visible);
        binding.linearLayoutTitleIncomeOther.setVisibility(visible);
        binding.rvIncomeOther.setVisibility(visible);
        binding.lineIncomeRv.setVisibility(visible);
    }

    private void colorTextSum(int idColor){
        int color = ContextCompat.getColor(requireContext(), idColor);
        binding.barCharts.getDescription().setTextColor(color);
    }

    private void setBarChartsData(List<BarEntry> visitor, AtomicInteger sumCost,  List<LegendEntry> legendEntries){
        AtomicReference<BarDataSet> dataSet = new AtomicReference<>(new BarDataSet(visitor, ""));
        dataSet.get().setColors(ColorEnum.COLORS.getColors());
        dataSet.get().setValueTextColor(Color.BLACK);
        dataSet.get().setValueTextSize(12);

        Legend legend = binding.barCharts.getLegend();
        legend.setCustom(legendEntries);

        AtomicReference<BarData> barData = new AtomicReference<>(new BarData(dataSet.get()));

        binding.barCharts.setData(barData.get());
        setSumText(sumCost);
        binding.barCharts.invalidate();
    }

    private void setSumText(AtomicInteger sumCost){
        binding.barCharts.getDescription().setText("Общая сумма " + sumCost.get());
    }

    private OnPieChartsListener setupPieCharts(){
        return (me, lastPerformedGesture) -> {
            incomeOrExpenses = !incomeOrExpenses;
            updateChartData();
            binding.barCharts.animateY(700, Easing.EaseInOutCubic);
        };
    }

    private void initMenu() {
         provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_financial_statistics_add, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.add_money){
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

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().removeMenuProvider(provider);
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            selectedMonth = selectedMonth + 1;
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDayOfMonth, selectedMonth, selectedYear );
            editText.setText(selectedDate);
            updateChartData();
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View item_layout, View but_delete, int id) {
        if (positionSelection == id) {
            AnimationHelper.animateItemDeselected(item_layout, but_delete);
            positionSelection = -1;
        }
    }
    @Override
    public void onLongClick(View item_layout, View but_delete,  int id) {
        if (positionSelection == -1) {
            positionSelection = id;
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null ){
            positionSelection = id;
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            AnimationHelper.animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }
    @Override
    public void onDeleteClick(View item_layout, View but_delete, T t) {
        if (t instanceof Income){
            incomeViewModel.delete((Income) t);
            AnimationHelper.cancelAnimation(item_layout, but_delete);
        } else if (t instanceof Expenses) {
            expensesViewModel.delete((Expenses) t);
            AnimationHelper.cancelAnimation(item_layout, but_delete);
        }
    }

    // Методы доходов
    private void observeIncomeCharts(LocalDate fromDate, LocalDate beforeDate) {
        incomeViewModel.getCombinedResults(fromDate, beforeDate).observe(getViewLifecycleOwner(), sqlBarCharts -> {
            List<LegendEntry> legendEntries  = new CopyOnWriteArrayList<>();
            List<BarEntry> visitor = new CopyOnWriteArrayList<>();
            AtomicInteger sumCost = new AtomicInteger();
            int x = 1;
            for (int i = 0; i < sqlBarCharts.size(); i++) {
                SqlBarCharts cost = sqlBarCharts.get(i);
                visitor.add(new BarEntry(x, cost.getSumCost()));
                if (x< 20) {
                    legendEntries.add(new LegendEntry(cost.getType(), Legend.LegendForm.SQUARE, 10f, 5f, null, ColorEnum.COLORS.getColors()[x - 1]));
                }
                x++;
                sumCost.addAndGet(cost.getSumCost());
            }
            requireActivity().runOnUiThread(() -> {
                setBarChartsData(visitor, sumCost, legendEntries);
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

    private void observeIncomeOtherRv(LocalDate fromDate, LocalDate beforeDate) {
        incomeViewModel.getAllIncomeByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), incomes -> {
            if (incomes != null) {
                adapterRVIncome.setExpensesList(incomes);
                binding.rvIncomeOther.setAdapter(adapterRVIncome);
            }
        });
    }

    private void initPopularProfitableHairstyle() {
        hairstyleVisitViewModel.getMostPopularHairstyle().observe(getViewLifecycleOwner(), sqlIncomeHairstyle -> {
            binding.tvPopular.setText(sqlIncomeHairstyle.getHaircutName());
            binding.tvPopularCount.setText(String.valueOf(sqlIncomeHairstyle.getCost()));
        });

        hairstyleVisitViewModel.getMostProfitableHairstyle().observe(getViewLifecycleOwner(), sqlIncomeHairstyle -> {
            binding.tvProfitable.setText(sqlIncomeHairstyle.getHaircutName());
            binding.tvProfitablePrice.setText(String.valueOf(sqlIncomeHairstyle.getCost()));
        });
    }

    // Методы вычисляющие расходы
    private void observeExpensesCharts(LocalDate fromDate, LocalDate beforeDate){
        expensesViewModel.getGroupTypeByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), sqlBarCharts -> {
            List<LegendEntry> legendEntries  = new CopyOnWriteArrayList<>();
            List<BarEntry> visitor = new CopyOnWriteArrayList<>();
            AtomicInteger sumCost = new AtomicInteger();
            int x = 1;
            for (int i = 0; i < sqlBarCharts.size(); i++) {
                SqlBarCharts pieCharts = sqlBarCharts.get(i);
                visitor.add(new BarEntry(x, pieCharts.getSumCost()));
                x += 1;
                legendEntries.add(new LegendEntry(pieCharts.getType(), Legend.LegendForm.SQUARE, 10f, 5f, null, ColorEnum.COLORS.getColors()[i]));
                sumCost.addAndGet(pieCharts.getSumCost());
            }
            requireActivity().runOnUiThread(() -> {
                setBarChartsData(visitor, sumCost, legendEntries);
            });
        });
    }

    private void observeExpensesRv(LocalDate fromDate, LocalDate beforeDate){
        expensesViewModel.getAllExpensesByRangeDate(fromDate, beforeDate).observe(getViewLifecycleOwner(), expenses -> {
            adapterRVExpenses.setExpensesList(expenses);
            binding.rvMainFinancialStatistics.setAdapter(adapterRVExpenses);
        });
    }
}
