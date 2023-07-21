package ddwu.mobile.finalproject.ma02_20201028.NaverAPI;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ddwu.mobile.finalproject.ma02_20201028.NetworkManager;
import ddwu.mobile.finalproject.ma02_20201028.R;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    EditText etInputCafe;
    ListView lvList;
    String apiAddress;

    String query;
    NaverBlogAdapter adapter;
    ArrayList<NaverBlogDTO> resultList;
    NaverSearchXmlParser parser;
    NetworkManager networkManager;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etInputCafe = findViewById(R.id.etInputCafe);
        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        adapter = new NaverBlogAdapter(this,R.layout.iv_search, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new NaverSearchXmlParser();
        networkManager = new NetworkManager(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                NaverBlogDTO blogDTO = resultList.get(pos);
                if(blogDTO.getLink() != "정보 없음") {
                    String link = blogDTO.getLink();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                }
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_search:
                if (!networkManager.isOnline()) {
                    Toast.makeText(SearchActivity.this, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                query = etInputCafe.getText().toString();

                try {
                    new NaverAsyncTask().execute(apiAddress, query);
                } catch (Exception e) {
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
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String query = strings[1];

            String apiURL = null;
            try {
                apiURL = address + URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String result = networkManager.downloadContents(apiURL);
            Log.d(TAG,result);
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            progressDlg.dismiss();
            ArrayList<NaverBlogDTO> parseredList = parser.parse(result);
            if (parseredList.size() == 0) {
                Toast.makeText(SearchActivity.this, "No data!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG + "List", String.valueOf(parseredList.get(0)));
                resultList = parseredList;
                adapter.setList(resultList);
                progressDlg.dismiss();
            }
        }
    }

}