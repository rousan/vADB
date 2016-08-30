package in.byter.vadb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ariyan Khan on 13-04-2016.
 */
public class PreferencesHolder {
    public static String SOCKET_PORT_HOLDER_PREF = "SOCKET_PORT_PREF";
    public static String ADB_ENABLE_DISABLE_PREF = "ADB_ENABLE_DISABLE_PREF";
    public static String SOCKET_RESETUP_COMPLETED_PREF = "SOCKET_RESETUP_COMPLETED_PREF";

    public static String APP_SETTINGS_PREF_FILE = "app_settings_prefs_holder_file";
    public static int SOCKET_DEFAULT_PORT = 1247;

    //Run in main thread
    public static void setupAppPrefs(final Context context) {
        try {

            SharedPreferences sp = context.getSharedPreferences(APP_SETTINGS_PREF_FILE, 0);
            SharedPreferences.Editor editor = sp.edit();
            if(!sp.contains(SOCKET_PORT_HOLDER_PREF)) {
                editor.putInt(SOCKET_PORT_HOLDER_PREF, SOCKET_DEFAULT_PORT);
            }
            if(!sp.contains(ADB_ENABLE_DISABLE_PREF)) {
                editor.putBoolean(ADB_ENABLE_DISABLE_PREF, false);
            }
            if(!sp.contains(SOCKET_RESETUP_COMPLETED_PREF)) {
                editor.putBoolean(SOCKET_RESETUP_COMPLETED_PREF, false);
            }

            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




















