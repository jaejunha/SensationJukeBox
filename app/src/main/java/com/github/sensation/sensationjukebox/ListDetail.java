package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

/**
 * Created by dream on 2017-09-16.
 */

public class ListDetail extends AppCompatActivity{

    private ListView listView;
    private ArrayList<ListObject> list;
    private ListAdapter adapter;
    private Context context;

    private Button buttonUp;
    private Button buttonPlay;
    private LinearLayout layoutDetail;

    private YouTubePlayerView youTubeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        listView = (ListView)findViewById(R.id.listView);
        this.context = getApplicationContext();

        buttonUp= (Button)findViewById(R.id.buttonUp);
        layoutDetail =(LinearLayout)findViewById(R.id.layoutDetail);
        buttonPlay = (Button)findViewById(R.id.buttonPlay);
      //  youTubeVideo=(YouTubePlayerView) findViewById(R.id.youtube);

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutDetail.getVisibility() == View.GONE) {
                    buttonUp.setText("▼");
                    layoutDetail.setVisibility(View.VISIBLE);
                }
                else {
                    layoutDetail.setVisibility(View.GONE);
                    buttonUp.setText("▲");
                }
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public class ListAdapter extends BaseAdapter {

        ArrayList<ListObject> object;

        public ListAdapter(ArrayList<ListObject> object) {
            super();
            this.object = object;
        }

        @Override
        public int getCount() {
            return object.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_detail, parent, false);
                holder = new ViewHolder();
                holder.textMusic = (TextView) convertView.findViewById(R.id.textMusic);
                holder.textStory = (TextView) convertView.findViewById(R.id.textStory);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        public class ViewHolder {
            TextView textStory, textMusic;
        }
    }
}
