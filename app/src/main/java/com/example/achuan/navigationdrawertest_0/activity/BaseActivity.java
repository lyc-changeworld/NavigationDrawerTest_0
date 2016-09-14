package com.example.achuan.navigationdrawertest_0.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.achuan.navigationdrawertest_0.App.MyApplication;
import com.example.achuan.navigationdrawertest_0.util.LogUtil;

/**
 * Created by achuan on 16-9-10.
 * 功能：基础活动,实现活动的创建和销毁等功能，对活动进行管理
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印显示当前处于的活动
        LogUtil.d("BaseActivity",getClass().getSimpleName());
        //创建活动时添加实例到数组中
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }
}
