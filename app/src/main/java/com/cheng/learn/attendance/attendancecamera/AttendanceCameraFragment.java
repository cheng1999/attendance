package com.cheng.learn.attendance.attendancecamera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.learn.attendance.R;

public class AttendanceCameraFragment extends Fragment implements AttendanceCameraContract.View{

    AttendanceCameraContract.Presenter mPresenter;

    //view's variables
    SurfaceView cameraView;
    ListView attend_ListView, absent_ListView;


    public AttendanceCameraFragment() {
        // Required empty public constructor
    }

    public static AttendanceCameraFragment newInstance() {
        return new AttendanceCameraFragment();
    }

    @Override
    public void setPresentor(AttendanceCameraContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.attendance_camera_frag, container, false);

        //Initial the variables of View
        cameraView = (SurfaceView)v.findViewById(R.id.camera_view);
        attend_ListView = (ListView)v.findViewById(R.id.attend_listView);
        absent_ListView = (ListView)v.findViewById(R.id.absent_listView);

        //add title to ListViews
        TextView attend_Title = new TextView(getContext()),
                 absent_Title = new TextView(getContext());

        attend_Title.setText("attend:");
        absent_Title.setText("absent:");

        attend_ListView.addHeaderView(attend_Title);
        absent_ListView.addHeaderView(absent_Title);

        //created view
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }
}
