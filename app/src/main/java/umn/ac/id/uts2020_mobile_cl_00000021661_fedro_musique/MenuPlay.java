package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuPlay extends AppCompatActivity {
    Button playBtn,backwardBtn,forwardBtn, backmusicBtn,nextmusicBtn;
    SeekBar songBar;
    TextView currDur,musicDur;
    MediaPlayer mp;
    Animation rotateAnimation;
    ImageView logomusic;
    int totalTime;
    int psnIndexSong;
    ArrayList<Data> musicDetails;
    private int seekForwardTime = 5 * 1000; // default 5 second
    private int seekBackwardTime = 5 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_play);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentMenuPlay = getIntent();
        psnIndexSong = intentMenuPlay.getExtras().getInt("idxsong");
        String psnDir = intentMenuPlay.getStringExtra("dir");
        musicDetails = (ArrayList<Data>) getIntent().getExtras().getSerializable ("musicDetails"); // get arraylist from putextras intent

        // cek if index same as item selected
        for (int i =0;i<musicDetails.size();i++){
            if (psnDir.equals(musicDetails.get(i).getCurrDir())){
                psnIndexSong = i;
                break;
            }
        }



        playBtn = (Button) findViewById(R.id.playbtn);
        forwardBtn = (Button) findViewById(R.id.nextbtn);
        backwardBtn = (Button) findViewById(R.id.prevbtn);
        backmusicBtn = (Button) findViewById(R.id.musicbackbtn);
        nextmusicBtn = (Button) findViewById(R.id.musicnextbtn);
        currDur = (TextView) findViewById(R.id.currtime);
        musicDur = (TextView)findViewById(R.id.musictime);
        rotateAnimation = AnimationUtils.loadAnimation(this,R.anim.logoanim);
        logomusic = (ImageView) findViewById(R.id.logomusic);


        startAmusic(psnIndexSong);

        /*mp = MediaPlayer.create(this, Uri.parse(psnDir));
        mp.setLooping(true);
        mp.seekTo(0);*/


        songBar = (SeekBar) findViewById(R.id.musicbar);
        songBar.setMax(totalTime);

        //seekbar music function
        songBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    mp.seekTo(progress);
                    songBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //forward music function
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null){
                    int currentPosition = mp.getCurrentPosition();
                    if (currentPosition + seekForwardTime <= mp.getDuration()){
                        mp.seekTo(currentPosition+seekForwardTime);
                    }else{
                        mp.seekTo(mp.getDuration());
                    }
                }
            }
        });

        //backward music function
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null){
                    int currentPosition = mp.getCurrentPosition();
                    if (currentPosition - seekBackwardTime >=0){
                        mp.seekTo(currentPosition - seekBackwardTime);
                    }else{
                        mp.seekTo(0);
                    }
                }
            }
        });

        //next music function
       nextmusicBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (psnIndexSong < musicDetails.size() -1){
                   psnIndexSong++;
               }else{
                   psnIndexSong = 0;
               }
               startAmusic(psnIndexSong);
           }
       });

       //previous music function
       backmusicBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (psnIndexSong <=0){
                   psnIndexSong = musicDetails.size()-1;
               }else{
                   psnIndexSong--;
               }
               startAmusic(psnIndexSong);
           }
       });

       // thread for music seekbar handler
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp!=null){
                    try{
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){

                    }
                }
            }
        }).start();

    }

    //function for playing music

    @SuppressLint("SetTextI18n")
    public void startAmusic(int Indexmusic){

        if (mp != null && mp.isPlaying()){
            mp.stop();
            logomusic.clearAnimation();
        }


        TextView titleMusic = (TextView) findViewById(R.id.musicTitle);
        titleMusic.setText(musicDetails.get(Indexmusic).getTitle()+"  --- by ---  "+ musicDetails.get(Indexmusic).getCurrArtist());


        mp = MediaPlayer.create(this, Uri.parse(musicDetails.get(Indexmusic).getCurrDir()));
        mp.setLooping(true);
        mp.seekTo(0);
        mp.start();
        logomusic.startAnimation(rotateAnimation);
        if (mp.isPlaying()){
            playBtn.setBackgroundResource(R.drawable.pause_btn);
        }else{
            playBtn.setBackgroundResource(R.drawable.play_btn);
        }

        totalTime = mp.getDuration();

    }

    //music seekbar handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            songBar.setProgress(currentPosition);

            String curentTime = createTimeLabel(currentPosition);
            currDur.setText(curentTime);

            String remaining = createTimeLabel(totalTime-currentPosition);
            musicDur.setText("- " + remaining);
        }
    };


    //function for music duration label
    public String createTimeLabel(int time){
        String timelabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timelabel = min + ":";
        if (sec<10) timelabel +="0";
        timelabel +=sec;

        return timelabel;
    }

    //function button play or pause
    public void playBtnClick(View view){

        if(!mp.isPlaying()){
            mp.start();
            playBtn.setBackgroundResource(R.drawable.pause_btn);
            logomusic.startAnimation(rotateAnimation);
        }else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play_btn);
            logomusic.clearAnimation();
        }
    }

    // stoping music if back to list
    @Override
    public void onBackPressed() {
        if (mp.isPlaying()){
            mp.stop();
        }
        super.onBackPressed();
    }

    // stoping music if back to list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mp.isPlaying()) {
                    mp.stop();
                }
                onBackPressed();
                return true;
        }
        return false;
    }
}
