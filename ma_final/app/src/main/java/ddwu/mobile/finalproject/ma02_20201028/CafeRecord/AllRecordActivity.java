package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import ddwu.mobile.finalproject.ma02_20201028.R;

public class AllRecordActivity extends AppCompatActivity {
    static final String TAG = "AllRecordActivity";
    Cursor cursor;
    RecordDBHelper helper;
    MyCursorAdapter adapter;
    ListView lvList;

    static final int UPDATE_PAGE = 0;
    int updateFlag = RESULT_OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        helper = new RecordDBHelper(this);
        adapter = new MyCursorAdapter(this, R.layout.lv_record, null );

        lvList = (ListView)findViewById(R.id.record_list);
        lvList.setAdapter(adapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long _id) {
                Intent intent = new Intent(AllRecordActivity.this, UpdateRecordActivity.class);
                RecordDTO recordDTO = new RecordDTO();

                recordDTO.set_id(_id);
                recordDTO.setCafeName(cursor.getString(cursor.getColumnIndex(RecordDBHelper.CAFE_NAME)));
                recordDTO.setLocation(cursor.getString(cursor.getColumnIndex(RecordDBHelper.LOCATION)));
                recordDTO.setContent(cursor.getString(cursor.getColumnIndex(RecordDBHelper.CONTENT)));
                recordDTO.setImgPath(cursor.getString(cursor.getColumnIndex(RecordDBHelper.IMG)));
                recordDTO.setDesk(cursor.getString(cursor.getColumnIndex(RecordDBHelper.DESK)));
                recordDTO.setPower(cursor.getInt(cursor.getColumnIndex(RecordDBHelper.POWER)));
                recordDTO.setStar(cursor.getString(cursor.getColumnIndex(RecordDBHelper.STAR)));

                intent.putExtra("recordDTO", (Serializable)recordDTO);
                startActivityForResult(intent, UPDATE_PAGE);
            }
        });

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("Range")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(AllRecordActivity.this);
                builder.setTitle("삭제")
                        .setMessage(cursor.getString(cursor.getColumnIndex(RecordDBHelper.CAFE_NAME))+"를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

                                String whereClause = RecordDBHelper.ID + "=?";
                                String[] whereArgs = new String[]{String.valueOf(id)};
                                int result = sqLiteDatabase.delete(RecordDBHelper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                readDBTable();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(this, AddRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateFlag = resultCode;
        switch (requestCode) {
            case UPDATE_PAGE:
                if (resultCode == RESULT_OK) {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues row = new ContentValues();

                    row.put(helper.CAFE_NAME, data.getStringExtra("et_cafename"));
                    row.put(helper.LOCATION, data.getStringExtra("et_location"));
                    row.put(helper.CONTENT, data.getStringExtra("et_content"));
                    row.put(helper.POWER, data.getIntExtra("rd_power", 0));
                    row.put(helper.DESK, data.getStringExtra("et_desk"));
                    row.put(helper.STAR, data.getStringExtra("rt_star"));
                    row.put(helper.IMG, data.getStringExtra("iv_img"));

                    String whereClause = helper.ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(data.getLongExtra("_id", 0))};

                    int result = db.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                    String msg = result > 0 ? "수정되었습니다!" : "수정 실패했습니다.";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updateFlag == RESULT_OK){
            readDBTable();
        }else if (updateFlag == RESULT_CANCELED) {
            Log.d(TAG, "수정된 데이터가 없습니다.");
        }
    }

    protected void readDBTable(){
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + helper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}

