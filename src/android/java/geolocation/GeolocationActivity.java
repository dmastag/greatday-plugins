package com.greatday.plugins.activity.geolocation;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.greatday.plugins.extention.AndroidPermissions;

import io.ionic.starter.R;

public class GeolocationActivity extends Activity {

    public static Integer PERMISSIONS_REQUEST_CODE = 1;
    public static Integer PERMISSIONS_REQUEST_SETTINGS = 2;

    AndroidPermissions mPermissions;

    LocationManager locationManager;

    LottieAnimationView searchingLocationLottie;
    TextView searchingLocationTitle;

    LottieAnimationView locationFoundLottie;
    TextView locationFoundTitle;
    TextView locationFoundDescription;
    TextView locationFoundQuestion;
    Button locationFoundYes;
    Button locationFoundRefresh;
    View locationFoundLeft;
    View locationFoundRight;
    TextView locationFoundOr;
    TextView locationFoundGoogleMaps;

    Double mLongitude;
    Double mLattitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialComponent();
        setupPermissions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPermissions() {
        mPermissions = new AndroidPermissions(this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        );
        if (mPermissions.checkPermissions()) {
            if (checkLocation()) {
                loadLocation();
            }
        } else {
            mPermissions.requestPermissions(PERMISSIONS_REQUEST_CODE);
        }
    }

    // Check GPS is active or not
    private Boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private Boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }


    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'. Please Enable Location to use this app")
                .setCancelable(false)
                .setPositiveButton("Location Settings", (dialog1, which) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, PERMISSIONS_REQUEST_SETTINGS);
                })
                .setNegativeButton("Cancel", (dialog1, which) -> {
                    finish();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (mPermissions.areAllRequiredPermissionsGranted(permissions, grantResults)) {
                setupPermissions();
            } else {
                onInsufficientPermissions();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_SETTINGS) {
            setupPermissions();
        }
    }

    private void onInsufficientPermissions() {
        finish();
    }

    private void loadLocation() {
        hideComponent();
        new Handler().postDelayed(() -> {
            try {
                GeoLocator geoLocator = new GeoLocator(getApplicationContext(), GeolocationActivity.this);
                mLongitude = geoLocator.getLongitude();
                mLattitude = geoLocator.getLattitude();
                locationFoundDescription.setText(geoLocator.getAddress());
            } catch (Exception ex) {
                mLongitude = 0.0;
                mLattitude = 0.0;
                locationFoundDescription.setText("Unable to trace your location");
            }
            showComponent();
        }, 3000);
    }

    private void initialComponent() {
        searchingLocationLottie = findViewById(R.id.lottie_animation_searching_location);
        searchingLocationTitle = findViewById(R.id.textView_searching_location);

        locationFoundLottie = findViewById(R.id.lottie_animation_location_found);
        locationFoundTitle = findViewById(R.id.textView_location_found_title);
        locationFoundDescription = findViewById(R.id.textView_location_found_description);
        locationFoundQuestion = findViewById(R.id.textView_location_found_question);
        locationFoundYes = findViewById(R.id.button_location_found_yes);
        locationFoundRefresh = findViewById(R.id.button_location_found_refresh);
        locationFoundLeft = findViewById(R.id.view_location_found_left);
        locationFoundRight = findViewById(R.id.view_location_found_right);
        locationFoundOr = findViewById(R.id.textView_location_found_or);
        locationFoundGoogleMaps = findViewById(R.id.textView_location_found_google_maps);

        locationFoundRefresh.setOnClickListener((i) -> {
            loadLocation();
        });

        locationFoundYes.setOnClickListener((i) -> {
            finish();
        });

        locationFoundGoogleMaps.setOnClickListener((i) -> {
            String geo = "geo:" + mLattitude + "," + mLongitude;
            Uri gmmIntentUri = Uri.parse(geo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });
    }

    private void showComponent() {
        searchingLocationLottie.setVisibility(View.GONE);
        searchingLocationTitle.setVisibility(View.GONE);

        locationFoundLottie.setVisibility(View.VISIBLE);
        locationFoundTitle.setVisibility(View.VISIBLE);
        locationFoundDescription.setVisibility(View.VISIBLE);
        locationFoundQuestion.setVisibility(View.VISIBLE);
        locationFoundYes.setVisibility(View.VISIBLE);
        locationFoundRefresh.setVisibility(View.VISIBLE);
        locationFoundLeft.setVisibility(View.VISIBLE);
        locationFoundRight.setVisibility(View.VISIBLE);
        locationFoundOr.setVisibility(View.VISIBLE);
        locationFoundGoogleMaps.setVisibility(View.VISIBLE);
    }

    private void hideComponent() {
        searchingLocationLottie.setVisibility(View.VISIBLE);
        searchingLocationTitle.setVisibility(View.VISIBLE);

        locationFoundLottie.setVisibility(View.GONE);
        locationFoundTitle.setVisibility(View.GONE);
        locationFoundDescription.setVisibility(View.GONE);
        locationFoundQuestion.setVisibility(View.GONE);
        locationFoundYes.setVisibility(View.GONE);
        locationFoundRefresh.setVisibility(View.GONE);
        locationFoundLeft.setVisibility(View.GONE);
        locationFoundRight.setVisibility(View.GONE);
        locationFoundOr.setVisibility(View.GONE);
        locationFoundGoogleMaps.setVisibility(View.GONE);
    }

}
