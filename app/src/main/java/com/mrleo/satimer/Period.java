package com.mrleo.satimer;

/**
 * Created by mrleo on 11/2/2018.
 */

public class Period {
    private int minutes;
    private String session;
    private int reminderTime;
    private boolean isRest;

    public Period(int minutes, String session, int reminderTime, boolean isRest) {
        this.minutes = minutes;
        this.session = session;
        this.reminderTime = reminderTime;
        this.isRest = isRest;
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

    public boolean getIsRest() {
        return isRest;
    }
}
