package com.marija.redpaw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by demouser on 8/6/15.
 */
public class ReportActivity extends Activity {
    private Location lastKnownLocation;
    private  LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }

    public void onClickBtnLocate (View view) {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER

        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        Log.d("carolina", "lastknownlocation : " + lastKnownLocation.toString());

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastKnownLocation = location;
                EditText fieldLocation = (EditText) findViewById(R.id.report_fieldLocation);
                fieldLocation.setText(String.format("[%.5f, %.5f]", lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                Log.d("carolina", "Got location onLocationChanged");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {
                Log.d("carolina", "Location Provider disabled");
                Context context = getApplicationContext();
                CharSequence text = "Can't access your location";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    public void onClickBtnUploadPhoto (View view) {

    }

    public void onClickBtnReport (View view) {
        ImageView imgViewPhoto = (ImageView) findViewById(R.id.report_imgViewPhoto);
        EditText fieldLocation = (EditText) findViewById(R.id.report_fieldLocation);
        EditText fieldDescription = (EditText) findViewById(R.id.report_fieldDescription);

        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
        startActivity(intent);

        // Tell the location manager that we don't want to know the location anymore
        locationManager.removeUpdates(locationListener);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
