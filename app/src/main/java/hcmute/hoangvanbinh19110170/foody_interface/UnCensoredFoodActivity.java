package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.FoodAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;

public class UnCensoredFoodActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    ArrayList<Food> arrFood;
    Database database;
    ListView lstfood;
    String user_Role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        init();
        HomeFunc();
    }

    public void init(){
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        user_Role = sharedPreferences.getString("user_Role","");
    }

    public void HomeFunc(){
        lstfood = findViewById(R.id.lstfood);
        FloatingActionButton btnAddFood = (FloatingActionButton) findViewById(R.id.btnAddFood);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnCensoredFoodActivity.this,AddNewFoodActivity.class);
                startActivity(intent);
            }
        });

        arrFood = new ArrayList<>();
        FoodAdapter foodAdapter = new FoodAdapter(UnCensoredFoodActivity.this,
                R.layout.list_item_line,
                arrFood);
        lstfood.setAdapter(foodAdapter);
        try {
            Cursor dataUser=database.GetData("SELECT * from FOOD where status = " + "'false'" );
            while(dataUser.moveToNext()){
                int foodID = dataUser.getInt(0);
                String foodName = dataUser.getString(1);
                byte[] foodImage = dataUser.getBlob(2);
                int quantity = dataUser.getInt(3);
                String description= dataUser.getString(4);
                double price =dataUser.getDouble(5);
                Date timeOpen = stringToDate(dataUser.getString(6),"yyyy-MM-dd");
                Date timeClose = stringToDate(dataUser.getString(7),"yyyy-MM-dd");
                Boolean status = dataUser.getString(8).equals("true");
                int sellerid = dataUser.getInt(9);
                String resAddress=dataUser.getString(10);
                String city =dataUser.getString(11);
                Food newFood = new Food(foodID,foodName,foodImage,quantity,description,price,timeOpen,timeClose
                        ,status,sellerid,resAddress,city);

                arrFood.add(newFood);
            }
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        lstfood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodAdapter.notifyDataSetChanged();
                int foodID = arrFood.get(i).getFoodID();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodId",Integer.toString(foodID));
                editor.commit();
                Intent intent = new Intent(UnCensoredFoodActivity.this,FoodDetailActivity.class);
                startActivity(intent);
            }
        });
        lstfood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    database.QueryData("Update FOOD set status = 'true' where Id="+ Integer.toString(arrFood.get(i).getFoodID()));
                    Toast.makeText(UnCensoredFoodActivity.this, "Approved FOOD: " + arrFood.get(i).getFoodName(), Toast.LENGTH_SHORT).show();
                    arrFood.remove(i);
                    foodAdapter.notifyDataSetChanged();
                }catch (Exception ex)
                {
                    Toast.makeText(UnCensoredFoodActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    public static Date stringToDate(String aDate,String aFormat) {
        try{
            SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
            Date stringDate = simpledateformat.parse(aDate);
            return stringDate;
        }catch (Exception ex)
        {
            return null;
        }

    }
}