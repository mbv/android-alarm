package com.bsuir.mbv.lab5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmDescriptionId = intent.getExtras().getInt(Constants.alarmDescriptionId);
        String alarmDescriptionUri = intent.getExtras().getString(Constants.alarmDescriptionUri);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra(Constants.alarmDescriptionId, alarmDescriptionId);
        serviceIntent.putExtra(Constants.alarmDescriptionUri, alarmDescriptionUri);
        serviceIntent.putExtra(Constants.startPlaying, startPlaying);

        context.startService(serviceIntent);
    }

}
