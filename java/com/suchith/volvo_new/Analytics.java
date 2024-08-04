package com.suchith.volvo_new;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private TextView tvTotalAssets, tvTotalSpares, tvTotalMaintenance;
    private TextView tvActivePercentage, tvInactivePercentage;
    private DonutProgress circleProgressAssets, circleProgressSpares, circleProgressMaintenance;
    private TableLayout tableLayout;
    private PieChart pieChartActiveInactive, pieChart;
    private HorizontalBarChart horizontalBarChart;
    private BarChart barChart;
    private LineChart lineChart;
    private RadarChart radarChart;
    private Button btnBack;

    private DatabaseReference databaseReference;

    // Define color scheme
    private final int COLOR_PRIMARY = Color.rgb(33, 150, 243);    // Primary blue
    private final int COLOR_SECONDARY = Color.rgb(3, 169, 244);   // Light blue
    private final int COLOR_ACCENT = Color.rgb(0, 188, 212);      // Cyan
    private final int COLOR_BACKGROUND = Color.rgb(238, 238, 238); // Light grey
    private final int COLOR_TEXT = Color.rgb(33, 33, 33);         // Dark grey

    // Aesthetic blue color palette
    private final int[] AESTHETIC_BLUE_COLORS = {
            Color.rgb(51, 102, 204),   // Deep Blue
            Color.rgb(92, 172, 238),   // Sky Blue
            Color.rgb(70, 130, 180),   // Steel Blue
            Color.rgb(30, 144, 255),   // Dodger Blue
            Color.rgb(135, 206, 250)   // Light Sky Blue
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics);

        initializeViews();
        setupCharts();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference analyticsReference = FirebaseDatabase.getInstance().getReference("Analytics");

        setupFirebaseListeners();
        fetchDataFromFirebase(analyticsReference);

        btnBack.setOnClickListener(view -> onBackPressed());
    }

    private void initializeViews() {
        tvTotalAssets = findViewById(R.id.tvTotalAssets);
        tvTotalSpares = findViewById(R.id.tvTotalSpares);
        tvTotalMaintenance = findViewById(R.id.tvTotalMaintenance);
        tvActivePercentage = findViewById(R.id.tvActivePercentage);
        tvInactivePercentage = findViewById(R.id.tvInactivePercentage);
        circleProgressAssets = findViewById(R.id.circleProgressAssets);
        circleProgressSpares = findViewById(R.id.circleProgressSpares);
        circleProgressMaintenance = findViewById(R.id.circleProgressMaintenance);
        tableLayout = findViewById(R.id.tableLayout);
        pieChartActiveInactive = findViewById(R.id.pieChartActiveInactive);
        horizontalBarChart = findViewById(R.id.horizontalBarChart);
        pieChart = findViewById(R.id.pieChartProduction);
        barChart = findViewById(R.id.barChartEfficiency);
        lineChart = findViewById(R.id.lineChartTrends);
        radarChart = findViewById(R.id.radarChartQuality);
        btnBack = findViewById(R.id.back_btn_analytics);
    }

    private void setupCharts() {
        setupPieChart();
        setupHorizontalBarChart();
        setupPieChartProduction();
        setupBarChartEfficiency();
        setupLineChart();
        setupRadarChart();
    }

    private void setupPieChart() {
        pieChartActiveInactive.setUsePercentValues(true);
        pieChartActiveInactive.getDescription().setEnabled(false);
        pieChartActiveInactive.setDrawHoleEnabled(true);
        pieChartActiveInactive.setHoleColor(COLOR_BACKGROUND);
        pieChartActiveInactive.setTransparentCircleRadius(61f);
        pieChartActiveInactive.setHoleRadius(58f);
        pieChartActiveInactive.setDrawCenterText(false);
        pieChartActiveInactive.setRotationEnabled(false);
        pieChartActiveInactive.setHighlightPerTapEnabled(false);
        pieChartActiveInactive.getLegend().setEnabled(false);
    }

    private void setupHorizontalBarChart() {
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.setDrawValueAboveBar(true);
        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawGridBackground(false);
        horizontalBarChart.setBackgroundColor(Color.WHITE);

        // Remove the left offset to make bars start from the left
        horizontalBarChart.setExtraLeftOffset(0f);

        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(COLOR_TEXT);

        YAxis leftAxis = horizontalBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // Start at 0
        leftAxis.setTextColor(COLOR_TEXT);

        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.getLegend().setEnabled(false);

        // Add padding to the right side
        horizontalBarChart.setExtraRightOffset(20f);
    }

    private void setupPieChartProduction() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Vehicle Types");
    }

    private void setupBarChartEfficiency() {
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        XAxis xAxisBar = barChart.getXAxis();
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGranularity(1f);
        xAxisBar.setGranularityEnabled(true);
    }

    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);
        XAxis xAxisLine = lineChart.getXAxis();
        xAxisLine.setGranularity(1f);
        xAxisLine.setGranularityEnabled(true);
    }

    private void setupRadarChart() {
        radarChart.getDescription().setEnabled(false);
    }

    private void setupFirebaseListeners() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalAssets = 0, totalSpares = 0, totalMaintenance = 0;
                int totalActive = 0, totalInactive = 0;

                for (DataSnapshot snapshot : dataSnapshot.child("nuts_bolts").getChildren()) {
                    totalAssets++;
                    if ("true".equals(snapshot.child("active").getValue(String.class))) {
                        totalActive++;
                    } else {
                        totalInactive++;
                    }
                }

                for (DataSnapshot snapshot : dataSnapshot.child("spare_parts_management").getChildren()) {
                    totalSpares++;
                    if ("true".equals(snapshot.child("active").getValue(String.class))) {
                        totalActive++;
                    } else {
                        totalInactive++;
                    }
                }

                for (DataSnapshot snapshot : dataSnapshot.child("maintenance_management").getChildren()) {
                    totalMaintenance++;
                    if ("true".equals(snapshot.child("active").getValue(String.class))) {
                        totalActive++;
                    } else {
                        totalInactive++;
                    }
                }

                updateUI(totalAssets, totalSpares, totalMaintenance, totalActive, totalInactive);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void fetchDataFromFirebase(DatabaseReference analyticsReference) {
        analyticsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updatePieChart(dataSnapshot.child("pieChart"));
                updateBarChart(dataSnapshot.child("barChart"));
                updateLineChart(dataSnapshot.child("lineChart"));
                updateRadarChart(dataSnapshot.child("radarChart"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void updateUI(int totalAssets, int totalSpares, int totalMaintenance, int totalActive, int totalInactive) {
        tvTotalAssets.setText(String.valueOf(totalAssets));
        tvTotalSpares.setText(String.valueOf(totalSpares));
        tvTotalMaintenance.setText(String.valueOf(totalMaintenance));

        int total = totalAssets + totalSpares + totalMaintenance;

        updateCircleProgress(circleProgressAssets, totalAssets, total, COLOR_PRIMARY);
        updateCircleProgress(circleProgressSpares, totalSpares, total, COLOR_SECONDARY);
        updateCircleProgress(circleProgressMaintenance, totalMaintenance, total, COLOR_ACCENT);

        updateTable(totalAssets, totalSpares, totalMaintenance);
        updateActiveInactivePieChart(totalActive, totalInactive);
        updateHorizontalBarChart(totalAssets, totalSpares, totalMaintenance);
    }

    private void updateCircleProgress(DonutProgress progress, int value, int total, int color) {
        int percentage = Math.round((float) value / total * 100);
        progress.setProgress(percentage);
        progress.setText(percentage + "%");
        progress.setFinishedStrokeColor(color);
        progress.setTextColor(color);
    }

    private void updateTable(int totalAssets, int totalSpares, int totalMaintenance) {
        tableLayout.removeAllViews();
        addTableRow("Category", "Total", "Percentage", true);
        addTableRow("Assets", String.valueOf(totalAssets), String.format("%.1f%%", (float) totalAssets / (totalAssets + totalSpares + totalMaintenance) * 100), false);
        addTableRow("Spares", String.valueOf(totalSpares), String.format("%.1f%%", (float) totalSpares / (totalAssets + totalSpares + totalMaintenance) * 100), false);
        addTableRow("Maintenance", String.valueOf(totalMaintenance), String.format("%.1f%%", (float) totalMaintenance / (totalAssets + totalSpares + totalMaintenance) * 100), false);
    }

    private void addTableRow(String category, String total, String percentage, boolean isHeader) {
        TableRow row = new TableRow(this);

        TextView tvCategory = new TextView(this);
        tvCategory.setText(category);
        tvCategory.setPadding(5, 5, 5, 5);
        tvCategory.setGravity(Gravity.CENTER);
        tvCategory.setTextColor(isHeader ? COLOR_BACKGROUND : COLOR_TEXT);
        tvCategory.setBackgroundColor(isHeader ? COLOR_PRIMARY : COLOR_BACKGROUND);
        row.addView(tvCategory);

        TextView tvTotal = new TextView(this);
        tvTotal.setText(total);
        tvTotal.setPadding(5, 5, 5, 5);
        tvTotal.setGravity(Gravity.CENTER);
        tvTotal.setTextColor(isHeader ? COLOR_BACKGROUND : COLOR_TEXT);
        tvTotal.setBackgroundColor(isHeader ? COLOR_PRIMARY : COLOR_BACKGROUND);
        row.addView(tvTotal);

        TextView tvPercentage = new TextView(this);
        tvPercentage.setText(percentage);
        tvPercentage.setPadding(5, 5, 5, 5);
        tvPercentage.setGravity(Gravity.CENTER);
        tvPercentage.setTextColor(isHeader ? COLOR_BACKGROUND : COLOR_TEXT);
        tvPercentage.setBackgroundColor(isHeader ? COLOR_PRIMARY : COLOR_BACKGROUND);
        row.addView(tvPercentage);

        tableLayout.addView(row);
    }

    private void updateActiveInactivePieChart(int totalActive, int totalInactive) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalActive, "Active"));
        entries.add(new PieEntry(totalInactive, "Inactive"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(COLOR_PRIMARY, COLOR_ACCENT);
        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChartActiveInactive.setData(data);
        pieChartActiveInactive.invalidate();

        float activePercentage = (float) totalActive / (totalActive + totalInactive) * 100;
        float inactivePercentage = (float) totalInactive / (totalActive + totalInactive) * 100;

        tvActivePercentage.setText(String.format("Active: %.1f%%", activePercentage));
        tvInactivePercentage.setText(String.format("Inactive: %.1f%%", inactivePercentage));
        tvActivePercentage.setTextColor(COLOR_PRIMARY);
        tvInactivePercentage.setTextColor(COLOR_SECONDARY);
    }

    private void updateHorizontalBarChart(int totalAssets, int totalSpares, int totalMaintenance) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalAssets));
        entries.add(new BarEntry(1, totalSpares));
        entries.add(new BarEntry(2, totalMaintenance));

        BarDataSet dataSet = new BarDataSet(entries, "Management Progress");
        dataSet.setColors(COLOR_PRIMARY, COLOR_SECONDARY, COLOR_ACCENT);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f); // Slightly narrower bars
        data.setValueTextColor(COLOR_TEXT);
        data.setValueTextSize(12f);

        horizontalBarChart.setData(data);
        horizontalBarChart.setFitBars(true);

        String[] labels = new String[]{"Assets", "Spares", "Maintenance"};
        horizontalBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        horizontalBarChart.getXAxis().setLabelCount(labels.length);

        // Ensure the chart displays all bars
        horizontalBarChart.setVisibleXRangeMaximum(3);
        horizontalBarChart.moveViewToX(0);

        horizontalBarChart.invalidate();
        horizontalBarChart.animateY(1000);
    }

    private void updatePieChart(DataSnapshot dataSnapshot) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            entries.add(new PieEntry(snapshot.child("value").getValue(Float.class),
                    snapshot.child("label").getValue(String.class)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(AESTHETIC_BLUE_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setUsePercentValues(true);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1000);
        pieChart.setCenterText("Vehicle\nType");
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(50f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.BLACK);
        l.setDrawInside(false);
    }

    private void updateBarChart(DataSnapshot dataSnapshot) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            entries.add(new BarEntry(index, snapshot.child("value").getValue(Float.class)));
            labels.add(snapshot.child("label").getValue(String.class));
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(AESTHETIC_BLUE_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextSize(10f);
        leftAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawGridBackground(false);

        barChart.invalidate();
        barChart.animateY(1000);
    }

    private void updateLineChart(DataSnapshot dataSnapshot) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            entries.add(new Entry(index, snapshot.child("value").getValue(Float.class)));
            labels.add(snapshot.child("label").getValue(String.class));
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(AESTHETIC_BLUE_COLORS[0]);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(AESTHETIC_BLUE_COLORS[1]);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(false);

        LineData data = new LineData(dataSet);

        lineChart.setData(data);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        lineChart.invalidate();
        lineChart.animateX(1000);
    }

    private void updateRadarChart(DataSnapshot dataSnapshot) {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            entries.add(new RadarEntry(snapshot.child("value").getValue(Float.class)));
            labels.add(snapshot.child("label").getValue(String.class));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "");
        dataSet.setColor(AESTHETIC_BLUE_COLORS[2]);
        dataSet.setFillColor(AESTHETIC_BLUE_COLORS[3]);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        RadarData data = new RadarData(dataSet);

        radarChart.setData(data);
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.getLegend().setEnabled(false);
        radarChart.getDescription().setEnabled(false);

        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebAlpha(100);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setTextSize(10f);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setDrawLabels(false);

        radarChart.invalidate();
        radarChart.animateXY(1000, 1000);
    }
}
