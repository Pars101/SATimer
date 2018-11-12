package com.example.mrleo.satimer;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Warning {
    private static final String WARNING_FILE_NAME = "warning";

    public static void doNotShowWarningAgain(Context context){
        try {
            FileOutputStream fout = context.openFileOutput(WARNING_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeBoolean(false);
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

    public static boolean showWarning(Context context){
        File file = context.getFileStreamPath(WARNING_FILE_NAME);
        if(file != null && file.exists()) {
            try {
                FileInputStream fis = context.openFileInput(WARNING_FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                boolean showWarning = ois.readBoolean();
                ois.close();
                fis.close();
                return showWarning;
            } catch (IOException e) {
                //Log.i("IOExceptionCardSet", e.getMessage());
            }
        }

        return true;
    }
}
