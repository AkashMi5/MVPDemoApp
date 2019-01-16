package com.demoapp.My_Interface;

import com.demoapp.Model.CategoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetVenueDataService {

    @GET("categories")
    Call<CategoryList> getVenueListData(@Query("client_id") String client_id,
                                        @Query("client_secret") String client_secret,
                                        @Query("v") String v);


    @GET("search")
    Call<CategoryList> getCategoryListData(@Query("ll")String latlong,
                                           @Query("categoryId")String category,
                                           @Query("client_id") String client_id,
                                           @Query("client_secret") String client_secret,
                                           @Query("v") String v);
}
