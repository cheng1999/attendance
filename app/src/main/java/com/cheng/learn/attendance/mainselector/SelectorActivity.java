package com.cheng.learn.attendance.mainselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.util.ActivityUtils;

public class SelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_act);

        //create the fragment
        SelectorFragment selectorFragment =
                (SelectorFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(selectorFragment==null){
            selectorFragment = SelectorFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    selectorFragment, R.id.contentFrame);
        }

        //setup the presenter
        new SelectorPresenter(this,selectorFragment);

    }
}
