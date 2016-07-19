package com.cheng.learn.attendance.configureserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.util.ActivityUtils;

public class ConfigureServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_server_act);

        //create the fragment
        ConfigureServerFragment ConfigureServerFragment =
                (ConfigureServerFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (ConfigureServerFragment == null) {
            ConfigureServerFragment = ConfigureServerFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    ConfigureServerFragment, R.id.contentFrame);
        }

        // Create the presenter
        new ConfigureServerPresenter(
                getApplicationContext(),
                ConfigureServerFragment
        );
    }
}
