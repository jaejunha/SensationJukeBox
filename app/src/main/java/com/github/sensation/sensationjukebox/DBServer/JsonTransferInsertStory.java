package com.github.sensation.sensationjukebox.DBServer;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//////////////////////////////////////////////////////////////////////
//     To connect with the server, This class is called.           //
////////////////////////////////////////////////////////////////////
public class JsonTransferInsertStory extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... params)
    {
        String Storyname = params[1];
        String Story = params[2];
        String date = params[3];
        String Musicname = params[4];
        String location1 = params[5];
        String location2 = params[6];

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