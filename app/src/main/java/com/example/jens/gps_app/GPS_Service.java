package com.example.jens.gps_app;

/**
 * Created by Jens on 06.04.2016.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GPS_Service extends Service implements LocationListener {
    final static String ACTION = "NotifyServiceAction";
    final static String STOP_SERVICE_BROADCAST_KEY = "StopServiceBroadcastKey";
    final static int RQS_STOP_SERVICE = 1;
    private static Timer timer = new Timer();
    private final String myBlog = "http://www.cs.dartmouth.edu/~campbell/cs65/cs65.html";
    String[] tasklist;
    int merker = 0;
    double lat;
    double lng;
    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (merker >= tasklist.length) {
                merker = 0;
            }

            Toast.makeText(getApplicationContext(), tasklist[merker], Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(), lat + " : " + lng, Toast.LENGTH_SHORT).show();


            merker++;


            if (merker == 4) {
                System.out.println("Start Notification, Merker: " + merker);
                notification_Test();
            }


        }
    };
    private Context ctx;
    private LocationManager locationManager;
    private String provider;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();

        LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default


        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        String permission = "android.permission.INTERNET";
        int res = checkCallingOrSelfPermission(permission);
        System.out.println("Test " + res);

        Location location = locationManager.getLastKnownLocation(provider);

        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onCreate();
        String test = "buuuh";
        if (intent != null) {
            test = intent.getStringExtra("test");
            tasklist = intent.getStringArrayExtra("tasklist");
        }
        Toast.makeText(this, "Service started " + test, Toast.LENGTH_LONG).show();
        startService();
    }

    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (double) (location.getLatitude());
        lng = (double) (location.getLongitude());
        // Toast.makeText()

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void notification_Test() {

        System.out.println("Innerhalb von notification");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        int mNotificationId = 001;

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private class mainTask extends TimerTask {
        public void run() {
            toastHandler.sendEmptyMessage(0);


        }
    }

}
