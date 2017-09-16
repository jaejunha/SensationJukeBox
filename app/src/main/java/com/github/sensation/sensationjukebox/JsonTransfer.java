package com.github.sensation.sensationjukebox;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//////////////////////////////////////////////////////////////////////
//     To connect with the server, This class is called.           //
////////////////////////////////////////////////////////////////////
public class JsonTransfer extends AsyncTask<String, Void, String> {

    public static String strJson;

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection;
        String Storyname = params[1];
        String Story = params[2];
        String date = params[3];
        String Musicname = params[4];
        String location1 = params[5];
        String location2 = params[6];
        int count = 0;

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("story_subject",Storyname)
                    .add("story_content", Story)
                    .add("date", date)
                    .add("music_name", Musicname)
                    .add("location1", location1)
                    .add("location2", location2)
                    .build();
            Request request = new Request.Builder()
                    .url(params[0])
                    .post(body)
                    .build();
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}