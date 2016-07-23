package com.cheng.learn.attendance.mainselector;

import com.cheng.learn.attendance.BasePresenter;
import com.cheng.learn.attendance.BaseView;

/**
 * Created by cheng on 7/1/16.
 */
public interface SelectorContract {
    interface View extends BaseView<Presenter>{
        void startSetup();
        void startScanBarcode();
    }

    interface Presenter extends BasePresenter{
    }
}
