package com.example.jens.gps_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jens on 10.03.2016.
 */

public class Fragment1 extends Fragment implements LocationListener {
    Button delete_button;
    SqlManager db;
    ListView listView;
    String[] books_string_array;
    List<Task> list;
    int merker = 1;

    private long lat;
    private long lng;


    private LocationManager locationManager;
    private String provider;

    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_1, container, false);

        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // Define the criteria how to select the locatioin provider -> use
        // default

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default


        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        String permission = "android.permission.INTERNET";
        int res = getContext().checkCallingOrSelfPermission(permission);

        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        }

        db = new SqlManager(this.getContext());

        // get all tasks

        tasks_laden();


        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.listView1);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {


                Location taskLocation = new Location("point A");
                taskLocation.setLatitude(list.get(position).getLat());
                taskLocation.setLongitude(list.get(position).getLng());

                Location currentLocation = new Location("point B");
                currentLocation.setLatitude(lat);
                currentLocation.setLongitude(lng);

                float distance = taskLocation.distanceTo(currentLocation);

                //Detail Dialog

                new AlertDialog.Builder(getContext())
                        .setTitle("Task Details")
                        .setMessage("Title: " + list.get(position).getTitle() + "\n" + "Description: " + list.get(position).getDesc() + "\n" + "Range: " + list.get(position).getRange() + " m" + "\n" + "Distance: " + distance + " m")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

            }


            // assuming string and if you want to get the value on click of list item
            // do what you intend to do on click of listview row

        });


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, books_string_array);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        delete_button = (Button) rootView.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (merker == 1) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, books_string_array);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(adapter);

                    merker = 2;

                } else {

                    merker = 1;
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete")
                            .setMessage("Do you want to delete this task?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // positiv
                                    //delete
                                    for (int i = 0; i < listView.getCheckedItemPositions().size(); i++) {
                                        int key = listView.getCheckedItemPositions().keyAt(i);
                                        db.deleteBook(list.get(key));
                                        tasks_laden();
                                    }

                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, books_string_array);
                                    listView.setAdapter(adapter);

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // negativ

                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, books_string_array);
                                    listView.setAdapter(adapter);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }


                // Perform action on click
            }
        });

        return rootView;

    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (long) (location.getLatitude());
        lng = (long) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(getActivity(), "Enabled new provider " + provider,
        //       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Toast.makeText(getActivity(), "Disabled provider " + provider,
        //         Toast.LENGTH_SHORT).show();
    }


    public void tasks_laden() {
        list = db.getAllTasks();
        books_string_array = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            books_string_array[i] = list.get(i).getTitle();
        }
    }
}
