package in.byter.vadb;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;

public class HelpCenterActivity extends Activity {

    private EditText email_input = null;
    private EditText message_input = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help_center);

            final ViewGroup pb_holder_panel = (ViewGroup)findViewById(R.id.pb_holder_panel);
            final TextView send_button = (TextView)findViewById(R.id.send_button);
            email_input = (EditText)findViewById(R.id.email_input);
            message_input = (EditText) findViewById(R.id.message_input);

            final Vector<EditText> inputs = new Vector<EditText>();
            inputs.add(email_input);
            inputs.add(message_input);

            for(EditText et : inputs) {
                final EditText temp = et;
                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#33000000"));
                        } else {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                        }
                    }
                });
            }


           /* try {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)email_input;
                Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                HashSet<String> stringHashSet = new HashSet<>();
                for(Account account : accounts) {
                    if(account.name.contains("@")) {
                        stringHashSet.add(account.name);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item_theme, new ArrayList<String>(stringHashSet));
                autoCompleteTextView.setAdapter(arrayAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }*/


            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final View pb = findViewById(R.id.pb_holder_panel);
            pb.setVisibility(View.GONE);
            send_button.setVisibility(View.VISIBLE);
            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final String email = email_input.getText().toString().trim();
                        final String message = message_input.getText().toString().trim();

                        if (email.isEmpty() || !email.contains("@")) {
                            Utils.showModifiedToast(getApplicationContext(), "Email is not valid");
                            return;
                        }

                        if (message.isEmpty()) {
                            Utils.showModifiedToast(getApplicationContext(), "Type some text as message");
                            return;
                        }

                        if (!RemoteServerHelper.isNetworkOrInternetConnected(getApplicationContext())) {
                            Utils.showModifiedToast(getApplicationContext(), "No internet connection available");
                            return;
                        }

                        pb.setVisibility(View.VISIBLE);
                        send_button.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    HashMap<String, String> outs = RemoteServerHelper.submitUserFeedback(email, message);
                                    String status = outs.get("STATUS").trim();
                                    if (status.equals("1")) {
                                        Utils.showModifiedToast(getApplicationContext(), "Your response has been sent, Thanks a lot.");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb.setVisibility(View.GONE);
                                                send_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        return;

                                    } else {
                                        Utils.showModifiedToast(getApplicationContext(), outs.get("REASON"));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb.setVisibility(View.GONE);
                                                send_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        return;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showModifiedToast(getApplicationContext(), e.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pb.setVisibility(View.GONE);
                                            send_button.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.help_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.send_button));

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help_center, menu);
        return true;
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
