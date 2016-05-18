package es.pue.googlemapstest;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG="MapsActivity";

    private GoogleMap mMap;
    LocationListener locationListener;
    List<LatLng> linePoints=new ArrayList<>();
    Polyline lastLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        linePoints.add(sydney);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Location lastLocation=null;
        try {
            LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000,
                    0,
                    new MapsLocationListener());

        }catch(SecurityException e){
            Log.e(TAG,"Could not register LocationListener");
        }
    }


    public class MapsLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {

            LatLng moveTo=new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLng(moveTo);
            mMap.animateCamera(cameraUpdate,2000,null);

            linePoints.add(moveTo);

            PolylineOptions options=new PolylineOptions().addAll(linePoints);
            Polyline currentline=mMap.addPolyline(options);

            if(lastLine!=null){
                lastLine.remove();
            }
            lastLine=currentline;

            Log.e(TAG,"REceived Update");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
