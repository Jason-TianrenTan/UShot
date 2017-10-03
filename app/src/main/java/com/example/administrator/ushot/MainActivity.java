package com.example.administrator.ushot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.widget.*;
import android.view.*;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

public class MainActivity extends AppCompatActivity {
    Button button;
    private static final int REQUEST_CODE_CHOOSE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(listener);
    }

    Button.OnClickListener listener = new Button.OnClickListener(){
        public void onClick(View v) {
            Matisse.from(MainActivity.this)
                    .choose(MimeType.of(MimeType.JPEG,MimeType.PNG))//选择mime的类型
                    .countable(true)//设置从1开始的数字
                    .maxSelectable(3)//选择图片的最大数量限制
                    .capture(true)//启用相机
                    .captureStrategy(new CaptureStrategy(true,"com.gaosi.provider.MyFileProvider"))//自定义FileProvider
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                    .theme(R.style.Matisse_Dracula) // 黑色背景
                    .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
        }
    };
}