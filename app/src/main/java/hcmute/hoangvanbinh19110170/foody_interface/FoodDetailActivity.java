package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;

public class FoodDetailActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView imgview;
    TextView txtprice,txtquantity,txtdescription,txtname,txtShopName,txtShopLocation;
    Database database;
    Button purchase;
    String user_Role;
    ImageButton starSaved;
    String sellerId = "";

    private String shopPhone, shopEmail, shopAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail_layout);

        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        user_Role = sharedPreferences.getString("user_Role","");

        String foodId = sharedPreferences.getString("foodId","");
        String userId = sharedPreferences.getString("userId","");
        txtShopName = findViewById(R.id.txtShopName);
        starSaved = findViewById(R.id.starSaved);
        starSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    database.QueryData("CREATE TABLE IF NOT EXISTS SavedFood(Uid INTEGER references USER(Id) , Fid INTEGER references FOOD(Id))");
                    if(userId.equals(""))
                    {
                        //Yeu cau dang nhap
                        Toast.makeText(FoodDetailActivity.this, "Login to use !", Toast.LENGTH_SHORT).show();
                    }else {
                        Cursor isSavedBefor = database.GetData("Select * from SavedFood where Uid = '" + userId + "' and Fid = '" + foodId + "'");
                        if(isSavedBefor.moveToNext())
                            Toast.makeText(FoodDetailActivity.this, "Food Saved", Toast.LENGTH_SHORT).show();
                        else
                        {
                            database.QueryData("Insert into SavedFood values ("+ userId + ", "+foodId+")");
                            Toast.makeText(FoodDetailActivity.this, "Saved Food SuccessFully", Toast.LENGTH_SHORT).show();
                        }

                    }
                }catch (Exception ex)
                {
                    Toast.makeText(FoodDetailActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgview = findViewById(R.id.detailimgview);
        txtprice = findViewById(R.id.detailprice);
        txtquantity= findViewById(R.id.detailquantity);
        txtdescription = findViewById(R.id.detaildescription);
        txtname = findViewById(R.id.detailname);
        purchase = findViewById(R.id.btnPurchase);
        txtShopLocation = findViewById(R.id.txtShopLocation);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(userId.equals(""))
                {
                    Intent intent = new Intent(FoodDetailActivity.this,SignInActivity.class);
                    startActivity(intent);
                    return;
                }
                database.INSERT_Cart(userId, foodId, 1);
                Toast.makeText(FoodDetailActivity.this, "Added to your cart", Toast.LENGTH_SHORT).show();
            }

        });

        try {
            Cursor dataUser=database.GetData("SELECT * from FOOD where id = " + foodId);
            while(dataUser.moveToNext()){
                txtname.setText(dataUser.getString(1));
                byte[] img = dataUser.getBlob(2);
                txtprice.setText(dataUser.getString(5));
                txtdescription.setText(dataUser.getString(4));
                txtquantity.setText(dataUser.getString(3));
                Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                imgview.setImageBitmap(bitmap);
                sellerId = dataUser.getString(9);
                txtShopLocation.setText(dataUser.getString(10) + ", " + dataUser.getString(11));
                break;
            }
            Cursor shopinfor = database.GetData("Select Name,Email,Phone,Address from User where Id = '" + sellerId + "'");
            while (shopinfor.moveToNext())
            {

                txtShopName.setText(shopinfor.getString(0));
                shopPhone = shopinfor.getString(2);
                shopEmail = shopinfor.getString(1);
                shopAddress = shopinfor.getString(3);
                break;
            }
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
        txtShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodDetailActivity.this,ShopFoodActivity.class);
                intent.putExtra("ShopId",sellerId);
                intent.putExtra("ShopName",txtShopName.getText().toString());
                intent.putExtra("shopPhone",shopPhone);
                intent.putExtra("shopEmail",shopEmail);
                intent.putExtra("shopAddress",shopAddress);

                startActivity(intent);
            }
        });
    }
}