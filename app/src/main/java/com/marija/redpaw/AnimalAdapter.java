package com.marija.redpaw;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Created by demouser on 8/6/15.
 */
public class AnimalAdapter extends BaseAdapter implements SpinnerAdapter {
    String[] animals ;
    Activity which;

    public AnimalAdapter(Activity _which, String[] animals) {
        which = _which;
        this.animals=animals;
    }

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
            view = (TextView) LayoutInflater.from(which).inflate(android.R.layout.simple_spinner_dropdown_item,null);
        } else {
            view = (TextView)convertView;
        }
        view.setPadding(80,30,80,30);
        view.setText(animals[position]);
        return view;
    }
}
