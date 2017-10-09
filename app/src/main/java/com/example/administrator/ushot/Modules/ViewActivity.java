package com.example.administrator.ushot.Modules;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.ushot.Events.AnalyseEvent;
import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.UploadTask;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{


    ZLoadingDialog dialog;
    String path = null;
    @BindView(R.id.nav_view)
    BottomNavigationBar bottomLayout;
    @BindView(R.id.img_view)
    ImageView imageView;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(AnalyseEvent event) {
        dialog.dismiss();
        String result = event.getResult();
        Intent intent = new Intent(ViewActivity.this, AnalyseActivity.class);
        intent.putExtra("json", result);
        startActivity(intent);
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
     //   startActivity(new Intent(ViewActivity.this, AnalyseActivity.class));
    }


    private void displayImage() {
        File imgFile = new  File(path);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }


    private void initNavigationBar() {
        BottomNavigationItem analyseItem, toolsItem, shareItem;
        analyseItem = new BottomNavigationItem(R.drawable.analyse, "分析").setActiveColor(R.color.blue);
        toolsItem = new BottomNavigationItem(R.drawable.tools, "工具").setActiveColor(R.color.red);
        shareItem = new BottomNavigationItem(R.drawable.share, "分享").setActiveColor(R.color.yellow);
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


    }

    @Override
    public void onTabUnselected(int position) {
    }


    @Override
    public void onTabReselected(int position) {

    }
}
