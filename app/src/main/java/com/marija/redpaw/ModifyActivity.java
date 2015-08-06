package com.marija.redpaw;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by demouser on 8/6/15.
 */
public class ModifyActivity extends ActionBarActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.modify_toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setLogo(R.mipmap.ic_launcher);

        Bundle bundle = getIntent().getExtras();
        Animal animal = (Animal)bundle.getSerializable("animal");
        int id = (int)bundle.getInt("id");

        // If we are editing an animal
        if(animal.getImg() != null || animal.getImg().equals("")){
            ImageView imageView = (ImageView)findViewById(R.id.modify_imgViewPhoto);
            imageView.setImageBitmap(Util.stringToBitmap(animal.getImg()));

            TextView description = (TextView)findViewById(R.id.modify_fieldDescription);
            description.setText(animal.getDescription());

            Spinner spinner = (Spinner)findViewById(R.id.modify_animalType);
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
        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
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
            ImageView imgViewPhoto = (ImageView) findViewById(R.id.report_imgViewPhoto);
            imgViewPhoto.setImageBitmap(image);
        }
    }
}
