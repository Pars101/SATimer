package com.mrleo.satimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textViewTimer;
    TextView textViewSession;
    CountDownTimer countDownTimer;
    Button startButton;
    Button cancelButton;
    RecyclerView recyclerView;
    SectionAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), SpeechService.class));

        ActivePeriod activePeriod = ActivePeriod.readActivePeriod(getApplicationContext());

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewSession = findViewById(R.id.textViewSession);
        startButton = findViewById(R.id.StartButton);
        cancelButton = findViewById(R.id.StopButton);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        sectionAdapter = new SectionAdapter(new PeriodManager().getPeriods());
        recyclerView.setAdapter(sectionAdapter);

        if(activePeriod != null && activePeriod.getStartPeriod() >= 0){
            sectionAdapter.setSelectedItem(activePeriod.getStartPeriod());
            sectionAdapter.setEnabled(false);

            startButton.setEnabled(false);
            cancelButton.setEnabled(true);

            startButton.setBackgroundResource(R.drawable.start_green);
            cancelButton.setBackgroundResource(R.drawable.stop_black);
        }
        else{
            sectionAdapter.setSelectedItem(-1);
            sectionAdapter.setEnabled(true);

            startButton.setEnabled(true);
            cancelButton.setEnabled(false);

            startButton.setBackgroundResource(R.drawable.start_black);
            cancelButton.setBackgroundResource(R.drawable.stop_gray);
        }



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(false);
                cancelButton.setEnabled(true);
                startButton.setBackgroundResource(R.drawable.start_green);
                cancelButton.setBackgroundResource(R.drawable.stop_black);
                int currentPeriod = sectionAdapter.getSelectedItem() == -1 ? 0 : sectionAdapter.getSelectedItem();
                sectionAdapter.setSelectedItem(currentPeriod);
                sectionAdapter.setEnabled(false);
                ActivePeriod.saveActivePeriod(getApplicationContext(), new ActivePeriod(currentPeriod, System.currentTimeMillis()));
                Alarm.setAlarm(getApplicationContext(), currentPeriod);
                updateClock();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                startButton.setBackgroundResource(R.drawable.start_black);
                cancelButton.setBackgroundResource(R.drawable.stop_gray);
                Alarm.cancelAlarm(getApplicationContext());
                ActivePeriod.clearActivePeriod(getApplicationContext());
                updateClock();
                sectionAdapter.notifyItemChanged(sectionAdapter.getSelectedItem());
                sectionAdapter.setSelectedItem(-1);
                sectionAdapter.setEnabled(true);
            }
        });

        if(Warning.showWarning(getApplicationContext())) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Note")
                    .setMessage("SAT is a trademark registered by the College Board, which is not affiliated with, and does not endorse, this app.")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton("Don't show this again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Warning.doNotShowWarningAgain(getApplicationContext());
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateClock();
    }

    private void updateClock(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }

        ActivePeriod activePeriod = ActivePeriod.readActivePeriod(getApplicationContext());
        int startPeriod = activePeriod != null ? activePeriod.getStartPeriod() : -1;
        if(startPeriod >= 0){
            ArrayList<Period> periods = new PeriodManager().getPeriods();
            long milliSeconds = System.currentTimeMillis() - activePeriod.getStartMilliSeconds();
            long start = 0;
            boolean hasActivePeriod = false;

            for(int i = startPeriod; i < periods.size(); i++){
                Period period = periods.get(i);
                long end = start + period.getMinutes() * 60 * 1000;
                textViewSession.setText(period.getSession());
                if(milliSeconds >= start && milliSeconds < end){
                    hasActivePeriod = true;
                    countDownTimer = new CountDownTimer(end - milliSeconds, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long seconds = millisUntilFinished / 1000;
                            long minutes = seconds / 60;
                            seconds %= 60;
                            textViewTimer.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                        }

                        public void onFinish() {
                            textViewTimer.setText("00:00");
                            updateClock();
                        }
                    }.start();
                    break;
                }

                start = end;
            }

            if(!hasActivePeriod){
                startButton.setBackgroundResource(R.drawable.start_black);
                cancelButton.setBackgroundResource(R.drawable.stop_gray);
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                sectionAdapter.setEnabled(true);
            }
        }
        else{
            textViewSession.setText("");
            textViewTimer.setText("00:00");
        }
    }
}
