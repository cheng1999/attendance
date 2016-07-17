package com.cheng.learn.attendance.attendancecamera;

import com.cheng.learn.attendance.BasePresenter;
import com.cheng.learn.attendance.BaseView;

/**
 * Created by cheng on 7/15/16.
 */
public interface AttendanceCameraContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter{
        void createCameraSource();

    }
}
