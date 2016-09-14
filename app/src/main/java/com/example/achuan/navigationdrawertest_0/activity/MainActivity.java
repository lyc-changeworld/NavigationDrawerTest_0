package com.example.achuan.navigationdrawertest_0.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.achuan.navigationdrawertest_0.App.MyApplication;
import com.example.achuan.navigationdrawertest_0.R;
import com.example.achuan.navigationdrawertest_0.receiver.BluetoothReceiver;
import com.example.achuan.navigationdrawertest_0.util.BluetoothUtil;
import com.example.achuan.navigationdrawertest_0.util.SharedPreferenceUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化布局控件
        openBluetooth();//打开本地蓝牙设备
        //添加蓝牙连接的广播监听器,实现整个程序的的监听连接上的设备的消息的监听
        registerReceiver(BluetoothReceiver.connectedDeviceReceiver,new IntentFilter(
                BluetoothDevice.ACTION_ACL_CONNECTED));//监听连接事件
        registerReceiver(BluetoothReceiver.connectedDeviceReceiver,new IntentFilter(
                BluetoothDevice.ACTION_ACL_DISCONNECTED));//监听断开连接事件
    }
    /*//５-添加子活动销毁后返回的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BluetoothUtil.getDiscoveryRequest())//核对请求码,对子活动中传递过来的数据进行处理
        {
            boolean isDiscoverable=resultCode>0;//如果用户拒绝了子活动中的请求,返回的将是负值,否则为正值
            if(isDiscoverable)//如果打开了发现机制
            {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
    /***４－打开蓝牙的方法***/
    private void openBluetooth() {
        //如果设置为开启软件自动打开蓝牙,则隐式的开启蓝牙发现机制,否则将在连接设备的时候进行显示提醒
        if(SharedPreferenceUtil.getAutoOpenBlueToothState())
        {
            if(!BluetoothUtil.getBluetooth().isEnabled())
            {
                //没有提示,直接打开蓝牙发现机制
                BluetoothUtil.getBluetooth().enable();
            }
        }
        else {
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
    }
    /*****3-初始化布局控件*****/
    private void initView() {
        //toolbar的设置，稍后写这个控件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //悬浮按钮控件，稍后讲这个控件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is a snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //设置DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //设置NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //为NavigationView控件添加item的点击监听事件
        navigationView.setNavigationItemSelectedListener(this);
        //先获取navigationView中的nav_header_main布局,0代表取第一个子元素
        RelativeLayout header_view= (RelativeLayout) navigationView.getHeaderView(0);
        header_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "修改个人资料", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*******2-对左侧的NavigationItem进行item的点击事件监听*********/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.bluetooth_connected) {


            //跳转到"蓝牙连接"的活动
            Intent intent=new Intent(this,BlueToothConnectActivity.class);
            startActivity(intent);
            /*//打开系统的蓝牙设置界面
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));*/
        }else if(id == R.id.preference_setting){
            //跳转到"设置"的活动
            Intent intent=new Intent(this,SettingActivity.class);
            startActivity(intent);
        }
        /*//点击单个item后关闭左侧打开界面
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }
    /*****1-直接退出App的方法******/
    //１Back键点击事件
    @Override
    public void onBackPressed() {
        //只有在主活动和用户交互的时候才可以直接退出App
        showExitDialog();//显示退出界面
    }
    //２弹出对话框,确认是否退出App
    private void showExitDialog() {
        //弹出一个对话框
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);//先创建一个构造实例
        dialog.setTitle("提示");//设置标题
        dialog.setMessage("确定退出蓝牙签到助手吗");//设置内容部分
        dialog.setCancelable(true);//设置是否可以通过Back键取消：false为不可以取消,true为可以取消
        //设置右边按钮的信息
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override//点击触发事件
            public void onClick(DialogInterface dialogInterface, int i) {
                BluetoothUtil.getBluetooth().disable();//退出应用后关闭蓝牙
                //点击确定后退出app
                //将所有的活动依次出栈,然后回收所有的资源
                MyApplication.getInstance().exitApp();
            }
        });
        //设置左边按钮的信息
        dialog.setNegativeButton("取消", null);
        dialog.show();//将对话框显示出来
    }
    /*******０－右上角的菜单栏*********/
    //1加载右上角的菜单列表的显示文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //2对右上角的单个菜单进行点击监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*通过下面两个方法,发现了finish只是将活动出栈,onDestroy是在finish后面执行,负责回收资源*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LogUtil.d("hh","销毁，我的监听被注销了");
    }
    @Override
    public void finish() {
        super.finish();
        //注销蓝牙是否打开的监听事件
        unregisterReceiver(BluetoothReceiver.connectedDeviceReceiver);
        //LogUtil.d("hh","结束-蓝牙连接的监听被注销了");
    }
}
