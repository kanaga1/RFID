package com.example.rfidapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfidapp.DateUtils;
import com.example.rfidapp.FileUtils;
import com.example.rfidapp.MainActivity;
import com.example.rfidapp.NumberTool;
import com.example.rfidapp.R;
import com.example.rfidapp.Retrofit.RetrofitInterface;
import com.example.rfidapp.Utils;
import com.rscja.deviceapi.RFIDWithUHFBluetooth;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RapidReadFragment  extends Fragment  {
    private boolean loopFlag = false;
    public int count=0;
    ConnectivityReceiver myReceiver;
    public List<Product> heroList;
    public String Date,Total,Available,Sold;
    private TextView tv_count;
    private boolean isExit = false;
    private long total = 0;
    private MainActivity mContext;
    private SimpleAdapter adapter;
    private HashMap<String, String> map,map1;
    private ArrayList<HashMap<String, String>> tagList;
    public  ArrayList<Product> productlist = new ArrayList<>();


    private String TAG = "DeviceAPI_UHFReadTag";

    //--------------------------------------获取 解析数据-------------------------------------------------

    public RapidReadFragment() {
        // Required empty public constructor
    }
    public static RapidReadFragment newInstance() {
        return new RapidReadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Utils.initSound(getContext());
        Log.i(TAG, "UHFReadTagFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        final ListView lvtags= (ListView)mContext.findViewById(R.id.LvTags);


        RetrofitInterface apiService = ApiClient.getClient().create(RetrofitInterface.class);
        Call<List<Product>> call = apiService.getSale();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                heroList = response.body();

                if(heroList.size()>0)
                {
                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < heroList.size(); i++) {
                        Date = heroList.get(i).getDate();
                        Total = heroList.get(i).getTotal();
                        Available = heroList.get(i).getAvailable();
                        Sold = heroList.get(i).getSold();


                    }
                    Product product = new Product(Date,Total,Available,Sold);
                    productlist.add(product);
                    ProductAdapter productadapter = new ProductAdapter(getActivity(),R.layout.list_item,productlist);
                    lvtags.setAdapter(productadapter);
                }





            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        lvtags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(),ProductDetails.class);
                startActivity(intent);
            }
        });



        mContext.uhf.setPower(30);
        myReceiver =new ConnectivityReceiver();

        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectivityReceiver();
        mContext.registerReceiver(myReceiver,intentFilter);
        System.out.println("dddddddddddddddddddddddd"+mContext.uhf.getPower());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rr, container, false);
    }
    @Override
    public void onPause() {
        System.out.println("333333333333333333333333");

        super.onPause();
//        stopInventory();
    }

    @Override
    public void onDestroyView() {
        System.out.println("4444444444444444444444444");

        super.onDestroyView();
        isExit = true;
    }


    @Override
    public void onResume() {
        System.out.println("55555555555555555555555555555");

        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.freeSound();
    }
}
