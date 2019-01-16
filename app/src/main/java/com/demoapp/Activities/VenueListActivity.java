package com.demoapp.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demoapp.Adapters.AdapterVenueListActivity;
import com.demoapp.Model.Venue;
import com.demoapp.My_Interface.MainContract;
import com.demoapp.R;
import com.demoapp.Utils.GPSTracker;
import com.demoapp.Utils.LocationAddress;
import com.demoapp.Utils.SessionManager;

import java.util.ArrayList;

public class VenueListActivity extends AppCompatActivity implements MainContract.MainView{
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView tv_categoryName;
    private RelativeLayout rl_back;
    private Toolbar toolbar;
    private MainContract.presenter presenter;
    private String category,categoryName;
    private GPSTracker gps;
    private double latitude, longitude;
    private SessionManager sessionManager;
    private static String[] PERMISSIONS_CONTACT = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    protected void makeRequest() {
        ActivityCompat.requestPermissions(VenueListActivity.this,
                new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE},
                101);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);
        sessionManager=new SessionManager(getApplicationContext());
        if (!hasPermissions(VenueListActivity.this, PERMISSIONS_CONTACT)) {
            makeRequest();
        } else {

        }
        Bundle bundle=getIntent().getExtras();
        if (!bundle.isEmpty()){
            category=bundle.getString("category");
            categoryName=bundle.getString("categoryName");
            Log.e("urleeeeeee",categoryName);
        }

        getLatLong();
        initializeToolbarAndRecyclerView();
        initProgressBar();

        presenter = new MainPresenterImpl(this, new GetVenueIntracterImpl(category,latitude,longitude));
        presenter.requestDataFromServer();

    }


    /**
     * Initializing Toolbar and RecyclerView
     */
    private void initializeToolbarAndRecyclerView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar_top);
        recyclerView=(RecyclerView)findViewById(R.id.rv_venues);
        tv_categoryName=(TextView)findViewById(R.id.tv_title);
        rl_back=(RelativeLayout) findViewById(R.id.rl_back);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VenueListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        tv_categoryName.setText(categoryName);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    /**
     * Initializing progressbar programmatically
     * */
    private void initProgressBar() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDataToRecyclerView(ArrayList<Venue> venueArrayList) {
        AdapterVenueListActivity adapter = new AdapterVenueListActivity(venueArrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(VenueListActivity.this,
                "Something went wrong...Error message: " + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void getLatLong(){
        // create class object
        gps = new GPSTracker(VenueListActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("VenueActivity", String.valueOf(longitude));
            Log.e("VenueActivity", String.valueOf(latitude));
            //LocationAddress locationAddress = new LocationAddress();
            //locationAddress.getAddressFromLocation(latitude, longitude, VenueListActivity.this, new MapsActivity.GeocoderHandler());
            sessionManager.setPreference(VenueListActivity.this,"latitude", String.valueOf(latitude));
            sessionManager.setPreference(VenueListActivity.this,"longitude", String.valueOf(longitude));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}

