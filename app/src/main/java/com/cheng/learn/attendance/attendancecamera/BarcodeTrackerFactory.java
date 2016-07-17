package com.cheng.learn.attendance.attendancecamera;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    AttendanceCameraContract.Presenter mPresenter;

    BarcodeTrackerFactory(AttendanceCameraPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeTracker(mPresenter);
    }

}
