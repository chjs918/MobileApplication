package mobile.example.dbtest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="Contact_table")
public class ContactDto {

	@PrimaryKey(autoGenerate = true)
	private long id;

	@ColumnInfo(name="name")
	private String name;
	@ColumnInfo(name="phone")
	private String phone;
	@ColumnInfo(name="category")
	private String category;
	@ColumnInfo(name="address")
	private String address;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ContactDto(String name, String phone, String category, String address) {
		this.name = name;
		this.phone = phone;
		this.category = category;
		this.address = address;
	}

	public ContactDto(long id, String name, String phone, String category, String address) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.category = category;
		this.address = address;
	}

	@Override
	public String toString() {
		return id + ". " + category + " - " + name + " (" + phone + ")";
	}
	
}
