package com.bsuir.mbv.lab5.model;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Locale;

public class Alarm implements Parcelable {

    private int id;
    private long time;
    private Uri ringtone;

    public Alarm() {

    }

    protected Alarm(Parcel in) {
        id = in.readInt();
        time = in.readLong();
        ringtone = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(time);
        dest.writeParcelable(ringtone, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Uri getRingtone() {
        return ringtone;
    }

    public boolean isDefaultRingtone() {
        return ringtone.toString().equals(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI);
    }

    public void setRingtone(Uri ringtone) {
        this.ringtone = ringtone;
    }

    public String getRingtoneString(Context context) {
        if (isDefaultRingtone()) {
            return "None";
        }
        return RingtoneManager.getRingtone(context, ringtone).getTitle(context);
    }

    public String getTimeSting() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        return String.format(Locale.getDefault(), "%1$02d:%2$02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }
}