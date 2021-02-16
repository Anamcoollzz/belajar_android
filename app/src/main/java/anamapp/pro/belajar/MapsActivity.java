package anamapp.pro.belajar;

import anamapp.pro.belajar.helpers.GoogleMapHelper;
import anamapp.pro.belajar.helpers.finder.DirectionFinderHelper;
import anamapp.pro.belajar.helpers.finder.DirectionFinderListener;
import anamapp.pro.belajar.helpers.finder.Route;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static anamapp.pro.belajar.helpers.GoogleMapHelper.buildCameraUpdate;
import static anamapp.pro.belajar.helpers.GoogleMapHelper.getDefaultPolyLines;
import static anamapp.pro.belajar.helpers.GoogleMapHelper.getDottedPolylines;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private enum PolylineStyle {
        DOTTED,
        PLAIN
    }

    private GoogleMap mMap;
    private Marker marker, pickupMarker, destinationMarker;
    private Button hapusMarkerBtn, centerMarkerBtn, animateCameraBtn, addressDetailBtn, drawRouteBtn;
    private FusedLocationProviderClient fusedLocationClient;
    private Activity activity;
    private TextView namaTempatTxt;
    private Polyline polyline;
    private PolylineStyle polylineStyle = PolylineStyle.PLAIN;
    private CardView placeNameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        hapusMarkerBtn = findViewById(R.id.hapus_marker_btn);
        hapusMarkerBtn.setOnClickListener(view -> marker.remove());
        centerMarkerBtn = findViewById(R.id.center_marker_btn);
        centerMarkerBtn.setOnClickListener(view -> {
            LatLng centerPosition = mMap.getCameraPosition().target;
            marker = buildMarker(centerPosition, "Center Position");
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        animateCameraBtn = findViewById(R.id.animate_camera_btn);
        animateCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCamera();
            }
        });
        activity = this;
        addressDetailBtn = findViewById(R.id.address_detail_btn);
        addressDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(activity.getLocalClassName(), mMap.getCameraPosition().target.latitude + "");
                getAddressDetail(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
            }
        });
        placeNameContainer = findViewById(R.id.place_name_container);
        namaTempatTxt = findViewById(R.id.place_name_text);
        setupAutoCompleteFragment();
        hapusMarkerBtn.setVisibility(View.GONE);
        centerMarkerBtn.setVisibility(View.GONE);
        animateCameraBtn.setVisibility(View.GONE);
        addressDetailBtn.setVisibility(View.GONE);
        placeNameContainer.setVisibility(View.GONE);
        setupAutoCompleteDestination();
        drawRouteBtn = findViewById(R.id.draw_route_btn);
        drawRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawRoute();
            }
        });
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
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng kosan = new LatLng(-8.172316, 113.718297);
//        MarkerOptions markerOptions = new MarkerOptions().position(kosan).title("Marker in Sydney");
//        marker = mMap.addMarker(markerOptions);
        LatLng kampusku = new LatLng(-8.165481, 113.717241);
        pickupMarker = buildMarker(kosan, "Pickup");
        destinationMarker = buildMarker(kampusku, "Destination");
        marker = destinationMarker;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kosan));
        fetchDirections(kosan.latitude + "," + kosan.longitude, kampusku.latitude + "," + kampusku.longitude);
        animateCamera();
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, location -> {
//                    // Got last known location. In some rare situations this can be null.
//                    if (location != null) {
//                        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
//                    }
//                });
    }

    private Marker buildMarker(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        if(title.equals("Destination")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker));
        }else if(title.equals("Pickup")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_marker));
        }
        return mMap.addMarker(markerOptions);
    }

    private void animateCamera() {
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18);
        mMap.animateCamera(zoom);
    }

    private void getAddressDetail(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String fullAddress = "";
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                fullAddress += address.getAddressLine(0);
//                if (!address.getPostalCode().equals(""))
//                    fullAddress += address.getPostalCode();
//                if (!address.getPostalCode().equals(""))
//                    fullAddress += address.getPostalCode();
//                if (!address.getPostalCode().equals(""))
//                    fullAddress += address.getPostalCode();
//                if (!address.getPostalCode().equals(""))
//                    fullAddress += address.getPostalCode();
            } else {
                fullAddress = "Tidak ditemukan nama areanya";
            }
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//            Log.d(activity.getLocalClassName(), address);
//            Log.d(activity.getLocalClassName(), city);
//            Log.d(activity.getLocalClassName(), state);
//            Log.d(activity.getLocalClassName(), country);
//            Log.d(activity.getLocalClassName(), postalCode);
//            Log.d(activity.getLocalClassName(), knownName);
            Log.e("addresses", addresses.toString());
            namaTempatTxt.setText(fullAddress);
            ;

        } catch (IOException | NullPointerException e) {
            Log.e(activity.getLocalClassName(), e.getMessage());
        }

    }

    private void setupAutoCompleteFragment() {
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setCountry("ID");

// Set up a PlaceSelectionListener to handle the response.
        String TAG = activity.getLocalClassName();
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                pickupMarker = buildMarker(place.getLatLng(), place.getName());
                namaTempatTxt.setText(place.getAddress());
//                animateCamera();

                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + ", " + place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void setupAutoCompleteDestination() {
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.destination_autocomplete);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setCountry("ID");

// Set up a PlaceSelectionListener to handle the response.
        String TAG = activity.getLocalClassName();
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destinationMarker = buildMarker(place.getLatLng(), place.getName());
//                namaTempatTxt.setText(place.getAddress());
//                animateCamera();

                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + ", " + place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void fetchDirections(String origin, String destination) {
        try {
            new DirectionFinderHelper(this, origin, destination).execute(); // 1
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        if (!routes.isEmpty() && polyline != null) polyline.remove();
        try {
            for (Route route : routes) {
                PolylineOptions polylineOptions = getDefaultPolyLines(route.points);
                if (polylineStyle == PolylineStyle.DOTTED)
                    polylineOptions = getDottedPolylines(route.points);
                polyline = mMap.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred on finding the directions...", Toast.LENGTH_SHORT).show();
        }
//        mMap.animateCamera(buildCameraUpdate(routes.get(0).endLocation), 10, null);
    }

    @Override
    public void onDirectionFinderStart() {

    }

    private void drawRoute() {
        fetchDirections(GoogleMapHelper.mergeLatLng(pickupMarker.getPosition()), GoogleMapHelper.mergeLatLng(destinationMarker.getPosition()));
    }
}
