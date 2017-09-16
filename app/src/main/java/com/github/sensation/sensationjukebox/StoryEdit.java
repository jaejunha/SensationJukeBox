package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.github.sensation.sensationjukebox.DBServer.JsonTransferInsertStory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StoryEdit extends AppCompatActivity implements View.OnClickListener{
    public static Button SongSearch_Button;
    public String senddate;

    public EditText storysubject, storytext;

    private ButtonRectangle cancel;
    private ButtonRectangle ok;

    JsonTransferInsertStory jt;
    SearchActivity sa;
    MapsActivity map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);
        SongSearch_Button = (Button)findViewById(R.id.SongSearch_Button);
        SongSearch_Button.setOnClickListener(this);

        storytext = (EditText)findViewById(R.id.editText);
        storytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storytext.setText(" ");
            }
        });

        storysubject = (EditText)findViewById(R.id.EditSubject);

        jt = new JsonTransferInsertStory();
        sa = new SearchActivity();
        map = new MapsActivity();

        ok = (ButtonRectangle)findViewById(R.id.OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                Date date = new Date();
                senddate =  dateFormat.format(date);

                String sendsubject = storysubject.getText().toString();
                String sendcontent = storytext.getText().toString();
                jt.execute("http://45.76.100.46/insert_story.php", sendsubject, sendcontent, senddate, sa.music,
                        map.location1, map.location2);
                finish();
            }
        });

        cancel = (ButtonRectangle)findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.SongSearch_Button :
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
        }
    }

    public void SetMusic(String name){
        SongSearch_Button.setText(name);
    }
}