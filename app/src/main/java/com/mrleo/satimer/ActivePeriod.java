package com.mrleo.satimer;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ActivePeriod implements Serializable {
    private static final String PERIOD_FILE_NAME = "alarmcardid";

    private int startPeriod;
    private long startMilliSeconds;

    public ActivePeriod(int startPeriod, long startMilliSeconds) {
        this.startPeriod = startPeriod;
        this.startMilliSeconds = startMilliSeconds;
    }

    public int getStartPeriod() {
        return startPeriod;
    }

    public long getStartMilliSeconds() {
        return startMilliSeconds;
    }

    public static void saveActivePeriod(Context context, ActivePeriod activePeriod){
        try {
            FileOutputStream fout = context.openFileOutput(PERIOD_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(activePeriod);
            oos.close();
            fout.close();
        } catch(NotSerializableException e){
            //Log.i("Not Serializable", e.getMessage());
        } catch(InvalidClassException e){
            //Log.i("Invalid Class", e.getMessage());
        } catch(IOException e){
            //Log.i("IO Class", e.getMessage());
        }
    }

    public static ActivePeriod readActivePeriod(Context context){
        File file = context.getFileStreamPath(PERIOD_FILE_NAME);
        if(file != null && file.exists()) {
            try {
                FileInputStream fis = context.openFileInput(PERIOD_FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ActivePeriod activePeriod = (ActivePeriod) ois.readObject();
                ois.close();
                fis.close();
                return activePeriod;
            } catch (IOException e) {
                //Log.i("IOExceptionCardSet", e.getMessage());
            } catch (ClassNotFoundException e) {
                //Log.i("ClassNotFound", e.getMessage());
            }
        }

        return null;
    }

    public static void clearActivePeriod(Context context){
        saveActivePeriod(context, new ActivePeriod(-1, 0));
    }
}
