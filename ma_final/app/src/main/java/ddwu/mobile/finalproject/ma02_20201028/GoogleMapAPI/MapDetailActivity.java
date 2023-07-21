package ddwu.mobile.finalproject.ma02_20201028.GoogleMapAPI;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import ddwu.mobile.finalproject.ma02_20201028.CafeRecord.AddRecordActivity;
import ddwu.mobile.finalproject.ma02_20201028.R;

public class MapDetailActivity extends AppCompatActivity {

    final static String TAG = "MapDetailActivity";
    private static final int MAP_DETAIL = 300;
    private PlacesClient placesClient;
    EditText etName;
    EditText etPhone;
    EditText etAddress;
    Place place;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        placesClient = Places.createClient(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        imageView = findViewById(R.id.imageView);

        place = getIntent().getParcelableExtra("place");

        if (place != null) {
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            final PhotoMetadata photoMetadata = metadata.get(0);
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                imageView.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                }
            });

            etName.setText(place.getName());
            if(etPhone.toString() == null){
                etPhone.setText("정보없음");
            }else {
                etPhone.setText(place.getPhoneNumber());
            }
            etAddress.setText(place.getAddress());

        }
    }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSave:
                    Intent intent = new Intent(this, AddRecordActivity.class);
                    intent.putExtra("place",place);
                    startActivity(intent);

                    finish();
                    break;
                case R.id.btnClose:
                    finish();
                    break;
            }
        }
    }
