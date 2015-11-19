package com.example.dell.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.util.LinkedList;

import classMediaPlayer.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Spinner spiDirectory;
    private Spinner spiFiles;
    private Files fFiles;

    private ImageView btnPlay, btnStop, btnPrevious, btnNext, btnVolume;
    private SeekBar seekBarMusic, seekBarVolume;
    private TextView textStarTime, textEndTime;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int dTimeProgress = 0;
    private boolean bValidateMax = true;
    private int iIncrement = 5000;
    private int iImageVolume[] = new int[4];
    private int progressChanged = 0;
    private AudioManager am;
    private int maxVolume;
    private int curVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPage();
    }

    public void loadPage() {
        //Load static variable General
        General.ACTIVITY = this;
        General.CONTEXT = this;
        General.ROUTE = Environment.getExternalStorageDirectory().toString();
        //Create  adapter spinner

        spiDirectory = (Spinner) findViewById(R.id.spiDirectory);
        spiFiles = (Spinner) findViewById(R.id.spiFiles);
        fFiles = new Files();

        spiDirectory.setAdapter(createAdapterSpinner(0, ""));
        spiDirectory.setOnItemSelectedListener(this);

        //Load varible button
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        btnStop = (ImageView) findViewById(R.id.btnStop);
        btnPrevious = (ImageView) findViewById(R.id.btnPrevious);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        textStarTime = (TextView) findViewById(R.id.textStarTime);
        textEndTime = (TextView) findViewById(R.id.textEndTime);

        mediaPlayer = MediaPlayer.create(this, R.raw.your_love);
        seekBarMusic = (SeekBar) findViewById(R.id.seekBarTime);
        seekBarMusic.setClickable(false);

        iImageVolume[0] = R.drawable.volume_muted;
        iImageVolume[1] = R.drawable.volume_low;
        iImageVolume[2] = R.drawable.volume_medium;
        iImageVolume[3] = R.drawable.volume_high;

        btnVolume = (ImageView) findViewById(R.id.btnVolume);
        btnVolume.setOnClickListener(this);

        seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
        seekBarVolume.setOnSeekBarChangeListener(this);
        am = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(curVolume);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        General.printToast("Error in  search files type error:" + parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayAdapter createAdapterSpinner(int iTypeAdapter, String sSelection) {
        LinkedList lDiretory = new LinkedList();
        ArrayAdapter adapter = null;
        switch (iTypeAdapter) {

            case 0:
                lDiretory = fFiles.searchDirectory(0);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lDiretory);

                break;
            case 1:
                /*lDiretory = fFiles.searchDirectory(0);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lDiretory);
                spiDirectory.setAdapter(adapter);
                spiDirectory.setOnItemSelectedListener(this);*/
                break;
        }
        return adapter;


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnPlay:
                playMusic();
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                dTimeProgress = 0;
                mediaPlayer.seekTo(dTimeProgress);
                btnPlay.setImageResource(R.drawable.play);

                break;
            case R.id.btnPrevious:
                forwardDalay(1);
                break;
            case R.id.btnNext:
                forwardDalay(0);
                break;
            case R.id.btnVolume:
                volumeMusic(0);
                break;


        }
    }

    public void forwardDalay(int iSelection) {
        int iTem = (int) startTime;
        if (iSelection == 0) {

            if ((iTem + iIncrement) <= finalTime) {

                dTimeProgress = (int) startTime + iIncrement;
                startTime = dTimeProgress;
                mediaPlayer.seekTo((int) startTime);
            }


        } else {
            if ((iTem - iIncrement) > 0) {
                dTimeProgress = (int) startTime - iIncrement;
                startTime = startTime - iIncrement;
                mediaPlayer.seekTo((int) startTime);

            }

        }

    }

    public void stopMusic() {


    }

    public void playMusic() {
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();


        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(dTimeProgress);
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause);

        } else {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play);
            dTimeProgress = mediaPlayer.getCurrentPosition();
        }

        if (bValidateMax) {
            textStarTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            textEndTime.setText(String.format("%d:%d ", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
            seekBarMusic.setProgress((int) startTime);
            seekBarMusic.setMax(mediaPlayer.getDuration());
            myHandler.postDelayed(UpdateSongTime, 100);
            bValidateMax = false;
        }

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            textStarTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekBarMusic.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }

    };

    @Override
    public void onDestroy() {

        myHandler.removeCallbacks(UpdateSongTime);
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progressChanged = progress;
        volumeMusic(progressChanged);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void volumeMusic(int volume) {
        int iImageResouce = 0;

        if (volume == 0) {

            iImageResouce = iImageVolume[0];
            seekBarVolume.setProgress(volume);

        } else if (volume > 0 && volume <= 5) {

            iImageResouce = iImageVolume[1];
        } else if (volume > 5 && volume < 10) {

            iImageResouce = iImageVolume[2];
        } else {

            iImageResouce = iImageVolume[3];

        }

       
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        btnVolume.setImageResource(iImageResouce);

    }

}
