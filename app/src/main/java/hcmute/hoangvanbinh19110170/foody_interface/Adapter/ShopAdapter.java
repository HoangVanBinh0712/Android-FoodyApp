package hcmute.hoangvanbinh19110170.foody_interface.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hcmute.hoangvanbinh19110170.foody_interface.Models.User;
import hcmute.hoangvanbinh19110170.foody_interface.R;

public class ShopAdapter extends BaseAdapter {
    Context myContext;
    List<User> arrUser;
    int myLayout;
    public ShopAdapter(Context context,int layout,List<User> arrUser)
    {
        this.myContext = context;
        this.myLayout = layout;
        this.arrUser = arrUser;
    }
    @Override
    public int getCount() {
        return arrUser.size();
    }

    @Override
    public Object getItem(int i) {
        return arrUser.get(i);
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
        TextView shopName = (TextView) view.findViewById(R.id.menuShopName);
        shopName.setText(arrUser.get(i).getName());

        TextView shopPhone = (TextView) view.findViewById(R.id.menuShopPhone);
        shopPhone.setText(arrUser.get(i).getPhone());


        return view;
    }
}
