package com.example.administrator.ushot.Modules;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.TakePhotoUtil;
import com.jph.takephoto.model.TResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    TakePhotoUtil photoUtil = null;
    String photoPath = "";
    private static final int REQUEST_CODE_CHOOSE = 23;
    @BindView(R.id.btn_select_photo)
    Button btnSelectPhoto;
    @BindView(R.id.btn_take_photo)
    Button btnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        photoUtil = new TakePhotoUtil(this);
        photoUtil.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            System.out.println("Selected list = " + list);
        }
        else {
            photoUtil.onActivityResult(requestCode, resultCode, data);
        }
    }


    @OnClick({R.id.btn_select_photo, R.id.btn_take_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select_photo:
                Matisse.from(MainActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//选择mime的类型
                        .countable(false)
                        .capture(true)//启用相机
                        .captureStrategy(new CaptureStrategy(true, "com.gaosi.provider.MyFileProvider"))//自定义FileProvider
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                        .theme(R.style.Matisse_Zhihu) // 黑色背景
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.btn_take_photo:
                photoUtil.takePhoto(TakePhotoUtil.Select_type.PICK_BY_TAKE, new TakePhotoUtil.SimpleTakePhotoListener() {
                    @Override
                    public void takeSuccess(TResult result) {
                        String path = result.getImage().getCompressPath();
                        File imgFile = new File(path);
                        Uri imgUri = Uri.fromFile(imgFile);
                    }
                });
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        photoUtil.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        photoUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}