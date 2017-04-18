package com.bsuir.mbv.lab5.activity.delegate;

import com.bsuir.mbv.lab5.model.Alarm;


public interface MainActivityDelegate {
    void openDetail(Alarm alarm);
    void deleteAlarm(Alarm alarm);
}
