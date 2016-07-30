package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;

import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;
import com.cheng.learn.attendance.model.dbhelper.DbOperator;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cheng on 7/15/16.
 */
public class AttendanceCameraPresenter implements AttendanceCameraContract.Presenter{

    private Context mContext;
    private AttendanceCameraContract.View mView;
    private DbOperator dboperator;

    //variables that will be insert into database
    private Date date;
    private int clubid;

    public AttendanceCameraPresenter(Context context, AttendanceCameraContract.View View){
        mContext = context;
        mView = View;
        dboperator = new DbOperator(context);

        date = new Date();
        clubid = dboperator.getMainClubid();

        mView.setPresentor(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void process_barcode(int barcode) {
        Attendancedata attendancedata = new Attendancedata(date, clubid, barcode, 1, null);

        String studentname = dboperator.getStudentnameByStudentno(attendancedata.studentno);
        Studentdata studentdata = new Studentdata(attendancedata.clubid, attendancedata.studentno, studentname);

        dboperator.attendance(
            attendancedata.date,
            attendancedata.clubid,
            attendancedata.studentno,
            attendancedata.status,
            attendancedata.remarks
        );

        mView.attend(studentdata);
    }

    @Override
    public ArrayList<Studentdata> getNameList() {
        return dboperator.getNamelist(clubid);
    }
}
