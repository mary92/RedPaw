package com.marija.redpaw;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
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
    private Toolbar actionToolbar;
    Button notificationsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        listView = (ListView)findViewById(R.id.animals_animalsInMyShelter);
        listAdapter= new MyAnimalAdapter();
        listView.setAdapter(listAdapter);

        account = new Account("worker", "savethepets", "8268");

        Firebase.setAndroidContext(this);
        sheltersDB = new Firebase(getString(R.string.database_shelters));
        addDBListener();

        actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.animals_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
        addNotifications();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionToolbar.setTitle("   All Animals");
    }

    private void addNotifications() {
        if (notificationsBtn == null) {
            notificationsBtn = new Button(getApplicationContext());
            notificationsBtn.setText("3");

            actionToolbar.addView(notificationsBtn);

            notificationsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AnimalsActivity.this, PickUpActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void addDBListener() {
        sheltersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                animalsInMyShelter.clear();

                Shelter shelter;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    shelter = data.getValue(Shelter.class);
                    if (account.getShelterId().equals(shelter.getId())) {
                        populateAnimalsList(shelter.getAnimals());
                    }
                }

                listView.invalidateViews();
            }

            private void populateAnimalsList(List<Animal> animals) {
                if(animals != null) {
                    for (Animal pet : animals) {
                        animalsInMyShelter.add(pet);
                    }
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

    public void editAnimal(View v) {
        Intent i = new Intent(AnimalsActivity.this, ModifyActivity.class);
        int position = listView.getPositionForView(v);
        i.putExtra("position", position);
        i.putExtra("shelterId", account.getShelterId());
        i.putExtra("animal", (Animal) listAdapter.getItem(position));
        startActivity(i);
    }

    public void addAnimal(View view) {
        Intent i = new Intent(AnimalsActivity.this, ModifyActivity.class);
        i.putExtra("position", listAdapter.getCount());
        i.putExtra("shelterId", account.getShelterId());
        i.putExtra("animal", new Animal());
        startActivity(i);
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
            viewHolder.position = position;

            return view;
        }

    }
}
