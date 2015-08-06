package com.marija.redpaw;

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

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by demouser on 8/6/15.
 */
public class AdoptActivity extends ActionBarActivity {
    private ArrayList<Animal> animalsInShelter;
    private ListView listView;
    private Firebase referenceShelters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        animalsInShelter=new ArrayList<Animal>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);
        // Get refernecse to shelters database
        Firebase.setAndroidContext(this);
        referenceShelters=new Firebase(getString(R.string.database_shelters));

        //Add adapter to list view.
        listView=(ListView)findViewById(R.id.adopt_listViewResults);
        MyAdapter listViewAdapter=new MyAdapter();
        listView.setAdapter(listViewAdapter);


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

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return animalsInShelter.size();
        }

        @Override
        public Object getItem(int position) {

            return animalsInShelter.get(position%animalsInShelter.size());
        }

        @Override
        public long getItemId(int position) {

            return position % animalsInShelter.size() ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            ViewHolder viewHolder;
            Animal currentAnimal=animalsInShelter.get(position%animalsInShelter.size());
            if(convertView!=null ){
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();
            }else {
                LayoutInflater inf=LayoutInflater.from(AdoptActivity.this);
                view= inf.inflate(R.layout.list_item, null);
                // Create links to view objects.
                viewHolder=new ViewHolder();
                viewHolder.img=(ImageView)findViewById(R.id.adoptitem_imgViewPhoto);
                viewHolder.description=(TextView)findViewById(R.id.adoptitem_description);
                viewHolder.shelter=(TextView)findViewById(R.id.adoptitem_shelter);
                view.setTag(viewHolder);
            }
            // Set values in images.
            viewHolder.img.setImageBitmap(currentAnimal.getImg());
            viewHolder.description.setText(currentAnimal.getDescription());
            //viewHolder.shelter.setText();<---- Where do we get the shelter from?
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
