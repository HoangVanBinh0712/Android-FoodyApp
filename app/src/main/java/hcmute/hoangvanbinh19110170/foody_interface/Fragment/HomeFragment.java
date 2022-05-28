package hcmute.hoangvanbinh19110170.foody_interface.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.FoodAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Adapter.ShopAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.AddNewFoodActivity;
import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.FoodDetailActivity;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;
import hcmute.hoangvanbinh19110170.foody_interface.Models.User;
import hcmute.hoangvanbinh19110170.foody_interface.R;
import hcmute.hoangvanbinh19110170.foody_interface.ShopFoodActivity;
import hcmute.hoangvanbinh19110170.foody_interface.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private SharedPreferences sharedPreferences;
    private Database database;
    private FoodAdapter foodAdapter;
    ArrayList<Food> arrayFood ;
    ArrayList<User> arrUser;
    private View rootView;
    private FloatingActionButton btnAddFood;
    private String user_Role;
    private EditText edtSearch;
    private ImageView imgSearch;
    private ListView lstfood,lstseller;
    private ShopAdapter shopAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
        user_Role = sharedPreferences.getString("user_Role","");


        AnhXa();

        return rootView;
    }


    private void AnhXa(){
        btnAddFood = (FloatingActionButton) rootView.findViewById(R.id.btnAddFood);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_Role.equals("1")  || user_Role.equals("2"))
                {
                    Intent intent = new Intent(getActivity(), AddNewFoodActivity.class);
                    startActivity(intent);
                }else if(user_Role.equals("0")){
                    Toast.makeText(getActivity(), "Seller or Admin only", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
            }
        });
        imgSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        edtSearch = (EditText) rootView.findViewById(R.id.edtSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = edtSearch.getText().toString();
                if(!search.equals("")) {
                    try {
                        arrayFood = new ArrayList<>();
                        Cursor searchFood = database.GetData("Select * from FOOD where foodName LIKE '%" + search.trim() + "%' and status = 'true'");
                        while (searchFood.moveToNext()) {
                            int foodID = searchFood.getInt(0);
                            String foodName = searchFood.getString(1);
                            byte[] foodImage = searchFood.getBlob(2);
                            int quantity = searchFood.getInt(3);
                            String description = searchFood.getString(4);
                            double price = searchFood.getDouble(5);
                            Date timeOpen = stringToDate(searchFood.getString(6), "yyyy-MM-dd");
                            Date timeClose = stringToDate(searchFood.getString(7), "yyyy-MM-dd");
                            Boolean status = searchFood.getString(8).equals("true");
                            int sellerid = searchFood.getInt(9);
                            String resAddress = searchFood.getString(10);
                            String city = searchFood.getString(11);
                            Food newFood = new Food(foodID, foodName, foodImage, quantity, description, price, timeOpen, timeClose
                                    , status, sellerid, resAddress, city);

                            arrayFood.add(newFood);
                        }
                        foodAdapter = new FoodAdapter(getActivity(), R.layout.list_item_line,arrayFood);
                        lstfood.setAdapter(foodAdapter);
                    }catch (Exception ex)
                    {
                        Toast.makeText(getActivity(), "Internal Sever Eoor", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    getFood();
                }
            }
        });
        lstfood = (ListView) rootView.findViewById(R.id.lstfood);
        lstseller = (ListView) rootView.findViewById(R.id.lstseller);
        getFood();
        getSeller();
        lstfood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodAdapter.notifyDataSetChanged();
                int foodID = arrayFood.get(i).getFoodID();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodId",Integer.toString(foodID));
                editor.commit();
                Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
             //   intent.putExtra("Food",  arrayFood.get(i));
                startActivity(intent);
            }
        });
        lstseller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ShopFoodActivity.class);
                intent.putExtra("ShopId",Integer.toString(arrUser.get(i).getUserID()));
                intent.putExtra("ShopName",arrUser.get(i).getName());
                intent.putExtra("shopPhone",arrUser.get(i).getPhone());
                intent.putExtra("shopEmail",arrUser.get(i).getEmail());
                intent.putExtra("shopAddress",arrUser.get(i).getAddress());
                startActivity(intent);
            }
        });
    }
    private void getSeller(){
        arrUser = new ArrayList<>();
        //lstseller
        database = new Database(getActivity(),"FoodyDB.sqlite",null,1);
        try {
            Cursor dataUser=database.GetData("SELECT * from User where role = 1" );
            while(dataUser.moveToNext()){
               int id = dataUser.getInt(0);
               String name = dataUser.getString(1);
               String password= dataUser.getString(2);
               Date birth = stringToDate(dataUser.getString(3),"yyyy-MM-dd");
               String address= dataUser.getString(4);
               String email =dataUser.getString(5);
               String Phone = dataUser.getString(6);
               int role = dataUser.getInt(7);
               User user = new User(id,name,birth,address,email,Phone,role,null);

               arrUser.add(user);


            }
            shopAdapter = new ShopAdapter(getActivity(),R.layout.line_seller_item,arrUser);
            lstseller.setAdapter(shopAdapter);

        }catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void getFood(){
        arrayFood = new ArrayList<>();
        database = new Database(getActivity(),"FoodyDB.sqlite",null,1);
        try {
            Cursor dataUser=database.GetData("SELECT * from FOOD where status = " + "'true'" );
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

                arrayFood.add(newFood);
                foodAdapter = new FoodAdapter(getActivity(), R.layout.list_item_line,arrayFood);
                lstfood.setAdapter(foodAdapter);
            }
        }catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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