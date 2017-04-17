package com.bsuir.mbv.lab5;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.bsuir.mbv.lab5.model.AlarmDescription;

public class DetailActivity extends AppCompatActivity {

    private Button selectRingtoneButton;
    private TimePicker timePicker;
    private AlarmDescription alarmDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        selectRingtoneButton = (Button) findViewById(R.id.select_ringtone);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

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

            }
        });
    }

    void selectRingtone() {
        Intent tmpIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        startActivityForResult(tmpIntent, Constants.ringtoneManagerRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ringtoneManagerRequestCode) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                   alarmDescription.setRingtone(uri);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmDescription = getIntent().getParcelableExtra(Constants.variableModelName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(Constants.variableModelName, alarmDescription);
        setResult(RESULT_OK, i);
        finish();
    }

}
