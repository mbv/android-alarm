package com.bsuir.mbv.lab5;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity implements DetailActivityCaller {
    AlarmList alarms = new AlarmList();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        populateRecords(alarms);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
                alarms.get(alarmDescription.getId()).setAlarmDescription(alarmDescription);
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

           //alarm.setIntent(new Intent(this, Alarm_Receiver.class));


            alarms.add(alarm);
        }
    }
}
