package com.example.achuan.navigationdrawertest_0.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by achuan on 16-9-12.
 */
public class BluetoothUtil {


    //访问主机设备上的默认蓝牙适配器
    private static BluetoothAdapter bluetooth=BluetoothAdapter.getDefaultAdapter();
    //定义一个蓝牙连接的socket对象
    private static BluetoothSocket socket;
    //定义一个UUID以识别用户的应用程序
    private static UUID uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //设置一个请求码
    private static int DISCOVERY_REQUEST=1;
    //创建一个集合存储所发现的蓝牙设备
    private static ArrayList<String> foundDevices=new ArrayList<String>();
    private static ArrayList<BluetoothDevice> foundHardDevices=new ArrayList<BluetoothDevice>();
    //初始化一个列表适配器
    private  static ArrayAdapter<String> aa;


    //1-获取蓝牙适配的方法
    public static BluetoothAdapter getBluetooth()
    {
        return bluetooth;
    }
    //2-获取请求码
    public static int getDiscoveryRequest() {
        return DISCOVERY_REQUEST;
    }
    //3-获取UUID识别码
    public static UUID getUuid() {
        return uuid;
    }
    //4-获取蓝牙设备列表适配器
    public  static ArrayAdapter<String> getAa() {
        return aa;
    }
    public static void setAa(ArrayAdapter<String> aa) {
        BluetoothUtil.aa = aa;
    }
    //5-获取蓝牙设备名称的数组
    public static ArrayList<String> getFoundDevices() {
        return foundDevices;
    }
    public static void setFoundDevices(ArrayList<String> foundDevices) {
        BluetoothUtil.foundDevices = foundDevices;
    }
    //6-获取蓝牙设备服务设备的数组
    public static ArrayList<BluetoothDevice> getFoundHardDevices() {
        return foundHardDevices;
    }
    //7-获取蓝牙连接的socket对象
    public static BluetoothSocket getSocket() {
        return socket;
    }
    public static void setSocket(BluetoothSocket socket) {
        BluetoothUtil.socket = socket;
    }

}
