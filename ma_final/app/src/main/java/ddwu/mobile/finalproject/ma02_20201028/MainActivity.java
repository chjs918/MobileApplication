package ddwu.mobile.finalproject.ma02_20201028;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ddwu.mobile.finalproject.ma02_20201028.CafeRecord.AddRecordActivity;
import ddwu.mobile.finalproject.ma02_20201028.CafeRecord.AllRecordActivity;
import ddwu.mobile.finalproject.ma02_20201028.GoogleMapAPI.ShowMapActivity;
import ddwu.mobile.finalproject.ma02_20201028.NaverAPI.LocalSearch.LocalSearchActivity;
import ddwu.mobile.finalproject.ma02_20201028.NaverAPI.SearchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btn_record:
                intent = new Intent(this, AllRecordActivity.class);
                break;
            case R.id.btn_write:
                intent = new Intent(this, AddRecordActivity.class);
                break;
            case R.id.btn_search:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.btn_map:
                intent = new Intent(this, ShowMapActivity.class);
                break;
            case R.id.btn_localsearch:
                intent = new Intent(this, LocalSearchActivity.class);
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }
}