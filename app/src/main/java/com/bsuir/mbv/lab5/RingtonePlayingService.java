package com.bsuir.mbv.lab5;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.SparseArray;

import com.bsuir.mbv.lab5.model.AlarmDescription;


public class RingtonePlayingService extends Service {
    private SparseArray<AlarmService> _alarmServies = new SparseArray<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        AlarmDescription alarmDescription = intent.getParcelableExtra(Constants.alarmDescription);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);
        if (alarmDescription != null) {
            if (alarmDescription.isDefaultRingtone()) {
                return START_NOT_STICKY;
            }
            AlarmService alarmService = _alarmServies.get(alarmDescription.getId());
            if (alarmService == null) {
                alarmService = new AlarmService();
                alarmService.setId(alarmDescription.getId());
                alarmService.setRingtone(alarmDescription.getRingtone());
                alarmService.setPlaying(false);
                _alarmServies.put(alarmDescription.getId(), alarmService);
            }



            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                    intent_main_activity, 0);

            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    // .setSmallIcon(R.drawable.ic_action_call)
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();


            if (!alarmService.isPlaying() && startPlaying) {
                if (alarmService.getMediaPlayer() == null) {
                    alarmService.setMediaPlayer(MediaPlayer.create(this, alarmService.getRingtone()));
                }
                alarmService.getMediaPlayer().start();
                //notify_manager.notify(0, notification_popup);

            } else if (alarmService.isPlaying() && !startPlaying) {
                alarmService.getMediaPlayer().stop();
                alarmService.getMediaPlayer().reset();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
