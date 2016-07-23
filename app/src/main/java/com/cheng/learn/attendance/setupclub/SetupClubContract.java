package com.cheng.learn.attendance.setupclub;

import com.cheng.learn.attendance.BasePresenter;
import com.cheng.learn.attendance.BaseView;
import com.cheng.learn.attendance.model.datastructure.Clubdata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.util.ArrayList;

/**
 * Created by cheng on 7/2/16.
 */
public interface SetupClubContract{
    interface View extends BaseView<Presenter> {
        void startServerConfigure();
        void showClubList(ArrayList<Clubdata> club_list);
        void showNameList(ArrayList<Studentdata> student_list);
        void finishActivity();
    }
    interface Presenter extends BasePresenter {
        void downloadClubList();
        void downloadNameList();
        void setMainClub(int clubid);
    }
}
