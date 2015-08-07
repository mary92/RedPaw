package com.marija.redpaw;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class SignInActivity extends ActionBarActivity {

    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ref = new Firebase("https://brilliant-inferno-9405.firebaseio.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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

    public void onSignIn(final View view) {
        final EditText emailBox = (EditText) findViewById(R.id.email_box);
        final EditText passwordBox = (EditText) findViewById(R.id.password_box);

        ref.authWithPassword(emailBox.getText().toString(), passwordBox.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("Auth", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent i = new Intent(SignInActivity.this, AnimalsActivity.class);
                startActivity(i);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e("Auth", "Failed authentication!");
                emailBox.setText("");
                passwordBox.setText("");
                Toast.makeText(view.getContext(), getString(R.string.failed_login), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
