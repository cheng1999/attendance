package com.cheng.learn.attendance.mainselector;

import android.content.Context;

import com.cheng.learn.attendance.model.dbhelper.DbOperator;

/**
 * Created by cheng on 7/1/16.
 */
public class SelectorPresenter implements SelectorContract.Presenter {

    private SelectorContract.View mSelectorView;
    private DbOperator dboperator;

    public SelectorPresenter(Context context, SelectorContract.View SeletorView){
        dboperator = new DbOperator(context);
        mSelectorView = SeletorView;

        mSelectorView.setPresentor(this);
    }

    /**
     * init
     */
    @Override
    public void start() {
        //if have no setup club yet
        if(dboperator.getClubid()==0){
            mSelectorView.startSetup();
        }
    }

    @Override
    public void startScanBarcode(){

    }

    @Override
    public void setupClub(){

    }
}
