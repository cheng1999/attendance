package com.cheng.learn.attendance.attendancecamera;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.datastructure.Studentdata;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class AttendanceCameraFragment extends Fragment implements AttendanceCameraContract.View{

    AttendanceCameraContract.Presenter mPresenter;

    // view's variables
    SurfaceView cameraView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    ListView attend_ListView, absent_ListView;
    FloatingActionButton fab;

    NamelistItemListAdapter attend_itemlist_adapter;
    NamelistItemListAdapter absent_itemlist_adapter;

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

        // Initial the variables of View
        cameraView = (SurfaceView) v.findViewById(R.id.camera_view);
        attend_ListView = (ListView)v.findViewById(R.id.attend_listView);
        absent_ListView = (ListView)v.findViewById(R.id.absent_listView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        // add title to ListViews
        TextView attend_Title = new TextView(getContext()),
                 absent_Title = new TextView(getContext());

        attend_Title.setText("attend:");
        absent_Title.setText("absent:");

        attend_ListView.addHeaderView(attend_Title);
        absent_ListView.addHeaderView(absent_Title);

        //floating action button click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        // created view
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCameraSource();

        // setup ListView
        ArrayList<Studentdata> namelist = mPresenter.getNameList();
        absent_itemlist_adapter = new NamelistItemListAdapter(getContext(), namelist);
        absent_ListView.setAdapter(absent_itemlist_adapter);

        attend_itemlist_adapter = new NamelistItemListAdapter(getContext(), null);
        attend_ListView.setAdapter(attend_itemlist_adapter);

    }


    /**
     *
     * part contain methods which will call from presenter
     */
    @Override
    public void attend(Studentdata studentdata) {
        attend_itemlist_adapter.add(studentdata);
        absent_itemlist_adapter.remove(studentdata);
    }

    @Override
    public void finishActivity(){
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    /**
     *
     * internal method
     */

    private void createCameraSource(){

        barcodeDetector =
                new BarcodeDetector.Builder(getContext()).build();

        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();


        // add callback for surfaceholder to handle camera lifecycle
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            @SuppressWarnings({"MissingPermission"})
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    Log.e("CAMERA SOURCE", e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // processor to process barcodes
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                for(int c=0;c<=barcodes.size();c++){
                    try{
                        int studentno_barcode = barcodes.valueAt(c).valueFormat;
                        mPresenter.process_barcode(studentno_barcode);
                    }catch(Exception e){}//ignore if not integer
                }
            }
        });
    }

}
