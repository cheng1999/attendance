package com.cheng.learn.attendance.mainselector;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.cheng.learn.attendance.model.LoadingDialog;
import com.cheng.learn.attendance.model.connectionhelper.ConnectionOperator;
import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.model.dbhelper.DbOperator;
import com.cheng.learn.attendance.util.Callback;

import java.util.ArrayList;

/**
 * Created by cheng on 7/1/16.
 */
public class SelectorPresenter implements SelectorContract.Presenter {

    private SelectorContract.View mSelectorView;
    private DbOperator dboperator;
    private Context mContext;
    private ConnectionOperator connectionOperator;

    public SelectorPresenter(Context context, SelectorContract.View SeletorView){
        mContext = context;
        dboperator = new DbOperator(context);
        mSelectorView = SeletorView;

        mSelectorView.setPresentor(this);
    }

    @Override
    public void start() {
        //if have no setup club yet
        if(dboperator.getMainClubid()==0){
            mSelectorView.startSetup();
        }
    }

    @Override
    public void SyncAttendance() {
        int clubid = dboperator.getMainClubid();
        String server_url = dboperator.getServerurl();
        final ArrayList<Attendancedata> attendancedataList = dboperator.getAttendancelist(clubid, null, dboperator.NOT_INCLUDED_SYNCED);

        final ProgressDialog loadingDialog = new LoadingDialog(mContext).loadingDialog;
        loadingDialog.show();

        connectionOperator.attendance(
                new Callback<String>(){
                    @Override
                    public void onResponse(String response) {
                        dboperator.syncedAttendance(attendancedataList);
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(Exception e){
                        Toast.makeText(mContext, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                attendancedataList
        );
    }
}
