package com.marija.redpaw;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


public class AddShelterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shelter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_shelter, menu);
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

    public void onClickSubmitBtn(View view){
        Shelter shelter= new Shelter();
        shelter.setId(((EditText) findViewById(R.id.add_shelter_id)).getText().toString());
        shelter.setName(((EditText) findViewById(R.id.add_shelter_name)).getText().toString());
        shelter.setPhonenumber(((EditText) findViewById(R.id.add_shelter_phone)).getText().toString());
        shelter.setAddress(((EditText) findViewById(R.id.add_shelter_address)).getText().toString());

        Firebase.setAndroidContext(this);
        Firebase referenceShelters=new Firebase(getString(R.string.database_shelters));
        referenceShelters.push().setValue(shelter);

        Context context = getApplicationContext();
        CharSequence text = "Your shelter has been added.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(AddShelterActivity.this, MainActivity.class);
        startActivity(intent);



    }
}
