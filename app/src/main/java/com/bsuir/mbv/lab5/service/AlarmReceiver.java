package com.bsuir.mbv.lab5.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bsuir.mbv.lab5.Constants;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmDescriptionId = intent.getExtras().getInt(Constants.alarmModelId);
        String alarmDescriptionUri = intent.getExtras().getString(Constants.alarmModelUri);
        boolean startPlaying = intent.getExtras().getBoolean(Constants.startPlaying);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra(Constants.alarmModelId, alarmDescriptionId);
        serviceIntent.putExtra(Constants.alarmModelUri, alarmDescriptionUri);
        serviceIntent.putExtra(Constants.startPlaying, startPlaying);

        context.startService(serviceIntent);
    }

}
