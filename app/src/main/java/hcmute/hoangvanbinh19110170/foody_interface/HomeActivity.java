package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import hcmute.hoangvanbinh19110170.foody_interface.Fragment.AccountFragment;
import hcmute.hoangvanbinh19110170.foody_interface.Fragment.CartFragment;
import hcmute.hoangvanbinh19110170.foody_interface.Fragment.HistoryFragment;
import hcmute.hoangvanbinh19110170.foody_interface.Fragment.HomeFragment;
import hcmute.hoangvanbinh19110170.foody_interface.Fragment.YourFoodFragment;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Cart;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    HistoryFragment historyFragment = new HistoryFragment();
    AccountFragment accountFragment = new AccountFragment();
    HomeFragment homeFragment = new HomeFragment();
    YourFoodFragment yourFoodFragment = new YourFoodFragment();
    CartFragment cartFragment = new CartFragment();
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home_layout);
        database = new Database(this,"FoodyDB.sqlite",null,1);

        userTable();
        foodTable();
        cartTable();
        invoiceTable();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.flFragment,accountFragment);
        fragmentTransaction.add(R.id.flFragment,homeFragment);
        fragmentTransaction.add(R.id.flFragment,historyFragment);
        fragmentTransaction.add(R.id.flFragment,yourFoodFragment);
        //cart
        fragmentTransaction.add(R.id.flFragment,cartFragment);
        fragmentTransaction.commit();

        bottomNavigationView.setSelectedItemId(R.id.homepage);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.account:
                fragmentTransaction.show(accountFragment);
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(historyFragment);
                fragmentTransaction.hide(yourFoodFragment);
                fragmentTransaction.hide(cartFragment);
                fragmentTransaction.commit();
                return true;

            case R.id.homepage:
                fragmentTransaction.show(homeFragment);
                fragmentTransaction.hide(accountFragment);
                fragmentTransaction.hide(historyFragment);
                fragmentTransaction.hide(yourFoodFragment);
                fragmentTransaction.hide(cartFragment);
                fragmentTransaction.commit();
                return true;

            case R.id.history:
                fragmentTransaction.show(historyFragment);
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(accountFragment);
                fragmentTransaction.hide(yourFoodFragment);
                fragmentTransaction.hide(cartFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.yourfoods:
                fragmentTransaction.show(yourFoodFragment);
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(historyFragment);
                fragmentTransaction.hide(accountFragment);
                fragmentTransaction.hide(cartFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.cartpage:
                fragmentTransaction.show(cartFragment);
                fragmentTransaction.hide(yourFoodFragment);
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(historyFragment);
                fragmentTransaction.hide(accountFragment);
                fragmentTransaction.commit();
            default:
                return true;
        }
    }

    public void cartTable(){
        try {
            database.QueryData("CREATE TABLE IF NOT EXISTS Cart(UserId int references User(Id), foodId int references FOOD(Id), amount int)");

//            //Insert Cart
//            database.INSERT_Cart("1","13",3);
//            database.INSERT_Cart("1","13",2);
//            database.INSERT_Cart("1","2",3);
//            database.INSERT_Cart("1","2",1);
//
//            //Get cart
//            String UserId = "1";//
//            Cursor cart_detail = database.GetData("Select * from cart where UserId = '" + UserId + "'");
//            ArrayList<Cart> arrCart = new ArrayList<>();
//            while(cart_detail.moveToNext())
//            {
//                String uid = cart_detail.getString(0);
//                String fid = cart_detail.getString(1);
//                int amount = cart_detail.getInt(2);
//                Cart cart = new Cart(uid,fid,amount);
//                arrCart.add(cart);
//            }
//            Log.d("TAGDDD", "cartTable: " + arrCart.size());

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void invoiceTable(){
        try {
            database.QueryData("CREATE TABLE IF NOT EXISTS INVOICE(Id INTEGER PRIMARY KEY AUTOINCREMENT,UserId int references User(Id),total double,dayOrder DATETIME)");
            database.QueryData("CREATE TABLE IF NOT EXISTS INVOICE_DETAIL(InvoiceId int references INVOICE(Id),foodId int references FOOD(Id),price double,amount int,total double)");

            //CreateInvoice();


        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void CreateInvoice(){
        try {
            //Get all data in Cart
            String UserId = "1";
            Cursor cart_detail = database.GetData("Select * from cart where UserId = '" + UserId + "'");
            ArrayList<Cart> arrCart = new ArrayList<>();
            double total = 0;
            while(cart_detail.moveToNext())
            {
                String uid = cart_detail.getString(0);
                String fid = cart_detail.getString(1);
                int amount = cart_detail.getInt(2);
                Cart cart = new Cart(uid,fid,amount);
                arrCart.add(cart);
                double FoodPrice = 0;
                //Get food Price
                Cursor dataUser=database.GetData("SELECT * from FOOD where id = " + fid);
                while(dataUser.moveToNext()){
                    FoodPrice = dataUser.getDouble(5);
                    break;
                }
                //Caculate total
                total += FoodPrice * amount;

            }
            if (arrCart.size() == 0)
                return;
            //Add Invoice
            database.INSERT_Invoice(UserId,total);
            //Add Invoice Detail
            //Get Id of Invoice Just Create
            Cursor invoice = database.GetData("Select Id from INVOICE Where UserId = "+ UserId + " ORDER BY Id DESC LIMIT 1 ");
            String invoiceId = "";
            while (invoice.moveToNext())
            {
                invoiceId = invoice.getString(0);
                break;
            }
            for(int i=0;i<arrCart.size();i++)
            {
                double foodPrice=0;
                String foodId = arrCart.get(i).getFoodId();
                int amount = arrCart.get(i).getAmount();
                //Get foodPrice of foodId in Cart
                Cursor dataUser=database.GetData("SELECT * from FOOD where id = " + foodId);
                while(dataUser.moveToNext()){
                    foodPrice = dataUser.getDouble(5);
                    break;
                }
                database.INSERT_InvoiceDetail(invoiceId,foodId,foodPrice,amount,
                        foodPrice*amount,UserId);
                database.QueryData("Delete FROM Cart where UserId = '" + UserId + "' and foodId = '"+ foodId +"'");
            }
        }catch (Exception err)
        {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public  void foodTable(){
        try {
            database.QueryData("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT, foodName VARCHAR(50),foodImage BLOB, quantity int, description VARCHAR(1000),price double,timeOpen DATETIME,timeClose DATETIME, status Boolean,sellerid int references USER(Id), resAddress VARCHAR(200), city VARCHAR(50))");
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public  void userTable() {
        try {
            database.QueryData("CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(50),Password varchar(50),Birth DATETIME, Address VARCHAR(200), Email VARCHAR(50),Phone VARCHAR(10),Role int, Auth VARCHAR(200) )");
            Cursor cursor = database.GetData("Select * from User where id = 1");
            if(!cursor.moveToNext())
                database.QueryData("Insert into User values (null,'Admin','123456','2001-12-7','Số 1 Võ Văn Ngân','spkt@gmail.com','0123456789',2,'')");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}