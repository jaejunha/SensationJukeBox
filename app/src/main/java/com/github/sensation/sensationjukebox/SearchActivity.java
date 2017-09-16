package com.github.sensation.sensationjukebox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    MusicListAdapter musicListAdapter;
    EditText searchEditText;
    Button searchButton;
    ArrayList<MusicItem> musicArrayList;

    StoryEdit sd;

    public static String music = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEditText = (EditText) findViewById(R.id.search_EditText);
        listView = (ListView) findViewById(R.id.Musit_ListView);
        searchButton = (Button) findViewById(R.id.search_Button);
        searchButton.setOnClickListener(this);

        sd = new StoryEdit();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                music = musicArrayList.get(position).getTitle();
                sd.SetMusic(music);
                finish();
            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchEditText.isFocusable()) {
                    new melonAsync().execute(searchEditText.getText().toString(), null, null);
                }
            }
        };
        searchEditText.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.search_Button : {
                new melonAsync().execute(searchEditText.getText().toString(),null,null);
            }
        }
    }

    public class melonAsync extends AsyncTask<String, String, ArrayList<MusicItem>> {

        @Override
        protected ArrayList<MusicItem> doInBackground(String... params) {
            //return melonparser.connectMelon(params[0]); //파싱 정보를 ArrayList로 가지고 옵니다.
            Document doc = null;
            try {
                doc = Jsoup.connect("http://music.naver.com/search/search.nhn?query="+params[0]+"&x=0&y=0").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements links = doc.select("span.ellipsis");
            musicArrayList = new ArrayList<MusicItem>();
            MusicItem musicItem = null;
            int count = 0;
            for (Element link : links) {
                if(count == 0){
                    musicItem = new MusicItem();
                    musicItem.setTitle(link.text());
                    count++;
                }
                else if(count == 1){
                    musicItem.setArtist(link.text());
                    count++;
                }
                else{
                    musicArrayList.add(musicItem);
                    count = 0;
                }
            }
            return musicArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<MusicItem> result) {
            musicListAdapter = new MusicListAdapter(SearchActivity.this, 0, result);
            listView.setAdapter(musicListAdapter);
        }
    }

}
