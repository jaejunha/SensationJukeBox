package com.github.sensation.sensationjukebox;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by dream on 2017-09-16.
 */

public class ListDetail extends AppCompatActivity {

    private ListView listView;
    private ArrayList<StoryItem> storyItemArrayList;
    private StoryListAdpater storyListAdpater;
    private Context context;
    private Button buttonUp;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        listView = (ListView) findViewById(R.id.detail_listView);
        this.context = getApplicationContext();
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonUp = (Button) findViewById(R.id.buttonUp);
        layoutDetail = (LinearLayout) findViewById(R.id.layoutDetail);
        storyTitle = (TextView) findViewById(R.id.storyTitle);
        storyContent = (TextView) findViewById(R.id.storyContent);
        songTitle = (TextView) findViewById(R.id.songTitle);
        seekBar = (SeekBar) findViewById(R.id.seekMusic);

        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        //-------임시로 데이터 만듬---------노래 임의로 때려박고 리스트에 값 추가한 작업.
        Field[] fields = R.raw.class.getFields();
        Uri music1 = null;
        Uri music2 = null;
        for (int count = 0; count < fields.length; count++) {
            //Log.e("노래", fields[count].getName());
            if (fields[count].getName().toString().contains("music1")) {
                music1 = Uri.parse("android.resource://" + getPackageName() + "/raw/" + fields[count].getName());
            } else if (fields[count].getName().toString().contains("music3")) {
                music2 = Uri.parse("android.resource://" + getPackageName() + "/raw/" + fields[count].getName());
            }
        }
        storyItemArrayList = new ArrayList<StoryItem>();
        StoryItem storyItem = new StoryItem();
        storyItem.setSongName("꽃이 핀다");
        storyItem.setStoryTitle("사랑에 빠졌습니다..");
        storyItem.setStoryContent("물론 상상속의 그녀와... ");
        storyItem.setUri(music2);
        storyItemArrayList.add(storyItem);


        StoryItem storyItem1 = new StoryItem();
        storyItem1.setSongName("스토커");
        storyItem1.setStoryTitle("좋아하는 사람이 있습니다.");
        storyItem1.setStoryContent("짝사랑을 하고있습니다..고민좀 들어주세요ㅠㅠ");
        storyItem1.setUri(music1);
        storyItemArrayList.add(storyItem1);
        storyListAdpater = new StoryListAdpater(storyItemArrayList);
        listView.setAdapter(storyListAdpater);
        //------데이터베이스에서 읽어와야 되지만 값없어서 임시로 만듬--------


        fab = (FloatingActionButton) findViewById(R.id.EditStory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(ListDetail.this, StoryEdit.class);
                startActivity(change);
            }
        });


        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutDetail.getVisibility() == View.GONE) {
                    buttonUp.setText("▼");
                    layoutDetail.setVisibility(View.VISIBLE);
                } else {
                    layoutDetail.setVisibility(View.GONE);
                    buttonUp.setText("▲");
                }
            }
        });

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            Log.e("바인드","ㅎ1ㅎㅎ");
            playIntent = new Intent(this, MusicService.class);
            isbind = bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isbind) {
            isbind = bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            Log.e("다시바인드","ㅎㅎㅎ");
            if(musicService.start){
                seekBar.setMax(musicService.getDuration());
            }
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

    public class StoryListAdpater extends BaseAdapter {

        ArrayList<StoryItem> object;

        public StoryListAdpater(ArrayList<StoryItem> object) {
            super();
            this.object = object;
        }

        @Override
        public int getCount() {
            return object.size();
        }

        @Override
        public StoryItem getItem(int position) {
            return object.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_detail, parent, false);
                holder = new ViewHolder();
                holder.textMusic = (TextView) convertView.findViewById(R.id.textMusic);
                holder.textStory = (TextView) convertView.findViewById(R.id.textStory);
                holder.playButton = (Button) convertView.findViewById(R.id.buttonPlay);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            StoryItem storyItem = (StoryItem) getItem(position); //포지션 별로 값을 채워 줍니다.
            holder.textMusic.setText(storyItem.getSongName());
            holder.textStory.setText(storyItem.getStoryTitle());
            holder.playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    buttonUp.setText("▼");
                    layoutDetail.setVisibility(View.VISIBLE);
                    storyTitle.setText(getItem(position).getStoryTitle());
                    storyContent.setText(getItem(position).getStoryContent());
                    songTitle.setText(getItem(position).getSongName());
                    playSong(getItem(position).getUri());
                    buttonPlay.setText("II");
                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView textStory, textMusic;
            Button playButton;
        }
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
}
