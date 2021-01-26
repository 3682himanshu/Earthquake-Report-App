/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&minmagnitude=3&minlatitude=8.4&maxlatitude=37.6&minlongitude=68.7&maxlongitude=97.25&orderby=time&limit=50";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    MyAdapter adapter;
    ListView earthquakeListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("output",position+"");
                EarthQuakelist cur=adapter.getItem(position);
                Uri earthquakeUri=Uri.parse(cur.uri);
                Intent websiteIntent=new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        EarthquakeAsync task=new EarthquakeAsync();
        task.execute(USGS_REQUEST_URL);

    }
    private void updateUi(ArrayList<EarthQuakelist> data)
    {

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new MyAdapter(this, data);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
    public class EarthquakeAsync extends AsyncTask<String,Void,ArrayList<EarthQuakelist>>
    {

        @Override
        protected ArrayList<EarthQuakelist> doInBackground(String... urls) {
            if(urls.length<1 || urls[0]==null)
                return null;
            return Utils.fetchData(urls[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuakelist> earthQuakelists) {
            if(adapter!=null)
                adapter.clear();
            if(earthQuakelists!=null && !earthQuakelists.isEmpty())
                updateUi(earthQuakelists);
        }
    }
}
