package com.sm42.leo.randomchat;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;

import chat.Administratie;


public class FacebookLogin extends FragmentActivity implements LocationListener {

    private EditText usernameView;
    private Spinner interests;
    private LocationManager locationManager;
    private String countryCode;
    private String provider;
    private double longitude;
    private double latidude;
    //private LoginButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_facebook_login);
        usernameView = (EditText) findViewById(R.id.username);
        interests = (Spinner) findViewById(R.id.spInterests);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interstGroups, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        interests.setAdapter(adapter);
        Administratie admin = Administratie.getInstance();
        File file = new File(getApplicationContext().getFilesDir(), "chats");
        if(file.exists())
        {
            admin.readChats(file);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        countryCode = tm.getSimCountryIso();

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider


            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
        }
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        CallbackManager callbackManager = CallbackManager.Factory.create();
//        //showHashKey(getApplicationContext());
//       // loginBtn = (LoginButton) findViewById(R.id.login_button);
//        // Other app specific specialization
//        loginBtn.setReadPermissions("user_likes");
//        // Callback registration
//        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Profile profile = Profile.getCurrentProfile();
//            }
//
//            @Override
//            public void onCancel()
//            {
//                usernameView.setText("Facebook cancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception)
//            {
//                usernameView.setText("Facebook error");
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook_login, menu);
        return true;
    }

    public void onLocationChanged(Location location)
    {
        this.longitude = location.getLongitude();
        this.latidude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void btnLogin_Click(View v)
    {
        Intent intent = new Intent(this, ChatList.class);
        intent.putExtra("username", usernameView.getText().toString());
        intent.putExtra("interest", interests.getSelectedItem().toString());
        intent.putExtra("longitude", this.longitude);
        intent.putExtra("latidude", this.latidude);
        startActivity(intent);
        finish();
    }
}
