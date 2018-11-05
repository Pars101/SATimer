package com.example.mrleo.satimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

/**
 * Created by mrleo on 8/8/2018.
 */

public class Alarm extends BroadcastReceiver{
    final static int REQ_CODE_INVALID = 0;
    final static int REQ_CODE_SESSION_START = 1;
    final static int REQ_CODE_REMINDER = 2;
    final static int REQ_CODE_REST = 4;
    final static int REQ_CODE_SESSION_END = 8;

    final static String PERIOD = "Period";
    final static String REQUEST_CODE = "RequestCode";

    static PeriodManager periodManager = new PeriodManager();

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra(Alarm.REQUEST_CODE, REQ_CODE_INVALID);
        int periodIndex = intent.getIntExtra(Alarm.PERIOD, 0);
        Period period = periodManager.getPeriod(periodIndex);

        if(requestCode == Alarm.REQ_CODE_SESSION_START){
            SpeechService.mTts.speak("Start the " + period.getSession() + " section.", TextToSpeech.QUEUE_ADD,null);
        }
        else if(requestCode == Alarm.REQ_CODE_SESSION_END){
            SpeechService.mTts.speak("Congratulations! You just finished the " + period.getSession() + " section.", TextToSpeech.QUEUE_ADD,null);

            if(period.getRestTime() > 0){
                SpeechService.mTts.speak("Start resting for " + period.getRestTime() + " minutes.", TextToSpeech.QUEUE_ADD,null);
            }
            else{
                setAlarm(context, periodIndex + 1);
            }
        }
        else if(requestCode == Alarm.REQ_CODE_REST){
            SpeechService.mTts.speak("Rest is over. Start the next section.", TextToSpeech.QUEUE_ADD,null);
            setAlarm(context, periodIndex + 1);
        }
        else if(requestCode == Alarm.REQ_CODE_REMINDER){
            SpeechService.mTts.speak("You have " + period.getReminderTime() + " minutes to finish the " + period.getSession() + " section.", TextToSpeech.QUEUE_ADD,null);
        }
    }

    public static void setAlarm(Context context, int currentPeriod){
        Period period = new PeriodManager().getPeriod(currentPeriod);
        if(period != null){
            long startInternal = 5 * 1000;
            long sessionMilliseconds = period.getMinutes() * 1000 * 60 + startInternal;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            setAlarm(alarmManager, context, currentPeriod, Alarm.REQ_CODE_SESSION_START, startInternal);
            setAlarm(alarmManager, context, currentPeriod, Alarm.REQ_CODE_SESSION_END, sessionMilliseconds);

            if(period.getReminderTime() > 0){
                setAlarm(alarmManager, context, currentPeriod, Alarm.REQ_CODE_REMINDER,
                        sessionMilliseconds - (period.getReminderTime() * 1000 * 60));
            }

            if(period.getRestTime() > 0){
                setAlarm(alarmManager, context, currentPeriod, Alarm.REQ_CODE_REST,
                        sessionMilliseconds + (period.getRestTime() * 1000 * 60));
            }
        }
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);

        PendingIntent pendingIntentSessionEnd = PendingIntent.getBroadcast(context, REQ_CODE_SESSION_END, intent, 0);
        alarmManager.cancel(pendingIntentSessionEnd);
        PendingIntent pendingIntentSessionStart = PendingIntent.getBroadcast(context, REQ_CODE_SESSION_START, intent, 0);
        alarmManager.cancel(pendingIntentSessionStart);
        PendingIntent pendingIntentReminder = PendingIntent.getBroadcast(context, REQ_CODE_REMINDER, intent, 0);
        alarmManager.cancel(pendingIntentReminder);
        PendingIntent pendingIntentRest = PendingIntent.getBroadcast(context, REQ_CODE_REST, intent, 0);
        alarmManager.cancel(pendingIntentRest);
    }

    private static void setAlarm(AlarmManager alarmManager, Context context, int period, int requestCode, long milliSeconds){
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(Alarm.REQUEST_CODE, requestCode);
        intent.putExtra(Alarm.PERIOD, period);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + milliSeconds, pendingIntent);
    }
}

