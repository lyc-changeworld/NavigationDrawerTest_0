package com.example.achuan.navigationdrawertest_0.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.achuan.navigationdrawertest_0.App.MyApplication;
import com.example.achuan.navigationdrawertest_0.util.BluetoothUtil;

/**
 * Created by achuan on 16-9-13.
 * 功能：实现关于蓝牙方面的广播接收器
 */
public class BluetoothReceiver {
    //1-连接设备广播的接收者类
    public static BroadcastReceiver connectedDeviceReceiver=new BroadcastReceiver() {
        //意图的类型
        String connected= BluetoothDevice.ACTION_ACL_CONNECTED;
        String disconnected=BluetoothDevice.ACTION_ACL_DISCONNECTED;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();//获取广播的意图
            BluetoothDevice remoteDevice;
            //获取远程蓝牙设备的服务类,包含了设备的属性值
            remoteDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //根据不同的意图进行不同的响应
            if(connected.equals(action))//如果连上了设备
            {
                Toast.makeText(MyApplication.getContext(), "已经连接到"+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
            }
            else if (disconnected.equals(action))//如果发现机制已经完成了
            {
                Toast.makeText(MyApplication.getContext(), "蓝牙已断开连接", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //2-创建一个新的广播接收器来监听蓝牙设备的发现设备广播
    /*
    * 需要注意的地方：该广播只有在发现蓝牙设备时才会监听到
    * */
    public static BroadcastReceiver discoveryResult=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();//获取广播的意图
            BluetoothDevice remoteDevice;
            //获取远程蓝牙设备的服务类,包含了设备的属性值
            remoteDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //已经配对过的设备：bluetooth.getBondedDevices().contains(remoteDevice)
            // 搜索设备时，取得设备的MAC地址
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                //String str=remoteDevice.getAddress();//先获取设备的MAC地址
                String str=remoteDevice.getName().toString();
                //如果发现的设备还未存储在设备列表中
                if(!BluetoothUtil.getFoundHardDevices().contains(remoteDevice))
                {
                    BluetoothUtil.getFoundHardDevices().add(remoteDevice);
                    BluetoothUtil.getFoundDevices().add(str); // 获取mac地址和是否配对的消息
                    BluetoothUtil.getAa().notifyDataSetChanged();//更新列表显示
                }
            }
        }
    };


}
