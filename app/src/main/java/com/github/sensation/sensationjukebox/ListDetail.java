package com.github.sensation.sensationjukebox;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dream on 2017-09-16.
 */

public class ListDetail extends AppCompatActivity implements View.OnTouchListener {

    private FrameLayout layout;
    private int i=0;
    private Context context;
    private RelativeLayout gone;
    private Button buttonPlay;
    private LinearLayout layoutDetail;
    private FloatingActionButton fab;
    private TextView storyTitle;
    private TextView storyContent;
    private TextView songTitle;
    private SeekBar seekBar;
    MusicService musicService;
    private Intent playIntent;
    private boolean musicStarted = false;
    musicThread musicthread;
    static boolean threadrunning = true;
    boolean isbind = false;
    private Button open_Button;
    String[] storysub;
    String[] storycontent;
    String[] musicname;
    String[] location1;
    String[] location2;
    private boolean isOpen = false;
    private static String jsontext = null;
    private int jsoncount = 0;
    static StoryItem saveStoryItem = new StoryItem();
    Uri music3;
    MapsActivity maps;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.context = getApplicationContext();
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        //open_Button = (Button) findViewById(R.id.open_Button);
        layoutDetail = (LinearLayout) findViewById(R.id.layoutDetail);
        storyTitle = (TextView) findViewById(R.id.storyTitle);
        storyContent = (TextView) findViewById(R.id.storyContent);
        songTitle = (TextView) findViewById(R.id.songTitle);
        seekBar = (SeekBar) findViewById(R.id.seekMusic);
        layout = (FrameLayout) findViewById(R.id.layout);
        gone = (RelativeLayout)findViewById(R.id.gone);
        storysub = new String[100];
        storycontent = new String[100];
        musicname = new String[100];
        location1 = new String[100];
        location2 = new String[100];
        maps = new MapsActivity();

        layout.setOnTouchListener(this);
        gone.setOnTouchListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        //-------임시로 데이터 만듬---------노래 임의로 때려박고 리스트에 값 추가한 작업.
        Field[] fields = R.raw.class.getFields();
        Uri music1 = null;
        music3 = null;
        for (int count = 0; count < fields.length; count++) {
            //Log.e("노래", fields[count].getName());
            if (fields[count].getName().toString().contains("music1")) {
                music1 = Uri.parse("android.resource://" + getPackageName() + "/raw/" + fields[count].getName());
            } else if (fields[count].getName().toString().contains("music2")) {
                music3 = Uri.parse("android.resource://" + getPackageName() + "/raw/" + fields[count].getName());
            }
        }

        /*fab = (FloatingActionButton) findViewById(R.id.EditStory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(ListDetail.this, StoryEdit.class);
                startActivity(change);
            }
        });*/


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    musicService.musicSeekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (musicStarted == false) {
                    storyTitle.setText("");
                    storyContent.setText("");
                    songTitle.setText("재생할 곡을 선택해주세요.");
                } else {
                    if (musicService.isPlaying()) {
                        musicService.musicPause();
                        buttonPlay.setText("▶");
                    } else {
                        musicService.musicStart();
                        if (!musicthread.isAlive()) {
                            musicthread = new musicThread();
                            musicthread.start();
                        }
                        buttonPlay.setText("II");
                    }
                }
            }
        });
        /*open_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isOpen) {
                    layoutDetail.setVisibility(View.GONE);
                    open_Button.setText(" ^ ");
                    isOpen = false;
                } else {
                    layoutDetail.setVisibility(View.VISIBLE);
                    open_Button.setText(" v ");
                    isOpen = true;
                }
            }
        });*/
        updateMetaInfo();

        if (jsontext != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsontext);
                Log.d("test", jsontext);
                jsoncount = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    storysub[i] = jsonArray.getJSONObject(i).getString("story_subject");
                    storycontent[i] = jsonArray.getJSONObject(i).getString("story_content");
                    musicname[i] = jsonArray.getJSONObject(i).getString("music_name");
                    location1[i] = jsonArray.getJSONObject(i).getString("location1");
                    location2[i] = jsonArray.getJSONObject(i).getString("location2");
                    Log.d("test", "" + storysub[i] + storycontent[i] + musicname[i] + location1[i] + location2[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int userlist = 0; userlist < jsoncount; userlist++){
            StoryItem storyItem = new StoryItem();
            storyItem.setSongName(String.valueOf(musicname[userlist]));
            storyItem.setStoryTitle(String.valueOf(storysub[userlist]));
            storyItem.setStoryContent(String.valueOf(storycontent[userlist]));
            if (userlist == 2) {
                storyItem.setUri(music3);
            } else if (userlist == 0) {
                storyItem.setUri(music1);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(musicService.alive){
            Log.e("바인드", "살아있었음");
            playIntent = new Intent(this, MusicService.class);
            isbind = bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            seekBar.setMax(MusicService.player.getDuration());
            seekBar.setProgress(MusicService.player.getCurrentPosition());
            storyTitle.setText(saveStoryItem.getStoryTitle());
            storyContent.setText(saveStoryItem.getStoryContent());
            songTitle.setText(saveStoryItem.getSongName());
            if(MusicService.player.isPlaying()) {
                musicStarted = true;
                buttonPlay.setText("II");
            }

        }else {
            if (playIntent == null) {
                Log.e("바인드", "ㅎ1ㅎㅎ");
                playIntent = new Intent(this, MusicService.class);
                isbind = bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                musicService.alive = true;
                startService(playIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isbind) {
            isbind = bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            Log.e("다시바인드", "ㅎㅎㅎ");/*
            if(musicService.start){
                seekBar.setMax(musicService.getDuration());
            }*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isbind) {
            unbindService(musicConnection);
            isbind = false;
        }
        //stopService(playIntent);
        //threadrunning = false;
    }

    public void playSong(Uri songPath) {
        try {
            musicService.musicReset();
            //player = MediaPlayer.create(this, songPath);//노래 다 끊고 새로운 노래로 갱신.
            musicService.setPlayer(songPath);
            seekBar.setMax(musicService.getDuration());
            seekBar.setProgress(0);

            play(null);
            //MusicService.start = true;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            //seekBar.setMax(musicService.getDuration());
            //seekBar.setProgress(musicService.getCurrentPosition());
/*
            if(musicService.isPlaying() == false && musicService.getCurrentPosition() != 0){
                //tv.setText("정지");
            }
            else if(musicService.isPlaying() == true){
                //tv.setText("재생중");
            }*/

            threadrunning = true;
            musicthread = new musicThread();
            musicthread.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void play(View view) {
        if (!musicService.isPlaying()) {
            musicService.musicStart();
            if (!musicthread.isAlive()) {
                musicthread = new musicThread();
                musicthread.start();
            }
            //tv = (TextView) this.findViewById(R.id.playStatus);//실행 상태 나타나는
            //tv.setText("재생중");
            musicStarted = true;
            buttonPlay.setText("II");
        }
    }

    public void stop(View view) {
        if (musicService.isPlaying()) {
            musicService.musicPause();
            //tv.setText("정지");
            buttonPlay.setText("II");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gone.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        i++;
        if(i>1 && i<3) {
            //musicService.musicStart();
            playSong(music3);
            gone.setBackground(getDrawable(R.drawable.completed3));
        }
        else if(i>4) {
            startActivity(new Intent(this, StoryEdit.class));
            overridePendingTransition(R.anim.anim_alphain, R.anim.anim_alphaout);
        }
        return false;
    }

    class musicThread extends Thread {
        public void run() {
            while (musicService.isPlaying() && threadrunning) {//이게 멈춰도 상관없고, 진행중이여도 싱크 잘 맞음.
                try {
                    seekBar.setProgress(musicService.getCurrentPosition());
                    Thread.sleep(1000);
                    Log.e("스레드 도는중.", "입니다");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Log.e("스레드 끝.", "입니다");
        }

    }

    private boolean updateMetaInfo() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "http://45.76.100.46/select_all.php";

            RequestBody body = new FormBody.Builder()
                    .add("zone",maps.Listlocation)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            jsontext = response.body().string();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
