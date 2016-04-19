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

/**
 * Created by Jens on 10.03.2016.
 */

public class Fragment2 extends Fragment {
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

        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db.addTask(new Task(task_title.getText().toString(), 0.02, 45.2, Integer.parseInt(task_range.getText().toString()), task_desc.getText().toString()));
                task_title.setText("");
                task_desc.setText("");
                task_range.setText("");

                // Perform action on click

            }
        });

        location_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                new AlertDialog.Builder(getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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


}


