package com.cheng.learn.attendance.model;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by cheng on 7/30/16.
 */
public class LoadingDialog {

    public ProgressDialog loadingDialog;

    public LoadingDialog(Context context) {
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("Loading. Please wait...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
}
