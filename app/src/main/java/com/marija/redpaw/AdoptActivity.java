package com.marija.redpaw;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by demouser on 8/6/15.
 */
public class AdoptActivity extends ActionBarActivity {
    private ArrayList<Animal> animalsInShelter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        animalsInShelter=new ArrayList<Animal>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);
        listView=(ListView)findViewById(R.id.adopt_listViewResults);
        MyAdapter adapter=new MyAdapter();
        listView.setAdapter(adapter);

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
            View v;
            ViewHolder vh;
            if(convertView!=null ){
                v=convertView;
                vh=(ViewHolder)v.getTag();
            }else {
                LayoutInflater inf=LayoutInflater.from(AdoptActivity.this);
                v= inf.inflate(R.layout.list_item, null);
                vh=new ViewHolder();
                v.setTag(vh);
            }
            return v;
        }
    }

    /*
    Needed to show text for each animal.
     */
    public class ViewHolder{
        //No objects in view yet.
    }
}
