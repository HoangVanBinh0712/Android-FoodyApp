package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hcmute.hoangvanbinh19110170.foody_interface.Adapter.InvoiceDetailAdapter;
import hcmute.hoangvanbinh19110170.foody_interface.Models.InvoiceDetail;

public class InvoiceDetailActivity1 extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Database database;
    ListView lstInvoiceDetail;
    TextView txtinvoiceNo;
    ArrayList<InvoiceDetail> arrInvoiceDetail;
    private InvoiceDetailAdapter invoiceDetailAdapter;
    int idi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail1);
        Intent invoiceDetailIntent = getIntent();
        idi = invoiceDetailIntent.getIntExtra("idInvoice",0);
        lstInvoiceDetail = findViewById(R.id.lstInvoiceDetail);
        txtinvoiceNo = findViewById(R.id.invoiceNo);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        getInvoiceData();

    }
    private void getInvoiceData(){

        arrInvoiceDetail = new ArrayList<>();
        try{
            Cursor cursor = database.GetData("SELECT * FROM INVOICE_DETAIL where InvoiceId = " + idi);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                int fid = cursor.getInt(1);
                Double price = cursor.getDouble(2);
                int amount = cursor.getInt(3);
                Double total = cursor.getDouble(4);

                InvoiceDetail newInvoiceDetail = new InvoiceDetail(id, fid, price, amount, total);

                arrInvoiceDetail.add(newInvoiceDetail);
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (arrInvoiceDetail.size() > 0)
        {
            invoiceDetailAdapter = new InvoiceDetailAdapter(this,arrInvoiceDetail,R.layout.row_invoice_detail);
            lstInvoiceDetail.setAdapter(invoiceDetailAdapter);
            txtinvoiceNo.setText(Integer.toString(idi));
        }else
        {
            arrInvoiceDetail = new ArrayList<>();
            invoiceDetailAdapter = new InvoiceDetailAdapter(this,arrInvoiceDetail,R.layout.row_invoice_detail);
            lstInvoiceDetail.setAdapter(invoiceDetailAdapter);
            txtinvoiceNo.setText(Integer.toString(idi));
        }
    }
}