package com.example.mrleo.satimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechService extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    public static TextToSpeech mTts;

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.US);
        }
    }

    @Override
    public void onUtteranceCompleted(String uttId) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
