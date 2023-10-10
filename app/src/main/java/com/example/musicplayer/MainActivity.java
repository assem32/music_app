package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.musicplayer.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList <MusicModel> musicList =new ArrayList<>();

    RecyclerViewMusic recyclerViewMusic =new RecyclerViewMusic();

    ActivityMainBinding binding ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel1 = new NotificationChannel("name1", "Channel(1)",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
        }




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String [] projection={
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION
                };

                String selection=MediaStore.Audio.Media.IS_MUSIC +" !=0 ";

                Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
                while (cursor.moveToNext()){
                    MusicModel musicModel= new MusicModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
                    if (new File(musicModel.getPath()).exists()) {
                        musicList.add(musicModel);

                    }
                }

                recyclerViewMusic.setList(musicList);
                binding.recyclerMusic.setAdapter(recyclerViewMusic);
                recyclerViewMusic.setOnClick(
                        new RecyclerViewMusic.OnClick() {
                            @Override
                            public void onClick(String name, String duration, String path) {
                                MediaPlayer player= MediaPlayeInstant.getInstance();
                                player=null;
                                Intent intent =new Intent(MainActivity.this,MusicDetails.class);
                                intent.putExtra("title",name);
                                intent.putExtra("duration",duration);
                                intent.putExtra("path",path);
                                startActivity(intent);


                                sendNotification("Music app",name);

//                                NotificationCompat.Builder builder= new NotificationCompat.Builder(MainActivity.this,"notification");
//                                builder.setContentTitle(name);
//                                builder.setContentText("hi");
//                                builder.setSmallIcon(R.drawable.musicimg);
//
//                                builder.setAutoCancel(true);
//                                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity.this);
//                                managerCompat.notify(1,builder.build());
//                                MediaPlayer mediaPlayer = new MediaPlayer();
//
//                                try {
//                                    mediaPlayer.setDataSource(String.valueOf(Uri.parse(path)));
//                                    mediaPlayer.prepare();
//                                    mediaPlayer.start();
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
                            }
                        }
                );
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendNotification(String title,  String messageBody) {


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "dafsda";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Notfication",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription( "test channel");
            channel.enableLights(true);
            channel.setLightColor(0xffBA55D3);

            long [] arr = {0, 1000, 500, 1000};
            channel.setVibrationPattern(arr);
            notificationManager.createNotificationChannel(channel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        long [] arr = {0, 1000, 500, 1000};
        NotificationCompat.Builder notificationBuilder =new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.play_solid)
                .setVibrate(arr)
                .setColor(0xffBA55D3)
                .setStyle(new  NotificationCompat.InboxStyle()
                        .addLine(messageBody)
                ).setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        // Since android Oreo notification channel is needed.
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}