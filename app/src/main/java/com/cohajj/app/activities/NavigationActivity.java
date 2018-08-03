package com.cohajj.app.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cohajj.app.R;
import com.cohajj.app.Utils;
import com.cohajj.app.api.ServiceConnector;
import com.cohajj.app.dialogs.SelectStatusFragment;
import com.cohajj.app.models.PostReport;
import com.cohajj.app.models.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {


    int noOfPeople = 1;


    public static final String KEY_NO_OF_USERS = "key_no_users";
    public static final String KEY_ROAD_ID = "key_road_id";
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 102;
    private GoogleMap mMap;
    Location lastLocation;
    Spinner spinner;

    View lyt;
    View route1, route2, route3;
    View progress;
    View refresh;
    View txtFinishTrip;

    Integer roadId;
    Long startTimestamp;
    int status;
    LocationCallback locationCallback;

    List<LatLng> road;

    View lytReport;

    View txtFire, txtHealth, txtCrowd, txtslowDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        roadId = getIntent().getIntExtra(KEY_ROAD_ID, 0);
        road = getIntent().getParcelableArrayListExtra("route");

        startTimestamp = getIntent().getLongExtra("startTime", System.currentTimeMillis());
        status = getIntent().getIntExtra("status", 4);
        lyt = findViewById(R.id.lyt_routes);
        lyt.setVisibility(View.GONE);
        route1 = findViewById(R.id.route_1);
        route2 = findViewById(R.id.route_2);
        route3 = findViewById(R.id.route_3);
        progress = findViewById(R.id.progress);
        refresh = findViewById(R.id.img_refresh);

        lytReport = findViewById(R.id.lyt_report);
        txtFinishTrip = findViewById(R.id.txt_finish_trip);

        txtFinishTrip.setOnClickListener(this);

        noOfPeople = getIntent().getIntExtra(KEY_NO_OF_USERS, 1);

        lytReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectStatusFragment fragment = new SelectStatusFragment();
                fragment.setOnClickListener(NavigationActivity.this);
                fragment.show(getSupportFragmentManager(), "TAG_SELECT_STATUS");

            }
        });

        route1.setVisibility(View.GONE);
        route2.setVisibility(View.GONE);
        route3.setVisibility(View.GONE);

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handleRefresh(spinner.getSelectedItemPosition());
//            }
//        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spinner = findViewById(R.id.spinner);

        String[] selectDestinations = new String[2];
        selectDestinations[0] = getString(R.string.select_your_destination);
        selectDestinations[1] = getString(R.string.going_to_jamarat_location);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, selectDestinations));
//        spinner.setOnItemSelectedListener(
//
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        handleRefresh(spinner.getSelectedItemPosition());
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                }
//
//
//        );
//        spinner.setSelection(1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (Utils.checkLocationServiceAvailablity(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {


                initFusedLocationServices();

                Location location = doGetLastLocation();
                lastLocation = location;
                moveToLocation(location);
                mMap.setMyLocationEnabled(true);
                bindRoute(road);
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Location location = doGetLastLocation();
            moveToLocation(location);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            lastLocation = location;

        }


    }


    private Location doGetLastLocation() {
        Location location = Utils.getLastKnownLocation(this);


        return location;
    }

    void moveToLocation(Location location) {
        try {

            LatLng latLng = Utils.fromLocation(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//            Utils.updateCameraBearing(mMap, location.getBearing());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void bindRoute(List<LatLng> routePoints) {


        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(21.419753, 39.874997);
        markerOptions.position(latLng).flat(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(routePoints);

        switch (status) {
            case 1:
                polylineOptions.color(0xff00ff00);
                break;
            case 2:
                polylineOptions.color(0xffffff00);
                break;
            case 3:
                polylineOptions.color(0xffff0000);
                break;
        }
        mMap.addPolyline(polylineOptions);


    }

    void bindRoute(View routeLyt, Route route) {
        ((TextView) routeLyt.findViewById(R.id.txt_km)).setText(route.getDistance() / 1000 + "Km");
        ((TextView) routeLyt.findViewById(R.id.txt_time)).setText(route.getAverageTime() + "Min.");

//        switch (route.getStatus()) {
//            case 1:
//                routeLyt.findViewById(R.id.img_1).setVisibility(View.VISIBLE);
//                routeLyt.setBackgroundColor(0xff006c35);
//                break;
//            case 2:
//                routeLyt.findViewById(R.id.img_2).setVisibility(View.VISIBLE);
//                routeLyt.setBackgroundColor(0xffeea81f);
//
//                break;
//            case 3:
//                routeLyt.findViewById(R.id.img_3).setVisibility(View.VISIBLE);
//                routeLyt.findViewById(R.id.img_4).setVisibility(View.VISIBLE);
//                routeLyt.setBackgroundColor(0xfff03838);
//                break;
//
//        }
//        routeLyt.setVisibility(View.VISIBLE);

    }


    private void initFusedLocationServices() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                moveToLocation(location);
            }
        });

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(2000);
//        locationRequest.setExpirationDuration(10000);
//        locationRequest.setSmallestDisplacement(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                try {
//                    Toast.makeText(NavigationActivity.this,"new Location",Toast.LENGTH_LONG).show();
                    Location location = locationResult.getLastLocation();
                    moveToLocation(location);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());


    }


    private void handleRefresh(int i) {
        if (i == 1) {
            progress.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.INVISIBLE);
            ServiceConnector.getRoutes(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.setVisibility(View.INVISIBLE);
                    Gson gson = new Gson();
                    Route[] routes = gson.fromJson(response, Route[].class);


                    if (routes.length > 0) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(21.419753, 39.874997);
                        markerOptions.position(latLng).flat(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mMap.addMarker(markerOptions);


                    } else {

                        refresh.setVisibility(View.VISIBLE);


                    }


                    if (routes.length > 0 && roadId.equals(routes[0].getRoad())) {
                        List<LatLng> latlngList0 = routes[0].getPointList();
                        latlngList0.add(new LatLng(21.403122, 39.897509));
                        latlngList0.add(new LatLng(21.404039, 39.897658));
                        latlngList0.add(new LatLng(21.406471, 39.900731));
                        latlngList0.add(new LatLng(21.411010, 39.892235));
                        latlngList0.add(new LatLng(21.417375, 39.883321));
                        latlngList0.add(new LatLng(21.418824, 39.878727));
                        latlngList0.add(new LatLng(21.419480, 39.875535));
                        bindRoute(route1, routes[0]);
                    }
                    if (routes.length > 1 && roadId.equals(routes[1].getRoad())) {
                        List<LatLng> latlngList1 = routes[1].getPointList();
                        latlngList1.add(new LatLng(21.403122, 39.897509));
                        latlngList1.add(new LatLng(21.404720, 39.896317));
                        latlngList1.add(new LatLng(21.407699, 39.893395));
                        latlngList1.add(new LatLng(21.409436, 39.890200));
                        latlngList1.add(new LatLng(21.417123, 39.882516));
                        latlngList1.add(new LatLng(21.419395, 39.875359));

                        bindRoute(route2, routes[1]);
                    }

                    if (routes.length > 2 && roadId.equals(routes[2].getRoad())) {
                        List<LatLng> latlngList2 = routes[2].getPointList();
                        latlngList2.add(new LatLng(21.403122, 39.897509));
                        latlngList2.add(new LatLng(21.406625, 39.897501));

                        latlngList2.add(new LatLng(21.412475, 39.900543));
                        latlngList2.add(new LatLng(21.412425, 39.896672));
                        latlngList2.add(new LatLng(21.412830, 39.895086));
                        latlngList2.add(new LatLng(21.413436, 39.893892));
                        latlngList2.add(new LatLng(21.415088, 39.890943));
                        latlngList2.add(new LatLng(21.415050, 39.889953));
                        latlngList2.add(new LatLng(21.414509, 39.889523));
                        latlngList2.add(new LatLng(21.417576, 39.884816));
                        latlngList2.add(new LatLng(21.417774, 39.883690));
                        latlngList2.add(new LatLng(21.418873, 39.881414));
                        latlngList2.add(new LatLng(21.419431, 39.879222));
                        latlngList2.add(new LatLng(21.419395, 39.875359));
                        bindRoute(route3, routes[2]);
                    }

//                    lyt.setVisibility(View.VISIBLE);
                    for (Route r : routes) {
                        if (!r.getPointList().isEmpty()) {

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.addAll(r.getPointList());

                            if (r.getStatus() != null && r.getStatus() == 1) {
                                polylineOptions.color(0xff00ff00);
                                try {
                                    List<LatLng> latLngList = r.getPointList();

                                    BitmapDescriptor ds = BitmapDescriptorFactory.fromResource(R.drawable.ic_empty);
                                    MarkerOptions markerOptions = new MarkerOptions().title(getString(R.string.best_route)).position(latLngList.get(latLngList.size() / 2)).icon(ds);
                                    mMap.addMarker(markerOptions).showInfoWindow();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (r.getStatus() != null && r.getStatus() == 2)
                                polylineOptions.color(0xffffff00);
                            else if (r.getStatus() != null && r.getStatus() == 3)
                                polylineOptions.color(0xffff0000);
                            mMap.addPolyline(polylineOptions);

                        }


                    }


                    try {
//                        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
//
//                        for (Route r : routes) {
//
//                            for (LatLng latLng : r.getPointList()) {
//                                latLngBoundsBuilder.include(latLng);
//
//                            }
//
//                        }

//                        if (lastLocation != null) {
//                            LatLng latLngLastLocation = Utils.fromLocation(lastLocation);
//                            latLngBoundsBuilder.include(latLngLastLocation);
//                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Utils.fromLocation(lastLocation), 3));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.VISIBLE);
                    Toast.makeText(NavigationActivity.this, R.string.error_connectivity, Toast.LENGTH_LONG).show();
                }
            });


        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (fusedLocationProviderClient != null)
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {


        int status = -1;
        switch (view.getId()) {
            case R.id.txt_plus:
                status = 3;
                submitFeedback(status, false);
                break;
            case R.id.txt_tutrle:
                status = 2;
                submitFeedback(status, false);
                break;
            case R.id.txt_good:
                status = 1;
                submitFeedback(status, false);
                break;
            case R.id.txt_fire:
                status = 3;
                submitFeedback(status, false);
                break;
            case R.id.txt_finish_trip:
                status = 1;
                submitFeedback(status, true);
                break;
        }


    }

    private void submitFeedback(int status, boolean finish) {

        PostReport postReportRequest = new PostReport();
        postReportRequest.setNumOfPeople(noOfPeople);
        postReportRequest.setStatus(status);


        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        postReportRequest.setStartTime(sdf.format(new Date(startTimestamp)));
        postReportRequest.setRoadID(roadId);

        if (finish) {
            Long endDate = System.currentTimeMillis();
            String endTime = sdf.format(endDate);
            postReportRequest.setEndTime(endTime);
            double duration = (double) ((endDate - startTimestamp) / 1000 / 60);
            postReportRequest.setDuration(duration);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.you_did_it);
            alertDialogBuilder.setMessage("Along your trip, you have walked for " + duration + " Minute(s).\n You started walking on " + postReportRequest.getStartTime() + " and finished at " + postReportRequest.getEndTime() + ".\n" + "Thanks for using " + getString(R.string.app_name));
            alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NavigationActivity.this.finish();
                }
            });
            alertDialogBuilder.show();
        } else {

            Toast.makeText(this, R.string.thank_you_for_submitting, Toast.LENGTH_LONG).show();

        }


        ServiceConnector.postReport(postReportRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }
}