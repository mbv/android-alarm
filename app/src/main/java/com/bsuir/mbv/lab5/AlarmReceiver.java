package com.bsuir.mbv.lab5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bsuir.mbv.lab5.model.AlarmDescription;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmDescription alarmDescription = intent.getParcelableExtra(Constants.alarmDescription);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra(Constants.alarmDescription, alarmDescription);
        serviceIntent.putExtra(Constants.startPlaying, startPlaying);

        context.startService(serviceIntent);
    }

}
