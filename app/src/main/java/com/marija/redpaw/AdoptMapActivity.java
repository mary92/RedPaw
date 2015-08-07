package com.marija.redpaw;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class AdoptMapActivity extends ActionBarActivity implements OnMapReadyCallback {
    private ArrayList<Shelter> shelters;
    private Firebase referenceShelters;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoptmap);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get refernecse to shelters database
        Firebase.setAndroidContext(this);
        referenceShelters = new Firebase(getString(R.string.database_shelters));
        referenceShelters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                shelters = new ArrayList<Shelter>();
                Shelter currentShelter;
                ArrayList<Shelter> tmpAnimal = new ArrayList<Shelter>((int) snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    currentShelter = dataSnapshot1.getValue(Shelter.class);
                    shelters.add(currentShelter);
                }
                
                if (mMap != null){
                    updateMarkers(mMap,shelters);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.adoptmap_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adopt_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListButtonClicked(View view) {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (shelters != null){
            updateMarkers(map,shelters);
        }
    }
    private void updateMarkers(GoogleMap map,ArrayList<Shelter> shelters){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Shelter shelter:shelters) {
            LatLng markerLatLng = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            builder.include(markerLatLng);
            map.setMyLocationEnabled(true);
           // map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 13));

            map.addMarker(new MarkerOptions()
                    .title(shelter.getName())
                    .snippet(shelter.getAddress() + "\n" + shelter.getPhonenumber())
                    .position(markerLatLng));
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
    }
}
