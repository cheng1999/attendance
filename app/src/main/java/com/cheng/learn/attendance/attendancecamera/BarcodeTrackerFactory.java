package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;
import android.widget.Toast;

import com.cheng.learn.attendance.model.connectionhelper.ConnectionOperator;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by cheng on 9/4/16.
 */
public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode>{
    private AttendanceCameraContract.Presenter mPresenter;
    private Context mContext;

    public BarcodeTrackerFactory(Context context,AttendanceCameraContract.Presenter presenter){
        mContext = context;
        mPresenter = presenter;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeTracker<>();
    }

    private class BarcodeTracker<T> extends Tracker<T>{
        @Override
        public void onUpdate(Detector.Detections<T> detectionResults, T item) {
            Barcode barcode = (Barcode)item;
            //try {
                int studentno_barcode = Integer.parseInt(barcode.displayValue);
                mPresenter.process_barcode(studentno_barcode);
            /*}catch(Exception e){//ignore if barcode is not interger
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }*/

        }
    }
}
