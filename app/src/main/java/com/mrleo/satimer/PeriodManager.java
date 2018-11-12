package com.mrleo.satimer;

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

    public ArrayList<Period> getPeriods(){
        return periods;
    }

    private void initPeriods(){
        periods = new ArrayList<Period>();

        periods.add(new Period(65, "Reading", 15, false));
        periods.add(new Period(10, "Break", 0, true));
        periods.add(new Period(35, "Writing and language", 10, false));
        periods.add(new Period(25, "Math without calculator", 5, false));
        periods.add(new Period(5, "Break", 0, true));
        periods.add(new Period(55, "Math with calculator", 15, false));

/*
        periods.add(new Period(3, "Reading", 1, false));
        periods.add(new Period(2, "Break", 0, true));
        periods.add(new Period(2, "Writing and language", 1, false));
        periods.add(new Period(2, "Math without calculator", 1, false));
        periods.add(new Period(1, "Break", 0, true));
        periods.add(new Period(4, "Math with calculator", 2, false));*/
    }
}
