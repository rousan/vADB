package in.byter.vadb;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import in.byter.vadb.lib.VLog;
import in.byter.vadb.utils.Utils;


public class TestActivity extends AppCompatActivity {

    final Object obj = new Object();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test);

            Button btn = (Button)findViewById(R.id.btn);
            final TextView tv = (TextView)findViewById(R.id.tv);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                    } catch (Exception e) {
                        Utils.showModifiedToast(getApplicationContext(), e);
                    }
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int i = 0;
                        while (true) {
                            VLog.e("firstthread", i + "");
                            Thread.sleep(100);
                            i++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int i = 0;
                        while (true) {
                            VLog.i("secondthread", i + "");
                            Thread.sleep(100);
                            i++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
}
