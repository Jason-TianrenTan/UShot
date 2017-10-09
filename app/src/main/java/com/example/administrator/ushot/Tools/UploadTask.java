package com.example.administrator.ushot.Tools;

import android.os.AsyncTask;

import com.android.internal.http.multipart.MultipartEntity;
import com.example.administrator.ushot.Configs.GlobalConfig;
import com.example.administrator.ushot.Events.AnalyseEvent;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class UploadTask extends AsyncTask<Void, Integer, String> {

    String url = GlobalConfig.URL;
    String filepath;


    public UploadTask(String path) {
        this.filepath = path;
        System.out.println("path = " + path);
    }

    @Override
    protected String doInBackground(Void... params) {
        uploadFile(filepath);
        return null;
    }

    protected void onPostExecute(String result) {

    }

    protected void onProgressUpdate(Integer... progress) {

    }

    /**
     * upload file to server
     *
     * @param uploadFile
     */
    private void uploadFile(String uploadFile) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            org.apache.http.entity.mime.MultipartEntity mpEntity = new org.apache.http.entity.mime.MultipartEntity();
            File file = new File(uploadFile);
            ContentBody cbFile = new FileBody(file);
            mpEntity.addPart("file", cbFile);

            httppost.setEntity(mpEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                String result = EntityUtils.toString(response.getEntity());
                EventBus.getDefault().post(new AnalyseEvent(result));
            }
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

