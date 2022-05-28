package hcmute.hoangvanbinh19110170.foody_interface.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.CartAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.FoodDetailActivity;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Cart;
import hcmute.hoangvanbinh19110170.foody_interface.R;
import hcmute.hoangvanbinh19110170.foody_interface.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view ;
    private Database database;
    private SharedPreferences sharedPreferences;
    private ArrayList<Cart> arrCart;
    private CartAdapter cartAdapter;
    private ListView userlstsavedcart;
    private TextView txtYourCart,txtTotalCart;
    private Button btnCheckout;
    private String UserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        sharedPreferences = getActivity().getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
        database = new Database(getActivity(),"FoodyDB.sqlite",null,1);
        UserId = sharedPreferences.getString("userId", "");
        txtYourCart = (TextView) view.findViewById(R.id.txtYourCart);
        userlstsavedcart = (ListView) view.findViewById(R.id.userlstsavedcart);
        txtTotalCart = (TextView)view.findViewById(R.id.txtTotalCart);
        btnCheckout = (Button) view.findViewById(R.id.btnCheckout);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrCart.size() == 0)
                {
                    Toast.makeText(getActivity(), "Bạn không có sản phẩm nào trong Cart !", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(getActivity());
                dialogConfirm.setMessage("Bạn có muốn mua tất cả sản phẩm trong giỏ hàng không ?" );
                dialogConfirm.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CreateInvoice();
                        Toast.makeText(getActivity(), "Confirmed", Toast.LENGTH_SHORT).show();
                        getCartData();
                    }
                });
                dialogConfirm.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialogConfirm.show();
            }
        });
        if(UserId.equals(""))
        {
            txtYourCart.setVisibility(View.VISIBLE);
            txtYourCart.setText("Login to see your cart");
        }else {
            txtYourCart.setVisibility(View.INVISIBLE);
            getCartData();
        }
        return view;
    }

    void CreateInvoice(){
        try {
            //Get all data in Cart
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
            //Khong co cart thi k lam duoc gi
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
            Toast.makeText(getActivity(), err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false)
        {
            UserId = sharedPreferences.getString("userId","");
            if(UserId.equals(""))
            {
                btnCheckout.setVisibility(View.INVISIBLE);
                txtTotalCart.setVisibility(View.INVISIBLE);
                txtYourCart.setVisibility(View.VISIBLE);
                txtYourCart.setText("Login to see your Cart");
                arrCart = new ArrayList<>();
                cartAdapter = new CartAdapter(getActivity(),arrCart,R.layout.row_cart);
                userlstsavedcart.setAdapter(cartAdapter);
            }else {
                btnCheckout.setVisibility(View.VISIBLE);
                txtTotalCart.setVisibility(View.VISIBLE);
                txtYourCart.setVisibility(View.INVISIBLE);
                getCartData();
            }
        }
    }
    private void getCartData(){
        arrCart = new ArrayList<>();
        double total = 0;
        try{
            Cursor cursor = database.GetData("Select * from Cart where UserId = " + UserId);
            while(cursor.moveToNext()){
                String uid = cursor.getString(0);
                String fid = cursor.getString(1);
                int amount = cursor.getInt(2);
                Cart cart = new Cart(uid,fid,amount);
                Cursor food = database.GetData("Select price from FOOD where Id = " + fid);
                while(food.moveToNext())
                {
                    total += food.getDouble(0)*amount;
                    break;
                }
                arrCart.add(cart);
            }
        }catch (Exception ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if(arrCart.size() > 0)
        {
            cartAdapter = new CartAdapter(getActivity(),arrCart,R.layout.row_cart);
            userlstsavedcart.setAdapter(cartAdapter);
            txtTotalCart.setText("Total: "+ String.format("%.2f", total));
        }else
        {
            txtYourCart.setText("You do not have cart");
            txtYourCart.setVisibility(View.VISIBLE);
            arrCart = new ArrayList<>();
            cartAdapter = new CartAdapter(getActivity(),arrCart,R.layout.row_cart);
            userlstsavedcart.setAdapter(cartAdapter);
            txtTotalCart.setText("");

        }
    }
}