package com.marija.redpaw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.marija.redpaw.AdoptActivity.*;

/**
 * Created by demouser on 8/6/15.
 */
public class AnimalsActivity extends ActionBarActivity {
    ArrayList<Animal> animalsInMyShelter = new ArrayList<>();
    private Account account;
    private ListView listView;
    private MyAnimalAdapter listAdapter;
    private Firebase sheltersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        listView = (ListView)findViewById(R.id.animalsInMyShelter);
        MyAnimalAdapter listViewAdapter= new MyAnimalAdapter();
        listView.setAdapter(listViewAdapter);

        account = new Account("worker", "savethepets", "90b88b9d-af39-4c3a-ae9e-04b632209ae6");

        Firebase.setAndroidContext(this);
        sheltersDB = new Firebase(getString(R.string.database_shelters));
        addDBListener();

        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.animals_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.animals_toolbar);
        actionToolbar.setTitle("   All Animals");
    }

    private void addDBListener() {
        sheltersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                animalsInMyShelter.clear();

                Shelter shelter;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    shelter = data.getValue(Shelter.class);
                    Log.d("animals", shelter.getId());
                    if (account.getShelterId().equals(shelter.getId())) {
                        populateAnimalsList(shelter.getAnimals());
                    }
                }

                listView.invalidateViews();
            }

            private void populateAnimalsList(List<Animal> animals) {
                for (Animal pet:animals) {
                    animalsInMyShelter.add(pet);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

    public class MyAnimalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return animalsInMyShelter.size();
        }

        @Override
        public Object getItem(int position) {
            return animalsInMyShelter.get(position % animalsInMyShelter.size());
        }

        @Override
        public long getItemId(int position) {
            return position%animalsInMyShelter.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            AnimalCardHolder viewHolder;

            Animal currentAnimal=animalsInMyShelter.get(position%animalsInMyShelter.size());

            if(convertView!=null ){
                view = convertView;
                viewHolder= (AnimalCardHolder) view.getTag();
            } else {
                LayoutInflater inf=LayoutInflater.from(AnimalsActivity.this);
                view= inf.inflate(R.layout.animal_card, null);

                viewHolder= new AnimalCardHolder();
                viewHolder.animalPic= (ImageView) view.findViewById(R.id.animalPic);
                viewHolder.animalDescription=(TextView) view.findViewById(R.id.animalDescription);
                view.setTag(viewHolder);
            }


            viewHolder.animalPic.setImageBitmap(Util.stringToBitmap(currentAnimal.getImg()));
            viewHolder.animalDescription.setText(currentAnimal.getDescription());

            return view;
        }

    }
}
