package ddwu.mobile.dbtest.roomexam01;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    Single<Long> insertFood(Food food);

    @Update
    Completable updateFood(Food food);

    @Delete
    Completable deleteFood(Food food);

    @Query("SELECT * FROM food_table")
    Flowable<List<Food>> getAllFoods();

    @Query("SELECT * FROM food_table WHERE id = :id")
    Single<Food> getFood(int id);

}
