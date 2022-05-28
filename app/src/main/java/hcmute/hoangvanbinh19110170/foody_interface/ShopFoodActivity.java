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
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.FoodAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;

public class ShopFoodActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    Database database;
    String shopId;
    ArrayList<Food> arrFood;
    FoodAdapter foodAdapter;
    ListView lstShopFood;
    TextView txtShopName,txtShopPhone,txtShopAddress,txtShopEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_food_layout);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        shopId = getIntent().getStringExtra("ShopId");
        lstShopFood = findViewById(R.id.lstShopFood);
        txtShopName = findViewById(R.id.txtShopName);
        txtShopAddress = findViewById(R.id.txtShopAddress);
        txtShopPhone = findViewById(R.id.txtShopPhone);
        txtShopEmail = findViewById(R.id.txtShopEmail);
        String shopPhone = getIntent().getStringExtra("shopPhone");
        String shopEmail = getIntent().getStringExtra("shopEmail");
        String shopAddress = getIntent().getStringExtra("shopAddress");
        txtShopName.setText(getIntent().getStringExtra("ShopName"));
        txtShopAddress.setText(shopAddress);
        txtShopEmail.setText(shopEmail);
        txtShopPhone.setText(shopPhone);
        getShopFood();
        lstShopFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int foodID = arrFood.get(i).getFoodID();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodId",Integer.toString(foodID));
                editor.commit();
                Intent intent = new Intent(ShopFoodActivity.this,FoodDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getShopFood(){
        arrFood = new ArrayList<>();
        try {
            Cursor dataUser=database.GetData("SELECT * from FOOD where sellerid = " + shopId + " and status = 'true'");
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
        foodAdapter = new FoodAdapter(ShopFoodActivity.this,
                R.layout.list_item_line,
                arrFood);
        lstShopFood.setAdapter(foodAdapter);
    }
    public static Date stringToDate(String aDate, String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
}