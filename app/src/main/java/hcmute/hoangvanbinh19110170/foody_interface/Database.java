package hcmute.hoangvanbinh19110170.foody_interface;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Models.User;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Truy van khong tra ket qua: insert, update, delete, create
    public void QueryData(String sql)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }
    //Truy van  tra ket qua: select

    public Cursor GetData(String sql){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }

    //User
    //  database.QueryData("INSERT INTO User VALUES(null,'Binh','"+ getDateTime()+"',
    //  'So 18','thebest11447@gmail.com','0337445596','2',null)");

    public void INSERT_Invoice(String UserId,double total){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        //Format cai ngay nay thanh yyyy-mm-dd HH-mm-ss roi qua ben kia luc ma hien len thi
        //format lai la xong cai nay t lam nhanh k co format
        SQLiteDatabase db= getWritableDatabase();
        String sql = "INSERT INTO INVOICE VALUES(null,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,UserId);
        statement.bindDouble(2,total);
        statement.bindString(3, date);
        statement.executeInsert();
    }
    public void INSERT_InvoiceDetail(String InvoiceId, String foodId,double price,int amount, double total, String UserId){
        SQLiteDatabase db= getWritableDatabase();
        String sql = "INSERT INTO INVOICE_DETAIL VALUES(?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,InvoiceId);
        statement.bindString(2,foodId);
        statement.bindDouble(3,price);
        statement.bindDouble(4,amount);
        statement.bindDouble(5,total);
        statement.executeInsert();
    }
    public void INSERT_Cart(String UserId, String foodId,int amount)
    {
        Cursor cs = GetData("Select * from Cart where UserId = '" + UserId + "' and foodId = '" + foodId +"'");
        if(cs.moveToNext())
        {
            SQLiteDatabase db= getWritableDatabase();
            String sql = "Update Cart set amount = ? where UserId = ? and foodId = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(2,UserId);
            statement.bindString(3,foodId);
            statement.bindDouble(1,amount + cs.getInt(2));
            statement.executeInsert();
            return;
        }
        SQLiteDatabase db= getWritableDatabase();
        String sql = "INSERT INTO Cart VALUES(?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,UserId);
        statement.bindString(2,foodId);
        statement.bindDouble(3,amount);
        statement.executeInsert();
    }

    public void INSERT_User(String name,String passWord,String birth, String address,String email,String phone,String role)
    {
        SQLiteDatabase db= getWritableDatabase();
        String sql = "INSERT INTO User VALUES(null,?,?,?,?,?,?,?,null)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,passWord);
        statement.bindString(3,birth);
        statement.bindString(4,address);
        statement.bindString(5,email);
        statement.bindString(6,phone);
        statement.bindString(7,role);
        statement.executeInsert();



    }
    public void INSERT_FOOD(String foodName, byte[] foodImage, int quantity, String description,
                            double price, String timeOpen, String timeClose, Boolean status, int sellerid, String resAddress, String city){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO FOOD VALUES(null,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,foodName);
        statement.bindBlob(2,foodImage);
        statement.bindDouble(3,quantity);
        statement.bindString(4,description);
        statement.bindDouble(5,price);
        statement.bindString(6,timeClose);
        statement.bindString(7,timeOpen);
        statement.bindString(8,status.toString());
        statement.bindDouble(9,sellerid);
        statement.bindString(10,resAddress);
        statement.bindString(11,city);

        statement.executeInsert();

    }
    public void UPDATE_FOOD(int Id,String foodName, byte[] foodImage, int quantity, String description,
                            double price, String timeOpen, String timeClose, int sellerid, String resAddress, String city){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE FOOD SET foodName = ?, foodImage = ?, quantity = ?," +
                "description = ?, price = ?, timeOpen = ?, timeClose = ?," +
                "sellerid = ?,resAddress = ?, city = ? WHERE Id = ?";
        //insert into user value(null,'binh','2001-12-7')

        //insert into user value(null,?,?)
        //bind 1: binh
        //binhd 2: 2001-12-7
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,foodName);
        statement.bindBlob(2,foodImage);
        statement.bindDouble(3,quantity);
        statement.bindString(4,description);
        statement.bindDouble(5,price);
        statement.bindString(6,timeOpen);
        statement.bindString(7,timeClose);
        statement.bindDouble(8,sellerid);
        statement.bindString(9,resAddress);
        statement.bindString(10,city);
        statement.bindDouble(11,Id);
        statement.executeUpdateDelete();

    }

    public void INSERT_PURCHASE(String UserId,String foodId,String foodName, int quantity, double price, double total,
                                String timeOrder, String timeTake, String resAddress, String Type_Order){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO PURCHASE VALUES(null,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, UserId);
        statement.bindString(2,foodId);
        statement.bindString(3,foodName);
        statement.bindDouble(4,quantity);
        statement.bindDouble(5,price);
        statement.bindDouble(6,total);
        statement.bindString(7,timeOrder);
        statement.bindString(8,timeTake);
        statement.bindString(9,resAddress);
        statement.bindString(10,Type_Order);
        statement.executeInsert();

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
