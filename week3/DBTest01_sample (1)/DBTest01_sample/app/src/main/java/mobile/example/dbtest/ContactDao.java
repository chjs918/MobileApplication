package mobile.example.dbtest;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    long insertContactDto(ContactDto contactDto);

    @Update
    int updateContactDto(ContactDto contactDto);

    @Delete
    void deleteContactDto(ContactDto contactDto);

    @Query("SELECT * FROM Contact_table")
    List<ContactDto> getAllContacts();
}
