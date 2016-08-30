package in.byter.vadb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import in.byter.vadb.utils.Utils;

public class AboutMeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_me);

            findViewById(R.id.email_explore_btn_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.ME_EMAIL)});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Help");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hi, Buddy");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "No email app is available");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.call_explore_btn_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + getString(R.string.ME_MOBILE)));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "Device does not support calling feature");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.browser_open_btn_panel_for_profile_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Uri uri = Uri.parse("https://" + getString(R.string.ME_PROFILE_LINK));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.browser_open_btn_panel_for_website).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Uri uri = Uri.parse("http://" + getString(R.string.ME_WEBSITE));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.googleplus_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://" + getString(R.string.ME_GOOGLE_PLUS));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });

            findViewById(R.id.twitter_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://" + getString(R.string.ME_TWITTER));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });

            findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://" + getString(R.string.ME_FB));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });


            findViewById(R.id.github_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://" + getString(R.string.ME_GITHUB));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });


            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.top_name_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.email_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.website_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.profile_link_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.mobile_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.follow_me_on_title));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_me, menu);
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
