package com.example.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.databinding.AcitvityMusicDetailsBinding;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MusicDetails extends AppCompatActivity {

    AcitvityMusicDetailsBinding binding;
    boolean isPlay=false;

    Handler handler=new Handler();

    MediaPlayer mediaPlayer =  MediaPlayeInstant.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding=AcitvityMusicDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String duration = intent.getStringExtra("duration");
        String path = intent.getStringExtra("path");

        binding.seekbar.setMax(mediaPlayer.getDuration());


        Update update1=new Update();
        handler.post(update1);

//        mediaPlayer.getCurrentPosition();

//        binding.durationStart.setText(mediaPlayer.getCurrentPosition()+"");

//        MusicDetails.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(mediaPlayer!=null){
//                    binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
//                    binding.durationStart.setText(convertTime(mediaPlayer.getCurrentPosition()+""));
//
//
//
//                }
//                new Handler().postDelayed(this,100);
//            }
//        });



//        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(mediaPlayer!=null && fromUser){
//                    mediaPlayer.seekTo(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                handler.removeCallbacks(updateSeekBar);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


        binding.durationEnd.setText(convertTime(duration));
        binding.title.setText(title);


        try {
            mediaPlayer.setDataSource(String.valueOf(Uri.parse(path)));
            mediaPlayer.prepare();
            mediaPlayer.start();
            binding.seekbar.setMax(mediaPlayer.getDuration());
//            binding.durationStart.setText(mediaPlayer.getCurrentPosition()+"");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        binding.seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        binding.ivPause.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println(isPlay);
                        if(isPlay==false){
                            isPlay= !isPlay;
                            binding.ivPause.setVisibility(View.INVISIBLE);
                            binding.ivPlay.setVisibility(View.VISIBLE);
                            mediaPlayer.pause();


                        }

                    }
                }
        );
        binding.ivPlay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isPlay==true){
                            isPlay= !isPlay;
                            binding.ivPause.setVisibility(View.VISIBLE);
                            binding.ivPlay.setVisibility(View.INVISIBLE);
                            mediaPlayer.start();
                        }

                    }
                }
        );

        binding.ivBackward.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(String.valueOf(Uri.parse(path)));
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );





    }

    private String convertTime(String duration){
        Long mills=Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(mills)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mills)%TimeUnit.MINUTES.toSeconds(1)
                );
    }

    public class Update implements Runnable{

        @Override
        public void run() {
            binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
            binding.durationStart.setText(convertTime(mediaPlayer.getCurrentPosition()+""));
            handler.postDelayed(this,100);
        }
    }


}
