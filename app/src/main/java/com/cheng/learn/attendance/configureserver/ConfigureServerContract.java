package com.cheng.learn.attendance.configureserver;

import com.cheng.learn.attendance.BasePresenter;
import com.cheng.learn.attendance.BaseView;

/**
 * Created by cheng on 7/7/16.
 */
public interface ConfigureServerContract {
    interface View extends BaseView<Presenter> {
        void connectingServer();
        void retryConfigure(String errormsg);
        void finishedActivity();
    }

    interface Presenter extends BasePresenter {
        void validateServer(final String server_url);
    }
}
