package com.example.jens.gps_app;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    int Merker;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;

    public Fragment1() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Hallo Beginn");

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);

        latituteField = (TextView) rootView.findViewById(R.id.TextView02);
        longitudeField = (TextView) rootView.findViewById(R.id.TextView04);


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
        System.out.println("Test " + res);

        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }


        Merker = 1;


        db = new SqlManager(this.getContext());

        // get all books
        list = db.getAllTasks();
        books_string_array = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            books_string_array[i] = list.get(i).getTitle();
        }

        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.listView1);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, books_string_array);


        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }

        });
        */


        delete_button = (Button) rootView.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, books_string_array);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setAdapter(adapter);

                if (Merker == 1) {
                    System.out.println("Merker: " + Merker);
                    Intent myIntent = new Intent(getActivity(), GPS_Service.class);
                    myIntent.putExtra("test", "Hallo Welt");
                    myIntent.putExtra("tasklist", books_string_array);
                    getActivity().startService(myIntent);
                    Merker = 2;
                } else {
                    System.out.println("Merker: " + Merker);
                    getActivity().stopService(new Intent(getActivity(), GPS_Service.class));
                    Merker = 1;
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
        long lat = (long) (location.getLatitude());
        long lng = (long) (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}


