package hcmute.hoangvanbinh19110170.foody_interface.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.List;

import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Cart;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;
import hcmute.hoangvanbinh19110170.foody_interface.R;

public class CartAdapter extends BaseAdapter {

    Database database;
    Context myContext;
    List<Cart> arrCart;
    int myLayout;

    public CartAdapter(Context myContext, List<Cart> arrCart, int myLayout) {
        this.myContext = myContext;
        this.arrCart = arrCart;
        this.myLayout = myLayout;
    }

    @Override
    public int getCount() {
        return arrCart.size();
    }

    @Override
    public Object getItem(int i) {
        return arrCart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(myLayout, null);
        }
        String foodId = arrCart.get(i).getFoodId();
        //Database
        Database database;
        database = new Database(myContext,"FoodyDB.sqlite",null,1);
        Cursor data = database.GetData("Select foodName, foodImage, price from FOOD where Id =" + foodId);
        byte[] img ;
        Double price = null;
        String foodname;
        TextView txtName = (TextView) view.findViewById(R.id.txtFoodItem);
        ImageView imgItem = (ImageView) view.findViewById(R.id.imgItem);
        while (data.moveToNext())
        {
            img = data.getBlob(1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
            imgItem.setImageBitmap(bitmap);
            foodname = data.getString(0);
            txtName.setText(foodname);
            price = data.getDouble(2);
            break;
        }
//        TextView txtAmount = (TextView) view.findViewById(R.id.txtAmountItem);
//        txtAmount.setText("Amount: ");
        TextView edtAmountItem = (TextView) view.findViewById(R.id.edtAmountItem);
        edtAmountItem.setText(Integer.toString(arrCart.get(i).getAmount()));
        int amount = arrCart.get(i).getAmount();
        TextView txtTotalPrice = (TextView) view.findViewById(R.id.txtPriceItem);
        txtTotalPrice.setText("Price: " + Double.toString(price));
        Button btnup = (Button)view.findViewById(R.id.btnup);
        Button btndown = (Button) view.findViewById(R.id.btndown);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = myContext.getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
                String UserId = sharedPreferences.getString("userId","");
                String amount = Integer.toString(Integer.parseInt(edtAmountItem.getText().toString()) + 1);
                edtAmountItem.setText(amount);
                database.QueryData("Update Cart set amount = " + amount + " where UserId = " +UserId+  " and foodId = " + foodId);
            }
        });
        btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = myContext.getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
                String UserId = sharedPreferences.getString("userId","");
                int amt = Integer.parseInt(edtAmountItem.getText().toString()) - 1;
                if(amt == 0)
                {
                    AlertDialog.Builder dialogXoa = new AlertDialog.Builder(myContext);
                    dialogXoa.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không ?" );
                    int fid = i;
                    dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try{
                                database.QueryData("DELETE from Cart WHERE foodId = '" + arrCart.get(fid).getFoodId() + "' and UserId ='"+ UserId +"'");
                                Toast.makeText(myContext, "Đã xóa xong", Toast.LENGTH_SHORT).show();
                                arrCart.remove(fid);
                            }catch (Exception ex)
                            {
                                Toast.makeText(myContext, "Delete: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialogXoa.show();
                    return;
                }else
                {
                    String amount = Integer.toString(amt);
                    edtAmountItem.setText(amount);
                    database.QueryData("Update Cart set amount = " + amount + " where UserId = " +UserId+  " and foodId = " + foodId);
                }
            }
        });

        return view;

    }
}
