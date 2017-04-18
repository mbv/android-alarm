package com.bsuir.mbv.lab5.activity;

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

import com.bsuir.mbv.lab5.activity.delegate.MainActivityDelegate;
import com.bsuir.mbv.lab5.service.AlarmReceiver;
import com.bsuir.mbv.lab5.Constants;
import com.bsuir.mbv.lab5.R;
import com.bsuir.mbv.lab5.activity.adapter.AlarmListViewAdapter;
import com.bsuir.mbv.lab5.db.AlarmDAO;
import com.bsuir.mbv.lab5.model.Alarm;

public class MainActivity extends AppCompatActivity implements MainActivityDelegate {
    AlarmDAO alarmDAO;
    RecyclerView recyclerView;
    AlarmListViewAdapter adapter;
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


                int id = alarmDAO.save(alarm);
                adapter.updateData(alarmDAO.getAll());
                openDetail(alarmDAO.get(id));
            }
        });


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new AlarmListViewAdapter(alarmDAO.getAll(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void openDetail(Alarm alarm) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.alarmModel, alarm);
        startActivityForResult(intent, Constants.detailViewRequestCode);
    }

    public void deleteAlarm(Alarm alarm) {
        disableAlarm(alarm);
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

    private void disableAlarm(Alarm alarm) {
        if (alarm == null) {
            return;
        }
        Intent intentOld = new Intent(getApplicationContext(), AlarmReceiver.class);
        intentOld.putExtra(Constants.alarmModelId, alarm.getId());
        intentOld.putExtra(Constants.alarmModelUri, alarm.getRingtone().toString());
        intentOld.putExtra(Constants.startPlaying, true);

        PendingIntent pendingIntentOld = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(),
                intentOld, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime(),
                pendingIntentOld);
        pendingIntentOld.cancel();

        intentOld.putExtra(Constants.startPlaying, false);

        sendBroadcast(intentOld);
    }

    private void enableAlarm(Alarm alarm) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(Constants.alarmModelId, alarm.getId());
        intent.putExtra(Constants.alarmModelUri, alarm.getRingtone().toString());
        intent.putExtra(Constants.startPlaying, true);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime(),
                pendingIntent);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.alarmModelId)) {
            int AlarmId = extras.getInt(Constants.alarmModelId);
            Alarm alarm = alarmDAO.get(AlarmId);
            disableAlarm(alarm);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.detailViewRequestCode) {
                Alarm alarm = data.getParcelableExtra(Constants.alarmModel);

                Alarm alarmOld = alarmDAO.get(alarm.getId());
                disableAlarm(alarmOld);


                alarmDAO.update(alarm);

                enableAlarm(alarm);

                adapter.updateData(alarmDAO.getAll());
            }
        }
    }
}
