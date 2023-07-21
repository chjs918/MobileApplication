package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.libraries.places.api.model.Place;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ddwu.mobile.finalproject.ma02_20201028.R;


public class AddRecordActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;

    private String mCurrentPhotoPath;

    ImageView ivPhoto;
    EditText etCafeName;
    EditText etLocation;
    RatingBar addStar;
    EditText etContent;
    RadioButton rb_yes;
    RadioButton rb_no;
    EditText deskChair;

    File photoFile = null;
    RecordDBHelper helper;
    Place place;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        helper = new RecordDBHelper(this);

        intent = getIntent();

        etCafeName = (EditText)findViewById(R.id.etFocus);
        etLocation = (EditText)findViewById(R.id.etLocation);
        addStar = findViewById(R.id.addStar);
        ivPhoto = (ImageView)findViewById(R.id.iv_photo);
        rb_yes = findViewById(R.id.rb_yes);
        rb_no = findViewById(R.id.rb_no);
        deskChair = (EditText)findViewById(R.id.deskChair);
        etContent = (EditText)findViewById(R.id.etContent);

        ivPhoto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    dispatchTakePictureIntent();
                    return true;
                }
                return false;
            }
        });
    }


    public void savePhoto() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(RecordDBHelper.CAFE_NAME, etCafeName.getText().toString());
        row.put(RecordDBHelper.LOCATION, etLocation.getText().toString());
        row.put(RecordDBHelper.STAR, Float.toString(addStar.getRating()));
        row.put(RecordDBHelper.CONTENT, etContent.getText().toString());
        row.put(RecordDBHelper.DESK, deskChair.getText().toString());
        row.put(RecordDBHelper.POWER, getCheckedType());
        row.put(RecordDBHelper.IMG, mCurrentPhotoPath);
        Log.d("ADD", "path : " + mCurrentPhotoPath);


        long result = db.insert(RecordDBHelper.TABLE_NAME, null, row);
        Log.d("result", String.valueOf(result));
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSave:
                savePhoto();
                helper.close();
                finish();
                Toast.makeText(this, "Save!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                // photoFile.delete();
                finish();
                break;
        }
    }

    /*원본 사진 파일 저장*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            try{
                photoFile = createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.ma02_20201028.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("ADD", mCurrentPhotoPath);
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private int getCheckedType() {
        if(rb_yes.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(intent.getParcelableExtra("place") != null){
            place = intent.getParcelableExtra("place");
            etCafeName.setText(place.getName());
            etLocation.setText(place.getAddress());
        }
        if(intent.getStringExtra("address") != null){
            etLocation.setText(intent.getStringExtra("address"));
        }
        if(intent.getStringExtra("title") != null){
            etCafeName.setText(intent.getStringExtra("title"));
            etLocation.setText(intent.getStringExtra("add"));
        }
    }


    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }

}