package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.widget.Toast;

import com.cheng.learn.attendance.model.dbhelper.DbOperator;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by cheng on 7/15/16.
 */
public class AttendanceCameraPresenter implements AttendanceCameraContract.Presenter{

    private Context mContext;
    private AttendanceCameraContract.View mView;
    private DbOperator dboperator;

    public AttendanceCameraPresenter(Context context, AttendanceCameraContract.View View){
        mContext = context;
        mView = View;
        dboperator = new DbOperator(context);

        mView.setPresentor(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void process_barcode(int barcode) {
        //Toast.makeText(mContext,barcode,Toast.LENGTH_LONG).show();
    }
}
