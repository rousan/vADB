package in.byter.vadb;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import in.byter.vadb.utils.DataTransactionSignals;
import in.byter.vadb.utils.FilesData;
import in.byter.vadb.utils.ObjectLocker;
import in.byter.vadb.utils.PreferencesHolder;
import in.byter.vadb.utils.Utils;
import in.byter.vadb.utils.VLog;

public class AdbServerService extends Service {

    private ServerSocket ss;
    private Thread server_socket_holder_thread;
    private Object socket_resetup_locker = new Object();
    private Object temp_file_writing_locker = new Object();
    private BroadcastReceiver resetup_socket_receiver;

    public AdbServerService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            PreferencesHolder.setupAppPrefs(getApplicationContext());//important

            if (ss == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (socket_resetup_locker) {
                                reSetupSocket();
                                if (server_socket_holder_thread == null || !server_socket_holder_thread.isAlive()) {
                                    server_socket_holder_thread = new Thread(new ServerSocketHolderThread());
                                    server_socket_holder_thread.start();
                                }
                            }
                        } catch (Exception e) {
                            postErrors(e);
                        }
                    }
                }).start();
            } else if (ss.isClosed()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (socket_resetup_locker) {
                                reSetupSocket();
                                if (server_socket_holder_thread == null || !server_socket_holder_thread.isAlive()) {
                                    server_socket_holder_thread = new Thread(new ServerSocketHolderThread());
                                    server_socket_holder_thread.start();
                                }
                            }
                        } catch (Exception e) {
                            postErrors(e);
                        }
                    }
                }).start();
            } else if(server_socket_holder_thread == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (socket_resetup_locker) {
                                reSetupSocket();
                                if (server_socket_holder_thread == null || !server_socket_holder_thread.isAlive()) {
                                    server_socket_holder_thread = new Thread(new ServerSocketHolderThread());
                                    server_socket_holder_thread.start();
                                }
                            }
                        } catch (Exception e) {
                            postErrors(e);
                        }
                    }
                }).start();
            } else if(!server_socket_holder_thread.isAlive()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (socket_resetup_locker) {
                                reSetupSocket();
                                if (server_socket_holder_thread == null || !server_socket_holder_thread.isAlive()) {
                                    server_socket_holder_thread = new Thread(new ServerSocketHolderThread());
                                    server_socket_holder_thread.start();
                                }
                            }
                        } catch (Exception e) {
                            postErrors(e);
                        }
                    }
                }).start();
            }

            if (resetup_socket_receiver == null) {
                resetup_socket_receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        synchronized (socket_resetup_locker) {
                                            reSetupSocket();
                                            if (server_socket_holder_thread == null || !server_socket_holder_thread.isAlive()) {
                                                server_socket_holder_thread = new Thread(new ServerSocketHolderThread());
                                                server_socket_holder_thread.start();
                                            }
                                            SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
                                            editor.putBoolean(PreferencesHolder.SOCKET_RESETUP_COMPLETED_PREF, true);
                                            editor.commit();
                                        }
                                    } catch (Exception e) {
                                        postErrors(e);
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            postErrors(e);
                        }
                    }
                };
                IntentFilter inf = new IntentFilter(Utils.RESETUP_RECEIVER_BROADCAST_ACTION);
                registerReceiver(resetup_socket_receiver, inf);
            }

        } catch (Exception e) {
            postErrors(e);
        }
        return  START_STICKY;
    }

    //Dont run in UI thread
    public void reSetupSocket() {
        try {
            postErrors("starting");
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (Exception e) {
                postErrors(e);
            }
            Thread.sleep(700);//sleeps until the socket is fully closed, very important

            SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
            int port = sp.getInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, PreferencesHolder.SOCKET_DEFAULT_PORT);
            ServerSocket temp_ss = null;
            while(true) {
                try {
                    temp_ss = new ServerSocket(port);
                    break;
                } catch (Exception e) {
                    postErrors(e);
                    try {
                        if (temp_ss != null) {
                            temp_ss.close();
                        }
                    } catch (Exception e1) {
                        postErrors(e1);
                    }
                    port++;
                    if(port > Utils.MAX_SEREVR_PORT) {
                        port = PreferencesHolder.SOCKET_DEFAULT_PORT;
                    }
                }
            }

            int actual_port = port;
            try {
                actual_port = temp_ss.getLocalPort();
            } catch (Exception e) {
                e.printStackTrace();
                actual_port = port;
            }

            ss = temp_ss;
            SharedPreferences.Editor editor = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0).edit();
            editor.putInt(PreferencesHolder.SOCKET_PORT_HOLDER_PREF, actual_port);
            editor.commit();

        } catch (Exception e) {
            postErrors(e);
        }
    }

    public void onDestroy() {
        try {
            if(ss != null) {
                ss.close();
            }
        } catch (Exception e) {
            postErrors(e);
        }
    }

    public class HandleReceivedSocket implements Runnable {
        private Socket s;

        public HandleReceivedSocket(Socket s) {
            this.s = s;
        }

        public void run() {
            try {

                OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream();

                int transacton_signal = is.read();
                if (transacton_signal < 0) {
                    throw new Exception();
                }

                postErrors(transacton_signal);

                if (transacton_signal == DataTransactionSignals.RUN_APP_SIGNAL) {
                    try {
                        FilesData.setup();//must

                        int bytes_in_size_str = is.read();
                        if (bytes_in_size_str <= 0)
                            throw new Exception();
                        byte[] size_bytes = new byte[bytes_in_size_str];
                        for(int i=0; i<bytes_in_size_str; i++) {
                            byte byt = (byte)is.read();
                            if (byt < 0)
                                throw new Exception();
                            size_bytes[i] = byt;
                        }
                        String size_str = new String (size_bytes, "UTF-8");
                        if (size_str.trim().isEmpty())
                            throw new Exception();
                        long size_in_bytes = 0;
                        try {
                            size_in_bytes = Long.parseLong(size_str.trim());
                        } catch (Exception e) {
                            postErrors(e);
                            throw new Exception();
                        }
                        if (size_in_bytes <= 0)
                            throw new Exception();

                        synchronized (temp_file_writing_locker) {
                            try {
                                FileOutputStream fos = new FileOutputStream(FilesData.getTemmAppFile());
                                long total_read = 0;
                                byte[] buff = new byte[1024*1024];
                                while (total_read < size_in_bytes) {
                                    int r = is.read(buff);
                                    if (r < 0) {
                                        try {
                                            fos.flush();
                                            fos.close();
                                        } catch (Exception e) {
                                            postErrors(e);
                                        } finally {
                                            throw new Exception();
                                        }
                                    }
                                    total_read += r;
                                    fos.write(buff, 0, r);
                                }

                                try {
                                    fos.flush();
                                    fos.close();
                                } catch (Exception e) {
                                    postErrors(e);
                                }

                                try {
                                    Thread.sleep(300);
                                } catch (Exception e) {
                                    postErrors(e);
                                }

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(FilesData.getTemmAppFile()), "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if(intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }

                                try {
                                    os.write(DataTransactionSignals.TRANSACTION_COMPLETED_SIGNAL);
                                    try {
                                        Thread.sleep(100);
                                    } catch (Exception e) {
                                        postErrors(e);
                                    }
                                    s.close();
                                } catch (Exception e) {
                                    postErrors(e);
                                    throw new Exception();
                                }

                            } catch (Exception e) {
                                postErrors(e);
                                actionOnSocketClose(s);
                                return;
                            }
                        }

                    } catch (Exception e) {
                        postErrors(e);
                        actionOnSocketClose(s);
                        return;
                     }
                } else if(transacton_signal == DataTransactionSignals.CLEAR_LOGS_SIGNAL) {
                    try {
                        FilesData.setup();//must
                        File folder_root = FilesData.getAppDataRoot();
                        File folder_data = new File(folder_root, FilesData.FOLDER_DATA);
                        File folder_logs = new File(folder_data, FilesData.FOLDER_LOGS);

                        String filter_str = readStringDataFromSocket(s, is).trim();
                        if (filter_str.isEmpty()) {
                            os.write(DataTransactionSignals.TRANSACTION_COMPLETED_SIGNAL);
                            Thread.sleep(200);
                            throw new Exception();
                        }

                        String[] filters = filter_str.split(",");
                        File[] packege_logs = folder_logs.listFiles();
                        HashSet<File> flterred_logs_packages = new HashSet<>();
                        for(File f : packege_logs) {
                            if (!f.isDirectory())
                                continue;
                            String name = f.getName().trim().toLowerCase();
                            for(String filter : filters) {
                                if (name.contains(filter.trim().toLowerCase())) {
                                    flterred_logs_packages.add(f);
                                    break;
                                }
                            }
                        }

                        for(File f : flterred_logs_packages) {
                            try {
                                if (!f.isDirectory()) {
                                    f.mkdirs();
                                }
                                final File log_file = new File(f, FilesData.FILE_LOGS_DATA);
                                SQLiteDatabase conn_db = SQLiteDatabase.openOrCreateDatabase(log_file.getAbsolutePath(), null, new DatabaseErrorHandler() {
                                    @Override
                                    public void onCorruption(SQLiteDatabase dbObj) {
                                        //nothing
                                    }
                                });
                                conn_db.delete(FilesData.MAIN_DATA_TABLE_NAME, null, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        os.write(DataTransactionSignals.TRANSACTION_COMPLETED_SIGNAL);
                        Thread.sleep(200);
                        throw new Exception();

                    } catch (Exception e) {
                        postErrors(e);
                        actionOnSocketClose(s);
                        return;
                    }
                } else if(transacton_signal == DataTransactionSignals.GET_LOGS_SIGNAL) {
                    try {
                        FilesData.setup();//must
                        File folder_root = FilesData.getAppDataRoot();
                        File folder_data = new File(folder_root, FilesData.FOLDER_DATA);
                        File folder_logs = new File(folder_data, FilesData.FOLDER_LOGS);

                        String filter_str = readStringDataFromSocket(s, is).trim();
                        if (filter_str.isEmpty()) {
                            String none_logs_result = "No logs found for this filter!";
                            os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL);
                            writeStringLineToSocket(s, os, none_logs_result);
                            os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_FINISHED_SIGNAL);
                            is.read();
                            throw new Exception();
                        }

                        String[] filters = filter_str.split(",");
                        File[] packege_logs = folder_logs.listFiles();
                        HashSet<File> flterred_logs_packages = new HashSet<>();
                        for(File f : packege_logs) {
                            if (!f.isDirectory())
                                continue;
                            String name = f.getName().trim().toLowerCase();
                            for(String filter : filters) {
                                if (name.contains(filter.trim().toLowerCase())) {
                                    flterred_logs_packages.add(f);
                                    break;
                                }
                            }
                        }

                        if(flterred_logs_packages.size() == 0) {
                            String none_logs_result = "No logs found for this filter!";
                            os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL);
                            writeStringLineToSocket(s, os, none_logs_result);
                            os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_FINISHED_SIGNAL);
                            is.read();
                            throw new Exception();
                        }

                        for(File pkg : flterred_logs_packages) {
                            try {
                                os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL);
                                writeStringLineToSocket(s, os, "------------------------ Logs For Package: " + pkg.getName() + " ------------------------");
                                if (!pkg.isDirectory())
                                    pkg.mkdirs();
                                File log_file = new File(pkg, FilesData.FILE_LOGS_DATA);
                                StringWriter sw = new StringWriter();
                                try {
                                    SQLiteDatabase conn_db = SQLiteDatabase.openOrCreateDatabase(log_file.getAbsolutePath(), null, new DatabaseErrorHandler() {
                                        @Override
                                        public void onCorruption(SQLiteDatabase dbObj) {
                                            //nothing
                                        }
                                    });
                                    Cursor cursor = conn_db.query(FilesData.MAIN_DATA_TABLE_NAME, null, null, null, null, null, null);
                                    while (cursor.moveToNext()) {
                                        String tag = cursor.getString(cursor.getColumnIndex(FilesData.TAG_COLUMN_NAME));
                                        String msg = cursor.getString(cursor.getColumnIndex(FilesData.MSG_COLUMN_NAME));
                                        String logs_type = cursor.getString(cursor.getColumnIndex(FilesData.LOGS_TYPE_COLUMN_NAME));
                                        String time = cursor.getString(cursor.getColumnIndex(FilesData.TIME_COLUMN_NAME));
                                        long timestamp = System.currentTimeMillis();
                                        try {
                                            timestamp = Long.parseLong(time);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            timestamp = System.currentTimeMillis();
                                        }
                                        String single_log = Utils.getFormattedLogs(tag, msg, logs_type, timestamp);
                                        sw.append(single_log);
                                        sw.append("\n");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sw.flush();
                                BufferedReader bfr = new BufferedReader(new StringReader(sw.toString()));
                                String line = null;
                                while ((line = bfr.readLine()) != null) {
                                    os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL);
                                    writeStringLineToSocket(s, os, line);
                                }
                                os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL);
                                writeStringLineToSocket(s, os, ""); //"" means in new line printed in logs_area
                            } catch (Exception e) {
                                postErrors(e);
                            }
                        }

                        try {

                            os.write(DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_FINISHED_SIGNAL);
                            is.read();
                            actionOnSocketClose(s);
                            return;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        postErrors(e);
                        actionOnSocketClose(s);
                        return;
                    }
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                postErrors(e);
                actionOnSocketClose(s);
                return;
            }
        }

        //It requuires the data transferring pattern
        private String readStringDataFromSocket(Socket s, InputStream is) {
            String outs = "";
            try {

                int bytes_in_size_str = is.read();
                if (bytes_in_size_str <= 0)
                    throw new Exception();
                byte[] size_bytes = new byte[bytes_in_size_str];
                for(int i=0; i<bytes_in_size_str; i++) {
                    byte byt = (byte)is.read();
                    if (byt < 0)
                        throw new Exception();
                    size_bytes[i] = byt;
                }
                String size_str = new String (size_bytes, "UTF-8");
                if (size_str.trim().isEmpty())
                    throw new Exception();
                int size_in_bytes = 0;
                try {
                    size_in_bytes = Integer.parseInt(size_str.trim());
                } catch (Exception e) {
                    postErrors(e);
                    throw new Exception();
                }
                if (size_in_bytes < 0)
                    throw new Exception();

                byte[] bytes = new byte[size_in_bytes];
                int z = 0;
                try {
                    int total_read = 0;
                    byte[] buff = new byte[size_in_bytes + 1];
                    while (total_read < size_in_bytes) {
                        int r = is.read(buff, 0, size_in_bytes - total_read);
                        if (r < 0) {
                            throw new Exception();
                        }
                        for(int i=0; i<r; i++) {
                            bytes[z] = buff[i];
                            z++;
                        }
                        total_read += r;
                    }

                } catch (Exception e) {
                    postErrors(e);
                    actionOnSocketClose(s);
                }

                outs = new String(bytes, "UTF-8");

            } catch (Exception e) {
                postErrors(e);
                actionOnSocketClose(s);
            }
            return outs;
        }

        private void writeStringLineToSocket(Socket s, OutputStream os, String str) {
            try {

                if (str == null)
                    str = "";

                String size_str = str.getBytes("UTF-8").length + "";
                os.write(size_str.getBytes().length);
                os.write(size_str.getBytes());
                os.write(str.getBytes("UTF-8"));

            } catch (Exception e) {
                postErrors(e);
                actionOnSocketClose(s);
            }
        }

        private void actionOnSocketClose(Socket s) {
            try {
                try {
                    if (s != null) {
                        s.close();
                    }
                } catch (Exception e) {
                    postErrors(e);
                }

            } catch (Exception e) {
                postErrors(e);
            }
        }

    }

    public class ServerSocketHolderThread implements Runnable {
        public void run() {
            try {

                while(true) {
                    try {

                        Socket s = ss.accept();
                        SharedPreferences sp = getSharedPreferences(PreferencesHolder.APP_SETTINGS_PREF_FILE, 0);
                        boolean adb_enabled = sp.getBoolean(PreferencesHolder.ADB_ENABLE_DISABLE_PREF, false);
                        if (!adb_enabled) {
                            try {
                                s.close();
                            } catch (Exception e) {
                                postErrors(e);
                            }
                            continue;
                        }
                        new Thread(new HandleReceivedSocket(s)).start();

                    } catch (Exception e) {
                       //postErrors(e);
                    }
                }

            } catch (Exception e) {
                postErrors(e);
            }
        }
    }

    public void postErrors(Object object) {
        try {
            object = (object == null) ? "null" : object;
            VLog.d("srvs", object.toString());
        } catch (Exception e) {
            VLog.d("srvs", e.toString());
        }
    }
}
















