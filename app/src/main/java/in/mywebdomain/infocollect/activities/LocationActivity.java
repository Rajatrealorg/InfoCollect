package in.mywebdomain.infocollect.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import in.mywebdomain.infocollect.R;
import in.mywebdomain.infocollect.entities.TheClient;
import in.mywebdomain.infocollect.others.Constants;
import in.mywebdomain.infocollect.others.PermissionUtils;
import in.mywebdomain.infocollect.others.RKSHttp;

public class LocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    TextView tv;
    String name, pno, dob;
    Button save;
    ProgressBar progressBar;

    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    LatLng glt;
    MarkerOptions markerOptions;
    static String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // TODO Get Data from previous Activity
        if(hasIntention()){
            fillData();
        }

        progressBar = findViewById(R.id.location_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        save = findViewById(R.id.location_btn_save);

        save.setOnClickListener(view -> {
            // Check If having Location

            // If Everything Fine
            TheClient theClient = new TheClient(name, pno, dob, "" + glt.latitude, "" + glt.longitude, tv.getText().toString());
            // Store in DB
//                ShowInfoDB infoDB = new ShowInfoDB(LocationActivity.this);
//                boolean isInserted = infoDB.insert(theClient);
//                infoDB.close();
//                if(!isInserted) Toast.makeText(LocationActivity.this, "Not Inserted in DB !!", Toast.LENGTH_SHORT).show();

            save.setEnabled(false);
            save.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);// Show ProgressBar
            // Send to Server
            new LoginTask(this).execute(theClient);
//            postExecution();
        });
        tv = findViewById(R.id.tv_location);
        tv.setText("");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(LocationActivity.this);
        }


    }

     private void postExecution() {
        if(response != null && response.length() > 0) {
            Log.i("RKSRest", response);
            if(response.equals("1")) {
                Toast.makeText(LocationActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                if(response.equals("2")) {
                    Toast.makeText(LocationActivity.this, "Phone No. already Exists !!", Toast.LENGTH_SHORT).show();
                } else if(response.equals("3")) {
                    Toast.makeText(LocationActivity.this, "Some Data Missing !!", Toast.LENGTH_SHORT).show();
                } else if(response.equals("4")) {
                    Toast.makeText(LocationActivity.this, "Error Sending Data !!", Toast.LENGTH_SHORT).show();
                }
                save.setVisibility(View.VISIBLE);
                save.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            save.setEnabled(true);
            save.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LocationActivity.this, "No Data Received !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillData() { name = getIntent().getStringExtra("name"); pno = getIntent().getStringExtra("pno"); dob = getIntent().getStringExtra("dob");}
    private boolean hasIntention() { return getIntent().hasExtra("name"); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.location_menu_back){
            Intent intent = new Intent(LocationActivity.this, EntryActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("pno", pno);
            intent.putExtra("dob", dob);
            // Could have done using shared preferences
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(LocationActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            // TODO: Add On Map Click Listener to change Location
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (lm != null) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(tv.getText().length()<4) {
            glt = new LatLng(location.getLatitude(), location.getLongitude());
            if (markerOptions == null) {
                markerOptions = new MarkerOptions().position(glt).title("You");
                googleMap.addMarker(markerOptions);
            } else markerOptions.position(glt);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(glt, 15));
            Geocoder g = new Geocoder(LocationActivity.this);
            List<Address> lst = null;
            try {
                lst = g.getFromLocation(glt.latitude, glt.longitude, 1);
                Address ad = lst.get(0);
                tv.setText(ad.getAddressLine(0)); // TODO : Add Address one time only to Edit Text
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private static class LoginTask extends AsyncTask<TheClient, Integer, String> {
        private WeakReference<LocationActivity> contextWeakReference;

        LoginTask(LocationActivity locationActivity) {
            contextWeakReference = new WeakReference<>(locationActivity);
        }

        @Override
        protected String doInBackground(TheClient... theClients) {
            TheClient theClient = theClients[0];
            try {
                response = RKSHttp.postRKSHttp(Constants.URL_LOGIN, theClient.getAsMap());
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("LOCA 1", "HTTP Error");
                response = "4";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            response = s;
            Log.i("LOCA 2", s);
            contextWeakReference.get().postExecution();
            super.onPostExecute(s);
        }
    }
}

