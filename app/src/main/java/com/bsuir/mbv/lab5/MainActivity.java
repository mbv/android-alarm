package com.bsuir.mbv.lab5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bsuir.mbv.lab5.model.Alarm;
import com.bsuir.mbv.lab5.model.AlarmDescription;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements DetailActivityCaller {
    AlarmList alarms = new AlarmList();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm alarm = new Alarm();

                AlarmDescription alarmDescription = new AlarmDescription();
                alarmDescription.setRingtone(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI));
                alarmDescription.setTime(0);

                alarm.setAlarmDescription(alarmDescription);

                alarms.add(alarm);
                adapter.notifyDataSetChanged();
            }
        });


        populateRecords(alarms);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(alarms, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void openDetail(Alarm alarm) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.variableModelName, alarm.getAlarmDescription());
        startActivityForResult(intent, Constants.variableRequestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.variableRequestCode) {
            if (resultCode == RESULT_OK) {
                AlarmDescription alarmDescription = data.getParcelableExtra(Constants.variableModelName);

                Alarm alarm = alarms.get(alarmDescription.getId());

                /*Intent intentOld = new Intent(this, AlarmReceiver.class);
                intentOld.putExtra(Constants.alarmDescription, alarm.getAlarmDescription());
                intentOld.putExtra(Constants.startPlaying ,true);

                PendingIntent pendingIntentOld = PendingIntent.getBroadcast(MainActivity.this, alarmDescription.getId(),
                        intentOld, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmDescription().getTime(),
                        pendingIntentOld);
                pendingIntentOld.cancel();*/

                alarm.setAlarmDescription(alarmDescription);

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra(Constants.alarmDescription, alarm.getAlarmDescription());
                intent.putExtra(Constants.startPlaying, true);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UUID.randomUUID().hashCode(),
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmDescription().getTime(),
                        pendingIntent);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void populateRecords(AlarmList alarms) {
        for (int i = 0; i < 20; i++) {
            Alarm alarm = new Alarm();

            AlarmDescription alarmDescription = new AlarmDescription();
            alarmDescription.setRingtone(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI));
            alarmDescription.setTime(0);

            alarm.setAlarmDescription(alarmDescription);

           //alarm.setIntent(new Intent(this, AlarmReceiver.class));


            alarms.add(alarm);
        }
    }
}
