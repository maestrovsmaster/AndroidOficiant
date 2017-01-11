package maestrovs.androidofficiant.application;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import maestrovs.androidofficiant.adapter.DatabaseHelper;
import maestrovs.androidofficiant.essences.Check;


/**
 * Created by userd088 on 28.04.2016.
 */
public class MainApplication extends Application {

    public static boolean firstStart=true;

    public static SQLiteDatabase sdb;
    public static DatabaseHelper dbHelper = null;

    public static String device_id = "";
    public static String device_name = "";


    public static String serverIP="192.168.0.1";
    public static String serverPort="8080";
    public static String mainURL = "";
    public static String serverName = "BarcodeServer3";

    public static String CAMERA_STATE="CAMERA_STATE";
    public static String CAMERA_ON="CAMERA_ON";
    public static String CAMERA_OFF="CAMERA_OFF";
    public static int UPDATE_STATE = 18;

    public static Check currentCheck = null;

    @Override
    public void onCreate() {
        super.onCreate();

        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        device_name = getDeviceName();



        readyDBHandler = new ReadyDBHandler();
        LoadDBThread loadDBThread = new LoadDBThread();
        loadDBThread.start();


    }





    class LoadDBThread extends Thread {
        @Override
        public void run() {
            dbHelper = new DatabaseHelper(getApplicationContext(), "mydatabase.db", null, 1);
            sdb = dbHelper.getReadableDatabase();
            Log.d("my", "SDB + " + dbHelper.getReadableDatabase().toString());
            ContentValues newValues = new ContentValues();
            newValues.put(dbHelper.PARAMETER, "1");
            newValues.put(dbHelper.VALUE, "1");

            boolean startSettings = false;

            String ip = dbHelper.getOption("IP");//getServerIp();
            if (ip == null) {
                Log.d("my", "ip =null");


                serverIP = "192.168.0.100";
                dbHelper.insertOrReplaceOption("IP", serverIP);//setServerIp(serverIP);
                startSettings = true;

            } else {
                Log.d("my", "ip =" + ip);
                serverIP = ip;
            }

            String port = dbHelper.getOption("PORT");//getServerPort();
            if (port == null) {
                Log.d("my", "port =null");
                serverPort = "8080";
                dbHelper.insertOrReplaceOption("PORT",port);//.setServerPort(serverPort);
                startSettings = true;

            } else {
                Log.d("my", "port =" + port);
                serverPort = port;
            }
            serverPort ="8080";
            mainURL = "http://" + serverIP + ":" + serverPort + "/" + serverName;



           /* String offlineMode = dbHelper.getOption("OFFLINE_MODE");
            if(offlineMode==null) dbHelper.insertOrReplaceOption("OFFLINE_MODE", "1");
            Log.d("my","1 OFFLINE MODE from DB = "+offlineMode);
            offlineMode = dbHelper.getOption("OFFLINE_MODE");
            Log.d("my","2 OFFLINE MODE from DB = "+offlineMode);*/
           // if(offlineMode.contains("1")) MainActivity.OFFLINE_MODE=true;
           // else MainActivity.OFFLINE_MODE=false;
            //startSettings = true; !!!!/////////////////////
            if (startSettings) {
              //  Intent intent2 = new Intent(MainActivity.this, WellcomeActivity2.class);

              //  startActivity(intent2);

            } else {
                readyDBHandler.sendEmptyMessage(0);
            }
        }
    }

    ReadyDBHandler readyDBHandler = null;


    class ReadyDBHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
         /*   Log.d("my", "db ready, server url = " + mainURL);
            if(!OFFLINE_MODE)
                if (serverIP != null && serverPort != null) mainFragment.updateStatus();*/
        }

    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }



}

