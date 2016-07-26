package com.mingko.moduolite.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.mingko.moduolite.R;
import com.mingko.moduolite.fragment.ModuoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //点击两次返回按钮退出程序 单位：毫秒
    private static final long EXIT_TIME = 1500;
    private long exitTime = 0;

    @BindView(R.id.id_tb)
    Toolbar toolbar;

    private FragmentManager fragmentManager;
    private ModuoFragment moduoFragment;

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
        setTitle("魔哆");
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        moduoFragment = new ModuoFragment();
        //初始化为moduoFragment
        fragmentManager.beginTransaction()
                .replace(R.id.id_fragment_container, moduoFragment)
                .commit();
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

}
