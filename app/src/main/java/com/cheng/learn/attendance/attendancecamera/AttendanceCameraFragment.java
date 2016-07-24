package com.cheng.learn.attendance.attendancecamera;

import android.app.Dialog;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.datastructure.Studentdata;
import com.cheng.learn.attendance.model.ui.camera.BarcodeGraphic;
import com.cheng.learn.attendance.model.ui.camera.CameraSource;
import com.cheng.learn.attendance.model.ui.camera.CameraSourcePreview;
import com.cheng.learn.attendance.model.ui.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class AttendanceCameraFragment extends Fragment implements AttendanceCameraContract.View{

    AttendanceCameraContract.Presenter mPresenter;

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // view's variables
    CameraSource mCameraSource;
    CameraSourcePreview mPreview;
    GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    ListView attend_ListView, absent_ListView;

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
        mPreview = (CameraSourcePreview) v.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) v.findViewById(R.id.graphicOverlay);
        attend_ListView = (ListView)v.findViewById(R.id.attend_listView);
        absent_ListView = (ListView)v.findViewById(R.id.absent_listView);

        // add title to ListViews
        TextView attend_Title = new TextView(getContext()),
                 absent_Title = new TextView(getContext());

        attend_Title.setText("attend:");
        absent_Title.setText("absent:");

        attend_ListView.addHeaderView(attend_Title);
        absent_ListView.addHeaderView(absent_Title);

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
        attend_ListView.setAdapter(absent_itemlist_adapter);
    }


    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
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


    /**
     * internal method
     */


    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
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
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        mCameraSource = builder.build();
    }


    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                /**
                 * don't know how to handler yet
                 */
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
}
