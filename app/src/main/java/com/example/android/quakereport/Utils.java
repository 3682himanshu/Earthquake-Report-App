package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public final class Utils {
    public static ArrayList<EarthQuakelist> fetchData(String requestUrl)
    {
        URL url=createUrl(requestUrl);
        String jsonResponse=null;
        try {
            jsonResponse=makeHttpRequest(url);
        }
        catch (Exception e)
        {
            Log.e("error","jsonResponse not created");
        }
        return extractData(jsonResponse);
    }
    private static String makeHttpRequest(URL url)throws IOException
    {
        String jsonResponse=null;
        if(url==null)
            return jsonResponse;
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
            urlConnection=(HttpsURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else
                Log.e("error","Response Code error");
        } catch (IOException e) {
            Log.e("error","problem getting data");
        }
        finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            if(inputStream!=null)
                inputStream.close();
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null)
            {
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }
    private static ArrayList<EarthQuakelist> extractData(String jsonResponse)
    {
        ArrayList<EarthQuakelist> res=new ArrayList<EarthQuakelist>();
        if(jsonResponse==null)
            return null;
        try {
            JSONObject root=new JSONObject(jsonResponse);
            JSONArray  featureArray=root.getJSONArray("features");
            if(featureArray.length()>0)
            {
                for(int i=0;i<featureArray.length();i++)
                {
                    JSONObject cur=featureArray.getJSONObject(i);
                    JSONObject properties=cur.getJSONObject("properties");

                    String place,uri;
                    long time;
                    Double mag;
                    uri=properties.getString("url");
                    mag=properties.getDouble("mag");
                    place=properties.getString("place");
                    Log.i("Place",place);
                    String Place_ar[]={"Near the"," "+place};
                    if(place.contains("of"))
                    {
                        Place_ar=place.split("of",2);
                        Place_ar[0]+=" of";
                    }
                    time=properties.getLong("time");
                    EarthQuakelist ob=new EarthQuakelist(mag,Place_ar[0],Place_ar[1].substring(1),time,uri);
                    if(place.contains("India"))
                        res.add(ob);
                }
            }

        } catch (JSONException e) {
            Log.e("error","Problem parsing the earthquake JSON results");
        }
        return res;
    }
    private static URL createUrl(String stringUrl)
    {
        URL url=null;
        try {
            url=new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("error","Url not created");
        }
        return url;
    }
}
