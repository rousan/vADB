package in.byter.vadb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import in.byter.vadb.R;
import in.byter.vadb.utils.AppInfo;
import in.byter.vadb.utils.DataTransactionSignals;
import in.byter.vadb.utils.FilesData;
import in.byter.vadb.utils.ObjectLocker;
import in.byter.vadb.utils.PreferencesHolder;
import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;

public class SettingsActivity extends Activity {

    private boolean BACK_PRESSED_WORKING = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            //All setups
            try {
                PreferencesHolder.setupAppPrefs(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.check_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (!RemoteServerHelper.isNetworkOrInternetConnected(getApplicationContext())) {
                            postErrors("No internet connection available");
                            return;
                        }

                        AlertDialog ad = null;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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

            findViewById(R.id.change_port_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AlertDialog ad = null;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        final LayoutInflater inflater = getLayoutInflater();
                        final ViewGroup root = (ViewGroup)inflater.inflate(R.layout.dialog_port_input_layout, null);
                        builder.setView(root);
                        ad = builder.create();
                        ad.setCanceledOnTouchOutside(false);
                        final AlertDialog temp = ad;
                        final TextView cancel_btn = (TextView)root.findViewById(R.id.cancel_btn);
                        final TextView change_btn = (TextView)root.findViewById(R.id.change_btn);
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                temp.cancel();
                            }
                        });
                       change_btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               try {
                                   final String port_input = ((EditText) root.findViewById(R.id.dialog_port_input)).getText().toString().trim();
                                   if (port_input.isEmpty() || !Utils.checkPort(port_input)) {
                                       Utils.showModifiedToast(getApplicationContext(), "Port must be in range " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
                                       return;
                                   }

                                   int port = PreferencesHolder.SOCKET_DEFAULT_PORT;
                                   try {
                                       port = Integer.parseInt(port_input);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                       port = PreferencesHolder.SOCKET_DEFAULT_PORT;
                                   }

                                   cancel_btn.setEnabled(false);
                                   change_btn.setEnabled(false);
                                   change_btn.setText("Checking");
                                   BACK_PRESSED_WORKING = false;
                                   change_btn.setBackgroundResource(R.drawable.dialog_btn_bg_pressed);
                                   final int temp2 = port;
                                   new Thread(new Runnable() {
                                       @Override
                                       public void run() {
                                           try {
                                               ServerSocket ss = null;
                                               int actual_port = temp2;
                                               try {
                                                   ss = new ServerSocket(temp2);
                                                   actual_port = ss.getLocalPort();
                                               } catch (Exception e) {
                                                   e.printStackTrace();
                                                   try {
                                                       if (ss != null)
                                                           ss.close();
                                                   } catch (Exception e2) {
                                                       e2.printStackTrace();
                                                   }
                                                   try {
                                                       Thread.sleep(1000);
                                                   } catch (Exception e3) {
                                                       e3.printStackTrace();
                                                   }
                                                   runOnUiThread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           cancel_btn.setEnabled(true);
                                                           change_btn.setEnabled(true);
                                                           change_btn.setText("Change");
                                                           BACK_PRESSED_WORKING = true;
                                                           change_btn.setBackgroundResource(R.drawable.dialog_btn_state);
                                                           Utils.showModifiedToast(getApplicationContext(), temp2 + " port is unavailable");
                                                       }
                                                   });
                                                   return;
                                               }

                                               try {
                                                   if (ss != null)
                                                       ss.close();
                                               } catch (Exception e2) {
                                                   e2.printStackTrace();
                                               }
                                               try {
                                                   Thread.sleep(1000);
                                               } catch (Exception e3) {
                                                   e3.printStackTrace();
                                               }

                                               runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       change_btn.setText("Changing");
                                                   }
                                               });

                                               SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                                               editor.putInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, actual_port);
                                               editor.commit();
                                               SharedPreferences.Editor editor2 = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                                               editor2.putBoolean(PreferencesHolder.SOCKET_RESETUP_COMPLETED_PREF, false);
                                               editor2.commit();
                                               try {
                                                   Thread.sleep(1000);
                                               } catch (Exception e) {
                                                   e.printStackTrace();
                                               }
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
                                                                   cancel_btn.setEnabled(true);
                                                                   change_btn.setEnabled(true);
                                                                   change_btn.setText("Change");
                                                                   BACK_PRESSED_WORKING = true;
                                                                   change_btn.setBackgroundResource(R.drawable.dialog_btn_state);
                                                                   SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                                                                   int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
                                                                   Utils.showModifiedToast(getApplicationContext(), "Adb port is changed at port " + port);
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }
                                                           }
                                                       });
                                                       return;
                                                   }
                                                   if (times > 10) {
                                                       runOnUiThread(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               try {
                                                                   cancel_btn.setEnabled(true);
                                                                   change_btn.setEnabled(true);
                                                                   change_btn.setBackgroundResource(R.drawable.dialog_btn_state);
                                                                   change_btn.setText("Change");
                                                                   BACK_PRESSED_WORKING = true;
                                                                   Utils.showModifiedToast(getApplicationContext(), "Adb port could be changed, try again later");
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }
                                                           }
                                                       });
                                                       return;
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
                        Utils.setFontsToAllTextViews(root);
                        //Utils.setHeadingTextFont((TextView) root.findViewById(R.id.cancel_btn));
                        //Utils.setHeadingTextFont((TextView)root.findViewById(R.id.change_btn));
                        ad.show();

                    } catch (Exception e) {
                        Utils.showModifiedToast(getApplicationContext(), e);
                    }
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.change_port_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.check_update_title));


        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    public void onBackPressed() {
        try {

            if (!BACK_PRESSED_WORKING) {
                Utils.showModifiedToast(getApplicationContext(), "Press Back again, a Transaction is working");
                return;
            }
            super.onBackPressed();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            super.onDestroy();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("REFRESH_HOME_PAGE", true));
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
