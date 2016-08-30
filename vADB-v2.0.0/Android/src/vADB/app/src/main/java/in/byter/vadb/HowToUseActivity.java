package in.byter.vadb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;

public class HowToUseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_how_to_use);

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            findViewById(R.id.get_desktop_app_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri = Uri.parse(RemoteServerHelper.getAddressOfDesktopAppDownloadLink().toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.get_desktop_app_btn_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri = Uri.parse(RemoteServerHelper.getAddressOfDesktopAppDownloadLink().toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.email_panel).setOnClickListener(new View.OnClickListener() {
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

            Utils.setFontsToAllTextViews((ViewGroup)findViewById(R.id.root));

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_to_use, menu);
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
