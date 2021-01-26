package com.example.android.quakereport;

public class EarthQuakelist {
    public double magnitude;
    String place1,uri,place2;
    long date;

    public EarthQuakelist(double mag,String cPlace1,String cPlace2,long cDate,String cUri)
    {
        magnitude=mag;
        place1=cPlace1;
        place2=cPlace2;
        date=cDate;
        uri=cUri;
    }
}
