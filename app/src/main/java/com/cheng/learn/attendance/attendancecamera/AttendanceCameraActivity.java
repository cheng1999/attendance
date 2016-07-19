package com.cheng.learn.attendance.attendancecamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.util.ActivityUtils;

public class AttendanceCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_camera_act);

        //create the fragment
        AttendanceCameraFragment AttendanceCameraFragment =
                (AttendanceCameraFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (AttendanceCameraFragment == null) {
            AttendanceCameraFragment = AttendanceCameraFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    AttendanceCameraFragment, R.id.contentFrame);
        }

        // Create the presenter
        new AttendanceCameraPresenter(
                getApplicationContext(),
                AttendanceCameraFragment
        );
    }
}
