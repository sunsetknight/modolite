package com.mingko.moduolite.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mingko.moduolite.R;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.id_tb)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.e("success");
    }

    //设置标题
    private void setTitle(String strTitle) {
        TextView tv = (TextView) toolbar.findViewById(R.id.id_tb_title);
        tv.setText(strTitle);
    }

}
