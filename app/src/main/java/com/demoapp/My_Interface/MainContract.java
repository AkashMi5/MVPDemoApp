package com.demoapp.My_Interface;

import com.demoapp.Model.Venue;

import java.util.ArrayList;

public interface MainContract {
    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface presenter{

        void onDestroy();
        void requestDataFromServer();

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(ArrayList<Venue> venueArrayList);

        void onResponseFailure(Throwable throwable);

    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetVenueIntractor {

        interface OnFinishedListener {
            void onFinished(ArrayList<Venue> venueArrayList);
            void onFailure(Throwable t);
        }
        void getVenueArrayList(OnFinishedListener onFinishedListener);
    }
}
