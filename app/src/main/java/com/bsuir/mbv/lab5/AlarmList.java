package com.bsuir.mbv.lab5;


import android.util.SparseArray;

import com.bsuir.mbv.lab5.model.Alarm;

public class AlarmList {
    private int currentId = 0;
    private SparseArray<Alarm> _alarms = new SparseArray<>();

    public void add(Alarm alarm) {
        alarm.getAlarmDescription().setId(currentId);
        _alarms.put(currentId, alarm);
        currentId++;
    }

    public Alarm get(int id) {
        return _alarms.get(id);
    }

    public void remove(int id) {
        _alarms.remove(id);
    }

    public int size() {
        return _alarms.size();
    }

    public int indexOf(Alarm alarm) {
        return _alarms.indexOfValue(alarm);
    }
}
