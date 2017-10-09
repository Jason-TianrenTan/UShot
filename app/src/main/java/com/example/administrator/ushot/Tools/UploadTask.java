package com.example.administrator.ushot.Tools;

import android.os.AsyncTask;

import com.example.administrator.ushot.Configs.GlobalConfig;
import com.example.administrator.ushot.Events.AnalyseEvent;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class UploadTask extends AsyncTask<Void, Integer, String> {

    String url = GlobalConfig.URL;
    String filepath;
    /**
     * Scene : {"Category":"home_theater","Attributes":["no horizon","man-made","enclosed area","cloth","vertical components"],"Indoor":"True","score":"0.477065652609"}
     * Analysis : {"BalancingElement":"0.0409112684429","Symmetry":"0.132171437144","RuleOfThirds":"-0.289971619844","Light":"-0.241546034813","MotionBlur":"-0.0573715120554","DoF":"-0.199838683009","ColorHarmony":"-0.00141979753971","Content":"-0.199531957507","Object":"-0.0151377618313","score":"35.1863801479","VividColor":"0.0811899900436","Repetition":"0.16214543581"}
     */

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
            MultipartEntity mpEntity = new MultipartEntity();
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

