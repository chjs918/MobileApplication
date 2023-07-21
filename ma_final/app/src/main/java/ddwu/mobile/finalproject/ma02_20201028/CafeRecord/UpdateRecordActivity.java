package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;


import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.LocationTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


import ddwu.mobile.finalproject.ma02_20201028.R;

public class UpdateRecordActivity extends AppCompatActivity {

    final static String TAG = "UpdateRecordActivity";
    private static final int REQUEST_TAKE_PHOTO = 200;

    ImageView iv_photo;
    EditText etCafeName;
    EditText etLocation;
    RatingBar addStar;
    RadioButton rb_yes;
    RadioButton rb_no;
    EditText deskChair;
    EditText etContent;
    String mCurrentPhotoPath;

    RecordDBHelper helper;
    RecordDTO recordDTO;

    File photoFile = null;
    Bitmap bitmapIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        etCafeName = (EditText) findViewById(R.id.etFocus);
        etLocation = (EditText) findViewById(R.id.etLocation);
        addStar = (RatingBar) findViewById(R.id.addStar);
        rb_yes = findViewById(R.id.rb_yes);
        rb_no = findViewById(R.id.rb_no);
        deskChair = (EditText) findViewById(R.id.deskChair);
        etContent = (EditText) findViewById(R.id.etContent);

        helper = new RecordDBHelper(this);
        recordDTO = (RecordDTO) getIntent().getSerializableExtra("recordDTO");
        setData(recordDTO);

        iv_photo.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
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


    private void setData(RecordDTO recordDTO){
        etCafeName.setText(recordDTO.getCafeName());
        etLocation.setText(recordDTO.getLocation());
        etContent.setText(recordDTO.getContent());
        deskChair.setText(recordDTO.getDesk());
        addStar.setRating((Float.valueOf(recordDTO.getStar())));

        if(recordDTO.getPower() == 1){ // 자리마다 콘센트 있음
            rb_yes.setChecked(true);
            rb_no.setChecked(false);
        }else{ // 콘센트 없음
            rb_yes.setChecked(false);
            rb_no.setChecked(true);
        }

        mCurrentPhotoPath = recordDTO.getImgPath();

        try {
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()) {
                bitmapIMG = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_photo.setImageBitmap(bitmapIMG);
            }
        }catch (Exception ex) {
            iv_photo.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private int getRadioChoice() {
        if(rb_yes.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSave:
                Intent intent = new Intent();
                intent.putExtra("_id",  recordDTO.get_id());
                intent.putExtra("et_cafename", etCafeName.getText().toString());
                intent.putExtra("et_location", etLocation.getText().toString());
                intent.putExtra("et_content", etContent.getText().toString());
                intent.putExtra("iv_img", mCurrentPhotoPath);
                intent.putExtra("et_desk", deskChair.getText().toString());
                intent.putExtra("rd_power", getRadioChoice());
                intent.putExtra("rt_star", Float.toString(addStar.getRating()));

                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_share:
                shareKakao();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // 파일을 정상 생성하였을 경우
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.ma02_20201028.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "Created file path: " + mCurrentPhotoPath);
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {
        int targetW = iv_photo.getWidth();
        int targetH = iv_photo.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        iv_photo.setImageBitmap(bitmap);
    }

    public void shareKakao() {
        LocationTemplate params = LocationTemplate.newBuilder(etLocation.getText().toString(),
                        ContentObject.newBuilder("카공하기 좋은 카페 추천 : " + etCafeName.getText().toString(),
                                        "http://www.kakaocorp.com/images/logo/og_daumkakao_151001.png",
                                        LinkObject.newBuilder()
                                                .setWebUrl("https://developers.kakao.com")
                                                .setMobileWebUrl("https://developers.kakao.com")
                                                .build())
                                                .setDescrption("내용 : " + etContent.getText().toString())
                                                .build())
                                                .setAddressTitle("카페 찾기")
                                                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }
            @Override
            public void onSuccess(KakaoLinkResponse result) { }
        });
    }
}
