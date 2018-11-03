package com.example.mrleo.satimer;

/**
 * Created by mrleo on 11/2/2018.
 */

public class Period {
    private int minutes;
    private String message;

    public Period(int minutes, String message) {
        this.minutes = minutes;
        this.message = message;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getMessage() {
        return message;
    }
}
