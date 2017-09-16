package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StoryEdit extends AppCompatActivity implements View.OnClickListener{
    Button SongSearch_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);
        SongSearch_Button = (Button)findViewById(R.id.SongSearch_Button);
        SongSearch_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.SongSearch_Button :
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
        }
    }
}
