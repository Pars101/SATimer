package com.example.mrleo.satimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), SpeechService.class));

        Button startButton = findViewById(R.id.StartButton);
        Button cancelButton = findViewById(R.id.StopButton);

        final int currentPeriod = 0;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm.setAlarm(getApplicationContext(), currentPeriod);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm.cancelAlarm(getApplicationContext());
            }
        });

    }
}
