package com.bsuir.mbv.lab5.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bsuir.mbv.lab5.Constants;
import com.bsuir.mbv.lab5.R;
import com.bsuir.mbv.lab5.model.Alarm;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private Button selectRingtoneButton;
    private TextView ringtoneTextView;
    private TimePicker timePicker;
    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        selectRingtoneButton = (Button) findViewById(R.id.selectRingtoneButton);
        ringtoneTextView = (TextView) findViewById(R.id.ringtoneTextView);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        selectRingtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRingtone();
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, view.getCurrentHour());
                calendar.set(Calendar.MINUTE, view.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                alarm.setTime(calendar.getTimeInMillis());
            }
        });
    }

    void selectRingtone() {
        Intent tmpIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        tmpIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarm.getRingtone());

        startActivityForResult(tmpIntent, Constants.ringtoneManagerRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ringtoneManagerRequestCode) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    alarm.setRingtone(uri);
                    ringtoneTextView.setText(alarm.getRingtoneString(this));
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarm = getIntent().getParcelableExtra(Constants.alarmModel);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        ringtoneTextView.setText(alarm.getRingtoneString(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(Constants.alarmModel, alarm);
        setResult(RESULT_OK, i);
        finish();
    }

}
