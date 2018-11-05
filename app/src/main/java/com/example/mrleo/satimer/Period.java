package com.example.mrleo.satimer;

/**
 * Created by mrleo on 11/2/2018.
 */

public class Period {
    private int minutes;
    private String session;
    private int reminderTime;
    private int restTime;

    public Period(int minutes, String session) {
        this(minutes, session, 0, 0);
    }

    public Period(int minutes, String session, int reminderTime, int restTime) {
        this.minutes = minutes;
        this.session = session;
        this.reminderTime = reminderTime;
        this.restTime = restTime;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getSession() {
        return session;
    }

    public int getReminderTime() {
        return reminderTime;
    }

    public int getRestTime() {
        return restTime;
    }
}
