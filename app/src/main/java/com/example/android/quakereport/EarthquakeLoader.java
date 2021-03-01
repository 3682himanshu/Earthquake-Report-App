package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthQuakelist>> {
    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String mUrl;
    public EarthquakeLoader(@NonNull Context context,String url) {
        super(context);
        mUrl=url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public ArrayList<EarthQuakelist> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        ArrayList<EarthQuakelist> earthquakes = Utils.fetchData(mUrl);
        return earthquakes;
    }
}
