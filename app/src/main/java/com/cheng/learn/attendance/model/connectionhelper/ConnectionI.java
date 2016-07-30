package com.cheng.learn.attendance.model.connectionhelper;

import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.util.Callback;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by cheng on 7/2/16.
 */
public interface ConnectionI {

    //the functions of connection
    void validateServer(final Callback<String> listener);
    void downloadClublist(final Callback<JSONArray> listener);
    void downloadNameList(final Callback<JSONArray> listener, int clubid);
    void attendance(final Callback<String> listener, ArrayList<Attendancedata> attendancedataList);
}
