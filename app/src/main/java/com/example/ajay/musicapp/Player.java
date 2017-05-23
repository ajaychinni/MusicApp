package com.example.ajay.musicapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.ajay.musicapp.R.drawable.pause;
import static com.example.ajay.musicapp.R.id.viewPager;

public class Player extends Activity implements  View.OnClickListener{

    public static final String TAG = Player.class.getSimpleName();

    MyService service =null;

    ViewPager mPager;
    CustomPageAdapter mCustomPageAdapter;

    MediaPlayer player = null;
    SeekBar bar = null;
    Boolean isPlay =true;

    Handler handler = new Handler();

    TextView durations;
    TextView times;

    Button BtnPlay;
    private ArrayList<Song> data=null;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mPager = (ViewPager) findViewById(viewPager);
        data= (ArrayList<Song>) getIntent().getSerializableExtra("songlist");
        mCustomPageAdapter = new CustomPageAdapter(this,data);
        pos = getIntent().getExtras().getInt("pos");
        mPager.setAdapter(mCustomPageAdapter);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
              //  data.get(position).getImage();
            }
        });




        init();

        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
              // player.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


//        Intent intent = new Intent(Player.this,MyService.class);
//        bindService(intent,new MyConnection(),BIND_AUTO_CREATE);
    }



    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
          /*  MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            service = binder.getServices();*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void init() {
        durations = (TextView) findViewById(R.id.textView2);
        times= (TextView) findViewById(R.id.textView3);

        BtnPlay   = (Button) findViewById(R.id.play);


        BtnPlay.setOnClickListener(this);

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(player!= null && player.isPlaying()){

                int time = player.getCurrentPosition();
                bar.setProgress(time);


                int seconds =  (time / 1000) % 60 ;
                int minutes =  (time / (1000*60) % 60);
                times.setText(minutes+":"+seconds);


                handler.postDelayed(this,500);
            }
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play:
                if(isPlay)
                {
                    isPlay=false;
                    BtnPlay.setBackgroundResource(R.drawable.pause);
                    if(player!=null && player.isPlaying()){
                        player.stop();
                        player.release();
                        player = null;
                    }
                    player = new MediaPlayer();
                  /* ArrayList<Song> data = new ArrayList<>();
                   data = (ArrayList<Song>) getIntent().getSerializableExtra("song");*/
                  // String data = getIntent().getStringExtra("song");

                    try {

                        player.setDataSource(data.get(pos).getDatas());
                       // player.setDataSource(String.valueOf(R.raw.song1));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.start();



                   /* if(player!=null && player.isPlaying()){
                        player.stop();
                        player.release();
                        player = null;
                    }
                    player = MediaPlayer.create(this,R.raw.song1);
                    player.start();*/

                    int duration = player.getDuration();
                    bar.setMax(duration);

                    int seconds =  (duration / 1000) % 60 ;
                    int minutes =  (duration / (1000*60) % 60);
                    durations.setText(minutes+":"+seconds);
                    handler.post(runnable);



               }
                else
                {
                    isPlay=true;
                    BtnPlay.setBackgroundResource(R.drawable.play);
                    if(player!=null && player.isPlaying()){
                        player.stop();
                        player.release();
                        player = null;
                    }
                }

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPasuse() is called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() is called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() is called");
    }
}
