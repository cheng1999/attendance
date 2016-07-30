package com.cheng.learn.attendance.attendancecamera;

import com.cheng.learn.attendance.BasePresenter;
import com.cheng.learn.attendance.BaseView;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.util.ArrayList;

/**
 * Created by cheng on 7/15/16.
 */
public interface AttendanceCameraContract {
    interface View extends BaseView<Presenter> {
        void attend(Studentdata studentdata);
        void finishActivity();

    }
    interface Presenter extends BasePresenter{
        //the method will call when scanned a barcode
        void process_barcode(int barcode);

        ArrayList<Studentdata> getNameList();
    }
}
