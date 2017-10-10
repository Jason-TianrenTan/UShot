package com.example.administrator.ushot.Views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.administrator.ushot.Modules.DataGraph;
import com.example.administrator.ushot.Modules.ResultBean;
import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.DataProcessor;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devlight.io.library.ArcProgressStackView;

import static com.example.administrator.ushot.Configs.GlobalConfig.ARC_TYPE;
import static com.example.administrator.ushot.Configs.GlobalConfig.BAR_TYPE;
import static com.example.administrator.ushot.Configs.GlobalConfig.BUBBLE_TYPE;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {


    float[] entry_arr = new float[7];
    String[] mActivities = new String[7];
    String[] baseStrings = new String[]{"Balancing", "Symmetry", "Light", "ColorHarmony",
            "Content", "Object", "Vivid"};
    ResultBean resultBean;
    @BindView(R.id.radar_chart)
    RadarChart chart;
    @BindView(R.id.progbar_total)
    NumberProgressBar scoreBar;
    @BindView(R.id.chart_recyclerview)
    RecyclerView recyclerView;
    Unbinder unbinder;

    ArrayList<DataGraph> graphList = new ArrayList<>();

    float[] arc_entries = new float[4];
    String[] arc_attr = new String[]{"RuleOfThirds", "MotionBlur", "DoF", "Repetition"};
    float[] bar_entries = new float[4];
    String[] bar_attr = new String[]{"Light", "ColorHarmony", "VividColor", "Repetition"};
    float[] bubble_entries = new float[6];
    String[] bubble_attr = new String[]{"Balancing", "Symmetry", "RuleOfThirds", "DoF", "Content", "Object"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyse_sheet_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        String json = getArguments().getString("json");
        Gson gson = new Gson();
        resultBean = gson.fromJson(json, ResultBean.class);
        initChart();
        setScore();
        initRecyclerView();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initChartData() {
        ResultBean.AnalysisBean analysisBean = resultBean.getAnalysis();
        //arc
        float[] arc_arr = new float[]{Float.parseFloat(analysisBean.getRuleOfThirds()), Float.parseFloat(analysisBean.getMotionBlur()),
                Float.parseFloat(analysisBean.getDoF()), Float.parseFloat(analysisBean.getRepetition())};
        for (int i = 0; i < arc_arr.length; i++)
            arc_entries[i] = arc_arr[i];

        //bar
        float[] bar_arr = new float[]{Float.parseFloat(analysisBean.getLight()),
                Float.parseFloat(analysisBean.getColorHarmony()), Float.parseFloat(analysisBean.getVividColor()), Float.parseFloat(analysisBean.getRepetition())};
        for (int i = 0; i < bar_arr.length; i++)
            bar_entries[i] = bar_arr[i];

        //bubble
        float[] bubble_arr = new float[]{Float.parseFloat(analysisBean.getBalancingElement()), Float.parseFloat(analysisBean.getSymmetry()),
                Float.parseFloat(analysisBean.getRuleOfThirds()), Float.parseFloat(analysisBean.getDoF()),
                Float.parseFloat(analysisBean.getContent()), Float.parseFloat(analysisBean.getObject())};
        for (int i = 0; i < bubble_arr.length; i++)
            bubble_entries[i] = bubble_arr[i];

        DataGraph arcGraph = new DataGraph(ARC_TYPE, arc_attr, arc_entries),
                barGraph = new DataGraph(BAR_TYPE, bar_attr, bar_entries),
                bubbleGraph = new DataGraph(BUBBLE_TYPE, bubble_attr, bubble_entries);
        graphList.add(arcGraph);
        graphList.add(barGraph);
        graphList.add(bubbleGraph);
    }


    private void initRecyclerView() {
        initChartData();
        GraphAdapter adapter = new GraphAdapter(graphList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void setScore() {
        float score = Float.parseFloat(resultBean.getAnalysis().getScore());
        scoreBar.setProgress((int) score);
    }


    private void processData() {
        ResultBean.SceneBean scene = resultBean.getScene();
        ResultBean.AnalysisBean analysis = resultBean.getAnalysis();
        float balancing = Float.parseFloat(analysis.getBalancingElement()),
                symmetry = Float.parseFloat(analysis.getSymmetry()),
                light = Float.parseFloat(analysis.getLight()),
                harmony = Float.parseFloat(analysis.getColorHarmony()),
                content = Float.parseFloat(analysis.getContent()),
                object = Float.parseFloat(analysis.getObject()),
                vivid = Float.parseFloat(analysis.getVividColor());
        float[] temp = {balancing, symmetry, light, harmony, content, object, vivid};
        for (int i = 0; i < temp.length; i++) {
            float f = DataProcessor.process(temp[i]);
            entry_arr[i] = f;
            mActivities[i] = baseStrings[i] + String.format("(%.1f)", f);
        }
    }

    private void initChart() {
        processData();//处理数据
        chart.setBackgroundColor(Color.rgb(60, 65, 82));
        chart.setWebLineWidth(1.1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebColorInner(Color.GRAY);
        chart.setWebAlpha(100);

        MarkerView mv = new RadarMarkerView(getActivity(), R.layout.radar_markerview);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        setRadarData();

        chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        //   xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = chart.getYAxis();
        //    yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //    l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }


    public void setRadarData() {

        ArrayList<RadarEntry> entries_user = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries_aver = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        float mult = 20, min = 50;
        for (int i = 0; i < 7; i++) {
            float val2 = (float) (Math.random() * mult) + min;
            entries_aver.add(new RadarEntry(val2));
            entries_user.add(new RadarEntry(entry_arr[i]));
        }


        RadarDataSet set1 = new RadarDataSet(entries_user, "照片分析");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries_aver, "平均水平");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();

        sets.add(set2);
        sets.add(set1);

        RadarData data = new RadarData(sets);
        //    data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }
}
