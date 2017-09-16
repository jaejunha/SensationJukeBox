package com.github.sensation.sensationjukebox;

import android.content.Intent;
import android.graphics.Color;

import com.github.sensation.sensationjukebox.DBServer.RemoteDBManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.CircleOptions;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMarkerClickListener
{

    private TextView state;
    private TextView textContent;
    private View view;

    public static String location1 = null;
    public static String location2 = null;

    private GoogleMap mMap;
    public static String Listlocation = null;

    double latitude = 37.824009;
    double longitude = 127.597996;

    double userlatitude;
    double userlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RemoteDBManager rdbm = new RemoteDBManager();
        rdbm.execute("http://45.76.100.46/select_top3.php", "zone", "zone1", "top_rank", "3");

        TextView top3TV = findViewById(R.id.textContent);
        String []top3_music = new String[3];

        try
        {
            while(!rdbm.done);
            JSONArray jsonArray = new JSONArray(rdbm.getJsonResponse());
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jObject = jsonArray.getJSONObject(i);  // JSONObject 추출
                String musicName = jObject.getString("music_name");
                top3_music[i] = musicName;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        top3TV.setText(top3_music[0]+"\n"+top3_music[1]+"\n"+top3_music[2]+"\n");
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

        //state = (TextView)findViewById(R.id.setState);
        mMap = googleMap;
        LatLng userposition = new LatLng(userlatitude, userlongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userposition));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        onAddMarker();
        onAddMarker2();
        onAddMarker3();
    }


    //마커 , 원추가(location 1)
    public void onAddMarker() {
        LatLng position = new LatLng(latitude, longitude);

        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position).title("Zone-A");   //마커위치

        // 반경 1KM원
        CircleOptions circle1KM = new CircleOptions().center(position) //원점
                .radius(50)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        this.mMap.addMarker(mymarker);

        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //this.mMap.setMyLocationEnabled(true);
        //this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setOnMarkerClickListener(this);

        //원추가
        this.mMap.addCircle(circle1KM);

        List<Address> list = null;

        try {
            Locale.setDefault(Locale.KOREA);
            Geocoder geocoder = new Geocoder(this);
            list = geocoder.getFromLocation(latitude, longitude, 1);

            String tmp = list.get(0).getAddressLine(0);
            String cut[] = tmp.split(" ");
            if (cut.length > 1)
                location1 = cut[1];
            if (cut.length > 2)
                location2 = cut[2];

            Log.e("test", "" + location1 + location2);
            if (list.size() == 0) {
                //error 처리
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //마커 , 원추가(location 2)
    public void onAddMarker2() {
        LatLng position = new LatLng(latitude + 0.005, longitude + 0.005);

        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position).title("Zone-B");   //마커위치

        // 반경 1KM원
        CircleOptions circle1KM = new CircleOptions().center(position) //원점
                .radius(50)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        this.mMap.addMarker(mymarker);

        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //this.mMap.setMyLocationEnabled(true);
        //this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setOnMarkerClickListener(this);

        //원추가
        this.mMap.addCircle(circle1KM);

        List<Address> list = null;

        try {
            Locale.setDefault(Locale.KOREA);
            Geocoder geocoder = new Geocoder(this);
            list = geocoder.getFromLocation(latitude, longitude, 1);

            String tmp = list.get(0).getAddressLine(0);
            String cut[] = tmp.split(" ");
            if (cut.length > 1)
                location1 = cut[1];
            if (cut.length > 2)
                location2 = cut[2];

            Log.e("test", "" + location1 + location2);
            if (list.size() == 0) {
                //error 처리
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //마커 , 원추가(location 3)
    public void onAddMarker3() {
        LatLng position = new LatLng(latitude + 0.005, longitude+ 0.005);

        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position).title("Zone-C");   //마커위치

        // 반경 1KM원
        CircleOptions circle1KM = new CircleOptions().center(position) //원점
                .radius(50)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        this.mMap.addMarker(mymarker);

        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //this.mMap.setMyLocationEnabled(true);
        //this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setOnMarkerClickListener(this);

        //원추가
        this.mMap.addCircle(circle1KM);

        List<Address> list = null;

        try {
            Locale.setDefault(Locale.KOREA);
            Geocoder geocoder = new Geocoder(this);
            list = geocoder.getFromLocation(latitude, longitude, 1);

            String tmp = list.get(0).getAddressLine(0);
            String cut[] = tmp.split(" ");
            if (cut.length > 1)
                location1 = cut[1];
            if (cut.length > 2)
                location2 = cut[2];

            Log.e("test", "" + location1 + location2);
            if (list.size() == 0) {
                //error 처리
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        userlatitude = location.getLatitude();
        userlongitude = location.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userlatitude, userlongitude), 18));
        if (distance(userlatitude, userlongitude, latitude, longitude) <= 50 &&
                distance(userlatitude, userlongitude, latitude, longitude) >= -50) {
            //state.setText("Change");
        } else {
            //state.setText("test");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent jump = new Intent(this, ListDetail.class);
        startActivity(jump);
        if(marker.getTitle().equals("Zone-A")){
            Listlocation = "zone1";
        } else if(marker.getTitle().equals("Zone-B")){
            Listlocation = "zone2";
        } else if(marker.getTitle().equals("Zone-C")){
            Listlocation = "zone3";
        }

        return false;
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta, distance;
        theta = lon1 - lon2;
        distance = Math.sin(degtorad(lat1)) * Math.sin(degtorad(lat2)) + Math.cos(degtorad(lat1))
                * Math.cos(degtorad(lat2)) * Math.cos(degtorad(theta));
        distance = Math.acos(distance);
        distance = radtodeg(distance);

        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344;    // 단위 mile 에서 km 변환.
        distance = distance * 1000.0;      // 단위  km 에서 m 로 변환

        return distance;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double degtorad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double radtodeg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }
}
