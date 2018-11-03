package com.example.mrleo.satimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int[] timePeriods = {50, 65, 75, 95, 110, 10, 12};
    public static String[] periodNames = {"You have fifteen minutes to finish", "The Reading Section", "Rest", "The Writing and Language Section", "Math without calculator", "Rest", "Math with calculator"};

    private ArrayList<Period> periods = new ArrayList<Period>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), SpeechService.class));

        initPeriods();

        Button startButton = findViewById(R.id.StartButton);
        Button cancelButton = findViewById(R.id.StopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPeriod = 0;
                while (currentPeriod < timePeriods.length){
                    countTime(currentPeriod);
                    currentPeriod++;
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPeriod = 0;

                while (currentPeriod < timePeriods.length){
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, Alarm.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), currentPeriod, intent, 0);
                    alarmManager.cancel(pendingIntent);
                    currentPeriod++;
                }

            }
        });

    }

    private void initPeriods(){
        periods.add(new Period(50, "You have fifteen minutes to finish reading session."));
        periods.add(new Period(65, "Congratulations! You finished reading session. Have ten minutes rest"));
        periods.add(new Period(75, "Rest is over, start writing and language session."));
        periods.add(new Period(100, "You have ten minutes to finish writing and language session."));
        periods.add(new Period(110, "Congratulations, you finished writing and language session. Now start math without calculator session."));
        periods.add(new Period(130, "You have 5 minutes to finish math without calculator session."));
        periods.add(new Period(135, "Congratulations, you finished math without calculator session. Have five minutes rest."));
        periods.add(new Period(140, "Rest is over, start math with calculator session."));
        periods.add(new Period(180, "You have fifteen minutes to finish math with calculator session."));
        periods.add(new Period(195, "Congratulations, you finished math with calculator session."));
    }

    private void countTime(int currentPeriod){
        long milliseconds = timePeriods[currentPeriod]*1000*60;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("Title", periodNames[currentPeriod]);
        //intent.putExtra("currentPeriod", currentPeriod);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), currentPeriod, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + milliseconds, pendingIntent);
    }
}
