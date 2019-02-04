package fi.jamk.k8760.showplaces;

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
import com.google.android.gms.maps.model.Marker;
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
    String url = "http://ptm.fi/materials/golfcourses/golf_courses.json";
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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
            JSONArray courseArray = object.getJSONArray("courses");
            int count = 0;
            for (int i = 0; i < courseArray.length(); ++i) {
                JSONObject course = courseArray.getJSONObject(i);
                float lat = Float.parseFloat(course.getString("lat"));
                float lng = Float.parseFloat(course.getString("lng"));
                LatLng location = new LatLng(lat, lng);
                if (i == 0) zoomLocation = location;
                String type = course.getString("type");
                MarkerOptions markerOptions = new MarkerOptions();
                if (type.equals("Etu"))
                    markerOptions.position(location)
                        .title(course.getString("course"))
                        .snippet(course.getString("address"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                else if (type.equals("Kulta"))
                    markerOptions.position(location)
                            .title(course.getString("course"))
                            .snippet(course.getString("address"))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                else
                    markerOptions.position(location)
                            .title(course.getString("course"))
                            .snippet(course.getString("address"))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                InfoWindowData info = new InfoWindowData();
                info.setPhone(course.getString("phone"));
                info.setEmail(course.getString("email"));
                info.setUrl(course.getString("web"));

                InfoWindow customInfoWindow = new InfoWindow(this);
                mMap.setInfoWindowAdapter(customInfoWindow);

                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
                m.showInfoWindow();
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomLocation, 5));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}