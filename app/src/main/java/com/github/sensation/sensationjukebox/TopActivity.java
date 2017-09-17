package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dream on 2017-09-16.
 */

public class TopActivity extends AppCompatActivity{

    private FloatingActionButton floatingActionButton;
    static String []top3_music;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        floatingActionButton =(FloatingActionButton)findViewById(R.id.floatMap);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        connect();
    }

    public void connect(){
        top3_music = new String[3];

        new RemoteDBManagerAsync().execute("http://45.76.100.46/select_top3.php", "zone", "zone2", "top_rank", "3");
    }

    class RemoteDBManagerAsync extends AsyncTask<String, Void, String>
    {
        private String jsonResponse;
        public boolean done = false;

        // param[0] : URL
        // param[i], paranm[i+1] : 키, 값
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                //while(!rdbm.done);
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jObject = jsonArray.getJSONObject(i);  // JSONObject 추출
                    String musicName = jObject.getString("music_name");
                    top3_music[i] = musicName;
                }
                Log.d("test", top3_music[0] + top3_music[1] + top3_music[2]);
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
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

                //jsonResponse = client.newCall(request).execute().body().string();

                Response response = client.newCall(request).execute();
                jsonResponse =  response.body().string();
                //done = true;
                response.body().close();
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
}
