package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dream on 2017-09-16.
 */

public class Intro extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MapsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
