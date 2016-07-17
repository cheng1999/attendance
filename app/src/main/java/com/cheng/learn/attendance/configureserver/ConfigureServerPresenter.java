package com.cheng.learn.attendance.configureserver;

import android.content.Context;

import com.cheng.learn.attendance.model.connectionhelper.ConnectionOperator;
import com.cheng.learn.attendance.model.dbhelper.DbOperator;
import com.cheng.learn.attendance.util.Callback;

/**
 * Created by cheng on 7/7/16.
 */
public class ConfigureServerPresenter implements ConfigureServerContract.Presenter {

    ConfigureServerContract.View mConfigureServerView;
    Context mContext;

    public ConfigureServerPresenter(Context context, ConfigureServerContract.View ConfigureServerView){
        mContext = context;
        mConfigureServerView = ConfigureServerView;

        ConfigureServerView.setPresentor(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void validateServer(final String server_url){

        new ConnectionOperator(mContext,server_url).validateServer(
            new Callback<String>(){
            @Override
            public void onResponse(String response) {
                new DbOperator(mContext).setServerurl(server_url);
                mConfigureServerView.finishedActivity();
            }
            @Override
            public void onError(Exception e){
                mConfigureServerView.retryConfigure(e.toString());
            }
            }
        );

    }
}
