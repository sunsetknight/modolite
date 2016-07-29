package com.mingko.moduolite.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mingko.moduolite.R;
import com.mingko.moduolite.fragment.ModuoFragment;
import com.mingko.moduolite.fragment.SettingConnectFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String IP = "192.168.2.15";
    public static final int PORT = 10001;

    //点击两次返回按钮退出程序 单位：毫秒
    private static final long EXIT_TIME = 1500;
    private long exitTime = 0;
    private boolean showMenu = true;

    @BindView(R.id.id_tb)
    Toolbar toolbar;

    private FragmentManager fragmentManager;
    private ModuoFragment moduoFragment;
    private SettingConnectFragment settingConnectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
        initFragment();
    }

    private void initView() {
        //actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("魔哆");

    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        moduoFragment = new ModuoFragment();
        settingConnectFragment = new SettingConnectFragment();
        //初始化为moduoFragment
        fragmentManager.beginTransaction()
                .add(R.id.id_fragment_container, moduoFragment)
                .add(R.id.id_fragment_container, settingConnectFragment)
                .hide(settingConnectFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        moduoFragment.initClientThread(IP, PORT);
    }

    //设置标题
    private void setTitle(String strTitle) {
        TextView tv = (TextView) toolbar.findViewById(R.id.id_tb_title);
        tv.setText(strTitle);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() < (exitTime + EXIT_TIME)) {
            super.onBackPressed();
        } else {
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if( showMenu ){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                switchFragment(settingConnectFragment, moduoFragment);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                showMenu = true;
                invalidateOptionsMenu();
                break;
            case R.id.id_menu_setting_connect:
                switchFragment(moduoFragment, settingConnectFragment);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                showMenu = false;
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment currentFragment, Fragment toFragment){
        fragmentManager.beginTransaction()
                .hide(currentFragment)
                .show(toFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
