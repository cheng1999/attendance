package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
        //Toast.makeText(mContext, "barcode: "+barcode, Toast.LENGTH_LONG).show();
        Attendancedata attendancedata = new Attendancedata(date, clubid, barcode, 1, null);

        Studentdata studentdata = attendancedata_to_studentdata(attendancedata);

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
    public void process_attended(){
        ArrayList<Attendancedata> attendancedata_list = dboperator.getAttendancelist(clubid,new Date(),false);
        for (int c=0;c<attendancedata_list.size();c++){
            Attendancedata attendancedata = attendancedata_list.get(c);
            if (attendancedata.status==Attendancedata.Status.attend){
                mView.attend(attendancedata_to_studentdata(attendancedata));
            }
        }
    }

    @Override
    public ArrayList<Studentdata> getNameList() {
        return dboperator.getNamelist(clubid);
    }

    /**
     * internal method
     */

    private Studentdata attendancedata_to_studentdata(Attendancedata attendancedata){
        String studentname = dboperator.getStudentnameByStudentno(attendancedata.studentno);
        Studentdata studentdata = new Studentdata(attendancedata.clubid, attendancedata.studentno, studentname);

        return studentdata;
    }
}
