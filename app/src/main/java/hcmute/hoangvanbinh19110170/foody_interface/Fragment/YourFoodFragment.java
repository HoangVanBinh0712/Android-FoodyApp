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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.FoodAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.FoodDetailActivity;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;
import hcmute.hoangvanbinh19110170.foody_interface.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourFoodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourFoodFragment newInstance(String param1, String param2) {
        YourFoodFragment fragment = new YourFoodFragment();
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
    private ListView lstSavedFood;
    private View view;
    private ArrayList<Food> arrFood;
    private String userId;
    FoodAdapter foodAdapter;
    private TextView txtfavoriteFood;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_your_food, container, false);;
        sharedPreferences = getActivity().getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
        userId = sharedPreferences.getString("userId","");
        lstSavedFood = (ListView) view.findViewById(R.id.userlstsavedfood);
        txtfavoriteFood =(TextView) view.findViewById(R.id.txtfavoriteFood);
        if(userId.equals(""))
        {
            txtfavoriteFood.setText("Login to view");
            txtfavoriteFood.setVisibility(View.VISIBLE);
        }else {
            txtfavoriteFood.setVisibility(View.INVISIBLE);
            getSavedFood();
        }

        lstSavedFood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogXoa = new AlertDialog.Builder(getActivity());
                dialogXoa.setMessage("Bạn có muốn xóa "+ arrFood.get(i).getFoodName() +" khỏi danh sách yêu thích không ?" );
                int fid = i;
                dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            database.QueryData("DELETE from SavedFood WHERE Fid = '" + Integer.toString(arrFood.get(fid).getFoodID()) + "' and Uid ='"+ userId +"'");
                            Toast.makeText(getActivity(), "Đã xóa xong", Toast.LENGTH_SHORT).show();
                            getSavedFood();

                        }catch (Exception ex)
                        {
                            Toast.makeText(getActivity(), "Delete: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialogXoa.show();
                return true;
            }
        });
        lstSavedFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int foodID = arrFood.get(i).getFoodID();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodId",Integer.toString(foodID));
                editor.commit();
                Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false)
        {

            userId = sharedPreferences.getString("userId","");
            if(userId.equals(""))
            {
                txtfavoriteFood.setText("Login to view");
                txtfavoriteFood.setVisibility(View.VISIBLE);
                arrFood = new ArrayList<>();
                foodAdapter = new FoodAdapter(getContext(),R.layout.list_item_line,arrFood);
                lstSavedFood.setAdapter(foodAdapter);
            }else {
                txtfavoriteFood.setVisibility(View.INVISIBLE);
                getSavedFood();
            }
        }
    }

    private void getSavedFood(){
        arrFood = new ArrayList<>();
        try {
            database = new Database(getActivity(),"FoodyDB.sqlite",null,1);

            Cursor dataUser = database.GetData("Select * from FOOD, SavedFood where Id = Fid and Uid = '" + userId+"'");
            while (dataUser.moveToNext())
            {
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
        }catch (Exception ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(arrFood.size() > 0)
        {
            foodAdapter = new FoodAdapter(getContext(),R.layout.list_item_line,arrFood);
            lstSavedFood.setAdapter(foodAdapter);
        }else
        {
            txtfavoriteFood.setText("You do not have favorite");
            txtfavoriteFood.setVisibility(View.VISIBLE);
            arrFood = new ArrayList<>();
            foodAdapter = new FoodAdapter(getContext(),R.layout.list_item_line,arrFood);
            lstSavedFood.setAdapter(foodAdapter);
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