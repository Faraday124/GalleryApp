package com.example.stud.galleryapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;


public class DetailsActivity extends AppCompatActivity {


    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private MediaPlayer mediaPlayer;
    private static int position;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        position = 0;
        if (extras != null) {
            if (extras.get("imageNumber") != null) {
                position = (int) extras.get("imageNumber");
            }

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(position, true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                resetButtonsHighlight();
                playNextSong(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation);
        mViewPager.startAnimation(hyperspaceJumpAnimation);


        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        handleSeekBar();
        onPlayClick(null);
    }

    private void handleSeekBar() {

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }
        });
    }

    public void onPlayClick(View v) {

        playBtn.setImageResource(R.drawable.img_btn_play_pressed);
        pauseBtn.setImageResource(R.drawable.img_btn_pause);
        createMediaPlayer(position);
        playSong();

    }

    private void resetButtonsHighlight() {
        playBtn.setImageResource(R.drawable.img_btn_play);
        pauseBtn.setImageResource(R.drawable.img_btn_pause);
    }

    public void onStopClick(View v) {

        if (mediaPlayer != null && mediaPlayer.getCurrentPosition() != 0) {
            mediaPlayer.pause();
            playBtn.setImageResource(R.drawable.img_btn_play);
            pauseBtn.setImageResource(R.drawable.img_btn_pause_pressed);
        }

    }

    private void playNextSong(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        createMediaPlayer(position);

    }

    private void createMediaPlayer(int position) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, ImageReferences.songs[position]);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    resetButtonsHighlight();
                }
            });
        }
    }

    public void playSong() {
        mediaPlayer.start();

    }


    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
