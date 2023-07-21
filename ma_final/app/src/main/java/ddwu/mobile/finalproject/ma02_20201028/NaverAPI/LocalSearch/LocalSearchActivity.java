package ddwu.mobile.finalproject.ma02_20201028.NaverAPI.LocalSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ddwu.mobile.finalproject.ma02_20201028.CafeRecord.AddRecordActivity;
import ddwu.mobile.finalproject.ma02_20201028.NetworkManager;
import ddwu.mobile.finalproject.ma02_20201028.R;

public class LocalSearchActivity extends AppCompatActivity {
    public static final String TAG = "NaverLocalActivity";

    EditText et_search;
    ListView lvList;
    String apiAddress;
    String query;
    NaverLocalAdapter adapter;
    ArrayList<NaverLocalDTO> resultList = null;
    NaverLocalXmlParser parser;
    NetworkManager networkManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localsearch);
        networkManager = new NetworkManager(this);

        et_search = findViewById(R.id.etLsearch);
        lvList = findViewById(R.id.lv_local);

        resultList = new ArrayList();
        adapter = new NaverLocalAdapter(LocalSearchActivity.this, R.layout.lv_local, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url2);
        parser = new NaverLocalXmlParser();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NaverLocalDTO localDTO = resultList.get(position);
                Intent intent = new Intent(getApplicationContext(), AddRecordActivity.class);
                intent.putExtra("title", localDTO.getTitle());
                intent.putExtra("add", localDTO.getRoadAddress());

                startActivity(intent);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_searchl:
                query = et_search.getText().toString();

                try {
                    new NaverAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class NaverAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(LocalSearchActivity.this, "Wait", "Downloading...");
        }


        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = networkManager.downloadNaverContents(address);
            if (result == null) return "Error!";
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);

            resultList = parser.parse(result);

            adapter.setList(resultList);
            adapter.notifyDataSetChanged();

            progressDlg.dismiss();
        }

    }
}