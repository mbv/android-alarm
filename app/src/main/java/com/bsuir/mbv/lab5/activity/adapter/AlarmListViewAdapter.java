package com.bsuir.mbv.lab5.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bsuir.mbv.lab5.R;
import com.bsuir.mbv.lab5.activity.delegate.MainActivityDelegate;
import com.bsuir.mbv.lab5.model.Alarm;

import java.util.List;

public class AlarmListViewAdapter extends RecyclerView.Adapter<AlarmListViewAdapter.ViewHolder> {

    private List<Alarm> _alarms;
    private Context _context;

    public AlarmListViewAdapter(List<Alarm> alarmDescriptions, Context context) {
        this._alarms = alarmDescriptions;
        this._context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(v);
    }

    public void updateData(List<Alarm> alarms) {
        this._alarms = alarms;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Alarm alarm = _alarms.get(i);

        viewHolder.timeLabel.setText(alarm.getTimeSting());
        viewHolder.ringtoneLabel.setText(alarm.getRingtoneString(_context));
        viewHolder.deleteButtonListener.setAlarm(alarm);
        viewHolder.viewItemListener.setAlarm(alarm);
    }

    @Override
    public int getItemCount() {
        return _alarms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeLabel;
        private TextView ringtoneLabel;
        private ImageButton deleteButton;
        private DeleteButtonListener deleteButtonListener;
        private ViewItemListener viewItemListener;

        public ViewHolder(View itemView) {
            super(itemView);
            timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            ringtoneLabel = (TextView) itemView.findViewById(R.id.ringtoneLabel);
            deleteButton = (ImageButton) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            deleteButtonListener = new DeleteButtonListener();
            viewItemListener = new ViewItemListener();
            deleteButton.setOnClickListener(deleteButtonListener);
            itemView.setOnClickListener(viewItemListener);
        }
    }


    private class DeleteButtonListener implements View.OnClickListener {
        private Alarm alarm;

        @Override
        public void onClick(View v) {
            if (alarm != null) {
                if (_context instanceof MainActivityDelegate) {
                    ((MainActivityDelegate) _context).deleteAlarm(alarm);
                }
            }
        }

        public void setAlarm(Alarm alarm) {
            this.alarm = alarm;
        }
    }


    private class ViewItemListener implements View.OnClickListener {
        private Alarm alarm;

        @Override
        public void onClick(View v) {
            if (alarm != null) {
                if (_context instanceof MainActivityDelegate) {
                    ((MainActivityDelegate) _context).openDetail(alarm);
                }
            }
        }

        public void setAlarm(Alarm alarm) {
            this.alarm = alarm;
        }
    }
}
