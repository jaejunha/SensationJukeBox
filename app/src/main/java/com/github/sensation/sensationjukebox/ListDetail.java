package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dream on 2017-09-16.
 */

public class ListDetail extends AppCompatActivity{

    private ListView listView;
    private ArrayList<ListObject> list;
    private ListAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        listView = (ListView)findViewById(R.id.listView);
        this.context = getApplicationContext();
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
