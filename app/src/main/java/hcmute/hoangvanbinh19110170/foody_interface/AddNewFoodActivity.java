package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddNewFoodActivity extends AppCompatActivity {

    //Add new food
    private static final int PICK_IMAGE = 100;
    SharedPreferences sharedPreferences ;
    int day, month, year;
    TextView timeOpen, timeClose;
    Button btnAddFood;
    ImageView addnewfoodImage;
    Database database;
    EditText addnewfoodPrice,addnewfoodQuantity,addnewfoodName,addnewfoodDescription,addnewfoodAddress,addnewfoodCity;
//------------------------------------------------------------------------------------
    DatePickerDialog.OnDateSetListener setListenerOpen,setListenerClose;
    String user_Role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_food);
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        user_Role = sharedPreferences.getString("user_Role","");

        AddnewFood();
    }

    public void AddnewFood(){
        btnAddFood = findViewById(R.id.btnAddFood);
        addnewfoodImage = findViewById(R.id.addnewfoodImage);
        addnewfoodPrice  = findViewById(R.id.addnewfoodPrice);
        addnewfoodQuantity = findViewById(R.id.addnewfoodQuantity);
        addnewfoodName  = findViewById(R.id.addnewFoodName);
        addnewfoodDescription  = findViewById(R.id.addnewfoodDescription);

        timeOpen = findViewById(R.id.timeopen);
        timeClose = findViewById(R.id.timeclose);
        timeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewFoodActivity.this, setListenerOpen,year, month,day);
                datePickerDialog.show();
            }
        });
        timeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewFoodActivity.this, setListenerClose,year, month,day);
                datePickerDialog.show();
            }
        });
        setListenerOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month+1;
                String date = year+"-"+month+"-"+dayofMonth;
                timeOpen.setText(date);
            }
        };
        setListenerClose = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month+1;
                String date = year+"-"+month+"-"+dayofMonth;
                timeClose.setText(date);
            }
        };

        addnewfoodAddress = findViewById(R.id.addnewfoodAddress);
        addnewfoodCity  = findViewById(R.id.addnewfoodCity);
        addnewfoodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,PICK_IMAGE);

            }
        });
        if(sharedPreferences.getString("userId","")!=null)
        {
            String uid = sharedPreferences.getString("userId","").toString();
            int userId = Integer.parseInt(uid);
            int finalUserId = userId;
            btnAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        database.INSERT_FOOD(addnewfoodName.getText().toString(),ImageView_To_Byte(),Integer.parseInt(addnewfoodQuantity.getText().toString()),addnewfoodDescription.getText().toString(),Double.parseDouble(addnewfoodPrice.getText().toString()),timeOpen.getText().toString(),timeClose.getText().toString(),false, finalUserId,addnewfoodAddress.getText().toString(),addnewfoodCity.getText().toString());
                        Toast.makeText(AddNewFoodActivity.this, "Insert Successfully" , Toast.LENGTH_SHORT).show();
                        AddNewFoodActivity.super.onBackPressed();

                    }catch (Exception e)
                    {
                        Toast.makeText(AddNewFoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
            if(requestCode==PICK_IMAGE)
                addnewfoodImage.setImageURI(data.getData());
    }

    public byte[] ImageView_To_Byte(){
        BitmapDrawable drawable = (BitmapDrawable) addnewfoodImage.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,30,stream);
        byte[] bytearray = stream.toByteArray();
        return bytearray;
    }
}