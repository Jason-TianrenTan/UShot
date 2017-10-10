package com.example.administrator.ushot.Modules;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.MyColorGenerator;
import com.example.administrator.ushot.Tools.TakePhotoUtil;
import com.jph.takephoto.model.TResult;
import com.manolovn.colorbrewer.ColorBrewer;
import com.manolovn.trianglify.TrianglifyView;
import com.manolovn.trianglify.generator.color.ColorGenerator;
import com.manolovn.trianglify.generator.color.RandomColorGenerator;
import com.manolovn.trianglify.generator.point.RegularPointGenerator;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    Uri imageUri = null;
    TakePhotoUtil photoUtil = null;
    String photoPath = "";
    private static final int REQUEST_CODE_CHOOSE = 23;
    @BindView(R.id.btn_select_photo)
    Button btnSelectPhoto;
    @BindView(R.id.btn_take_photo)
    Button btnTakePhoto;
    @BindView(R.id.trianglify_view)
    TrianglifyView trianglifyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //背景生成
        trianglifyView.getDrawable().setCellSize(175);
        trianglifyView.getDrawable().setVariance(75);
        final ColorBrewer[] colors = ColorBrewer.values();
        Random rand = new Random();
        int i = rand.nextInt(ColorBrewer.values().length);
        trianglifyView.getDrawable().setColorGenerator(new MyColorGenerator(colors[i]));
        trianglifyView.getDrawable().setPointGenerator(new RegularPointGenerator());

        photoUtil = new TakePhotoUtil(this);
        photoUtil.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            imageUri = list.get(0);
            startViewActivity();
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
                        imageUri = Uri.fromFile(imgFile);
                        startViewActivity();
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


    public void startViewActivity() {
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        try {
            intent.putExtra("imageUri", getPath(this, imageUri));
        } catch(Exception e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }



    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}