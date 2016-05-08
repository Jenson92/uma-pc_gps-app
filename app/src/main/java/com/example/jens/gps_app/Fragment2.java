package com.example.jens.gps_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by Jens on 10.03.2016.
 */

public class Fragment2 extends Fragment {
    private static TextView lat_text;
    private static TextView lng_text;
    SqlManager db;
    Button add_button, location_button;
    EditText task_title;
    EditText task_desc;
    EditText task_range;


    public Fragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        db = new SqlManager(this.getContext());

        add_button = (Button) rootView.findViewById(R.id.add_button);
        location_button = (Button) rootView.findViewById(R.id.location_button);
        task_title = (EditText) rootView.findViewById(R.id.task_title);
        task_desc = (EditText) rootView.findViewById(R.id.task_desc);
        task_range = (EditText) rootView.findViewById(R.id.task_range);
        lat_text = (TextView) rootView.findViewById(R.id.lat_text);
        lng_text = (TextView) rootView.findViewById(R.id.lng_text);


        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println(task_title.getText().toString() + " " + Double.parseDouble(lat_text.getText().toString()) + " " + Double.parseDouble(lng_text.getText().toString()) + " " + Integer.parseInt(task_range.getText().toString()) + " " + task_desc.getText().toString());


                db.addTask(new Task(task_title.getText().toString(), Double.parseDouble(lat_text.getText().toString()), Double.parseDouble(lng_text.getText().toString()), Integer.parseInt(task_range.getText().toString()), task_desc.getText().toString()));
                task_title.setText("");
                task_desc.setText("");
                task_range.setText("");
                lat_text.setText("");
                lng_text.setText("");

                MainActivity mApp = ((MainActivity) getContext());
                mApp.mViewPager.setCurrentItem(2, true);
                mApp.mViewPager.setCurrentItem(0, true);

                // Perform action on click

            }
        });

        location_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                new AlertDialog.Builder(getContext())
                        .setTitle("Enter location")
                        .setMessage("Click yes for enter from a map or abort to enter from a list")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                MainActivity mApp = ((MainActivity) getContext());
                                mApp.mViewPager.setCurrentItem(3, true);

                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return rootView;
    }

    public void getLocationPoint(LatLng point) {
        System.out.println("in getLocationPoint");
        System.out.println("Lat: " + point.latitude);
        System.out.println("Lng: " + point.longitude);

        lat_text.setText(String.valueOf(point.latitude));
        lng_text.setText(String.valueOf(point.longitude));
    }

}


