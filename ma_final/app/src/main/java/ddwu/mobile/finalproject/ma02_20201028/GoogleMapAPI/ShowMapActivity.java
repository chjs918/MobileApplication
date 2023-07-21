package ddwu.mobile.finalproject.ma02_20201028.GoogleMapAPI;

import static com.google.android.libraries.places.api.model.PlaceTypes.CAFE;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


import ddwu.mobile.finalproject.ma02_20201028.CafeRecord.AddRecordActivity;
import ddwu.mobile.finalproject.ma02_20201028.R;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "ShowMapActivity";
    public static final int MAP_REQUEST_CODE = 100;
    final static int PERMISSION_REQ_CODE = 1;
    final static int DETAIL_SUCCESS_CODE = 2;
    Bitmap bitmap = null;
    LatLng lastLoc;


    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private LocationManager locationManager;
    Location lastLocation;


    ArrayList<Marker> marker_list;
    private String addressOutput = null;
    private String bestProvider;

    private Geocoder geocoder;
    private PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapLoad();
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key) );
        placesClient = Places.createClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        bestProvider = LocationManager.GPS_PROVIDER;
        geocoder = new Geocoder(this, Locale.getDefault());
        marker_list = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermission()){

            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lastLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            searchStart(CAFE);
        }

    }

    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(lastLoc.latitude,lastLoc.longitude)
                .radius(49999)
                .type(type)
                .build()
                .execute();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAIL_SUCCESS_CODE){
            Intent intent = new Intent();
            setResult(MAP_REQUEST_CODE, intent);
        }
    }


    /*Place ID 의 장소에 대한 세부정보 획득*/
    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER,
                Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();

                        // Get the photo metadata.
                        final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                        if (metadata == null || metadata.isEmpty()) {
                            Log.w(TAG, "No photo metadata.");
                            return;
                        }
                        final PhotoMetadata photoMetadata = metadata.get(0);


                        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                                                                .setMaxWidth(500) // Optional.
                                                                                .setMaxHeight(300) // Optional.
                                                                                .build();
                        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                            bitmap = fetchPhotoResponse.getBitmap();
                            callDetailActivity(place);
                        }).addOnFailureListener((exception) -> {
                            if (exception instanceof ApiException) {
                                final ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + exception.getMessage());
                                final int statusCode = apiException.getStatusCode();
                            }
                        });
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ApiException apiException = (ApiException) e;
                        int statusCode = apiException.getStatusCode();
                        Log.e(TAG, "Place not found: " + statusCode + " " + e.getMessage());
                    }
                }
        );
    }

    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesFailure(PlacesException e) {}
        @Override
        public void onPlacesStart() {}
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d(TAG, "Adding Markers");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                        Log.d(TAG, place.getName() + " " + place.getPlaceId());
                    }
                }
            });
        }
        @Override
        public void onPlacesFinished() {}
    };

    private void callDetailActivity(Place place) {
        Intent intent = new Intent(ShowMapActivity.this, MapDetailActivity.class);
        intent.putExtra("place",place);
        startActivityForResult(intent,DETAIL_SUCCESS_CODE);
    }

    public void onMapReady(GoogleMap googleMap) {
        markerOptions = new MarkerOptions();
        mGoogleMap = googleMap;

        Log.d(TAG,"Map ready");

        if(checkPermission()){
            mGoogleMap.setMyLocationEnabled(true);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng lastLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc,17));

            MarkerOptions options = new MarkerOptions();
            options.position(lastLoc);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            Marker marker= mGoogleMap.addMarker(options);
            marker.setPosition(lastLoc);
            marker.showInfoWindow();
        }

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(ShowMapActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(ShowMapActivity.this, String.format("현재위치:(%f, %f)", location.getLatitude(), location.getLongitude()), Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(marker.getTag() == null){
                    List<String> address = getAddress(marker.getPosition().latitude, marker.getPosition().longitude);
                    Toast.makeText(ShowMapActivity.this, address.toString(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ShowMapActivity.this, AddRecordActivity.class);
                    intent.putExtra("address",address.toString());
                    startActivityForResult(intent,DETAIL_SUCCESS_CODE);
                } else {
                    String placeId = marker.getTag().toString();
                    getPlaceDetail(placeId);
                }
            }
        });

        mGoogleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(@NonNull Marker marker) {
                double distance = SphericalUtil.computeDistanceBetween(marker.getPosition(), lastLoc);
                Toast.makeText(ShowMapActivity.this, String.format("현재위치와의 거리:%f m", distance), Toast.LENGTH_SHORT).show();
            }
        });

        //long클릭할 경우 -> 마커 추가
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(getAddress(latLng.latitude,latLng.longitude).toString());
                options.snippet(addressOutput);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                Marker marker= mGoogleMap.addMarker(options);
                marker.setPosition(latLng);
                marker_list.add(marker);
                marker.showInfoWindow();
            }
        });
    }

    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapLoad();
            } else {
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    Geocoding
    private List<String> getAddress(double latitude, double longitude) {

        List<Address> addresses = null;
        ArrayList<String> addressFragments = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<String>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }
        }

        return addressFragments;
    }

}
