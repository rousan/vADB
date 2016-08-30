package in.byter.vadb.lib;

import android.content.ContentValues;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import in.byter.vadb.BuildConfig;

public class VLog {
    private static volatile SQLiteDatabase db_connection;
    private static String MAIN_DATA_TABLE_NAME = "LOGS";
    private static String TAG_COLUMN_NAME = "TAG";
    private static String LOGS_TYPE_COLUMN_NAME = "LOGS_TYPE";
    private static String TIME_COLUMN_NAME = "TIME";
    private static String MSG_COLUMN_NAME = "MSG";
    private static String TABLE_CREATE_SQL_COMMAND = "CREATE TABLE IF NOT EXISTS " + MAIN_DATA_TABLE_NAME + " (\n" +
            " " + TAG_COLUMN_NAME + " TEXT ,\n" +
            " " + LOGS_TYPE_COLUMN_NAME + " TEXT ,\n" +
            " " + TIME_COLUMN_NAME + " TEXT ,\n" +
            " " + MSG_COLUMN_NAME + " TEXT \n" +
            ");";

    private static boolean vlogEnabled = true;
    private static String FOLDER_DATA = "Data";
        private static String FOLDER_LOGS = "Logs";
            private static String FILE_LOGS_DATA = "logsdata.db";

    private static class Settings {
        private static void setVlogEnabled(boolean enabled) {
            vlogEnabled = enabled;
        }
    }

    private static File getAppDataRoot() {
        return new File(Environment.getExternalStorageDirectory(), "vADB");
    }

    private static SQLiteDatabase getDbConnection() {
        SQLiteDatabase db = null;
        try {
            if (db_connection == null || !db_connection.isOpen() || !getLogsDBFile().isFile()) {
                if (db_connection != null) {
                    try {
                        db_connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                db_connection = openOrCreateDbConnection();
            }
            try {
                db_connection.execSQL(TABLE_CREATE_SQL_COMMAND);
            } catch (Exception e) {
                e.printStackTrace();
            }
            db = db_connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    private static SQLiteDatabase openOrCreateDbConnection() {
        SQLiteDatabase db = null;
        try {
            setup();
            db = SQLiteDatabase.openOrCreateDatabase(getLogsDBFile().getAbsolutePath(), null, new DatabaseErrorHandler() {
                @Override
                public void onCorruption(SQLiteDatabase dbObj) {
                    try {
                        try {
                            if (db_connection != null)
                                db_connection.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            File folder_root = getAppDataRoot();
                            File folder_data = new File(folder_root, FOLDER_DATA);
                            File folder_logs = new File(folder_data, FOLDER_LOGS);
                            String pkg_name = getAppPackageName();
                            File folder_package = new File(folder_logs, pkg_name);
                            for(File file : folder_package.listFiles()) {
                                file.delete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        db_connection = openOrCreateDbConnection();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    private static void setup() {
        try {
            File folder_root = getAppDataRoot();
            File folder_data = new File(folder_root, FOLDER_DATA);
            File folder_logs = new File(folder_data, FOLDER_LOGS);
            String pkg_name = getAppPackageName();
            File folder_package = new File(folder_logs, pkg_name);

            if (!folder_root.isDirectory())
                folder_root.mkdirs();
            if (!folder_data.isDirectory())
                folder_data.mkdirs();
            if (!folder_logs.isDirectory())
                folder_logs.mkdirs();
            if (!folder_package.isDirectory())
                folder_package.mkdirs();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAppPackageName() {
        return  BuildConfig.APPLICATION_ID;
    }

    private static File getLogsDBFile() {
        return new File(getAppDataRoot().getAbsolutePath() + File.separator + FOLDER_DATA + File.separator + FOLDER_LOGS + File.separator + getAppPackageName() + File.separator + FILE_LOGS_DATA);
    }

    public static boolean d(String tag, String msg) {
        boolean done = false;
        try {
            if (!vlogEnabled) {
                Log.d(tag, msg);
                return true;
            }
            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;

            ContentValues cv = new ContentValues();
            cv.put(TAG_COLUMN_NAME, tag);
            cv.put(MSG_COLUMN_NAME, msg);
            cv.put(TIME_COLUMN_NAME, System.currentTimeMillis() + "");
            cv.put(LOGS_TYPE_COLUMN_NAME, "d");

            SQLiteDatabase db_conn = getDbConnection();
            long done_int = db_conn.insert(MAIN_DATA_TABLE_NAME, null, cv);
            done = (done_int == -1) ? (false) : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    public static boolean e(String tag, String msg) {
        boolean done = false;
        try {
            if (!vlogEnabled) {
                Log.e(tag, msg);
                return true;
            }
            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;

            ContentValues cv = new ContentValues();
            cv.put(TAG_COLUMN_NAME, tag);
            cv.put(MSG_COLUMN_NAME, msg);
            cv.put(TIME_COLUMN_NAME, System.currentTimeMillis() + "");
            cv.put(LOGS_TYPE_COLUMN_NAME, "e");

            SQLiteDatabase db_conn = getDbConnection();
            long done_int = db_conn.insert(MAIN_DATA_TABLE_NAME, null, cv);
            done = (done_int == -1) ? (false) : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    public static boolean i(String tag, String msg) {
        boolean done = false;
        try {
            if (!vlogEnabled) {
                Log.i(tag, msg);
                return true;
            }
            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;

            ContentValues cv = new ContentValues();
            cv.put(TAG_COLUMN_NAME, tag);
            cv.put(MSG_COLUMN_NAME, msg);
            cv.put(TIME_COLUMN_NAME, System.currentTimeMillis() + "");
            cv.put(LOGS_TYPE_COLUMN_NAME, "i");

            SQLiteDatabase db_conn = getDbConnection();
            long done_int = db_conn.insert(MAIN_DATA_TABLE_NAME, null, cv);
            done = (done_int != -1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    public static boolean w(String tag, String msg) {
        boolean done = false;
        try {
            if (!vlogEnabled) {
                Log.w(tag, msg);
                return true;
            }
            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;

            ContentValues cv = new ContentValues();
            cv.put(TAG_COLUMN_NAME, tag);
            cv.put(MSG_COLUMN_NAME, msg);
            cv.put(TIME_COLUMN_NAME, System.currentTimeMillis() + "");
            cv.put(LOGS_TYPE_COLUMN_NAME, "w");

            SQLiteDatabase db_conn = getDbConnection();
            long done_int = db_conn.insert(MAIN_DATA_TABLE_NAME, null, cv);
            done = (done_int == -1) ? (false) : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    public static boolean v(String tag, String msg) {
        boolean done = false;
        try {
            if (!vlogEnabled) {
                Log.v(tag, msg);
                return true;
            }
            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;

            ContentValues cv = new ContentValues();
            cv.put(TAG_COLUMN_NAME, tag);
            cv.put(MSG_COLUMN_NAME, msg);
            cv.put(TIME_COLUMN_NAME, System.currentTimeMillis() + "");
            cv.put(LOGS_TYPE_COLUMN_NAME, "v");

            SQLiteDatabase db_conn = getDbConnection();
            long done_int = db_conn.insert(MAIN_DATA_TABLE_NAME, null, cv);
            done = (done_int == -1) ? (false) : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }
}



























