package com.example.todolist.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
import com.example.todolist.auth.SessionManager;
import com.example.todolist.db.DB;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private TextView tvTotal, tvComplete, tvIncomplete, tvOverdue;
    private PieChart pieChart;
    private BarChart barChart;
    private DB db;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DB(getContext());
        userId = new SessionManager(getContext()).getCurrentUserId();

        tvTotal = view.findViewById(R.id.tvTotal);
        tvComplete = view.findViewById(R.id.tvComplete);
        tvIncomplete = view.findViewById(R.id.tvIncomplete);
        tvOverdue = view.findViewById(R.id.tvOverdue);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);

        loadStatistics();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadStatistics() {
        int[] stats = db.getStatistics(userId); // [total, complete, incomplete, overdue]
        animateCounter(tvTotal, stats[0]);
        animateCounter(tvComplete, stats[1]);
        animateCounter(tvIncomplete, stats[2]);
        animateCounter(tvOverdue, stats[3]);
        setupPieChart(stats[1], stats[2], stats[3]);
        setupBarChart();
    }

    private void animateCounter(TextView tv, int target) {
        ValueAnimator animator = ValueAnimator.ofInt(0, target);
        animator.setDuration(900);
        animator.addUpdateListener(a -> tv.setText(String.valueOf((int) a.getAnimatedValue())));
        animator.start();
    }

    private void setupPieChart(int complete, int incomplete, int overdue) {
        List<PieEntry> entries = new ArrayList<>();
        if (complete > 0)   entries.add(new PieEntry(complete, "Hoàn thành"));
        if (incomplete > 0) entries.add(new PieEntry(incomplete, "Chưa xong"));
        if (overdue > 0)    entries.add(new PieEntry(overdue, "Quá hạn"));

        if (entries.isEmpty()) {
            pieChart.setNoDataText("Chưa có task nào");
            pieChart.invalidate();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(0xFF00D4AA, 0xFF6C63FF, 0xFFFF5252);
        dataSet.setValueTextColor(0xFFFFFFFF);
        dataSet.setValueTextSize(13f);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(8f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setHoleRadius(48f);
        pieChart.setTransparentCircleRadius(53f);
        pieChart.setHoleColor(0xFF1A1A2E);
        pieChart.setCenterText("Tasks");
        pieChart.setCenterTextColor(0xFFFFFFFF);
        pieChart.setCenterTextSize(16f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(0xFFCCCCCC);
        pieChart.getLegend().setFormSize(10f);
        pieChart.setEntryLabelTextSize(11f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupBarChart() {
        int[] catStats = db.getCategoryStats(userId);
        String[] categories = {"Study", "Work", "Personal", "Health", "Finance", "Other"};

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < catStats.length; i++) {
            entries.add(new BarEntry(i, catStats[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tasks theo danh mục");
        dataSet.setColors(0xFF6C63FF, 0xFF00D4AA, 0xFFFFA726, 0xFFEF5350, 0xFF42A5F5, 0xFFAB47BC);
        dataSet.setValueTextColor(0xFFFFFFFF);
        dataSet.setValueTextSize(11f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(categories));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setTextColor(0xFFCCCCCC);
        barChart.getXAxis().setGridColor(0xFF333355);
        barChart.getAxisLeft().setTextColor(0xFFCCCCCC);
        barChart.getAxisLeft().setGridColor(0xFF333355);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setTextColor(0xFFCCCCCC);
        barChart.setBackgroundColor(0x00000000);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
