package com.example.rfidapp;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfidapp.MainActivity;
import com.example.rfidapp.R;
import com.example.rfidapp.Retrofit.RetrofitInterface;
import com.example.rfidapp.Utils;
import com.rscja.deviceapi.RFIDWithUHFBluetooth;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BarcodeFragment extends Fragment implements View.OnClickListener {
    public  EditText et;
   public Button bt;
    List<User> heroList;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://rfidpoc1.herokuapp.com/";
    //    static boolean isExit_=false;
//    MainActivity mContext;
//    ScrollView scrBarcode;
//    TextView tvData;
//    Button btnScan,btClear;
//    Object lock=new Object();
//    Spinner spingCodingFormat;
//
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.obj.toString()!=null) {
//
//                    btClear.setEnabled(true);
//                    btnScan.setEnabled(false);
//                    btClear.setAlpha(1f);
//                    btnScan.setAlpha(0.3f);
//
//                tvData.setText(tvData.getText() + msg.obj.toString() + "\r\n");
//                scroll2Bottom(scrBarcode, tvData);
//                Utils.playSound(1);
//            }
//        }
//    };
    public BarcodeFragment() {
        // Required empty public constructor
    }

    public static BarcodeFragment newInstance() {
        return new BarcodeFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_expiry, container, false);
        // Inflate the layout for this fragment

         et = (EditText)v.findViewById(R.id.editText1);
        bt = (Button)v.findViewById(R.id.button);
        retrofitInterface = retrofit.create(RetrofitInterface.class);
//        String id = et.getText().toString();
//        System.out.println("555555555555555555555"+id);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();

                System.out.println( "hshshfuisdhfffffffffffffff"+et.getText().toString());
                map.put("Sale_Id", et.getText().toString());
                System.out.println( "hshshfuisdhfffffffffffffff"+map);
                Call<Void> call = retrofitInterface.executesale(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println( "333333333333333333333"+response);
                        if (response.code() == 200) {
                            System.out.println( "333333333333333333333"+et.getText().toString());
                            Toast.makeText(getContext(),
                                    "Tag registered successfully", Toast.LENGTH_SHORT).show();

                        } else if (response.code() == 400) {
                            System.out.println( "77777777777777777777777"+et.getText().toString());
                            Toast.makeText(getContext(),
                                    "Already registered", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Not Connected",
                                Toast.LENGTH_LONG).show();
                    }
                });

         }

        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        isExit_=false;
//        Utils.initSound(getContext());
//        scrBarcode=(ScrollView)getActivity().findViewById(R.id.scrBarcode);
//        tvData=(TextView)getActivity().findViewById(R.id.tvData);
//        btnScan=(Button)getActivity().findViewById(R.id.btnScan);
//        btClear=(Button)getActivity().findViewById(R.id.btClear);
//        btnScan.setOnClickListener(this);
//        btClear.setOnClickListener(this);
//        spingCodingFormat=(Spinner)getActivity().findViewById(R.id.spingCodingFormat);
//        mContext=(MainActivity) getActivity();
//        mContext.uhf.setKeyEventCallback(new RFIDWithUHFBluetooth.KeyEventCallback() {
//            @Override
//            public void getKeyEvent(int keycode) {
//                Log.d("DeviceAPI_setKeyEvent","  keycode ="+keycode +"   ,isExit_="+isExit_);
//                if(!isExit_ && mContext.uhf.getConnectStatus()==RFIDWithUHFBluetooth.StatusEnum.CONNECTED)
//                    scan();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mContext.uhf.getConnectStatus() == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
//            btnScan.setEnabled(true);
//            btnScan.setAlpha(1f);
//            btClear.setAlpha(0.3f);
//        }
//        else
//        {
//            btnScan.setEnabled(false);
//            btnScan.setAlpha(0.3f);
//            btClear.setAlpha(0.3f);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        isExit_=true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btnScan:
////                scan();
//
//                break;
//            case R.id.btClear:
//                btClear.setEnabled(false);
//                btnScan.setEnabled(true);
//                btClear.setAlpha(0.3f);
//                btnScan.setAlpha(1f);
//                tvData.setText("");
//                break;
//        }
        }


//    private synchronized void scan(){
//        if(!isRuning){
//            isRuning=true;
//            new ScanThread().start();
//        }
//    }

//    boolean isRuning=false;
//    class   ScanThread  extends Thread{
//        public void run(){
//            String data=null;
//            byte[] temp=mContext.uhf.scanBarcodeToBytes();
//            if(temp!=null) {
//                if (spingCodingFormat.getSelectedItemPosition() == 1) {
//                    try {
//                        data = new String(temp, "utf8");
//                    } catch (Exception ex) {
//                    }
//                } else if (spingCodingFormat.getSelectedItemPosition() == 2) {
//                    try {
//                        data = new String(temp, "gb2312");
//                    } catch (Exception ex) {
//                    }
//                } else {
//                    data = new String(temp);
//                }
//
//                if (data != null && !data.isEmpty()) {
//                    Log.d("DeviceAPI_setKeyEvent","data="+data);
//                    Message msg = Message.obtain();
//                    msg.obj = data;
//                    handler.sendMessage(msg);
//                }
//            }
//            isRuning=false;
//        }
//    }
//
//    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                if (scroll == null || inner == null) {
//                    return;
//                }
//                // 内层高度超过外层
//                int offset = inner.getMeasuredHeight()
//                        - scroll.getMeasuredHeight();
//                if (offset < 0) {
//                    offset = 0;
//                }
//                scroll.scrollTo(0, offset);
//            }
//        });
//
//    }

    }
}
