package hcmute.hoangvanbinh19110170.foody_interface.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import hcmute.hoangvanbinh19110170.foody_interface.Models.Invoice;
import hcmute.hoangvanbinh19110170.foody_interface.R;

public class InvoiceAdapter extends BaseAdapter {
    Context myContext;
    List<Invoice> arrInvoice;
    int myLayout;

    public InvoiceAdapter(Context myContext, List<Invoice> arrInvoice, int myLayout) {
        this.myContext = myContext;
        this.arrInvoice = arrInvoice;
        this.myLayout = myLayout;
    }


    @Override
    public int getCount() {
        return arrInvoice.size();
    }

    @Override
    public Object getItem(int i) {
        return arrInvoice.get(i);
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
        TextView invoiceID = (TextView) view.findViewById(R.id.invoiceNumber);
        invoiceID.setText(Integer.toString(arrInvoice.get(i).getId()));//Cai settext phải trueyèn vào String
        TextView invoiceTotal = (TextView) view.findViewById(R.id.invoiceTotal);
        invoiceTotal.setText(Double.toString(arrInvoice.get(i).getTotal())+ " $");
        //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        TextView dayOrder = (TextView) view.findViewById(R.id.invoiceDay);
        dayOrder.setText(arrInvoice.get(i).getDayOrder());
        return view;
    }
}
