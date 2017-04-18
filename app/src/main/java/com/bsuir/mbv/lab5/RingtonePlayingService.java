package com.bsuir.mbv.lab5;


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

import com.bsuir.mbv.lab5.model.Alarm;


public class RingtonePlayingService extends Service {
    private SparseArray<AlarmService> _alarmServies = new SparseArray<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        int alarmDescriptionId = intent.getExtras().getInt(Constants.alarmDescriptionId);
        String alarmDescriptionUri = intent.getExtras().getString(Constants.alarmDescriptionUri);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);
        if (alarmDescriptionUri != null) {
            Alarm alarm = new Alarm();
            alarm.setId(alarmDescriptionId);
            alarm.setRingtone(Uri.parse(alarmDescriptionUri));
            if (alarm.isDefaultRingtone()) {
                return START_NOT_STICKY;
            }
            AlarmService alarmService = _alarmServies.get(alarm.getId());
            if (alarmService == null) {
                alarmService = new AlarmService();
                alarmService.setId(alarm.getId());
                alarmService.setRingtone(alarm.getRingtone());
                alarmService.setPlaying(false);
                _alarmServies.put(alarm.getId(), alarmService);
            }



            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                    intent_main_activity, 0);

            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setSmallIcon(R.drawable.abc_btn_switch_to_on_mtrl_00001)
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();


            if (!alarmService.isPlaying() && startPlaying) {
                if (alarmService.getMediaPlayer() == null) {
                    alarmService.setMediaPlayer(MediaPlayer.create(this, alarmService.getRingtone()));
                }
                notify_manager.notify(alarmService.getId(), notification_popup);
                alarmService.getMediaPlayer().start();
                alarmService.setPlaying(true);


            } else if (alarmService.isPlaying() && !startPlaying) {
                alarmService.getMediaPlayer().stop();
                alarmService.getMediaPlayer().reset();
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
