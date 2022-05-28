package hcmute.hoangvanbinh19110170.foody_interface.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import hcmute.hoangvanbinh19110170.foody_interface.Database;
import hcmute.hoangvanbinh19110170.foody_interface.Models.Invoice;
import hcmute.hoangvanbinh19110170.foody_interface.Models.InvoiceDetail;
import hcmute.hoangvanbinh19110170.foody_interface.R;

public class InvoiceDetailAdapter extends BaseAdapter {
    Context myContext;
    List<InvoiceDetail> arrInvoiceDetail;
    int myLayout;

    public InvoiceDetailAdapter(Context myContext, List<InvoiceDetail> arrInvoiceDetail, int myLayout) {
        this.myContext = myContext;
        this.arrInvoiceDetail = arrInvoiceDetail;
        this.myLayout = myLayout;
    }

    @Override
    public int getCount() {
        return arrInvoiceDetail.size();
    }

    @Override
    public Object getItem(int i) {
        return arrInvoiceDetail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(myLayout, null);
        }
        int foodId = arrInvoiceDetail.get(i).getFoodId();
        //Database
        Database database;
        database = new Database(myContext,"FoodyDB.sqlite",null,1);
        Cursor data = database.GetData("SELECT foodName FROM FOOD WHERE Id=" + foodId);
        String foodname;
        TextView txtFoodName = (TextView) view.findViewById(R.id.txtDetailName);
        while(data.moveToNext()){
            foodname = data.getString(0);
            txtFoodName.setText(foodname);
        }
        TextView txtPrice = (TextView) view.findViewById(R.id.txtPriceInvoice);
        txtPrice.setText(Double.toString(arrInvoiceDetail.get(i).getPrice())+" $");
        TextView txtAmount = (TextView) view.findViewById(R.id.txtQuatityInvoice);
        txtAmount.setText(Integer.toString(arrInvoiceDetail.get(i).getAmount()));
        TextView txtTotal = (TextView) view.findViewById(R.id.txtTotalInvoice);
        txtTotal.setText(Double.toString(arrInvoiceDetail.get(i).getTotal())+" $");

        return view;
    }
}
