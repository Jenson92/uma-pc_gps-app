package com.example.jens.gps_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    Button add_button;
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
        task_title = (EditText) rootView.findViewById(R.id.task_title);
        task_desc = (EditText) rootView.findViewById(R.id.task_desc);
        task_range = (EditText) rootView.findViewById(R.id.task_range);

        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db.addBook(new Book(task_title.getText().toString(), "Jens"));
                // Perform action on click

            }
        });


        return rootView;
    }


}


