package com.marija.redpaw;

import android.content.Context;
import android.content.Intent;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.client.Firebase;


public class MainActivity extends ActionBarActivity {
    private final static int FOUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);
        Firebase.setAndroidContext(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        actionToolbar.setTitle("   Red Paw");
    }

    public void onClickBtnFound(View view){
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivityForResult(intent, FOUND);
    }

    public void onClickBtnAdopt(View view){
        Intent intent = new Intent(MainActivity.this, AdoptActivity.class);
        startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, PickUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_shelter_login) {
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }else if(id==R.id.menu_add_shelter){
            Intent i = new Intent(MainActivity.this, AddShelterActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
