package com.mingko.moduolite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mingko.moduolite.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 连接魔哆服务器设置
 *
 * Created by SunsetKnight on 2016/7/27.
 */
public class SettingConnectFragment extends Fragment {

    @BindView(R.id.id_et_server)
    EditText etServer;
    @BindView(R.id.id_et_port)
    EditText etPort;
    @BindView(R.id.id_btn_save)
    Button btnSave;
    @BindView(R.id.id_btn_delete)
    Button btnDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_connect_server, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
    }

    @OnClick(R.id.id_btn_save)
    public void setBtnSave(){
        String s = etServer.getText().toString();
        String p = etPort.getText().toString();
        Toast.makeText(this.getActivity(), s + ":" + p, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.id_btn_delete)
    public void setBtnDelete(){
        etServer.setText("");
        etPort.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
