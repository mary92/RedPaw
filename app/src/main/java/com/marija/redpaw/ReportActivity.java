package com.marija.redpaw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

/**
 * Created by demouser on 8/6/15.
 */
public class ReportActivity extends ActionBarActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Location lastKnownLocation;
    private  LocationManager locationManager;
    private LocationListener locationListener;
    private Bitmap image;
    private Firebase referenceReports;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // Get reference to reports database
        Firebase.setAndroidContext(this);
        referenceReports=new Firebase(getString(R.string.database_reports));
        Spinner spinner = (Spinner)findViewById(R.id.report_animalType);
        SpinnerAdapter adapter = new AnimalAdapter();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Dog")) {
                    type = Type.Dog;
                } else if (parent.getItemAtPosition(position).equals("Cat")) {
                    type = Type.Cat;
                } else {
                    type = Type.Other;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Context context = getApplicationContext();
                CharSequence text = "Please choose an animal type";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.report_toolbar);
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

    public void onClickBtnLocate (View view) {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //String locationProvider = LocationManager.NETWORK_PROVIDER;
        String locationProvider = LocationManager.GPS_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER

        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation != null)
            Log.d("carolina", "lastknownlocation : " + lastKnownLocation.toString());

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastKnownLocation = location;
                EditText fieldLocation = (EditText) findViewById(R.id.report_fieldLocation);
                fieldLocation.setText(String.format("[%.5f, %.5f]", lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                Log.d("carolina", "Got location onLocationChanged");

                // Tell the location manager that we don't want to know the location anymore
                locationManager.removeUpdates(locationListener);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            ImageView imgViewPhoto = (ImageView) findViewById(R.id.report_imgViewPhoto);
            imgViewPhoto.setImageBitmap(image);
        }
    }


    /*
        If the user clicks on the Report button. Then a Report object is created and uploaded to the
        reports database.
     */
    public void onClickBtnReport (View view) {
        EditText fieldDescription = (EditText) findViewById(R.id.report_fieldDescription);

        if(lastKnownLocation == null) {
            Context context = getApplicationContext();
            CharSequence text = "Please provide a location";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if(image == null && fieldDescription.getText().equals("A brief description")) {
            Context context = getApplicationContext();
            CharSequence text = "Please provide a picture or a description";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if(type == null) {
            Context context = getApplicationContext();
            CharSequence text = "Please choose an animal type";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            ImageView imgViewPhoto = (ImageView) findViewById(R.id.report_imgViewPhoto);
            EditText fieldLocation = (EditText) findViewById(R.id.report_fieldLocation);

            Bitmap imageToUpload;
            if (image == null) {
                imageToUpload = BitmapFactory.decodeResource(getResources(), R.drawable.default_dog);
            } else {
                imageToUpload = image;
            }

            Report report = new Report(fieldDescription.getText().toString(), Type.Dog, Util.bitmapToString(imageToUpload), lastKnownLocation);

            referenceReports.push().setValue(report);

            // Tell the location manager that we don't want to know the location anymore
            locationManager.removeUpdates(locationListener);

            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private class AnimalAdapter extends BaseAdapter implements SpinnerAdapter{
        String[] animals = {"Cat", "Dog", "Other"};

        @Override
        public int getCount() {
            return animals.length;
        }

        @Override
        public Object getItem(int position) {
            return animals[position%animals.length];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view;

            if(convertView == null) {
                view = (TextView)LayoutInflater.from(ReportActivity.this).inflate(android.R.layout.simple_spinner_dropdown_item,null);
            } else {
                view = (TextView)convertView;
            }

            view.setText(animals[position]);
            return view;
        }
    }
}
