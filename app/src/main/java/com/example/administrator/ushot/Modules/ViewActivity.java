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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.administrator.ushot.Events.AnalyseEvent;
import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.UploadTask;
import com.example.administrator.ushot.Views.BottomSheetFragment;
import com.example.administrator.ushot.Views.RadarMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
    LinearLayout bottomSheet;
    @BindView(R.id.btn_moreinfo)
    Button infoButton;

    BottomSheetFragment fragment = null;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(AnalyseEvent event) {
        dialog.dismiss();
        String result = event.getResult();
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

        if (position == 1) {
            //display tool menu
        }

        if (position == 2) {
            //分享按钮
            File imgFile = new File(path);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgFile.getAbsolutePath());
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "分享"));
        }
    }


    private void generateBottomSheet() {
        if (fragment == null) {
            fragment = new BottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("json", json);
            fragment.setArguments(bundle);
        }
        fragment.show(getSupportFragmentManager(), BottomSheetFragment.class.getSimpleName());
    }

}
