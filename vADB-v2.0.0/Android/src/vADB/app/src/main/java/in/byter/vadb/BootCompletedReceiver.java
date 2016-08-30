package in.byter.vadb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import in.byter.vadb.utils.PreferencesHolder;
import in.byter.vadb.utils.Utils;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            Utils.CHECK_APP_UPDATE = true;//importnt to top

            Intent adb_server_service = new Intent();
            adb_server_service.setClassName("in.byter.vadb", "in.byter.vadb.AdbServerService");
            context.startService(adb_server_service);

            Intent app_service = new Intent();
            app_service.setClassName("in.byter.vadb", "in.byter.vadb.AppService");
            context.startService(app_service);

            try {
                SharedPreferences sp2 = context.getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                boolean adb_enabled = sp2.getBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
                if (adb_enabled)
                    Utils.displayAdbServiceNotification(context.getApplicationContext(), true, true);
                else {
                    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(Utils.ADB_SERVICE_IS_RUNNING_NOTIFICATION_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




















