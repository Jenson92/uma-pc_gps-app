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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GPS_Service extends Service implements LocationListener {
    private static Timer timer = new Timer();
    String[] tasklist;
    int merker = 0;
    double lat;
    double lng;
    SqlManager db;
    List<Task> myList;
    private Context ctx;
    private LocationManager locationManager;
    private String provider;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        db = new SqlManager(this);

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

        locationManager.requestLocationUpdates(provider, 5000, 1, this);

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
    }

    @Override
    public void onLocationChanged(Location location) {

        System.out.println("Innerhalb Location Update");

        lat = (double) (location.getLatitude());
        lng = (double) (location.getLongitude());

        System.out.println("AllTasks " + db.getAllTasks());

        for (int i = 0; i < db.getAllTasks().size(); i++) {

            db.getAllTasks().get(i).getLat();
            db.getAllTasks().get(i).getLng();

            Location taskLocation = new Location("point A");
            taskLocation.setLatitude(db.getAllTasks().get(i).getLat());
            taskLocation.setLongitude(db.getAllTasks().get(i).getLng());

            Location currentLocation = new Location("point B");
            currentLocation.setLatitude(lat);
            currentLocation.setLongitude(lng);

            float distance = taskLocation.distanceTo(currentLocation);
            System.out.println("Distanz zu Task " + db.getAllTasks().get(i).getTitle() + " : " + distance);
            System.out.println("Range: " + db.getAllTasks().get(i).getRange());

            if (distance < db.getAllTasks().get(i).getRange()) {
                notification_Test(db.getAllTasks().get(i).getTitle());
            }

            Toast.makeText(this, "Distanz zu Task " + db.getAllTasks().get(i).getTitle() + " : " + distance, Toast.LENGTH_SHORT);


        }
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

    public void notification_Test(String taskTitle) {

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
                        .setContentTitle("GPS-App")
                        .setContentText("In range of " + taskTitle);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
