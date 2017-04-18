package com.bsuir.mbv.lab5.service;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.SparseArray;

import com.bsuir.mbv.lab5.Constants;
import com.bsuir.mbv.lab5.R;
import com.bsuir.mbv.lab5.activity.MainActivity;
import com.bsuir.mbv.lab5.model.Alarm;


public class RingtonePlayingService extends Service {
    private SparseArray<AlarmService> _alarmServices = new SparseArray<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int alarmDescriptionId = intent.getExtras().getInt(Constants.alarmModelId);
        String alarmDescriptionUri = intent.getExtras().getString(Constants.alarmModelUri);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);
        if (alarmDescriptionUri != null) {
            Alarm alarm = new Alarm();
            alarm.setId(alarmDescriptionId);
            alarm.setRingtone(Uri.parse(alarmDescriptionUri));
            if (alarm.isDefaultRingtone()) {
                return START_NOT_STICKY;
            }
            AlarmService alarmService = _alarmServices.get(alarm.getId());
            if (alarmService == null) {
                alarmService = new AlarmService();
                alarmService.setId(alarm.getId());
                alarmService.setRingtone(alarm.getRingtone());
                alarmService.setPlaying(false);
                _alarmServices.put(alarm.getId(), alarmService);
            } else {
                alarmService.setRingtone(alarm.getRingtone());
            }



            NotificationManager notifyManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Intent intentMainActivity = new Intent(this.getApplicationContext(), MainActivity.class);
            intentMainActivity.putExtra(Constants.alarmModelId, alarm.getId());
            PendingIntent pendingIntentMainActivity = PendingIntent.getActivity(this, Constants.stopAlarmRequestCode + alarm.getId(),
                    intentMainActivity, 0);

            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntentMainActivity)
                    .setAutoCancel(true)
                    .build();


            if (!alarmService.isPlaying() && startPlaying) {
                if (alarmService.getMediaPlayer() == null) {
                    alarmService.setMediaPlayer(MediaPlayer.create(this, alarmService.getRingtone()));
                }
                notifyManager.notify(alarmService.getId(), notification_popup);
                alarmService.getMediaPlayer().start();
                alarmService.setPlaying(true);


            } else if (alarmService.isPlaying() && !startPlaying) {
                alarmService.getMediaPlayer().stop();
                alarmService.setMediaPlayer(null);
                alarmService.setPlaying(false);
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
