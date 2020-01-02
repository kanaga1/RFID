package com.example.rfidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class ProductAdapter extends ArrayAdapter<Product> {
    ArrayList<Product> P = new ArrayList<>();
    private Context mContext;
    int mResource;
    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        P = objects;
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String date = getItem(position).getDate();
        String total= getItem(position).getTotal();
        String available = getItem(position).getAvailable();
        String sold= getItem(position).getSold();

        Product p = new Product(date,total,available,sold);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView datetv = (TextView)convertView.findViewById(R.id.date);
        TextView totaltv =(TextView) convertView.findViewById(R.id.total);
        TextView availabletv = (TextView) convertView.findViewById(R.id.available);
        TextView soldtv =(TextView)  convertView.findViewById(R.id.sold);

        datetv.setText(date);
        totaltv.setText(total);
        availabletv.setText(available);
        soldtv.setText(sold);

        return convertView;



    }
}
