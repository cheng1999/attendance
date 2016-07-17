package com.cheng.learn.attendance.setupclub;

import android.content.Context;

import com.cheng.learn.attendance.model.connectionhelper.ConnectionOperator;
import com.cheng.learn.attendance.model.datastructure.Clubdata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;
import com.cheng.learn.attendance.model.dbhelper.DbOperator;
import com.cheng.learn.attendance.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by cheng on 7/2/16.
 */
public class SetupClubPresenter implements SetupClubContract.Presenter{

    private Context mContext;
    private SetupClubContract.View mSetupClubView;
    private ConnectionOperator connectionOperator;
    private DbOperator dboperator;

    public SetupClubPresenter(Context context, SetupClubContract.View SetupClubView){
        mContext = context;
        mSetupClubView = SetupClubView;
        dboperator = new DbOperator(context);

        mSetupClubView.setPresentor(this);
    }

    @Override
    public void start() {

        //if already have clublist in database
        ArrayList<Clubdata> clubs_data = dboperator.getClublist();
        if(clubs_data.size()==0){
            mSetupClubView.showClubList(clubs_data);
            return ;//end this function
        }

        //validate url first
        String server_url = dboperator.getServerurl();

        connectionOperator = new ConnectionOperator(mContext,server_url);
        connectionOperator.validateServer(
            new Callback<String>(){
            @Override
            public void onResponse(String response) {
                downloadClubList();
            }
            @Override
            public void onError(Exception error){
                //start ConfigureServerActivity to reconfigure
                mSetupClubView.startServerConfigure();
            }
            }
        );
    }

    @Override
    public void downloadClubList() {
        //renew server_url because the server url maybe reconfigured
        renewServerURL();

        connectionOperator.downloadClublist(
            new Callback<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        ArrayList<Clubdata> clubs_data = Clubdata.fromJsonArray(response);
                        dboperator.importClublist(clubs_data);
                        mSetupClubView.showClubList(clubs_data);
                    }catch(JSONException e){
                        /**
                         * don know how to handler yet~ lalala~
                         */
                    }
                }
                @Override
                public void onError(Exception error) {
                    /**
                     * don know how to handler yet~ lalala~
                     */
                }
            });
    }

    @Override
    public void downloadNameList() {
        //renew server_url because the server url maybe reconfigured
        renewServerURL();

        //reference from interface:
        //  void downloadNameList(final Callback<JSONArray> listener, int clubid);
        connectionOperator.downloadNameList(
                new Callback<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            ArrayList<Studentdata> students_data = Studentdata.fromJsonArray(response);
                            dboperator.importNamelist(students_data);
                            //mSetupClubView.showNameList(students_data);
                        }catch(JSONException e){
                            /**
                             * don know how to handler yet~ lalala~
                             */
                        }
                    }

                    @Override
                    public void onError(Exception error) {
                        /**
                         * don know how to handler yet~ lalala~
                         */
                    }
                },
                dboperator.getClubid()//the club which want to download its namelist
        );
    }

    @Override
    public void setMainClub(int clubid) {
        dboperator.setMainclub(clubid);
    }

    ////function to renew the server_url for connection
    private void renewServerURL(){
        String server_url = dboperator.getServerurl();
        connectionOperator = new ConnectionOperator(mContext,server_url);
    }
}
