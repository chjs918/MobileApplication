package mobile.example.dbtest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactDto.class}, version=1, exportSchema = false)
public abstract class ContactDB extends RoomDatabase {
    public abstract ContactDao contactDao();
}
