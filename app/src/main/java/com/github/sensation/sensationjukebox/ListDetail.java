package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class ListDetail extends AppCompatActivity{

    private ListView listView;
    private ArrayList<StoryItem> storyItemArrayList;
    private StoryListAdpater storyListAdpater;
    private Context context;
    private Button buttonUp;
    private LinearLayout layoutDetail;
    private FloatingActionButton fab;
    private TextView storyTitle;
    private TextView storyContent;
    private TextView songTitle;
    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        listView = (ListView) findViewById(R.id.detail_listView);
        this.context = getApplicationContext();

        buttonUp = (Button) findViewById(R.id.buttonUp);
        layoutDetail = (LinearLayout) findViewById(R.id.layoutDetail);
        storyTitle = (TextView) findViewById(R.id.storyTitle);
        storyContent = (TextView) findViewById(R.id.storyContent);
        songTitle = (TextView) findViewById(R.id.songTitle);

        //-------임시로 데이터 만듬---------

        Field[] fields = R.raw.class.getFields();
        Uri music1 = null;
        Uri music2 = null;
        for (int count = 0; count < fields.length; count++) {
            Log.e("노래", fields[count].getName());
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
        storyItem.setStoryContent("물론 꿈에서 빠졌습니다.. ");
        storyItem.setUri(music2);
        storyItemArrayList.add(storyItem);


        StoryItem storyItem1 = new StoryItem();
        storyItem1.setSongName("스토커");
        storyItem1.setStoryTitle("좋아하는 사람이 있습니다.");
        storyItem1.setStoryContent("저는 걔를 안좋아하는데 걔는 저를 안좋아해요..ㅠㅠ");
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
    }

    /*public void playSong(Uri songPath) {
        try {
            musicService.musicReset();
            //player = MediaPlayer.create(this, songPath);//노래 다 끊고 새로운 노래로 갱신.
            musicService.setPlayer(songPath);
            seekbar.setMax(musicService.getDuration());
            seekbar.setProgress(0);

            play(null);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void play(View view) {
        if (!musicService.isPlaying()) {
            musicService.musicStart();
            if (!musicthread.isAlive()) {
                musicthread = new musicThread();
                musicthread.start();
            }
            tv = (TextView) this.findViewById(R.id.playStatus);
            tv.setText("재생중");
        }
    }

    public void stop(View view) {
        if (musicService.isPlaying()) {
            musicService.musicPause();
            tv.setText("정지");
        }
    }*/

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
            holder.playButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    buttonUp.setText("▼");
                    layoutDetail.setVisibility(View.VISIBLE);
                    storyTitle.setText(getItem(position).getStoryTitle());
                    storyContent.setText(getItem(position).getStoryContent());
                    songTitle.setText(getItem(position).getSongName());

                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView textStory, textMusic;
            Button playButton;
        }
    }
}
