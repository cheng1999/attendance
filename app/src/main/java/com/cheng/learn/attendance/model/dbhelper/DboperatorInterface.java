package com.cheng.learn.attendance.model.dbhelper;

import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.model.datastructure.Clubdata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cheng on 7/2/16.
 */
public interface DboperatorInterface {

    /**
     * read database
     */
    String getServerurl();
    int getMainClubid();
    String getStudentnameByStudentno(int studentno);
    ArrayList<Clubdata> getClublist();
    ArrayList<Studentdata> getNamelist(int clubid);
    ArrayList<Attendancedata> getAttendancelist(int clubid, Date date, boolean includedSynced);

    /**
     * write database
     */
    void setServerurl(String url);
    void setMainclub(int clubid);

    //import the Club data in  Clubdata's array list
    void importClublist(ArrayList<Clubdata> clublist);
    void importNamelist(ArrayList<Studentdata> namelist);

    //this not need is because I want it insert into database one by one not together
    //@param date : 20161231
    void attendance(int date, int clubid, int studentno, int status, String remarks);
    void syncedAttendance(ArrayList<Attendancedata> attendancelist);
}
