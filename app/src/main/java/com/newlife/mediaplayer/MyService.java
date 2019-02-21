package com.newlife.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MyService extends Service {

    MediaPlayer mediaPlayer;
    ArrayList<Song> arraySong;
    //int position;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startPlayer(int pos){
        /*arraySong = new ArrayList<>();
        arraySong.add(new Song("Chuyen Tinh Toi",R.raw.chuyentinhtoi));
        arraySong.add(new Song("Duong mot chieu",R.raw.duongmotchieu));
        arraySong.add(new Song("La",R.raw.la));
        arraySong.add(new Song("La Lung",R.raw.lalung));
        arraySong.add(new Song("Phia Sau Em",R.raw.phiasauem));*/
        mediaPlayer=MediaPlayer.create(getApplicationContext(),arraySong.get(pos).getFile());
        //if(mediaPlayer.isPlaying()) {
            //mediaPlayer.stop();
        //}
        mediaPlayer.start();
    }

    public void pausePlayer()
    {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resumePlayer(){
        if(mediaPlayer!=null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public class MyBinder extends Binder {
        public Service getService(){
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
