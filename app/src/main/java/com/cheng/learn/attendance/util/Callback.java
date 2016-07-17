package com.cheng.learn.attendance.util;

/**
 * Created by cheng on 7/8/16.
 */
public interface Callback<T> {
    void onResponse(T response);
    void onError(Exception error);
}
