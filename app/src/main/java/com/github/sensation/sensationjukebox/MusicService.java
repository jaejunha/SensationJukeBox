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
    static boolean alive = false;
    static MediaPlayer player= new MediaPlayer();
    private final IBinder musicBind = new MusicBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        player.stop();
        super.onDestroy();
    }
    public void stopservice(){
        alive = false;
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

