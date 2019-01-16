package com.demoapp.Activities;

import android.util.Log;

import com.demoapp.Model.CategoryList;
import com.demoapp.Model.Location;
import com.demoapp.Model.Venue;
import com.demoapp.My_Interface.GetVenueDataService;
import com.demoapp.My_Interface.MainContract;
import com.demoapp.Network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetVenueIntracterImpl implements MainContract.GetVenueIntractor {

    String Client_ID = "YA44VODAYYG2OKI03BTWTY0JDNVGJNMSADCYT2KBMIYASNII";
    String Client_Secret = "PCTW3UJZKIKPY4YYDTXJGERSHFERS1JTLIWZTYZUENVQQ5OA";
    String apiVersion = "20190110";
    String geoLocation = "26.95902,75.7781925";
    String query = "cafe";
    String category;
    double latitude, longitude;
    String location;

    public GetVenueIntracterImpl(String category, double latitude, double longitude) {
        this.category=category;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    public void getVenueArrayList(final OnFinishedListener onFinishedListener) {
        location=(latitude+","+longitude);
        /** Create handle for the RetrofitInstance interface*/
        GetVenueDataService service = RetrofitInstance.getRetrofitInstance().create(GetVenueDataService.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<CategoryList> call = service.getCategoryListData(location,category,Client_ID,Client_Secret,apiVersion);
        /**Log the URL called*/
        Log.e("URL Called", call.request().url() + "");
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                onFinishedListener.onFinished(response.body().getResponse().getVenuesCatList());
                Log.e("URLCalled", String.valueOf(response.body().getResponse().getVenuesCatList()));
                ArrayList<Venue> venues=response.body().getResponse().getVenuesCatList();
                for (int i = 0; i < venues.size(); i++) {
                    Log.e("URLCalled", venues.get(i).getId());
                    Log.e("URLCalled", venues.get(i).getName());
                }
            }
            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                onFinishedListener.onFailure(t);

            }
        });
    }
}
