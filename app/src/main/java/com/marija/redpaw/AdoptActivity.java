package com.marija.redpaw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by demouser on 8/6/15.
 */
public class AdoptActivity extends ActionBarActivity {
    private String[] animals= {"Cat", "Dog", "Other", "All"};
    private ArrayList<Pair> animalsInShelter;
    private ListView listView;
    private Firebase referenceShelters;
    private Type animalType;
    private ArrayList<Shelter> shelters;
    private Shelter shelter=new Shelter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        animalsInShelter=new ArrayList<Pair>();
        shelters=new ArrayList<Shelter>();
        shelters.add(shelter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);
        // Get refernecse to shelters database
        Firebase.setAndroidContext(this);
        referenceShelters=new Firebase(getString(R.string.database_shelters));

        //Set Default
        animalType=Type.All;
        shelter=null;

        // Add adapter to list view.
        listView=(ListView)findViewById(R.id.adopt_listViewResults);
        MyAdapter listViewAdapter=new MyAdapter();
        listView.setAdapter(listViewAdapter);

        // Add adapter, and action listener for type spinner.
        Spinner spinner = (Spinner)findViewById(R.id.adopt_animalType);
        spinner.setAdapter(new AnimalAdapter(this, animals));
        spinner.setOnItemSelectedListener(new AnimalSpinnerListener());

        // Add adapter, and action listener for type spinner.
        Spinner shelterSpinner = (Spinner)findViewById(R.id.adopt_shelter);
        shelterSpinner.setAdapter(new ShelterAdapter());
        shelterSpinner.setOnItemSelectedListener(new ShelterSpinnerListener());


        // Add database event listener
        referenceShelters.addValueEventListener(new DBEventListener());

        // Widget Toolbar.
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.adopt_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.adopt_toolbar);
        actionToolbar.setTitle("   Red paw");
        // Add database event listener.
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onMapButtonClicked(View view){
        Intent intent = new Intent(AdoptActivity.this, AdoptMapActivity.class);
        startActivity(intent);
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

    public class MyAdapter extends BaseAdapter {

        private ArrayList<Pair> displayedAnimals = new ArrayList<>();

        public void filterByType(Type type, Shelter shelter) {
            displayedAnimals.clear();                
            for (Pair animal : animalsInShelter) {
                if (shelter.getName().equals("All")||animal.shelter.getName().equals(shelter.getName())){
                    if (type == Type.All || animal.animal.getType().equals(type)) {
                        displayedAnimals.add(animal);
                    }
                }
            }

        }

        @Override
        public int getCount() {
            return displayedAnimals.size();
        }

        @Override
        public Object getItem(int position) {

            return displayedAnimals.get(position%displayedAnimals.size());
        }

        @Override
        public long getItemId(int position) {

            return position % displayedAnimals.size() ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            ViewHolder viewHolder;
            Pair currentPair=displayedAnimals.get(position%displayedAnimals.size());
            // Reuse views.
            if(convertView!=null ){
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();
            } else {
                LayoutInflater inf=LayoutInflater.from(AdoptActivity.this);
                view= inf.inflate(R.layout.list_item, null);
                // Create links to view objects.
                viewHolder=new ViewHolder();
                viewHolder.img=(ImageView)view.findViewById(R.id.adoptitem_imgViewPhoto);
                viewHolder.description=(TextView)view.findViewById(R.id.adoptitem_description);
                viewHolder.shelter=(TextView)view.findViewById(R.id.adoptitem_shelter);
                view.setTag(viewHolder);
            }
            // Set values in images.
            viewHolder.img.setImageBitmap(Util.stringToBitmap(currentPair.animal.getImg()));
            viewHolder.description.setText(currentPair.animal.getDescription());
            viewHolder.shelter.setText(currentPair.shelter.getName());
            //viewHolder.shelter.setText();<---- Where do we get the shelter from?
            return view;
        }
    }

    public class DBEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            animalsInShelter.clear();
            shelters.clear();
            Shelter currentShelter;
            ArrayList<Shelter> tmpAnimal = new ArrayList<Shelter>((int) snapshot.getChildrenCount());
            // Go through all of the shelters.
            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                currentShelter = dataSnapshot1.getValue(Shelter.class);
                // Go through all of the animals in this shelter.
                if(currentShelter!=null) {
                    shelters.add(currentShelter);
                    if( currentShelter.getAnimals()!=null) {
                        for (Animal animal : currentShelter.getAnimals()) {
                            animalsInShelter.add(new Pair(animal, currentShelter));
                        }
                    }
                }
            }
            shelters.add(new Shelter());
            // Force the view to update.
//            ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            MyAdapter adapter = ((MyAdapter) listView.getAdapter());
            adapter.filterByType(animalType, shelter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    }

    /*
    Needed to keep track of each animal's shelter.
     */
    public class Pair{
        public Animal animal;
        public Shelter shelter;

        public Pair(Animal animal, Shelter shelter){
            this.animal=animal;
            this.shelter=shelter;
        }
    }

    public class AnimalSpinnerListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position).equals("Dog")) {
                animalType=Type.Dog;
            } else if (parent.getItemAtPosition(position).equals("Cat")) {
                animalType=Type.Cat;
            } else if (parent.getItemAtPosition(position).equals("Other")) {
                animalType=Type.Other;
            }else{
                animalType=Type.All;
            }
            //referenceShelters.removeValueEventListener();
            MyAdapter adapter = ((MyAdapter) listView.getAdapter());
            adapter.filterByType(animalType, shelter);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
               animalType=null; // Show all types.
        }

    }

    public class ShelterSpinnerListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            for(Shelter curShelter:shelters) {
                if (parent.getItemAtPosition(position).equals(curShelter.getName())) {
                    shelter=curShelter;
                }
            }
            //referenceShelters.removeValueEventListener();
            MyAdapter adapter = ((MyAdapter) listView.getAdapter());
            adapter.filterByType(animalType, shelter);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            animalType=null; // Show all types.
        }

    }

    public class ShelterAdapter extends BaseAdapter implements SpinnerAdapter {

        @Override
        public int getCount() {
            return shelters.size();
        }

        @Override
        public Object getItem(int position) {
            return shelters.get(position%shelters.size()).getName();
        }

        @Override
        public long getItemId(int position) {
            return position%shelters.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view;

            if(convertView == null) {
                view = (TextView) LayoutInflater.from(AdoptActivity.this).inflate(android.R.layout.simple_spinner_dropdown_item,null);
            } else {
                view = (TextView)convertView;
            }
            view.setPadding(80,30,80,30);
            view.setText(shelters.get(position%shelters.size()).getName());
            return view;
        }
    }

    /*
    Needed to show text for each animal.
     */
    public class ViewHolder{
        public ImageView img;
        public TextView description;
        public TextView shelter;
    }
}