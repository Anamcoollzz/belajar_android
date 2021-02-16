package anamapp.pro.belajar.maps;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.MyActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

public class PlaceAutoCompleteCustomActivity extends FragmentActivity implements OnMapReadyCallback, MyActivity {

    //    Init additional var
    String API_KEY;
    PlacesClient placesClient;
    private GoogleMap mMap;
    private StringBuilder mResult;
    private String TAG = "PlaceAutoCompleteCustomActivity";
    String COUNTRY_CODE = "ID"; // SCOPE MAPS PLACE

    //    init view
    Button searchPlaceButton;
    AutoCompleteTextView autoCompleteTextView;
    TextView searchResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_auto_complete_custom);
        initVar();
        initMap();
        initView();
        initAction();
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }
        // Create a new Places client instance.
        placesClient = Places.createClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void initView() {
        searchPlaceButton = findViewById(R.id.search_place_button);
        autoCompleteTextView = findViewById(R.id.autocomplete_text);
        searchResultText = findViewById(R.id.search_result_text);
    }

    @Override
    public void initAction() {
        searchPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPlace();
            }
        });
    }

    @Override
    public void initVar() {

        API_KEY = getString(R.string.google_maps_key);
    }

    void fetchPlace() {
        String query = autoCompleteTextView.getText().toString();
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        // Create a RectangularBounds object.
//        RectangularBounds bounds = RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.184363), //dummy lat/lng
//                new LatLng(-33.858754, 151.229596));

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry(COUNTRY_CODE)//Nigeria
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build();


        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            mResult = new StringBuilder();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                mResult.append(" ").append(prediction.getFullText(null) + "\n");
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
//                Log.i(TAG, prediction.get)
                Toast.makeText(PlaceAutoCompleteCustomActivity.this, prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_SHORT).show();
            }
            searchResultText.setText(String.valueOf(mResult));
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }
}
