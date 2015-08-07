package com.marija.redpaw;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * Created by demouser on 8/6/15.
 */
public class PickUpActivity extends ActionBarActivity implements OnMapReadyCallback {
    private ArrayList<Report> reports;
    private Firebase referenceReports;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.pickup_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get refernecse to shelters database
        Firebase.setAndroidContext(this);
        referenceReports = new Firebase(getString(R.string.database_reports));
        referenceReports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reports = new ArrayList<Report>();
                Report currentReport;
                ArrayList<Shelter> tmpAnimal = new ArrayList<Shelter>((int) snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    currentReport = dataSnapshot1.getValue(Report.class);
                    reports.add(currentReport);
                }
                if (mMap != null) {
                    updateMarkers(mMap, reports);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.pickup_toolbar);
        actionToolbar.setTitle("   Red paw");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (reports != null){
            updateMarkers(map,reports);
        }
    }

    private void updateMarkers(GoogleMap map,ArrayList<Report> reports){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Report report:reports) {
            LatLng markerLatLng = new LatLng(report.getLatitude(), report.getLongitude());
            builder.include(markerLatLng);
            map.setMyLocationEnabled(true);
            // map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 13));

            map.addMarker(new MarkerOptions()
                    .title("Sydney")
                    .snippet("The most populous city in Australia.")
                    .position(markerLatLng));
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
    }
}
