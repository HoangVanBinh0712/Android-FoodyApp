package hcmute.hoangvanbinh19110170.foody_interface.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import hcmute.hoangvanbinh19110170.foody_interface.R;
import hcmute.hoangvanbinh19110170.foody_interface.SignInActivity;
import hcmute.hoangvanbinh19110170.foody_interface.UnCensoredFoodActivity;
import hcmute.hoangvanbinh19110170.foody_interface.UserFoodActivity;
import hcmute.hoangvanbinh19110170.foody_interface.ViewInformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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

    private View view;
    private SharedPreferences sharedPreferences;
    private String userId;
    private LinearLayout linearLogin, linearYourFoods, linearViewAccount,linearLogOut, linearUncensoredFood;
    private String user_Role;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = getActivity().getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);

        AnhXa();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AnhXa();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            AnhXa();
        }
    }

    private void AnhXa(){
        linearLogin = (LinearLayout) view.findViewById(R.id.linearLogin);
        linearYourFoods = (LinearLayout) view.findViewById(R.id.linearYourFoods);
        linearViewAccount = (LinearLayout) view.findViewById(R.id.linearViewAccount);
        linearLogOut = (LinearLayout) view.findViewById(R.id.linearLogout);
        linearUncensoredFood = (LinearLayout) view.findViewById(R.id.linearUncensoredFood);

        user_Role = sharedPreferences.getString("user_Role","");
        userId = sharedPreferences.getString("userId","");
        if(user_Role.equals("2"))
        {
            linearUncensoredFood.setVisibility(View.VISIBLE);
            linearUncensoredFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user_Role.equals("2"))
                    {
                        Intent intent = new Intent(getActivity(), UnCensoredFoodActivity.class);
                        startActivity(intent);
                    }else
                        Toast.makeText(getActivity(), "Admin Only", Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            linearUncensoredFood.setVisibility(View.INVISIBLE);
        }

        if(!userId.equals(""))
        {
            //Co user
            linearLogin.setVisibility(View.INVISIBLE);
            linearLogOut.setVisibility(View.VISIBLE);
            linearLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences.edit().clear().commit();
                    Toast.makeText(getActivity(), "Logout Successfully !", Toast.LENGTH_SHORT).show();
                    onResume();
                }
            });
        }else {
            linearLogin.setVisibility(View.VISIBLE);
            linearLogOut.setVisibility(View.INVISIBLE);
            linearLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
            });
        }

        if(user_Role.equals("0"))
        {
            linearYourFoods.setVisibility(View.INVISIBLE);
        }else
            linearYourFoods.setVisibility(View.VISIBLE);
        linearYourFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(isLogin()){
                    intent = new Intent(getActivity(), UserFoodActivity.class);
                }else {
                    intent = new Intent(getActivity(), SignInActivity.class);
                }
                startActivity(intent);
            }
        });
        linearViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(isLogin()){
                    intent = new Intent(getActivity(), ViewInformation.class);
                }else {
                    intent = new Intent(getActivity(), SignInActivity.class);
                }
                startActivity(intent);

            }
        });

    }
    private boolean isLogin(){
        return !userId.equals("");
    }
}