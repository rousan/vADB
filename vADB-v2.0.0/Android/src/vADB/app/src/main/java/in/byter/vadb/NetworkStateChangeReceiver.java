package in.byter.vadb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.byter.vadb.utils.Utils;

public class NetworkStateChangeReceiver extends BroadcastReceiver {
    public NetworkStateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            Intent app_service = new Intent();
            app_service.setClassName("in.byter.vadb", "in.byter.vadb.AppService");
            context.startService(app_service);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
