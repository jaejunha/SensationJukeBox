package com.github.sensation.sensationjukebox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Taek on 2016. 11. 14..
 */

public class MusicService extends Service {
    static boolean start = false;
    MediaPlayer player= new MediaPlayer();
    private final IBinder musicBind = new MusicBinder();
    boolean isBindSignal = false;
    @Override
    public IBinder onBind(Intent intent) {
        //player = MediaPlayer.create(this, R.raw.music1);
        player = new MediaPlayer();
        //start = true;
        //IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //registerReceiver(mBroadcastReceiver,filter);
        return musicBind;
    }

    /*private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        if(start == true) {
                            stopservice();
                            start = false;
                        }
                    }
                }
            }

        }
    };*/
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        //unregisterReceiver(mBroadcastReceiver);
        player.stop();
        super.onDestroy();
    }
    public void stopservice(){
        this.stopSelf();
    }
    public void musicReset() {
        player.reset();
    }

    public void musicStart() {
        player.start();
    }

    public void musicPause() {
        player.pause();
    }
    public void musicSeekTo(int progress) {
        player.seekTo(progress);
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public void setPlayer(Uri songPath) {
        player = MediaPlayer.create(this, songPath);
    }
}

