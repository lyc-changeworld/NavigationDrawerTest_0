package com.example.achuan.navigationdrawertest_0.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.achuan.navigationdrawertest_0.R;
import com.example.achuan.navigationdrawertest_0.receiver.BluetoothReceiver;
import com.example.achuan.navigationdrawertest_0.util.BluetoothUtil;

import java.io.IOException;

/**
 * Created by achuan on 16-9-8.
 * 功能：外部蓝牙设备的搜索,以及建立联系
 */
public class BlueToothConnectActivity extends BaseActivity {
    private Button mBt_Search;
    private ListView mLv_Discovered;
    private Animation mAnimation;
    public  static final int CLOSE_DISCOVERY=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }*/
        setContentView(R.layout.bluetooth_connected_layout);
        initView();//1-初始化控件
        //进行蓝牙开启的请求
        if(!BluetoothUtil.getBluetooth().isEnabled())//如果蓝牙未打开
        {
            //显式提醒用户打开可发现机制
            Intent disc=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            disc.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            //跳转到子活动,并带上一个请求码
            startActivityForResult(disc,BluetoothUtil.getDiscoveryRequest());
        }
    }
    /***２-添加一个子线程来进行计时处理,一段时间后执行某个操作,然后在主线程中更新UI****/
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CLOSE_DISCOVERY:
                    //必须在主线程中进行UI操作
                    mBt_Search.setText("搜索设备");
                    Toast.makeText(BlueToothConnectActivity.this, "搜索结束", Toast.LENGTH_SHORT).show();
                    if(mAnimation==null)
                    {
                        mAnimation= AnimationUtils.loadAnimation(BlueToothConnectActivity
                                .this,R.anim.rotate);
                    }
                    mBt_Search.startAnimation(mAnimation);
                    break;
            }
        }
    };
    /*****1-初始化控件*****/
    private void initView() {
        mBt_Search= (Button) findViewById(R.id.bt_search);
        mLv_Discovered= (ListView) findViewById(R.id.lv_discovered);
        //初始化列表适配器
        BluetoothUtil.setAa(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                BluetoothUtil.getFoundDevices()));
        //为设备列表注册适配器
        mLv_Discovered.setAdapter(BluetoothUtil.getAa());
        /***1搜索设备按钮点击监听事件***/
        mBt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //为找到远程蓝牙设备的意图注册监听器
                registerReceiver (BluetoothReceiver.discoveryResult//相应意图的接收器
                        ,new IntentFilter(BluetoothDevice.ACTION_FOUND));//配置意图为找到设置
                //如果发现机制没有启动
                if(!BluetoothUtil.getBluetooth().isDiscovering()){
                    BluetoothUtil.getFoundDevices().clear();//清空发现设备的集合,重新开始存储
                    BluetoothUtil.getFoundHardDevices().clear();
                    //BluetoothUtil.getAa().clear();//清空列表显示适配器,直接将列表变为空显示
                    //还可以通过清空数据集合,再更新列表显示来使列表变为空显示（这个更好用些）
                    BluetoothUtil.getAa().notifyDataSetChanged();//更新列表显示
                    //启动发现机制
                    BluetoothUtil.getBluetooth().startDiscovery();
                }
                //开启一个子线程,15秒后将发现机制关闭,同时关闭发现设备的广播监听器
                mBt_Search.setText("正在搜索设备...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15000);//延时15秒
                            //需要进行的操作（注销发现设备发现的广播监听器,关闭发现机制）
                            /*执行15秒后,执行关闭操作*/
                            unregisterReceiver(BluetoothReceiver.discoveryResult);//注销广播监听器
                            BluetoothUtil.getBluetooth().cancelDiscovery();//关闭发现机制
                            Message message=new Message();
                            message.what=CLOSE_DISCOVERY;
                            mHandler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



                //遗留了一个问题,就是按钮如果在15秒内连续点击,会发生连续创建子线程的问题
                //还有不清楚如何知道广播接收器是否被注册,如果知道是否注册,可以做很多优化操作
                //连续点击按钮会出现连续注册广播监听器的bug,后续将解决这个问题

            }
        });
        /*****2设备列表的item点击监听事件*****
         * 功能：该方法是当我方设备作为客户端时,监听到服务端的accept()请求后,调用connect()方法
         *　　　　实现socket通道的连接,从而实现数据的传送
        */
        mLv_Discovered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //为配对事件和UI更新添加一个异步消息处理方法
                AsyncTask<Integer,Void ,Void> connectTask=new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... integers) {
                        try {
                            //获取蓝牙设备的实例操作对象
                            BluetoothDevice device=BluetoothUtil.getFoundHardDevices().get(integers[0]);
                            BluetoothUtil.setSocket(device.createRfcommSocketToServiceRecord(BluetoothUtil.getUuid()));
                            BluetoothUtil.getSocket().connect();//为没有配对过的设备执行配对操作
                        } catch (IOException connectException) {
                            connectException.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        //switchUI();//更新UI
                        super.onPostExecute(aVoid);
                    }
                };
                //启动这个异步消息处理的方法
                connectTask.execute(i);//i代表进度值
            }
        });
    }
}
