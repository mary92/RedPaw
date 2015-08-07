package com.marija.redpaw;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by demouser on 8/6/15.
 */
public class AnimalSpinnerOnItemSelectListener implements AdapterView.OnItemSelectedListener{
    private Type type;
    private Activity which;

    public AnimalSpinnerOnItemSelectListener(Activity activity) {
        which = activity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("Dog")) {
            setType(Type.Dog);
        } else if (parent.getItemAtPosition(position).equals("Cat")) {
            setType(Type.Cat);
        } else {
            setType(Type.Other);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Context context = which.getApplicationContext();
        CharSequence text = "Please choose an animal type";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
