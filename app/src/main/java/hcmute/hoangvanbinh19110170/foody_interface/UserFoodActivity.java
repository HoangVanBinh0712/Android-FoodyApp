package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.UserFoodAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;

public class UserFoodActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    Database database;
    //Update
    int day, month, year;
    TextView timeOpen,timeClose;
    DatePickerDialog.OnDateSetListener setListenerOpen,setListenerClose;
    EditText edtprice,edtquantity , edtname, edtdescription, edtresAddress, edtcity;
    ImageView foodImg;
    Button btnUpdate, btnCancel;
    ListView lstfood;
    String userId;
    ArrayList<Food> arrFood;
    UserFoodAdapter foodAdapter;
    String user_Role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_item_layout);

        //Load food cua user
        database = new Database(this,"FoodyDB.sqlite",null,1);
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        user_Role = sharedPreferences.getString("user_Role","");

        FloatingActionButton btnAddFood = (FloatingActionButton)findViewById(R.id.btnAddFood);
        lstfood = findViewById(R.id.userlstfood);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserFoodActivity.this,AddNewFoodActivity.class);
                startActivity(intent);
            }
        });
        userId = sharedPreferences.getString("userId","");

        getDataFood();

        lstfood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int foodID = arrFood.get(i).getFoodID();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodId",Integer.toString(foodID));
                editor.commit();
                Intent intent = new Intent(UserFoodActivity.this,FoodDetailActivity.class);
                startActivity(intent);
            }
        });

        lstfood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogXoa = new AlertDialog.Builder(UserFoodActivity.this);
                dialogXoa.setMessage("Bạn có muốn xóa "+ arrFood.get(i).getFoodName() +" không ?" );
                int fid = i;
                dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            database.QueryData("DELETE from FOOD WHERE Id = '" + Integer.toString(arrFood.get(fid).getFoodID()) + "'");
                            Toast.makeText(UserFoodActivity.this, "Đã xóa xong", Toast.LENGTH_SHORT).show();
                        }catch (Exception ex)
                        {
                            Toast.makeText(UserFoodActivity.this, "Delete: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        getDataFood();
                    }
                });
                dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialogXoa.show();
                return true;            }
        });
    }
    //Getdata food
    public void getDataFood(){
        arrFood = new ArrayList<>();
        try {
            Cursor dataUser=database.GetData("SELECT * from FOOD where sellerid = " + userId);
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
        foodAdapter = new UserFoodAdapter(UserFoodActivity.this,
                R.layout.user_food_line,
                arrFood);
        lstfood.setAdapter(foodAdapter);
    }
    public static Date stringToDate(String aDate, String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
    public void DialgoUpdateFood(Food food){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_food);

        edtprice = (EditText) dialog.findViewById(R.id.addnewfoodPrice);
        edtquantity = (EditText) dialog.findViewById(R.id.addnewfoodQuantity);
        edtname = (EditText) dialog.findViewById(R.id.addnewFoodName);
        edtdescription = (EditText) dialog.findViewById(R.id.addnewfoodDescription);
        timeOpen = (TextView) dialog.findViewById(R.id.timeopen);
        timeClose = (TextView) dialog.findViewById(R.id.timeclose);
        edtresAddress = (EditText) dialog.findViewById(R.id.addnewfoodAddress);
        edtcity = (EditText) dialog.findViewById(R.id.addnewfoodCity);
        foodImg = (ImageView) dialog.findViewById(R.id.addnewfoodImage) ;

        btnUpdate = dialog.findViewById(R.id.btnupdateFood);
        btnCancel = dialog.findViewById(R.id.btncancelFood);

        edtprice.setText(Double.toString(food.getPrice()));
        edtquantity.setText(Integer.toString((food.getQuantity())));
        edtname.setText(food.getFoodName());
        edtdescription.setText(food.getDescription());
        edtresAddress.setText(food.getResAddress());
        edtcity.setText(food.getCity());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String timeOp = dateformat.format(food.getTimeOpen());
        String timeCl = dateformat.format(food.getTimeClose());

        timeOpen.setText(timeOp);
        timeClose.setText(timeCl);
        foodImg.setImageBitmap(BitmapFactory.decodeByteArray(food.getFoodImage(),0,food.getFoodImage().length));

        //Time picker
        timeOpen = dialog.findViewById(R.id.timeopen);
        timeClose = dialog.findViewById(R.id.timeclose);
        timeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserFoodActivity.this, setListenerOpen,year, month,day);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserFoodActivity.this, setListenerClose,year, month,day);
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
        //---------------------


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int foodId = food.getFoodID();
                double foodPrice = Double.parseDouble(edtprice.getText().toString());
                int foodquantity =Integer.parseInt( edtquantity.getText().toString());
                String fooddes = edtdescription.getText().toString();
                String foodName = edtname.getText().toString();
                String time_open = timeOpen.getText().toString();
                String time_close = timeClose.getText().toString();
                String foodAddress = edtresAddress.getText().toString();
                String foodCity = edtcity.getText().toString();
                try{
                    database.UPDATE_FOOD(food.getFoodID(),foodName,food.getFoodImage(),foodquantity,
                            fooddes,foodPrice,time_open,time_close,food.getSellerid(),foodAddress,foodCity);
                    Toast.makeText(UserFoodActivity.this, "Update " +foodName+ " Successfully !", Toast.LENGTH_SHORT).show();

                }catch (Exception ex)
                {
                    Toast.makeText(UserFoodActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                getDataFood();
            }
        });
        dialog.show();
    }

}