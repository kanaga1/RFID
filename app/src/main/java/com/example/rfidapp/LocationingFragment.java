package com.example.rfidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfidapp.Retrofit.RetrofitInterface;
import com.rscja.deviceapi.RFIDWithUHFBluetooth;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationingFragment extends Fragment {
    private AutoCompleteTextView et_locateTag;
    private RangeGraph locationBar;

    public static short TagProximityPercent = -1;
    public String myInt;

    public String item,tag,Uniquetag;
    Button btn_locate,btn_stop;
    List<User> heroList;
    private String TAG = "DeviceAPI_UHFReadTag";
    Toast mToast;

    private String BASE_URL = "http://192.168.0.101:3000";
    private boolean loopFlag = false;

    // private Button btInventory; "single"

    // private Button btClear;

    private boolean isExit = false;
    private long total = 0;
    private MainActivity mContext;
    private SimpleAdapter adapter;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;

    //--------------------------------------获取 解析数据-------------------------------------------------
    final int FLAG_START = 0;//开始
    final int FLAG_STOP = 1;//停止
    final int FLAG_UHFINFO = 3;
    final int FLAG_SUCCESS = 10;//成功
    final int FLAG_FAIL = 11;//失败

    boolean isRuning = false;
    private long mStrTime;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FLAG_STOP:
                    if (msg.arg1 == FLAG_SUCCESS) {
                        //停止成功
//                        btClear.setEnabled(true);
                        btn_locate.setEnabled(false);
                    } else {
                        //停止失败
                        //Utils.playSound(2);
                        Toast.makeText(mContext, "Inventory Fail", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FLAG_START:
                    if (msg.arg1 == FLAG_SUCCESS) {
                        //开始读取标签成功
                        btn_locate.setEnabled(true);
//                        btClear.setEnabled(false);

                    } else {
                        //开始读取标签失败
                        //Utils.playSound(2);
                    }
                    break;
                case FLAG_UHFINFO:
                    UHFTAGInfo info = (UHFTAGInfo) msg.obj;
                    addEPCToList(info, "N/A");
                    // Utils.playSound(1);
                    break;
            }
        }
    };
    private void addEPCToList(UHFTAGInfo uhftagInfo, String rssi) {
        if (!TextUtils.isEmpty(uhftagInfo.getEPC())) {

                    // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");


            System.out.println("jujujujuuuuuuuuuuu"+Uniquetag+uhftagInfo.getEPC()+Uniquetag.equals(uhftagInfo.getEPC()));

            if(Uniquetag.equals(uhftagInfo.getEPC()))
                        {
                        System.out.println("pppppppppppppppppppp"+Uniquetag+uhftagInfo.getEPC());
                            locationBar.setValue(80);
                            locationBar.invalidate();
                            locationBar.requestLayout();
//                        System.out.println("dddd"+ heroList.get(i).getP_name()+"uuu"+heroList.get(i).getP_type()+"hhh");
                        }
            else
            {
                locationBar.setValue(0);
                locationBar.invalidate();
                locationBar.requestLayout();
            }




            float useTime = (System.currentTimeMillis() - mStrTime) / 1000.0F;
            adapter.notifyDataSetChanged();
        }
    }

    private Timer mTimer = new Timer();
    private TimerTask mInventoryPerMinuteTask;
    private long period = 6 * 1000; // 每隔多少ms
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BluetoothReader" + File.separator;
    private String fileName;


    private void inventoryPerMinute() {
        System.out.println("llllllllllllllll");


        mContext.scaning = true;
        fileName = path + "battery_" + DateUtils.getCurrFormatDate(DateUtils.DATEFORMAT_FULL) + ".txt";
        mInventoryPerMinuteTask = new TimerTask() {
            @Override
            public void run() {
                String data = DateUtils.getCurrFormatDate(DateUtils.DATEFORMAT_FULL) + "\t电量：" + mContext.uhf.getBattery() + "%\n";
//                FileUtils.writeFile(fileName, data, true);
                //  inventory();
            }
        };
        mTimer.schedule(mInventoryPerMinuteTask, 0, period);
        cancelInventoryTask();
    }

    private void cancelInventoryTask() {
        if(mInventoryPerMinuteTask != null) {
            mInventoryPerMinuteTask.cancel();
            mInventoryPerMinuteTask = null;
        }
    }
    public LocationingFragment() {
        // Required empty public constructor
    }
    public static LocationingFragment newInstance() {
        return new LocationingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_locationing, container, false);


    }

    private void init() {
        System.out.println("777777777777777777777777");
        RetrofitInterface apiService = ApiClient.getClient().create(RetrofitInterface.class);
        Call<List<User>> call = apiService.getUsers();
        mContext.uhf.setPower(15);
        System.out.println("dddddddddddddddddddddddd"+mContext.uhf.getPower());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                heroList = response.body();
                if(heroList.size()>1)
                {
                    for(int i=0;i<heroList.size();i++)
                    {
                        if(tag.equals(heroList.get(i).getP_ID()))
                        {
                            System.out.println("uuuuuuuuuuuuuugggggggggggggggggggg"+Uniquetag);

                            Uniquetag=heroList.get(i).getEPCID();
                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        isExit = false;

        //    btInventory = (Button) mContext.findViewById(R.id.btInventory); "single"



        tagList = new ArrayList<>();
        adapter = new SimpleAdapter(mContext, tagList, R.layout.listtag_items,
                new String[]{"tagData", "tagCount", "tagRssi"},
                new int[]{R.id.TvTagUii, R.id.TvTagCount,
                        R.id.TvTagRssi});

        mContext.uhf.setKeyEventCallback(new RFIDWithUHFBluetooth.KeyEventCallback() {
            @Override
            public void getKeyEvent(int keycode) {
                Log.d("DeviceAPI_ReadTAG", "  keycode =" + keycode + "   ,isExit=" + isExit);
                if (!isExit && mContext.uhf.getConnectStatus() == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
                    startThread(loopFlag);
                }
            }
        });

    }
    public void startThread(boolean isStop) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        if (isRuning) {
            return;
        }
        isRuning = true;
        new TagThread(isStop).start();
    }
    private void stopInventory() {
        cancelInventoryTask();
        loopFlag = false;
        if(mContext.scaning) {
            RFIDWithUHFBluetooth.StatusEnum statusEnum = mContext.uhf.getConnectStatus();
            Message msg = handler.obtainMessage(FLAG_STOP);
            boolean result = mContext.uhf.stopInventoryTag();
            if (result || statusEnum == RFIDWithUHFBluetooth.StatusEnum.DISCONNECTED) {
                msg.arg1 = FLAG_SUCCESS;
            } else {
                msg.arg1 = FLAG_FAIL;
            }
            if (statusEnum == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
                //在连接的情况下，结束之后继续接收未接收完的数据
                //getUHFInfoEx();
            }
            mContext.scaning = false;
            handler.sendMessage(msg);
        }
    }
    class TagThread extends Thread {

        boolean isStop = false;

        public TagThread(boolean isStop) {
            this.isStop = isStop;
        }

        public void run() {
            if (isStop) {
                stopInventory();
                isRuning = false;//执行完成设置成false
            } else {
                Message msg = handler.obtainMessage(FLAG_START);
                System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+msg);
                if (mContext.uhf.startInventoryTag()) {
                    loopFlag = true;
                    mContext.scaning = true;
                    mStrTime = System.currentTimeMillis();
                    msg.arg1 = FLAG_SUCCESS;
                } else {
                    msg.arg1 = FLAG_FAIL;
                }
                handler.sendMessage(msg);
                isRuning = false;//执行完成设置成false
                while (loopFlag) {
                    getUHFInfo();
                }
            }
        }
    }
    private boolean getUHFInfo() {
        ArrayList<UHFTAGInfo> list = mContext.uhf.readTagFromBuffer();
        if (list != null) {
            for (int k = 0; k < list.size(); k++) {
                UHFTAGInfo info = list.get(k);
                Message msg = handler.obtainMessage(FLAG_UHFINFO);
                msg.obj = info;
                handler.sendMessage(msg);
                if(!loopFlag) {
                    break;
                }
            }
            if (list.size() > 0)
                return true;
        } else {
            return false;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Change the icon
        mContext = (MainActivity) getActivity();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.dl_loc);
        locationBar = (RangeGraph) getActivity().findViewById(R.id.locationBar);
        locationBar.setValue(0);
         btn_locate = (Button) getActivity().findViewById(R.id.btn_locate);

           tag = SharedPreference.getTag_ID(getContext());
            System.out.println("eeeeeee"+tag);
//        locationBar = (RangeGraph) getActivity().findViewById(R.id.locationBar);
        // distance=(TextView)getActivity().findViewById(R.id.distance);
        et_locateTag = (AutoCompleteTextView) getActivity().findViewById(R.id.lt_et_epc);

        init();

        et_locateTag.setText(tag);

            btn_locate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn_locate.getText().equals("SEARCH")) {
                        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkooooo");
                        btn_locate.setText("STOP");
                        startThread(false);

                        inventoryPerMinute();
                    }
                    else
                    {
                        System.out.println("oooooooooookkkkkkkkkkkkkkkkkkkkkkk");

                        btn_locate.setText("SEARCH");
                        locationBar.setValue(0);
                        locationBar.invalidate();
                        locationBar.requestLayout();
                    }


                }
            });





//            if (myInt!= null) {
//                if (btn_locate != null)
//                {
//                    btn_locate.setText(getResources().getString(R.string.stop_title));
//                    inventory();
//                }
//                showTagLocationingDetails();
//            } else {
//                if (btn_locate != null) {
//
//                    btn_locate.setText(getResources().getString(R.string.start_title));
//                }
//            }


   //     String value = getArguments().getString("YourKey");

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreference.clearTagId(mContext);
    }
    @Override
    public void onDetach() {
        super.onDetach();

        myInt = et_locateTag.getText().toString();
    }

}
