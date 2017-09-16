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
            Date senddate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("test", Storyname + Story + date + Musicname + location1 + location2);

        String result = null;
        try{
            //To connect with server, Setting this option
            urlConnection = (HttpURLConnection) ((new URL(params[0]).openConnection()));
            urlConnection.setDoOutput(true);
            //      urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(10000); //10000ms
            urlConnection.setConnectTimeout(15000); //15000ms
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
            urlConnection.setRequestMethod("POST");

            //Write the data from the server
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(Storyname);
            writer.write(Story);
            writer.write(date);
            writer.write(Musicname);
            writer.write(location1);
            writer.write(location2);
            writer.write(count);
            writer.close();
            outputStream.close();

            //Read the data
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
                Log.d("line",line);
            }

            bufferedReader.close();
            result = sb.toString();
            //To use another class, Set the value in the global value
            strJson = result;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){e.printStackTrace();}
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}