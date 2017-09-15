package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

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
        setContentView(R.layout.activity_maps);

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

    }
}
