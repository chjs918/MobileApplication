package mobile.example.dbtest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.room.Room;

public class AllContactsActivity extends Activity {
	
	private ListView lvContacts = null;

	private ArrayAdapter<ContactDto> adapter;
	private ContactDBHelper helper;
	private ArrayList<ContactDto> contactList;

	private ContactDB contactDB;
	private ContactDao contactDao;
	private Intent intent;
	private final static String UPDATE_CODE = "updateItem";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);

		helper = new ContactDBHelper(this);
		contactList = new ArrayList<ContactDto>();

		lvContacts = (ListView) findViewById(R.id.lvContacts);
		adapter = new ArrayAdapter<ContactDto>(this, android.R.layout.simple_list_item_1, contactList);

		lvContacts.setAdapter(adapter);

		contactDB = Room.databaseBuilder(getApplicationContext(), ContactDB.class, "contact_db.db")
				.build();
		contactDao = contactDB.contactDao();


		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()  {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				contactDao.deleteContactDto(InsertContactActivity.name, getPhone(), get );
				return false;
	}
	});

		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactDto upItem = new ContactDto();
				upItem = (ContactDto)adapter.getItem(position);

				intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
				intent.putExtra(upItem);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		

	}

}




