package com.example.mrleo.satimer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), SpeechService.class));

        ActivePeriod activePeriod = ActivePeriod.readActivePeriod(getApplicationContext());
        startButton = findViewById(R.id.StartButton);
        cancelButton = findViewById(R.id.StopButton);

        if(activePeriod != null && activePeriod.getStartPeriod() >= 0){
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);

            startButton.setBackgroundResource(R.drawable.start_gray);
            cancelButton.setBackgroundResource(R.drawable.stop_black);
        }
        else{
            startButton.setEnabled(true);
            cancelButton.setEnabled(false);

            startButton.setBackgroundResource(R.drawable.start_black);
            cancelButton.setBackgroundResource(R.drawable.stop_gray);
        }

        final int currentPeriod = 0;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(false);
                cancelButton.setEnabled(true);
                startButton.setBackgroundResource(R.drawable.start_gray);
                cancelButton.setBackgroundResource(R.drawable.stop_black);
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
            }
        });

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewSession = findViewById(R.id.textViewSession);
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
            }
        }
        else{
            textViewSession.setText("");
            textViewTimer.setText("00:00");
        }
    }
}
