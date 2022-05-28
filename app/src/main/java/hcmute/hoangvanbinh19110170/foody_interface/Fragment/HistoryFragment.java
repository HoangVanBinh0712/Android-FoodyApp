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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.InvoiceAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.InvoiceDetailActivity1;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Invoice;
import hcmute.hoangvanbinh19110170.foody_interface.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    private View view ;
    private ListView userlstinvoice;
    private TextView invoiceText;
    private String userId;
    private InvoiceAdapter invoiceAdapter;
    private ArrayList<Invoice> arrInvoice;
    //Xoa cai cu di ma lam cai moi
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invoice, container, false);
        sharedPreferences = getActivity().getSharedPreferences("dataCookie", Context.MODE_MULTI_PROCESS);
        database = new Database(getActivity(),"FoodyDB.sqlite",null,1);
        userId = sharedPreferences.getString("userId", "");
        invoiceText = (TextView) view.findViewById(R.id.txtYourInvoice);
        userlstinvoice = (ListView) view.findViewById(R.id.userlstinvoice);
        if(userId.equals(""))
        {
            invoiceText.setVisibility(View.VISIBLE);
            invoiceText.setText("Login to see your invoice");
        }else {
            invoiceText.setVisibility(View.INVISIBLE);
            getInvoiceData();
        }
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
                invoiceText.setVisibility(View.VISIBLE);
                invoiceText.setText("Login to see your invoice");
                arrInvoice = new ArrayList<>();
                invoiceAdapter = new InvoiceAdapter(getActivity(),arrInvoice,R.layout.row_invoice);
                userlstinvoice.setAdapter(invoiceAdapter);
            }else {
                invoiceText.setVisibility(View.INVISIBLE);
                getInvoiceData();
            }
        }
    }

    private void getInvoiceData(){
        arrInvoice = new ArrayList<>();
        try{
            Cursor cursor = database.GetData("Select * from INVOICE where UserId = " + userId);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                int uid = cursor.getInt(1);
                Double total = cursor.getDouble(2);
                String dayOder = cursor.getString(3);
                Invoice newInvoice = new Invoice(id, uid, total, dayOder);

                arrInvoice.add(newInvoice);
            }
        }catch (Exception ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if(arrInvoice.size() > 0)
        {
            invoiceAdapter = new InvoiceAdapter(getActivity(),arrInvoice,R.layout.row_invoice);
            userlstinvoice.setAdapter(invoiceAdapter);
            userlstinvoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent invoiceIntent = new Intent(getActivity(), InvoiceDetailActivity1.class);
                    invoiceIntent.putExtra("idInvoice", arrInvoice.get(i).getId());
                    startActivity(invoiceIntent);
                }
            });
        }else
        {
            invoiceText.setVisibility(View.VISIBLE);
            invoiceText.setText("You do not have invoice");
            arrInvoice = new ArrayList<>();
            invoiceAdapter = new InvoiceAdapter(getActivity(),arrInvoice,R.layout.row_invoice);
            userlstinvoice.setAdapter(invoiceAdapter);
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