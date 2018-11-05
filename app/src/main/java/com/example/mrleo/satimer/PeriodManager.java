package com.example.mrleo.satimer;

import java.util.ArrayList;

public class PeriodManager {
    private ArrayList<Period> periods;

    public PeriodManager() {
        initPeriods();
    }

    public Period getPeriod(int index){
        if(index >= 0 && index < periods.size()){
            return periods.get(index);
        }

        return null;
    }

    private void initPeriods(){
        periods = new ArrayList<Period>();
        /*
        periods.add(new Period(50, "You have fifteen minutes to finish reading session."));
        periods.add(new Period(15, "Congratulations! You finished reading session. Have ten minutes rest"));
        periods.add(new Period(10, "Rest is over, start writing and language session."));
        periods.add(new Period(25, "You have ten minutes to finish writing and language session."));
        periods.add(new Period(10, "Congratulations, you finished writing and language session. Now start math without calculator session."));
        periods.add(new Period(5, "You have 5 minutes to finish math without calculator session."));
        periods.add(new Period(135, "Congratulations, you finished math without calculator session. Have five minutes rest."));
        periods.add(new Period(140, "Rest is over, start math with calculator session."));
        periods.add(new Period(180, "You have fifteen minutes to finish math with calculator session."));
        periods.add(new Period(195, "Congratulations, you finished math with calculator session."));
        */

        periods.add(new Period(3, "reading", 2, 2));
        periods.add(new Period(5, "writing and language", 2, 1));
        periods.add(new Period(2, "math with calculator"));
        //periods.add(new Period(3, "You have ten minutes to finish writing and language session."));
        //periods.add(new Period(3, "Congratulations, you finished math with calculator session."));
    }
}
