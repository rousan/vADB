package in.byter.vadb.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import in.byter.vadb.MainActivity;
import in.byter.vadb.R;

/**
 * Created by Ariyan Khan on 10-04-2016.
 */
public class Utils {

    public static int MIN_SERVER_PORT = 0;
    public static int MAX_SEREVR_PORT = 65535;
    public static String RESETUP_RECEIVER_BROADCAST_ACTION = "in.byter.vadb.action.broadcast.resetupsocket";
    public static String APP_UPDATE_CHECK_PERMISSION = "in.byter.vadb.permission.appupdatecheck";
    public static boolean CHECK_APP_UPDATE = false;
    public static final int APP_UPDATE_NOTIFICATION_ID = 20161;
    public static final int ADB_SERVICE_IS_RUNNING_NOTIFICATION_ID = 20162;

    public static void setFontsToAllTextViews(ViewGroup root) {
        try {

            for(int i=0; i<root.getChildCount(); i++) {
                try {
                    View view = root.getChildAt(i);
                    if(view instanceof TextView) {
                        setParagraphTextFont((TextView)view);
                    } else if (view instanceof ViewGroup){
                        setFontsToAllTextViews((ViewGroup)view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModifiedToast(final Context context, final int gravity, final int xOffset, final int yOffset, Object object) {
        try {

            final String text = (object == null) ? ("null") : (object.toString());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast toast = new Toast(context);
                        toast.setGravity(gravity, xOffset, yOffset);
                        ViewGroup viewGroup = (ViewGroup)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast_layout, null);
                        TextView textView = (TextView)viewGroup.findViewById(R.id.textview);
                        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf"));
                        textView.setText(text);
                        toast.setView(viewGroup);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Handler handler = new Handler(context.getMainLooper());
            handler.post(runnable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModifiedToast(final Context context, Object object) {
        try {
            showModifiedToast(context, Gravity.BOTTOM, 0, 0, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setHeadingTextFont(TextView textView) {
        try {

            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/OpenSans-Bold.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //call from diff thread, not from main thread
    public static InetAddress getDeviceIp(Context context) {
        InetAddress ia = null;
        try {

            ia = InetAddress.getByName("192.168.43.1");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ia;
    }

    public static void setParagraphTextFont(TextView textView) {
        try {

            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Don't run in ui thread
    public static Vector<String> getAllDeviceIPV4Address() {
        Vector<String> outs = new Vector<>();
        try {

            for(Enumeration<NetworkInterface> nfE = NetworkInterface.getNetworkInterfaces(); nfE.hasMoreElements();) {
                final NetworkInterface nf = nfE.nextElement();
                for(final Enumeration<InetAddress> iaE = nf.getInetAddresses(); iaE.hasMoreElements();) {
                    final String ip =  iaE.nextElement().getHostAddress();
                    if (ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {//like 127.0.0.1
                        outs.add(ip);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outs;
    }


    public static Vector<String> getLocalIPV4S() {
        Vector<String> ips = new Vector<>();
        try {

            ips.add("127.0.0.1");
            ips.add("0.0.0.0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ips;
    }

    public static void displayAdbServiceNotification(Context context, boolean enableRing, boolean enableVibrate) {
        try {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            int notif_id = Utils.ADB_SERVICE_IS_RUNNING_NOTIFICATION_ID;
            builder.setContentTitle("vADB");
            builder.setContentText("ADB Service is running");
            builder.setSmallIcon(R.drawable.app_icon_for_notification);
            Intent resultIntent = new Intent(context, MainActivity.class);
            //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //stackBuilder.addParentStack(MainActivity.class);
            //stackBuilder.addNextIntent(resultIntent);
            //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
            builder.setContentIntent(resultPendingIntent);
            if (enableRing)
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            if (enableVibrate)
                vibrate(context, 300);
            builder.setOngoing(true);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notif_id, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void vibrate(Context context, long millseconds) {
        try {

            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(millseconds);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIPV4(String ip) {
        boolean outs = false;
        try {

            if(ip == null) {
                return false;
            }

            ip = ip.trim();

            if(!ip.isEmpty() && ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                return true;
            } else {
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return outs;
    }

    public static String getFormattedLogs(String tag, String msg, String type_str, long time) {
        String formattedLogs = "";
        try {

            tag = (tag == null || tag.trim().isEmpty()) ? ("null") : tag;
            msg = (msg == null || msg.isEmpty()) ? ("   ") : msg;
            type_str = (type_str == null || type_str.trim().isEmpty()) ? ("d") : type_str;

            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(time);
            String milisecods = (cl.get(Calendar.MILLISECOND) < 10) ? ("0" + cl.get(Calendar.MILLISECOND)) : (cl.get(Calendar.MILLISECOND) + "");
            String seconds = ((cl.get(Calendar.SECOND) + 1) < 10) ? ("0" + (cl.get(Calendar.SECOND) + 1)) : ((cl.get(Calendar.SECOND) + 1) + "");
            String minutes = (cl.get(Calendar.MINUTE) < 10) ? ("0" + cl.get(Calendar.MINUTE)) : (cl.get(Calendar.MINUTE) + "");
            String hours = (cl.get(Calendar.HOUR_OF_DAY) < 10) ? ("0" + cl.get(Calendar.HOUR_OF_DAY)) : (cl.get(Calendar.HOUR_OF_DAY) + "");
            String day_month = (cl.get(Calendar.DAY_OF_MONTH) < 10) ? ("0" + cl.get(Calendar.DAY_OF_MONTH)) : (cl.get(Calendar.DAY_OF_MONTH) + "");
            String month = ((cl.get(Calendar.MONTH) + 1) < 10) ? ("0" + (cl.get(Calendar.MONTH) + 1)) : ((cl.get(Calendar.MONTH) + 1) + "");
            String year = (cl.get(Calendar.YEAR) < 10) ? ("0" + cl.get(Calendar.YEAR)) : (cl.get(Calendar.YEAR) + "");

            formattedLogs = String.format("%s-%s-%s %s:%s:%s.%s: %s/%s: %s",
                    day_month, month, year, hours, minutes, seconds, milisecods, type_str, tag, msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedLogs;
    }

    public static boolean checkPort(String port) {
        boolean outs = false;
        try {

            if(port == null) {
                return false;
            }

            port = port.trim();

            if(port.isEmpty())
                return false;

            if(!port.matches("\\d+")) {
                return false;
            }

            try {
                int port_int = Integer.parseInt(port);
                if(port_int < MIN_SERVER_PORT || port_int > MAX_SEREVR_PORT) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return outs;
    }

}
















