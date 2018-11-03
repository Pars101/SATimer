package com.example.mrleo.satimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

/**
 * Created by mrleo on 8/8/2018.
 */

public class Alarm extends BroadcastReceiver{
    String title;
    Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("Title");
        SpeechService.mTts.speak("Congratulations! " + "You just finished " + title, TextToSpeech.QUEUE_FLUSH,null);
        //int currentPeriod = intent.getIntExtra("currentPeriod", 0);
    }
}

