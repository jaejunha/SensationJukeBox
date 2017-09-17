package com.github.sensation.sensationjukebox.DBServer;

/**
 * Created by jisu choi on 2017-09-16.
 * 아래 클래스는 원격 서버의 DB를 이용하기 위한 매니저 클래스입니다.
 */

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RemoteDBManager extends AsyncTask<String, Void, String>
{
    private String jsonResponse;
    public boolean done = false;

    // param[0] : URL
    // param[i], paranm[i+1] : 키, 값
    @Override
    protected String doInBackground(String... params)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        done = false;

        // 키 : 값 add
        for(int i = 1; i < params.length - 1; i++)
            formBodyBuilder.add(params[i], params[i + 1]);

        // 쿼리 실행
        try
        {
            RequestBody body = formBodyBuilder.build();
            Request request = new Request.Builder()
                    .url(params[0])
                    .post(body)
                    .build();

            jsonResponse = client.newCall(request).execute().body().string();
            done = true;
        }
        catch (IOException e)
        {e.printStackTrace();}

        return null;
    }
    public String getJsonResponse()
    {
        if(done)
            return jsonResponse;
        else
            return null;
    }
}
