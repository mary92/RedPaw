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

import java.util.ArrayList;


public class AdoptMapActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoptmap);

        // Get refernecse to shelters database
        /*Firebase.setAndroidContext(this);
        referenceShelters=new Firebase(getString(R.string.database_shelters));

        //Add adapter to list view.
        listView=(ListView)findViewById(R.id.adopt_listViewResults);
        MyAdapter listViewAdapter=new MyAdapter();
        listView.setAdapter(listViewAdapter);

        referenceShelters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Shelter currentShelter;
                ArrayList<Shelter> tmpAnimal = new ArrayList<Shelter>((int) snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    currentShelter = dataSnapshot1.getValue(Shelter.class);
                    for(Animal animal:currentShelter.getAnimals()){
                        animalsInShelter.add(new Pair(animal,currentShelter));
                    }
                }
                ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });*/
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

    public void onListButtonClicked(View view){
        super.onBackPressed();
    }
}
