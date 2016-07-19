package com.cheng.learn.attendance.attendancecamera;

import com.cheng.learn.attendance.model.ui.camera.BarcodeGraphic;
import com.cheng.learn.attendance.model.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private AttendanceCameraContract.Presenter mPresenter;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    /**
     *
     * @param presenter
     * @param barcodeGraphicOverlay
     */
    BarcodeTrackerFactory(AttendanceCameraContract.Presenter presenter, GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay) {
        mGraphicOverlay = barcodeGraphicOverlay;
        mPresenter = presenter;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeTracker(mPresenter, mGraphicOverlay, graphic);
    }

}
