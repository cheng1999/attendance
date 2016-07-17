package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;

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

    //gms vision
    CameraSource mCameraSource;

    public AttendanceCameraPresenter(Context context, AttendanceCameraContract.View View){
        mContext = context;
        mView = View;
        dboperator = new DbOperator(context);

        mView.setPresentor(this);
    }

    @Override
    public void start() {
        createCameraSource();
    }

    @Override
    public void createCameraSource(){

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(mContext).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());


        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(mContext, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setAutoFocusEnabled(true);
        }
    }

}
