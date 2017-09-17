package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by dream on 2017-09-16.
 */

public class TopActivity extends AppCompatActivity implements View.OnTouchListener{

    private FloatingActionButton floatingActionButton;
    private FrameLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        layout = (FrameLayout)findViewById(R.id.layout) ;
        layout.setOnTouchListener(this);
        floatingActionButton =(FloatingActionButton)findViewById(R.id.floatMap);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        return false;
    }
}
