package com.cheng.learn.attendance.attendancecamera;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.ui.camera.BarcodeGraphic;
import com.cheng.learn.attendance.model.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class AttendanceCameraFragment extends Fragment implements AttendanceCameraContract.View{

    AttendanceCameraContract.Presenter mPresenter;

    //view's variables
    GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
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
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) v.findViewById(R.id.graphicOverlay);
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
        createCameraSource();
    }

    public void createCameraSource(){

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext()).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mPresenter, mGraphicOverlay);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());


        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(getContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setAutoFocusEnabled(true);
        }
    }
}
