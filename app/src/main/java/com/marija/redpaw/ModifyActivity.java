package com.marija.redpaw;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by demouser on 8/6/15.
 */
public class ModifyActivity extends ActionBarActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap image;
    private AnimalSpinnerOnItemSelectListener listener;
    private int id;
    private String shelterId;
    Firebase sheltersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Firebase.setAndroidContext(this);
        sheltersRef=new Firebase(getString(R.string.database_shelters) + "/");

        Spinner spinner = (Spinner)findViewById(R.id.modify_animalType);
        String [] animalTypes = {"Cat", "Dog", "Other"};
        SpinnerAdapter adapter = new AnimalAdapter(ModifyActivity.this, animalTypes);
        spinner.setAdapter(adapter);
        listener = new AnimalSpinnerOnItemSelectListener(ModifyActivity.this);
        spinner.setOnItemSelectedListener(listener);

        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.modify_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);

        Animal animal = (Animal)getIntent().getSerializableExtra("animal");
        shelterId = getIntent().getStringExtra("shelterId");
        id = getIntent().getIntExtra("position", 0);

        // If we are editing an animal
        if(animal.getImg() != null && !animal.getImg().equals("")){
            ImageView imageView = (ImageView)findViewById(R.id.modify_imgViewPhoto);
            image = Util.stringToBitmap(animal.getImg());
            imageView.setImageBitmap(image);

            TextView description = (TextView)findViewById(R.id.modify_fieldDescription);
            description.setText(animal.getDescription());

            int position;
            switch (animal.getType()) {
                case Cat: position = 0;
                    break;
                case Dog: position = 1;
                    break;
                default: position = 2;
                    break;
            }
            spinner.setSelection(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.modify_toolbar);
        actionToolbar.setTitle("   Add/Edit Animal");
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

    public void onClickBtnUploadPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            ImageView imgViewPhoto = (ImageView) findViewById(R.id.modify_imgViewPhoto);
            imgViewPhoto.setImageBitmap(image);
        }
    }

    public void onClickBtnSave(View view) {
        TextView description = (TextView)findViewById(R.id.modify_fieldDescription);

        Animal animal = new Animal(description.getText().toString(), listener.getType(), Util.bitmapToString(image));

        Map<String, Animal> map = new HashMap<>();
        map.put("" + id, animal);
        sheltersRef.child("/" + shelterId + "/animals/" + id).setValue(animal);
        Intent intent = new Intent(ModifyActivity.this, AnimalsActivity.class);
        startActivity(intent);
    }
}
