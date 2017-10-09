package com.example.administrator.ushot.Modules;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.administrator.ushot.Events.AnalyseEvent;
import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.UploadTask;
import com.example.administrator.ushot.Views.RadarMarkerView;
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
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ArcProgressStackView;

public class ViewActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {


    BottomSheetBehavior behavior;
    ZLoadingDialog dialog;
    String path = null;
    float[] entry_arr = new float[7];
    String json = null;
    @BindView(R.id.radar_chart)
    RadarChart chart;
    String[] mActivities = new String[]{"Balancing", "Symmetry", "Light", "ColorHarmony", "Content", "Object", "Vivid"};
    ResultBean resultBean;
    @BindView(R.id.progbar_total)
    NumberProgressBar scoreBar;
    @BindView(R.id.nav_view)
    BottomNavigationBar bottomLayout;
    @BindView(R.id.img_view)
    ImageView imageView;
    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @BindView(R.id.btn_moreinfo)
    Button infoButton;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(AnalyseEvent event) {
        dialog.dismiss();
        String result = event.getResult();
        initBottomData(result);
        json = result;
        generateBottomSheet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        path = getIntent().getStringExtra("imageUri");
        uploadImage();
        initNavigationBar();
        displayImage();
        initBottomSheet();
    }


    private void initBottomData(String json) {
        Gson gson = new Gson();
        resultBean = gson.fromJson(json, ResultBean.class);
        initChart();
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
            float f = temp[i];
            if (f > 0)
                f = f * 100 + 45;
            else
                f = 70 + f * 20;
            if (f <= 0)
                f = 15;
            if (f > 100)
                f = 100;
            entry_arr[i] = f;
            mActivities[i] += String.format("(%.1f)", f);
        }
    }

    private void initChart() {
        processData();//处理数据
        chart.setBackgroundColor(Color.rgb(60, 65, 82));
        chart.setWebLineWidth(1.1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebColorInner(Color.GRAY);
        chart.setWebAlpha(100);

        MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
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


    private void initBottomSheet() {
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewActivity.this, AnalyseActivity.class);
                intent.putExtra("json", json);
                startActivity(intent);
            }
        });
    }


    private void displayImage() {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }


    private void initNavigationBar() {
        BottomNavigationItem analyseItem, toolsItem, shareItem;
        analyseItem = new BottomNavigationItem(R.drawable.ic_assessment_24dp, "分析").setActiveColor(R.color.red);
        toolsItem = new BottomNavigationItem(R.drawable.ic_burst_mode_24dp, "工具").setActiveColor(R.color.red);
        shareItem = new BottomNavigationItem(R.drawable.ic_share_24dp, "分享").setActiveColor(R.color.red);
        bottomLayout.setMode(BottomNavigationBar.MODE_FIXED);
        bottomLayout.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomLayout.addItem(analyseItem)
                .addItem(toolsItem)
                .addItem(shareItem)
                .setFirstSelectedPosition(0)
                .initialise();
        bottomLayout.setTabSelectedListener(this);
    }


    private void uploadImage() {
        dialog = new ZLoadingDialog(ViewActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)
                .setLoadingColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setCanceledOnTouchOutside(false)
                .setHintText("分析图片中...")
                .show();
        new UploadTask(path).execute();
    }

    @Override
    public void onTabSelected(int position) {
        tabPressed(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }


    @Override
    public void onTabReselected(int position) {
        tabPressed(position);
    }


    private void tabPressed(int position) {
        if (position == 0) {
            //display bottom sheet
            generateBottomSheet();
        }
    }


    private void generateBottomSheet() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

}
