package com.cheng.learn.attendance.setupclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.util.ActivityUtils;

public class SetupClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_club_act);

        //create the fragment
        SetupClubFragment setupClubFragment =
                (SetupClubFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(setupClubFragment==null){
            setupClubFragment = SetupClubFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    setupClubFragment, R.id.contentFrame);
        }

        //setup the presenter
        new SetupClubPresenter(this, setupClubFragment);

    }
}
