package com.example.achuan.navigationdrawertest_0.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.achuan.navigationdrawertest_0.R;
import com.example.achuan.navigationdrawertest_0.util.SharedPreferenceUtil;

/**
 * Created by achuan on 16-9-11.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private AppCompatCheckBox mCB_setting_OpenBluetooth;
    private LinearLayout mLL_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        //1-初始化布局控件
        mCB_setting_OpenBluetooth= (AppCompatCheckBox) findViewById(R.id.cb_setting_open_bluetooth);
        mLL_feedback= (LinearLayout) findViewById(R.id.ll_setting_feedback);
        //2-初始化数据
        mCB_setting_OpenBluetooth.setChecked(SharedPreferenceUtil.getAutoOpenBlueToothState());
        //3-监听复选框的变化,记录状态到SharedPreference文件中
        mCB_setting_OpenBluetooth.setOnCheckedChangeListener(this);
        //4-为其它选项设置点击事件
        mLL_feedback.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_setting_feedback:
                Toast.makeText(this, "意见反馈功能后续将实现", Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
           switch (compoundButton.getId()){
               case R.id.cb_setting_open_bluetooth:
                   //更新复选框的状态
                   SharedPreferenceUtil.setAutoOpenBlueToothState(b);
                   break;
               default:break;
           }
    }


}
