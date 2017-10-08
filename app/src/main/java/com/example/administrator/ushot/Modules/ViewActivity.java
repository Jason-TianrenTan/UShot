package com.example.administrator.ushot.Modules;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.ushot.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{


    String path = null;
    @BindView(R.id.nav_view)
    BottomNavigationBar bottomLayout;
    @BindView(R.id.img_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra("imageUri");
        System.out.println("path = " + path);
        initNavigationBar();
        displayImage();
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
