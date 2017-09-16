package com.github.sensation.sensationjukebox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dream on 2017-09-16.
 */

public class Intro extends AppCompatActivity{
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        context = getApplicationContext();
        new Thread(){
            public void run(){

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(context, TopActivity.class));
                overridePendingTransition(R.anim.anim_alphain, R.anim.anim_alphaout);
                finish();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_alphain, R.anim.anim_alphaout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.anim_alphain, R.anim.anim_alphaout);
    }
}
