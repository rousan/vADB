package in.byter.vadb.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Ariyan Khan on 13-04-2016.
 */
public class FilesData {

    public static final String INTERNAL_FOLDER_DATA = "data";
        public static final String INTERNAL_FOLDER_UPDATES = "updates";
            public static final String INTERNAL_FOLDER_APP_UPDATES = "app_updates";
                public static final String INTERNAL_FILE_UPDATED_DETAILS = "updated_app_details.xml";

    public static String FOLDER_DATA = "Data";
        public static String FOLDER_LOGS = "Logs";
                public static String FILE_LOGS_DATA = "logsdata.db";
        public static String FOLDER_TEMP_APP_DATA = "TempApp Data";
            public static String FILE_TEMP_APP = "tempapp.apk";

    public static String MAIN_DATA_TABLE_NAME = "LOGS";
    public static String TAG_COLUMN_NAME = "TAG";
    public static String LOGS_TYPE_COLUMN_NAME = "LOGS_TYPE";
    public static String TIME_COLUMN_NAME = "TIME";
    public static String MSG_COLUMN_NAME = "MSG";

    public static File getAppDataRoot() {
        return new File(Environment.getExternalStorageDirectory(), "vADB");
    }

    //can run in UI THread
    public static void setup() {
        try {

            File folder_root = getAppDataRoot();
            File folder_data = new File(folder_root, FOLDER_DATA);
            File folder_logs = new File(folder_data, FOLDER_LOGS);
            File folder_temp_app_data = new File(folder_data, FOLDER_TEMP_APP_DATA);

            if (!folder_root.isDirectory())
                folder_root.mkdirs();
            if (!folder_data.isDirectory())
                folder_data.mkdirs();
            if (!folder_logs.isDirectory())
                folder_logs.mkdirs();
            if (!folder_temp_app_data.isDirectory())
                folder_temp_app_data.mkdirs();

            File file_tmp_app = new File(folder_temp_app_data, FILE_TEMP_APP);
            if (!file_tmp_app.isFile())
                file_tmp_app.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //can run in UI Thread
    public static void internalSetup(final Context context, boolean inThreadFlag) {
        try {
            File dir_root = context.getDir(INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
            File dir_updates = new File(dir_root, INTERNAL_FOLDER_UPDATES);
            File dir_app_updates = new File(dir_updates, INTERNAL_FOLDER_APP_UPDATES);

            if (!dir_root.isDirectory())
                dir_root.mkdirs();
            if (!dir_updates.isDirectory())
                dir_updates.mkdirs();
            if (!dir_app_updates.isDirectory())
                dir_app_updates.mkdirs();

            final String this_app_info = "<APPUPDATE>\n" +
                    "    <RESTRICTION>false</RESTRICTION>\n" +
                    "    <VERSIONCODE>20162</VERSIONCODE>\n" +
                    "    <VERSIONNAME>2.0.0</VERSIONNAME>\n" +
                    "    <SIZE>2.4MB</SIZE>\n" +
                    "    <DOWNLOADLINK>http://byter.in/applications/vADB/downloads/20162/vADB-2.0.0.apk</DOWNLOADLINK>\n" +
                    "    <EXTRADATAHTMLURL>http://byter.in/applications/vADB/downloads/20162/extra_data.html</EXTRADATAHTMLURL>\n" +
                    "    <DESCRIPTION>This version has some extra features like command run etc, and it has been fixed some bugs.$NLSo update now.</DESCRIPTION>\n" +
                    "</APPUPDATE>\n";

            final File file_updated_app_details = new File(dir_app_updates, INTERNAL_FILE_UPDATED_DETAILS);
            if (!file_updated_app_details.isFile()) {
                if (inThreadFlag) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (ObjectLocker.UPDATED_APP_DETAILS_FILE_OPERATION_LOCKER) {
                                    if (!file_updated_app_details.isFile()) {
                                        file_updated_app_details.createNewFile();
                                        writeFile(file_updated_app_details, this_app_info, false);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    synchronized (ObjectLocker.UPDATED_APP_DETAILS_FILE_OPERATION_LOCKER) {
                        if (!file_updated_app_details.isFile()) {
                            file_updated_app_details.createNewFile();
                            writeFile(file_updated_app_details, this_app_info, false);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getTemmAppFile() {
        File folder_root = getAppDataRoot();
        File folder_data = new File(folder_root, FOLDER_DATA);
        File folder_logs = new File(folder_data, FOLDER_LOGS);
        File folder_temp_app_data = new File(folder_data, FOLDER_TEMP_APP_DATA);
        return new File(folder_temp_app_data, FILE_TEMP_APP);
    }


    public static void writeFile(File file, String str, boolean append) {
        try {

            FileOutputStream fos = new FileOutputStream(file, append);
            fos.write(str.getBytes("UTF-8"));
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        String contents = "";
        try {

            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = null;
            while((line = bfr.readLine()) != null) {
                contents += line;
            }
            bfr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

}























