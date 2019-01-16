package com.demoapp.Activities;

import com.demoapp.Model.Venue;
import com.demoapp.My_Interface.MainContract;

import java.util.ArrayList;

public class MainPresenterImpl implements MainContract.presenter, MainContract.GetVenueIntractor.OnFinishedListener {
    private MainContract.MainView mainView;
    private MainContract.GetVenueIntractor getVenueIntractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.GetVenueIntractor getVenueIntractor) {
        this.mainView = mainView;
        this.getVenueIntractor = getVenueIntractor;
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void requestDataFromServer() {
        getVenueIntractor.getVenueArrayList(this);
    }


    @Override
    public void onFinished(ArrayList<Venue> venueArrayList) {
        if(mainView != null){
            mainView.setDataToRecyclerView(venueArrayList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(mainView != null){
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}
