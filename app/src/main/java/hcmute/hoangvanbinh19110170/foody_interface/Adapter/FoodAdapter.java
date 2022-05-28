package hcmute.hoangvanbinh19110170.foody_interface.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import hcmute.hoangvanbinh19110170.foody_interface.Models.Food;
import hcmute.hoangvanbinh19110170.foody_interface.R;

public class FoodAdapter extends BaseAdapter {
    Context myContext;
    List<Food> arrFood;
    int myLayout;
    public FoodAdapter(Context context,int layout,List<Food> foodlist)
    {
        this.myContext = context;
        this.myLayout = layout;
        this.arrFood = foodlist;
    }
    @Override
    public int getCount() {
        return arrFood.size();
    }

    @Override
    public Object getItem(int i) {
        return arrFood.get(i);
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
        ImageView image = (ImageView) view.findViewById(R.id.imgviewLeft);
        byte[] img = arrFood.get(i).getFoodImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));
        TextView foodName = (TextView) view.findViewById(R.id.menuFoodname);
        foodName.setText(arrFood.get(i).getFoodName());

        TextView foodPrice = (TextView) view.findViewById(R.id.menufoodPrice);
        foodPrice.setText(Double.toString(arrFood.get(i).getPrice()) + " $");
        TextView foodQuantity = (TextView) view.findViewById(R.id.menufoodQuantity);
        foodQuantity.setText("Stock: " + Integer.toString(arrFood.get(i).getQuantity()));


        return view;
    }
}
