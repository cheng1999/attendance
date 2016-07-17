package com.cheng.learn.attendance.mainselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.configureserver.ConfigureServerActivity;
import com.cheng.learn.attendance.setupclub.SetupClubActivity;

public class SelectorFragment extends Fragment implements SelectorContract.View, View.OnClickListener {

    private SelectorContract.Presenter mPresenter;

    public static SelectorFragment newInstance() {
        return new SelectorFragment();
    }

    public SelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public void setPresentor(SelectorContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.selector_frag, container, false);

        Button buttonStartScan = (Button)v.findViewById(R.id.button_startscan);
        Button buttonSetup = (Button)v.findViewById(R.id.button_setup);
        buttonStartScan.setOnClickListener(this);
        buttonSetup.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_startscan:
                mPresenter.startScanBarcode();
                break;
            case R.id.button_setup:
                startSetup();
                break;
        }
    }

    /**
     * start other activities
     */
    @Override
    public void startSetup() {
        Intent intent = new Intent(getContext(), SetupClubActivity.class);
        startActivity(intent);
    }

    @Override
    public void startServerConfigure() {
        Intent intent = new Intent(getContext(), ConfigureServerActivity.class);
        startActivity(intent);
    }
}
