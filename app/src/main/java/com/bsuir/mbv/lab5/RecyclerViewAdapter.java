package com.bsuir.mbv.lab5;

import android.content.Context;
import android.media.RingtoneManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bsuir.mbv.lab5.model.Alarm;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private AlarmList _alarms;
    private Context _context;

    public RecyclerViewAdapter(AlarmList alarmDescriptions, Context context) {
        this._alarms = alarmDescriptions;
        this._context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Alarm alarm = _alarms.get(i);

        viewHolder.timeLabel.setText(alarm.getAlarmDescription().getTimeSting());
        viewHolder.ringtoneLabel.setText(RingtoneManager.getRingtone(_context, alarm.getAlarmDescription().getRingtone()).getTitle(_context));
        viewHolder.deleteButtonListener.setAlarm(alarm);
        viewHolder.viewItemListener.setAlarm(alarm);
    }

    @Override
    public int getItemCount() {
        return _alarms.size();
    }


    private void delete(Alarm alarm) {
        int position = _alarms.indexOf(alarm);
        _alarms.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeLabel;
        private TextView ringtoneLabel;
        private Button deleteButton;
        private DeleteButtonListener deleteButtonListener;
        private ViewItemListener viewItemListener;

        public ViewHolder(View itemView) {
            super(itemView);
            timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            ringtoneLabel = (TextView) itemView.findViewById(R.id.ringtoneLabel);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
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
                delete(alarm);
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
                if (_context instanceof DetailActivityCaller) {
                    ((DetailActivityCaller) _context).openDetail(alarm);
                }
            }
        }

        public void setAlarm(Alarm alarm) {
            this.alarm = alarm;
        }
    }
}
