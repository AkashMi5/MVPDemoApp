package com.demoapp.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress {
    private static final String TAG = "LocationAddress";
    Context context;
    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String city = null,state=null,country=null,postal_code=null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude,1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");

                        }
                        sb.append(address.getAddressLine(0)).append("\n");
                        /*Log.e("getAddressLine",(address.getAddressLine(0)));
                        Log.e("getSubAdminArea",address.getSubAdminArea());
                        Log.e("getAdminArea",address.getAdminArea());
                        Log.e("getCountryName",address.getCountryName());
                        Log.e("getPostalCode",address.getPostalCode());*/
                        city=address.getLocality();
                        state=address.getAdminArea();
                        country=address.getCountryName();
                        postal_code=address.getPostalCode();
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder. Please go to map to select your locality.", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
//                        "Latitude: " + latitude + " Longitude: " + longitude +
                        //result = "Address:\n" + result;
                        bundle.putString("address", result);
                        bundle.putString("city",city);
                        bundle.putString("state",state);
                        bundle.putString("country",country);
                        bundle.putString("postal_code",postal_code);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
//                        "Latitude: " + latitude + " Longitude: " + longitude +
                        result ="\n Unable to get address for this lat-long. Please go to map to select your locality.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
