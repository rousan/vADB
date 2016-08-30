package in.byter.vadb.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

/**
 * Created by Ariyan Khan on 28-02-2016.
 */
public final class VLog {

    public static int inc = 0;

    public static Object lock = new Object();

    private static File getRoot() {
        return new File(Environment.getExternalStorageDirectory(), "Virtual Adb");
    }

    private static File getLogFile() {
        return new File(getRoot(), "logs.txt");
    }

    public static void d(String tag, Object logs) {

        logs = (logs == null) ? ("null") : (logs);
        //Log.d(tag, logs.toString());

        /*try {
            if(!getRoot().isDirectory()) {
                getRoot().mkdirs();
            }
            File file = getLogFile();
            if(!file.isFile()) {
                file.createNewFile();
            }

            boolean append = true;
            if(file.length() > (5*1024)) {
                append = false;
            }

            FileOutputStream fos = new FileOutputStream(file, append);
            PrintStream ps = new PrintStream(fos, true, "UTF-8");
            synchronized (lock) {
                ps.println(
                        Calendar.getInstance().get(Calendar.HOUR) + ":" +
                                Calendar.getInstance().get(Calendar.MINUTE) + ":" +
                                Calendar.getInstance().get(Calendar.SECOND) + "   " +
                                Calendar.getInstance().get(Calendar.DATE) + "-" +
                                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" +
                                Calendar.getInstance().get(Calendar.YEAR)
                );

                ps.println(tag + " : " + logs.toString());
                ps.flush();
                ps.close();
            }

        } catch (Exception e) {
            //nothing
        }*/
    }

}















