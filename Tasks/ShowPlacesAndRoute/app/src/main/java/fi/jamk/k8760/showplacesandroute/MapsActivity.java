package fi.jamk.k8760.showplacesandroute;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String url = "https://student.labranet.jamk.fi/~K8760/json/Kotipizzat.json";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    LatLng zoomLocation;
    LatLng myLocation;
    List<LatLng> path = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermissions();
        Polyline line = mMap.addPolyline(
                new PolylineOptions().add(
                        new LatLng(62.241631, 25.748927),
                        new LatLng(62.288284,25.716505)
                ).width(2).color(Color.BLUE).geodesic(true)
        );
    }

    private void checkPermissions() {
        // check permission
        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // permission is not granted yet
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // ask it -> a dialog will be opened
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLastLocation();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray pizzaArray = object.getJSONArray("kotipizzat");
            for(int i = 0; i < pizzaArray.length(); ++i) {
                JSONObject pizza = pizzaArray.getJSONObject(i);
                float lat = Float.parseFloat(pizza.getString("lat"));
                float lng = Float.parseFloat(pizza.getString("lng"));
                LatLng location = new LatLng(lat, lng);
                if (i == 0) zoomLocation = location;
                mMap.addMarker(new MarkerOptions().position(location).title(pizza.getString("title")));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomLocation,12));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getLastLocation() {
        int hasLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                //myLocation = new LatLng(62.275560, 25.758100);
                                mMap.addMarker(new MarkerOptions().position(myLocation).title("You are here").icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,12));
                            }
                        }
                    });
        }
    }



}
