package com.example.jens.gps_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment3 extends Fragment implements GoogleMap.OnMapLongClickListener {

    MapView mMapView;
    SqlManager db;
    longClick mCallback;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_3_1, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately


        db = new SqlManager(this.getContext());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

//TODO: Improve the permission Check
        PackageManager pm = this.getContext().getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                this.getContext().getPackageName());

        System.out.println("Permission: " + hasPerm);

        if (hasPerm != PackageManager.PERMISSION_GRANTED) {

            // do stuff
        }

        googleMap.setMyLocationEnabled(true);

        System.out.println("jojojo: " + db.getAllTasks().size());

        for (int i = 0; i < db.getAllTasks().size(); i++) {

            googleMap.addMarker(new MarkerOptions()
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .position(new LatLng(db.getAllTasks().get(i).getLat(), db.getAllTasks().get(i).getLng()))
                    .title(db.getAllTasks().get(i).getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(49.484014, 8.462512)).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        googleMap.setOnMapLongClickListener(this);



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


//-------------------------------------------------------

    public void onMapLongClick(LatLng point) {

        final LatLng point2 = point;

        new AlertDialog.Builder(getContext())
                .setTitle("Location detected")
                .setMessage("Do you want to add this location to a task")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        System.out.println("Test1");


                        MainActivity mApp = ((MainActivity) getContext());
                        mApp.mViewPager.setCurrentItem(1, true);


                        someMethod(point2);


                        // positiv
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        googleMap.addMarker(new MarkerOptions()
                .position(point)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (longClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void someMethod(LatLng point) {
        mCallback.sendPoint(point);
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    public interface longClick {
        public void sendPoint(LatLng point);
    }

    //--------------------------------------------------------

}