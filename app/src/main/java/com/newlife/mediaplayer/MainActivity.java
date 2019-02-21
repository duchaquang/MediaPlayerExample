package com.newlife.mediaplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    ListView lvSong;

    ArrayList<Song> arraySong;
    ArrayList<String> arrayList;
    int position = 0;
    MediaPlayer mediaPlayer;

    MyService audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateView();
        AddSong();
        CreateMediaPlayer();

        //mediaPlayer =MediaPlayer.create(MainActivity.this,arraySong.get(position).getFile());
        //txtTitle.setText(arraySong.get(position).getTitle());
        arrayList = new ArrayList<String>();
        for(int i=0 ; i < arraySong.size(); i++)
        {
            arrayList.add(arraySong.get(i).getTitle());
            //System.out.println(arraySong.get(i).getTitle());
        }
        //System.out.println(arrayList.size());
        final ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        lvSong.setAdapter(arrayAdapter);

        Intent intent= new Intent(MainActivity.this, MyService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                position = pos;
                if(mediaPlayer.isPlaying())
                {
                    //neu dang phat
                   mediaPlayer.stop();
                    //mediaPlayer.release();
                }
                /*if(audioPlayer!= null)
                {
                    audioPlayer.onDestroy();
                }*/
                CreateMediaPlayer();
                //audioPlayer.startPlayer(pos);
                mediaPlayer.start();
                //txtTitle.setText(arraySong.get(position).getTitle());
                //txtTitle.setText(arraySong.get(pos).getTitle());
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    //neu dang phat
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else
                {
                    //dang ngung
                    mediaPlayer.start();
                    //audioPlayer.startPlayer(position);
                    btnPlay.setImageResource(R.drawable.pause);
                }
                /*if(audioPlayer!=null)
                {
                    audioPlayer.pausePlayer();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else
                {
                    audioPlayer.startPlayer(position);
                    btnPlay.setImageResource(R.drawable.pause);
                }*/
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play);
                CreateMediaPlayer();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position > arraySong.size()-1)
                {
                    position = 0;
                }
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                //audioPlayer.startPlayer(position);
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position --;
                if(position <0)
                {
                    position = arraySong.size() -1;
                }
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                //audioPlayer.startPlayer(position);
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(audioPlayer!=null) {
                    mediaPlayer.seekTo(skSong.getProgress());
                }
            }
        });
    }

    private void CreateMediaPlayer()
    {
        mediaPlayer = MediaPlayer.create(MainActivity.this,arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Chuyen Tinh Toi",R.raw.chuyentinhtoi));
        arraySong.add(new Song("Duong mot chieu",R.raw.duongmotchieu));
        arraySong.add(new Song("La",R.raw.la));
        arraySong.add(new Song("La Lung",R.raw.lalung));
        arraySong.add(new Song("Phia Sau Em",R.raw.phiasauem));

        //ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arraySong);
        //lvSong.setAdapter(arrayAdapter);
    }

    private void CreateView() {
        txtTimeSong = (TextView)findViewById(R.id.song_time);
        txtTimeTotal = (TextView)findViewById(R.id.total_time);
        txtTitle = (TextView)findViewById(R.id.textviewTitle);
        skSong = (SeekBar)findViewById(R.id.seekbar);
        btnPrev = (ImageButton)findViewById(R.id.imgPrev);
        btnPlay = (ImageButton)findViewById(R.id.imgPlay);
        btnStop = (ImageButton)findViewById(R.id.imgStop);
        btnNext = (ImageButton)findViewById(R.id.imgNext);
        lvSong = (ListView)findViewById(R.id.listSong);
    }

    private void SetTimeTotal()
    {
        SimpleDateFormat time_format = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(time_format.format(mediaPlayer.getDuration()) + "");
        //set max cho skSong = mediaPlayer.getDuration()
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void UpdateTimeSong()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    SimpleDateFormat time_format = new SimpleDateFormat("mm:ss");
                    txtTimeSong.setText(time_format.format(mediaPlayer.getCurrentPosition()));
                    //update progress for skSong
                    skSong.setProgress(mediaPlayer.getCurrentPosition());
                    //check time song -> if end -> next
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            position++;
                            if (position > arraySong.size() - 1) {
                                position = 0;
                            }
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                            }
                            CreateMediaPlayer();
                            mediaPlayer.start();
                            btnPlay.setImageResource(R.drawable.pause);
                            SetTimeTotal();
                            UpdateTimeSong();
                        }
                    });
                    handler.postDelayed(this, 500);
            }
        },100);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            audioPlayer = (MyService)myBinder.getService();
            //audioPlayer.arraySong1 = arraySong;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
