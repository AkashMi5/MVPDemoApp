package com.demoapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demoapp.Model.Category;
import com.demoapp.Model.CategoryList;
import com.demoapp.Model.SubCategory;
import com.demoapp.My_Interface.GetVenueDataService;
import com.demoapp.Network.RetrofitInstance;
import com.demoapp.R;
import com.demoapp.Utils.GPSTracker;
import com.demoapp.Utils.LocationAddress;
import com.demoapp.Utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    String Client_ID = "YA44VODAYYG2OKI03BTWTY0JDNVGJNMSADCYT2KBMIYASNII";
    String Client_Secret = "PCTW3UJZKIKPY4YYDTXJGERSHFERS1JTLIWZTYZUENVQQ5OA";
    String apiVersion = "20190110";
    String geoLocation = "40.7,-74";
    String query = "cafe";
    private GoogleMap mMap;
    private double latitude, longitude;
    private String locationAddress,city,state,country,postal_code,categoryId,categoryName;
    private GPSTracker gps;
    private LatLng garageLocation;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private AutoCompleteTextView actv_search;
    private Button btn_search;
    private ArrayList<String> names;
    private ArrayList<String> shortNames;
    private ArrayList<String> categoryIdlist;
    private ArrayAdapter<String> adapter;

    private static String[] PERMISSIONS_CONTACT = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    protected void makeRequest() {
        ActivityCompat.requestPermissions(MapsActivity.this,
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
        setContentView(R.layout.activity_maps);
        sessionManager=new SessionManager(getApplicationContext());
        names=new ArrayList<>();
        shortNames=new ArrayList<>();
        categoryIdlist=new ArrayList<>();

        if (!hasPermissions(MapsActivity.this, PERMISSIONS_CONTACT)) {
            makeRequest();
        } else {

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        actv_search=(AutoCompleteTextView)findViewById(R.id.actv_searchVenue);
        btn_search=(Button)findViewById(R.id.btn_searchCategory);

        getLatLong();
        getCategory();

        actv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).length() != 0) {
                    searchCategory(String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=actv_search.getText().toString();
                if (category.isEmpty()){
                    actv_search.setError("Please select category");
                }else {
                    Intent intent = new Intent(MapsActivity.this, VenueListActivity.class);
                    intent.putExtra("category",categoryId);
                    intent.putExtra("categoryName",categoryName);
                    startActivity(intent);
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude, MapsActivity.this, new GeocoderHandler());

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // create class object
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                garageLocation = latLng;
                latitude=garageLocation.latitude;
                longitude=garageLocation.longitude;
                sessionManager.setPreference(MapsActivity.this,"latitude", String.valueOf(latitude));
                sessionManager.setPreference(MapsActivity.this,"longitude", String.valueOf(longitude));
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude, MapsActivity.this, new GeocoderHandler());
            }
        });
    }

    public void getLatLong(){
        // create class object
        gps = new GPSTracker(MapsActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("MapsActivity", String.valueOf(longitude));
            Log.e("MapsActivity", String.valueOf(latitude));
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude, MapsActivity.this, new GeocoderHandler());
            sessionManager.setPreference(MapsActivity.this,"latitude", String.valueOf(latitude));
            sessionManager.setPreference(MapsActivity.this,"longitude", String.valueOf(longitude));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    Log.i("bundle", String.valueOf(bundle));
                    locationAddress = bundle.getString("address");
                    city = bundle.getString("city");
                    state = bundle.getString("state");
                    country = bundle.getString("country");
                    postal_code = bundle.getString("postal_code");
                    //Toast.makeText(MapsActivity.this, locationAddress, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    locationAddress = null;
            }
        }
    }

    public void getCategory(){
        /*Create handle for the RetrofitInstance interface*/
        GetVenueDataService service = RetrofitInstance.getRetrofitInstance().create(GetVenueDataService.class);
        /*Call the method with parameter in the interface to get the employee data*/
        Call<CategoryList> call = service.getVenueListData(Client_ID,Client_Secret,apiVersion);
        /*Log the URL called*/
        Log.e("URLCalled", call.request().url() + "");
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (response!=null) {

                    ArrayList<Category> empDataList=response.body().getResponse().getVenueArrayList();
                    names.add(String.valueOf(response.body().getResponse().getVenueArrayList()));
                    for (int i = 0; i < empDataList.size(); i++) {
                        names.add(empDataList.get(i).getName());
                        shortNames.add(empDataList.get(i).getShortName());
                        categoryIdlist.add(empDataList.get(i).getId());
                        categoryId=empDataList.get(i).getId();

                        ArrayList<SubCategory> subCategories=empDataList.get(i).getSubCategoryList();
                        for (int j = 0; j < subCategories.size(); j++) {
                            names.add(subCategories.get(j).getName());
                            shortNames.add(subCategories.get(j).getShortName());
                            categoryIdlist.add(subCategories.get(j).getId());
                            categoryId = subCategories.get(j).getId();

                            ArrayList<SubCategory> subCategories2=subCategories.get(j).getSubCategoryList();

                            for (int k = 0; k < subCategories2.size(); k++) {
                                names.add(subCategories2.get(k).getName());
                                shortNames.add(subCategories2.get(k).getShortName());
                                categoryIdlist.add(subCategories2.get(k).getId());
                                categoryId = subCategories2.get(k).getId();

                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!"+t, Toast.LENGTH_SHORT).show();
                Log.e("URLCalled", String.valueOf(t));

            }
        });
    }

    public void searchCategory(String s) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).contains(s)) {
                categoryId = categoryIdlist.get(i);
                categoryName = names.get(i);
                Log.e("URLCalled3", categoryId);
                adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1, names) {

                        @Override
                        public View getView(int position,
                        View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    actv_search.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
