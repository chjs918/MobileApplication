package ddwu.mobile.dbtest.roomexam01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etFood;
    EditText etNation;
    ListView listView;

    ArrayAdapter<Food> adapter;

    FoodDB foodDB;
    FoodDao foodDao;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFood = findViewById(R.id.etFood);
        etNation = findViewById(R.id.etNation);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<Food>(this, android.R.layout.simple_list_item_1, new ArrayList<Food>());
        foodDB = Room.databaseBuilder(this, FoodDB.class, "food_db.db").build();

        //매번 만들지않고 있는지 없는지 검사하고 없으면 생성
        foodDB = FoodDB.getDatabase(this);
        foodDao = foodDB.foodDao();

    }


    public void onClick(View v) {

        final String food = etFood.getText().toString();
        final String nation = etNation.getText().toString();

        switch (v.getId()) {
            case R.id.btnInsert:
                Single<Long> insertResult = foodDao.insertFood(new Food(food, nation));

                insertResult.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result->Log.d(TAG, "Insertion success"+result),
                                throwable -> Log.d(TAG, "error")
                        );
            }
                //                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        foodDao.insertFood(new Food(food, nation));
//                    }
//                }).start();
                break;
            case R.id.btnUpdate:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        foodDao.updateFood(new Food(1, food, nation));
                    }
                }).start();
                break;
            case R.id.btnDelete:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        foodDao.deleteFood(new Food(1, food, nation));
                    }
                }).start();
                break;
            case R.id.btnShow:
                Flowable<List<Food>> resultFoods = foodDao.getAllFoods();

                resultFoods.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                foods -> {
                                    for (Food aFood : foods) {
                                        Log.d(TAG, aFood.toString());
                                    }
                                    adapter.clear();
                                    adapter.addAll(foods);
                                },
                                throwable -> Log.d(TAG,"error", throwable)
                        );
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<Food> foodList = foodDao.getAllFoods();
//                        for(Food afood : foodList){
//                            Log.d(TAG, afood.toString());
//                        }
//                    }
//                }).start();
                break;
        }
    }
}