package mobile.example.dbtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.room.Room;

public class InsertContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etName;
	EditText etPhone;
	EditText etCategory;
	EditText etAddress;

	private ContactDB contactDB;
	private ContactDao contactDao;

	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

//      DBHelper 생성
		helper = new ContactDBHelper(this);
		
		etName = (EditText)findViewById(R.id.editText1);
		etPhone = (EditText)findViewById(R.id.editText2);
		etCategory = (EditText)findViewById(R.id.editText3);
		etAddress = (EditText)findViewById(R.id.editText4);

		contactDB = Room.databaseBuilder(getApplicationContext(), ContactDB.class, "contact_db.db")
				.build();
		contactDao = contactDB.contactDao();
	}
	
	
	public void onClick(View v) {
		final String name = etName.getText().toString();
		final String phone = etPhone.getText().toString();
		final String category = etCategory.getText().toString();
		final String address = etAddress.getText().toString();

		contactDao.insertContactDto(new ContactDto(name, phone, category, address));

	}
	

}
