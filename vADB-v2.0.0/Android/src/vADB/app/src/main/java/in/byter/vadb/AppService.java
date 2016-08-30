package in.byter.vadb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import in.byter.vadb.utils.AppInfo;
import in.byter.vadb.utils.FilesData;
import in.byter.vadb.utils.ObjectLocker;
import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;

public class AppService extends Service {

    private final static int MAX_UPDATE_CHECK = 5;
    private Thread APP_UPDATE_CHECKER;

    public AppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            try {
                if (RemoteServerHelper.isNetworkOrInternetConnected(getApplicationContext())) {
                    if (Utils.CHECK_APP_UPDATE) {
                        if (APP_UPDATE_CHECKER == null) {
                            APP_UPDATE_CHECKER = new Thread(new AppUpdateCheck());
                            APP_UPDATE_CHECKER.start();
                        } else if (!APP_UPDATE_CHECKER.isAlive()) {
                            APP_UPDATE_CHECKER = new Thread(new AppUpdateCheck());
                            APP_UPDATE_CHECKER.start();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public class AppUpdateCheck implements Runnable {

        public void run() {
            try {

                if (!Utils.CHECK_APP_UPDATE) {
                    return;
                }
                int count = 0;
                while (true) {
                    try {
                        HashMap<String , String> outs = RemoteServerHelper.checkAppUpdate();
                        String status = outs.get("STATUS").trim();
                        if (status.equals("1")) {
                            String app_details = "<APPUPDATE>\n" +
                                    "    <RESTRICTION>" + outs.get("RESTRICTION").concat("") + "</RESTRICTION>\n" +
                                    "    <VERSIONCODE>" + outs.get("VERSIONCODE").concat("") + "</VERSIONCODE>\n" +
                                    "    <VERSIONNAME>" + outs.get("VERSIONNAME").concat("") + "</VERSIONNAME>\n" +
                                    "    <SIZE>" + outs.get("SIZE").concat("") + "</SIZE>\n" +
                                    "    <DOWNLOADLINK>" + outs.get("DOWNLOADLINK").concat("") + "</DOWNLOADLINK>\n" +
                                    "    <EXTRADATAHTMLURL>" + outs.get("EXTRADATAHTMLURL").concat("") + "</EXTRADATAHTMLURL>\n" +
                                    "    <DESCRIPTION>" + outs.get("DESCRIPTION").concat("") + "</DESCRIPTION>\n" +
                                    "</APPUPDATE>";

                            FilesData.internalSetup(getApplicationContext(), false);
                            File dir_root = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                            File dir_updates = new File(dir_root, FilesData.INTERNAL_FOLDER_UPDATES);
                            File dir_app_updates = new File(dir_updates, FilesData.INTERNAL_FOLDER_APP_UPDATES);
                            final File file_updated_app_details = new File(dir_app_updates, FilesData.INTERNAL_FILE_UPDATED_DETAILS);
                            String updated_data = "";
                            synchronized (ObjectLocker.UPDATED_APP_DETAILS_FILE_OPERATION_LOCKER) {
                                FilesData.writeFile(file_updated_app_details, app_details, false);
                                updated_data = FilesData.readFile(file_updated_app_details);
                            }
                            updated_data = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + updated_data + " </ROOT>";
                            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(updated_data)));
                            String version_Code = doc.getElementsByTagName("VERSIONCODE").item(0).getTextContent().trim();
                            int version_c = Integer.parseInt(version_Code);
                            if (version_c > AppInfo.APP_VERSION_CODE) {
                                try {
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                    int notif_id = Utils.APP_UPDATE_NOTIFICATION_ID;
                                    builder.setContentTitle("New version is available");
                                    builder.setContentText("Latest version v" + outs.get("VERSIONNAME"));
                                    builder.setSmallIcon(R.drawable.app_icon_for_notification);
                                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                   // TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                   // stackBuilder.addParentStack(MainActivity.class);
                                    //stackBuilder.addNextIntent(resultIntent);
                                    //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                                    PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);
                                    builder.setContentIntent(resultPendingIntent);
                                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                    builder.setOngoing(true);
                                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(notif_id, builder.build());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            Utils.CHECK_APP_UPDATE = false;//important
                            return;
                        } else {
                            if (count >= MAX_UPDATE_CHECK) {
                                return;
                            }
                            count++;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}




















