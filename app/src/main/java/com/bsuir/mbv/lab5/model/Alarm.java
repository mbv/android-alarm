package com.bsuir.mbv.lab5.model;

import android.app.PendingIntent;
import android.content.Intent;

public class Alarm {
    private AlarmDescription alarmDescription;
    private Intent intent;
    private PendingIntent pendingIntent;

    public AlarmDescription getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(AlarmDescription alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
}
