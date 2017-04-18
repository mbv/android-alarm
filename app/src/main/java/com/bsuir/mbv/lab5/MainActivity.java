package com.bsuir.mbv.lab5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bsuir.mbv.lab5.db.AlarmDAO;
import com.bsuir.mbv.lab5.model.Alarm;

public class MainActivity extends AppCompatActivity implements DetailActivityCaller {
    AlarmDAO alarmDAO;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmDAO = new AlarmDAO(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm alarm = new Alarm();
                alarm.setRingtone(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI));
                alarm.setTime(0);


                alarmDAO.save(alarm);
                adapter.updateData(alarmDAO.getAll());
                //adapter.notifyDataSetChanged();
            }
        });


        //populateRecords(alarms);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(alarmDAO.getAll(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void openDetail(Alarm alarm) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.variableModelName, (Parcelable) alarm);
        startActivityForResult(intent, Constants.variableRequestCode);
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        alarmDAO.delete(alarm);
        adapter.updateData(alarmDAO.getAll());
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
                Alarm alarm = data.getParcelableExtra(Constants.variableModelName);

                Alarm alarmOld = alarmDAO.get(alarm.getId());

                Intent intentOld = new Intent(getApplicationContext(), AlarmReceiver.class);
                intentOld.putExtra(Constants.alarmDescriptionId, alarmOld.getId());
                intentOld.putExtra(Constants.alarmDescriptionUri, alarmOld.getRingtone().toString());
                intentOld.putExtra(Constants.startPlaying, true);

                PendingIntent pendingIntentOld = PendingIntent.getBroadcast(getApplicationContext(), alarmOld.getId(),
                        intentOld, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmOld.getTime(),
                        pendingIntentOld);
                pendingIntentOld.cancel();

                intentOld.putExtra(Constants.startPlaying, false);

                sendBroadcast(intentOld);

                alarmDAO.update(alarm);

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra(Constants.alarmDescriptionId, alarm.getId());
                intent.putExtra(Constants.alarmDescriptionUri, alarm.getRingtone().toString());
                intent.putExtra(Constants.startPlaying, true);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(),
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime(),
                        pendingIntent);
                adapter.notifyDataSetChanged();

                adapter.updateData(alarmDAO.getAll());
            }
        }
    }

    private void populateRecords(AlarmList alarms) {
        for (int i = 0; i < 20; i++) {

            Alarm alarm = new Alarm();
            alarm.setRingtone(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI));
            alarm.setTime(0);

           //alarm.setIntent(new Intent(this, AlarmReceiver.class));


            alarms.add(alarm);
        }
    }
}
