package in.byter.vadb;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import in.byter.vadb.lib.VLog;
import in.byter.vadb.utils.AppInfo;
import in.byter.vadb.utils.FilesData;
import in.byter.vadb.utils.ObjectLocker;
import in.byter.vadb.utils.PreferencesHolder;
import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;


public class MainActivity extends Activity {

    private long HOME_FIRST_BACK_PRESSED_TIMESTAMP = System.currentTimeMillis();
    private long HOME_INTERVAL_TWO_BACK_PRESSED = 2*1000;//10sec

    private TextView enable_disable_adb_btn;
    private TextView restart_adb_btn;
    private TextView ip_holder_tv;
    private TextView port_holder_tv;
    private View triangle;
    private View popup_menu_panel;
    private View pop_up_mask;

    private View refesh_view;
    private ViewGroup option_menu_view;

    private boolean BACK_PRESSED_WORKING = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            startService(new Intent(getApplicationContext(), AdbServerService.class));
            startService(new Intent(getApplicationContext(), AppService.class));


            //All setups
            try {
                PreferencesHolder.setupAppPrefs(getApplicationContext());
                FilesData.setup();
                FilesData.internalSetup(getApplicationContext(), true);
                checkAppUpdateAtAppStart();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                SharedPreferences sp2 = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                boolean adb_enabled = sp2.getBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
                if (adb_enabled)
                    Utils.displayAdbServiceNotification(getApplicationContext(), false, false);
                else {
                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.cancel(Utils.ADB_SERVICE_IS_RUNNING_NOTIFICATION_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

           // postErrors(Utils.CHECK_APP_UPDATE);

            enable_disable_adb_btn = (TextView)findViewById(R.id.enable_disable_adb_btn);
            restart_adb_btn = (TextView)findViewById(R.id.restart_adb_btn);
            ip_holder_tv = (TextView)findViewById(R.id.ip_holder_tv);
            port_holder_tv = (TextView)findViewById(R.id.port_holder_tv);
            option_menu_view = (ViewGroup)findViewById(R.id.option_view);
            triangle = findViewById(R.id.triangle);
            popup_menu_panel = findViewById(R.id.popup_menu_panel);
            option_menu_view = (ViewGroup)findViewById(R.id.option_view);
            pop_up_mask = findViewById(R.id.popup_mask);
            refesh_view = findViewById(R.id.refresh_view);

            triangle.setVisibility(View.GONE);
            popup_menu_panel.setVisibility(View.GONE);
            pop_up_mask.setVisibility(View.GONE);


            findViewById(R.id.about_me_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), AboutMeActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.about_app_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), AboutAppActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.how_to_use_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), HowToUseActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.help_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), HelpCenterActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.settings_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.update_menuitem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        triangle.setVisibility(View.GONE);
                        popup_menu_panel.setVisibility(View.GONE);
                        pop_up_mask.setVisibility(View.GONE);

                        if (!RemoteServerHelper.isNetworkOrInternetConnected(getApplicationContext())) {
                            postErrors("No internet connection available");
                            return;
                        }

                        AlertDialog ad = null;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final LayoutInflater inflater = getLayoutInflater();
                        final ViewGroup root = (ViewGroup)inflater.inflate(R.layout.dialog_root, null);
                        ViewGroup loader_root = (ViewGroup)inflater.inflate(R.layout.loader_dialog, null);
                        Utils.setFontsToAllTextViews(loader_root);
                        root.removeAllViews();
                        root.addView(loader_root);
                        builder.setView(root);
                        ad = builder.create();
                        ad.show();

                        final AlertDialog temp = ad;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    HashMap<String, String> outs = RemoteServerHelper.checkAppUpdate();
                                    //postErrors(outs);
                                    String status = outs.get("STATUS").trim();
                                    if (status.equals("1")) {
                                        try {

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
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            ViewGroup update_found_root = (ViewGroup)inflater.inflate(R.layout.dialog_app_update_found, null);
                                                            update_found_root.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {
                                                                        temp.cancel();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            update_found_root.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {

                                                                        Uri uri = Uri.parse(doc.getElementsByTagName("DOWNLOADLINK").item(0).getTextContent().trim());
                                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                                            startActivity(intent);
                                                                        } else {
                                                                            postErrors("There are no browser in your system to show this web page!");
                                                                        }

                                                                    } catch (Exception e) {
                                                                        e.fillInStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            ((TextView)update_found_root.findViewById(R.id.version_name_holder)).setText("Version  :  " + doc.getElementsByTagName("VERSIONNAME").item(0).getTextContent().trim());
                                                            ((TextView)update_found_root.findViewById(R.id.app_size_holder)).setText("Size  :  " + doc.getElementsByTagName("SIZE").item(0).getTextContent().trim());
                                                            ((TextView)update_found_root.findViewById(R.id.app_description_holder)).setText(doc.getElementsByTagName("DESCRIPTION").item(0).getTextContent().trim().replaceAll(Pattern.quote("$NL"), "\n"));
                                                            Utils.setFontsToAllTextViews(update_found_root);
                                                            root.removeAllViews();
                                                            root.addView(update_found_root);
                                                            if (!temp.isShowing())
                                                                temp.show();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            ViewGroup no_update_found = (ViewGroup)inflater.inflate(R.layout.dialog_app_no_update_found, null);
                                                            no_update_found.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {
                                                                        temp.cancel();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            no_update_found.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {
                                                                        temp.cancel();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            Utils.setFontsToAllTextViews(no_update_found);
                                                            root.removeAllViews();
                                                            root.addView(no_update_found);
                                                            if (!temp.isShowing())
                                                                temp.show();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utils.showModifiedToast(getApplicationContext(), e);
                                            try {
                                                temp.cancel();
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            return;
                                        }
                                    } else {
                                        try {

                                            FilesData.internalSetup(getApplicationContext(), false);
                                            File dir_root = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                                            File dir_updates = new File(dir_root, FilesData.INTERNAL_FOLDER_UPDATES);
                                            File dir_app_updates = new File(dir_updates, FilesData.INTERNAL_FOLDER_APP_UPDATES);
                                            final File file_updated_app_details = new File(dir_app_updates, FilesData.INTERNAL_FILE_UPDATED_DETAILS);
                                            String updated_data = "";
                                            synchronized (ObjectLocker.UPDATED_APP_DETAILS_FILE_OPERATION_LOCKER) {
                                                updated_data = FilesData.readFile(file_updated_app_details);
                                            }
                                            updated_data = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + updated_data + " </ROOT>";
                                            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(updated_data)));
                                            String version_Code = doc.getElementsByTagName("VERSIONCODE").item(0).getTextContent().trim();
                                            int version_c = Integer.parseInt(version_Code);
                                            if (version_c > AppInfo.APP_VERSION_CODE) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            ViewGroup update_found_root = (ViewGroup)inflater.inflate(R.layout.dialog_app_update_found, null);
                                                            update_found_root.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {
                                                                        temp.cancel();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            update_found_root.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    try {

                                                                        Uri uri = Uri.parse(doc.getElementsByTagName("DOWNLOADLINK").item(0).getTextContent().trim());
                                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                                            startActivity(intent);
                                                                        } else {
                                                                            postErrors("There are no browser in your system to show this web page!");
                                                                        }

                                                                    } catch (Exception e) {
                                                                        e.fillInStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            ((TextView)update_found_root.findViewById(R.id.version_name_holder)).setText("Version  :  " + doc.getElementsByTagName("VERSIONNAME").item(0).getTextContent().trim());
                                                            ((TextView)update_found_root.findViewById(R.id.app_size_holder)).setText("SIZE  :  " + doc.getElementsByTagName("SIZE").item(0).getTextContent().trim());
                                                            ((TextView)update_found_root.findViewById(R.id.app_description_holder)).setText(doc.getElementsByTagName("DESCRIPTION").item(0).getTextContent().trim().replaceAll(Pattern.quote("$NL"), "\n"));
                                                            Utils.setFontsToAllTextViews(update_found_root);
                                                            root.removeAllViews();
                                                            root.addView(update_found_root);
                                                            if (!temp.isShowing())
                                                                temp.show();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Utils.showModifiedToast(getApplicationContext(), outs.get("REASON"));
                                                try {
                                                    temp.cancel();
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                                return;
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utils.showModifiedToast(getApplicationContext(), e);
                                            try {
                                                temp.cancel();
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            return;
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showModifiedToast(getApplicationContext(), e);
                                    try {
                                        temp.cancel();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    return;
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            option_menu_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        boolean b1 = triangle.getVisibility() == View.GONE;
                        boolean b2 = popup_menu_panel.getVisibility() == View.GONE;
                        boolean b3 = pop_up_mask.getVisibility() == View.GONE;

                        if (b1 || b2 || b3) {
                            AlphaAnimation aa = new AlphaAnimation(0, 1);
                            aa.setDuration(100);
                            aa.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            triangle.setVisibility(View.VISIBLE);
                                            popup_menu_panel.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pop_up_mask.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            triangle.startAnimation(aa);
                            popup_menu_panel.startAnimation(aa);
                        } else {
                            AlphaAnimation aa = new AlphaAnimation(1, 0);
                            aa.setDuration(100);
                            aa.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    pop_up_mask.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            triangle.setVisibility(View.GONE);
                                            popup_menu_panel.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            triangle.startAnimation(aa);
                            popup_menu_panel.startAnimation(aa);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        postErrors(e);
                    }
                }
            });

            pop_up_mask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        AlphaAnimation aa = new AlphaAnimation(1, 0);
                        aa.setDuration(100);
                        aa.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                pop_up_mask.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        triangle.setVisibility(View.GONE);
                                        popup_menu_panel.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        triangle.startAnimation(aa);
                        popup_menu_panel.startAnimation(aa);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final TextView adb_status_holder = (TextView)findViewById(R.id.adb_status_holder_tv);
            SharedPreferences sp2 = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
            boolean adb_enabled = sp2.getBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
            if (adb_enabled) {
                enable_disable_adb_btn.setText("Disable");
                adb_status_holder.setText("Enabled");
            }
            else {
                enable_disable_adb_btn.setText("Enable");
                adb_status_holder.setText("Disabled");
            }
            enable_disable_adb_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        BACK_PRESSED_WORKING = false;
                        enable_disable_adb_btn.setBackgroundResource(R.drawable.btn_pressed_2);
                        enable_disable_adb_btn.setEnabled(false);

                        if(enable_disable_adb_btn.getText().toString().trim().equals("Enable")) {
                            SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                            editor.putBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, true);
                            editor.apply();
                            enable_disable_adb_btn.setText("Disable");
                            adb_status_holder.setText("Enabled");
                            Utils.displayAdbServiceNotification(getApplicationContext(), true, true);
                            enable_disable_adb_btn.setBackgroundResource(R.drawable.btn_bg_state2);
                            enable_disable_adb_btn.setEnabled(true);
                            BACK_PRESSED_WORKING = true;
                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                            editor.putBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
                            editor.apply();
                            enable_disable_adb_btn.setText("Enable");
                            adb_status_holder.setText("Disabled");
                            enable_disable_adb_btn.setBackgroundResource(R.drawable.btn_bg_state2);
                            enable_disable_adb_btn.setEnabled(true);
                            BACK_PRESSED_WORKING = true;
                            try {
                                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.cancel(Utils.ADB_SERVICE_IS_RUNNING_NOTIFICATION_ID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            restart_adb_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        BACK_PRESSED_WORKING = false;
                        restart_adb_btn.setBackgroundResource(R.drawable.btn_bg_pressed);
                        restart_adb_btn.setText("Restarting");
                        restart_adb_btn.setEnabled(false);
                        SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                        editor.putBoolean(PreferencesHolder.SOCKET_RESETUP_COMPLETED_PREF, false);
                        editor.apply();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendBroadcast(new Intent(Utils.RESETUP_RECEIVER_BROADCAST_ACTION));
                                        }
                                    });
                                    int times = 0;
                                    while (true) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                                        if (sharedPreferences.getBoolean(PreferencesHolder.SOCKET_RESETUP_COMPLETED_PREF, false)) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        restart_adb_btn.setBackgroundResource(R.drawable.btn_bg_state);
                                                        restart_adb_btn.setEnabled(true);
                                                        restart_adb_btn.setText("Restart");
                                                        BACK_PRESSED_WORKING = true;
                                                        SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                                                        int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
                                                        port_holder_tv.setText(port + "");
                                                        Utils.showModifiedToast(getApplicationContext(), "Server is restarted at " + port);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                        if (times > 10) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        restart_adb_btn.setBackgroundResource(R.drawable.btn_bg_state);
                                                        restart_adb_btn.setEnabled(true);
                                                        restart_adb_btn.setText("Restart");
                                                        BACK_PRESSED_WORKING = true;
                                                        SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                                                        int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
                                                        port_holder_tv.setText(port + "");
                                                        Utils.showModifiedToast(getApplicationContext(), "Server could not restarted");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                        times++;
                                        Thread.sleep(1000);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont(enable_disable_adb_btn);
            Utils.setHeadingTextFont(restart_adb_btn);
            ((TextView)findViewById(R.id.app_title)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));

            try {
                SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
                port_holder_tv.setText(port + "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String txt = "";
                            Vector<String> local_ips = Utils.getLocalIPV4S();
                            for(String ip : Utils.getAllDeviceIPV4Address()) {
                                if (local_ips.contains(ip.trim()))
                                    continue;
                                txt += ip.trim() + "\n";
                            }
                            txt = txt.trim();
                            if (txt.isEmpty())
                                txt = "Not Found";
                            final String temp = txt;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ip_holder_tv.setText(temp);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            refesh_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshViewCallBack();
                }
            });


            refesh_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        Utils.showModifiedToast(getApplicationContext(), Gravity.TOP, 0, 100, "Refresh Adb Status");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            option_menu_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        Utils.showModifiedToast(getApplicationContext(), Gravity.TOP, 0, 100, "More Options");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


        } catch (Exception e) {
            postErrors(e);
        }
    }

    public void refreshViewCallBack() {
        try {
            final TextView adb_status_holder = (TextView)findViewById(R.id.adb_status_holder_tv);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_z_inifinite);
            refesh_view.startAnimation(animation);
            refesh_view.setEnabled(false);
            try {
                SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
                port_holder_tv.setText(port + "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                SharedPreferences sp2 = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                boolean adb_enabled = sp2.getBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
                if (adb_enabled) {
                    enable_disable_adb_btn.setText("Disable");
                    adb_status_holder.setText("Enabled");
                } else {
                    enable_disable_adb_btn.setText("Enable");
                    adb_status_holder.setText("Disabled");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String txt = "";
                        Vector<String> local_ips = Utils.getLocalIPV4S();
                        for (String ip : Utils.getAllDeviceIPV4Address()) {
                            if (local_ips.contains(ip.trim()))
                                continue;
                            txt += ip.trim() + "\n";
                        }
                        txt = txt.trim();
                        if (txt.isEmpty())
                            txt = "Not Found";
                        final String temp = txt;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ip_holder_tv.setText(temp);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refesh_view.clearAnimation();
                                refesh_view.setEnabled(true);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postErrors(Object o) {
        try {

            o = (o == null) ? "null" : o;

            final Object temp = o;
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utils.showModifiedToast(getApplicationContext(), temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    public void onResume() {
        super.onResume();
        try {

            //refreshViewCallBack();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        try {

            if (!BACK_PRESSED_WORKING) {
                Utils.showModifiedToast(getApplicationContext(), "Press Back again, a Transaction is working");
                return;
            }

            boolean b1 = triangle.getVisibility() == View.VISIBLE;
            boolean b2 = popup_menu_panel.getVisibility() == View.VISIBLE;
            boolean b3 = pop_up_mask.getVisibility() == View.VISIBLE;

            if (b1 || b2 || b3) {
                AlphaAnimation aa = new AlphaAnimation(1, 0);
                aa.setDuration(300);
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        pop_up_mask.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                triangle.setVisibility(View.GONE);
                                popup_menu_panel.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                triangle.startAnimation(aa);
                popup_menu_panel.startAnimation(aa);
            } else {
                if(Math.abs(System.currentTimeMillis() - HOME_FIRST_BACK_PRESSED_TIMESTAMP) <= HOME_INTERVAL_TWO_BACK_PRESSED) {
                    finish();
                } else {
                    postErrors("Press again to exit");
                    HOME_FIRST_BACK_PRESSED_TIMESTAMP = System.currentTimeMillis();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            postErrors(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onNewIntent(Intent intent) {
        try {

            super.onNewIntent(intent);
            if(intent == null) {
                return;
            }

            boolean refresh_page = intent.getBooleanExtra("REFRESH_HOME_PAGE", false);
            if (refresh_page)
                refreshViewCallBack();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //run in UI Thread
    public void checkAppUpdateAtAppStart() {
        try {

            AlertDialog ad = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            final LayoutInflater inflater = getLayoutInflater();
            final ViewGroup root = (ViewGroup)inflater.inflate(R.layout.dialog_root, null);
            builder.setView(root);
            ad = builder.create();

            final AlertDialog temp = ad;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        FilesData.internalSetup(getApplicationContext(), false);
                        File dir_root = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                        File dir_updates = new File(dir_root, FilesData.INTERNAL_FOLDER_UPDATES);
                        File dir_app_updates = new File(dir_updates, FilesData.INTERNAL_FOLDER_APP_UPDATES);
                        final File file_updated_app_details = new File(dir_app_updates, FilesData.INTERNAL_FILE_UPDATED_DETAILS);
                        String updated_data = "";
                        synchronized (ObjectLocker.UPDATED_APP_DETAILS_FILE_OPERATION_LOCKER) {
                            updated_data = FilesData.readFile(file_updated_app_details);
                        }
                        updated_data = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + updated_data + " </ROOT>";
                        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(updated_data)));
                        String version_Code = doc.getElementsByTagName("VERSIONCODE").item(0).getTextContent().trim();
                        int version_c = Integer.parseInt(version_Code);
                        if (version_c > AppInfo.APP_VERSION_CODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ViewGroup update_found_root = (ViewGroup)inflater.inflate(R.layout.dialog_app_update_found, null);
                                        update_found_root.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    temp.cancel();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        update_found_root.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {

                                                    Uri uri = Uri.parse(doc.getElementsByTagName("DOWNLOADLINK").item(0).getTextContent().trim());
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                                        startActivity(intent);
                                                    } else {
                                                        postErrors("There are no browser in your system to show this web page!");
                                                    }

                                                } catch (Exception e) {
                                                    e.fillInStackTrace();
                                                }
                                            }
                                        });
                                        ((TextView)update_found_root.findViewById(R.id.version_name_holder)).setText("Version  :  " + doc.getElementsByTagName("VERSIONNAME").item(0).getTextContent().trim());
                                        ((TextView)update_found_root.findViewById(R.id.app_size_holder)).setText("Size  :  " + doc.getElementsByTagName("SIZE").item(0).getTextContent().trim());
                                        ((TextView)update_found_root.findViewById(R.id.app_description_holder)).setText(doc.getElementsByTagName("DESCRIPTION").item(0).getTextContent().trim().replaceAll(Pattern.quote("$NL"), "\n"));
                                        Utils.setFontsToAllTextViews(update_found_root);
                                        root.removeAllViews();
                                        root.addView(update_found_root);
                                        if (!temp.isShowing())
                                            temp.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

























