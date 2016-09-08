package com.cheng.learn.attendance.model.datastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cheng on 7/14/16.
 */
public class Attendancedata {

    public int date;
    public int clubid;
    public int studentno;
    public int status;
    public String remarks;

    public Attendancedata(Date date, int clubid, int studentno, int status, String remarks) {

        DateFormat ft = new SimpleDateFormat("yyyyMMdd");
        String datestring = ft.format(date);
        this.date = Integer.parseInt(datestring); //date in yyyyMMdd format to store into database
        this.clubid = clubid;
        this.studentno = studentno;
        this.status = status;
        this.remarks = remarks;
    }

    //this one is integer date
    public Attendancedata(int date, int clubid, int studentno, int status, String remarks) {

        this.date = date;
        this.clubid = clubid;
        this.studentno = studentno;
        this.status = status;
        this.remarks = remarks;
    }

    /**
     * Factory method to export JSON array
     * Attendance.toJsonArray(jsonArray);
     * <p>
     * --the json:--
      [
            {'date':'20161231,clubid:1,'studentno':2013045,'status':1,'remarks':null},
            {'date':'20161231,clubid:1,'studentno':2013046,'status':1,'remarks':null},
            {'date':'20161231,clubid:1,'studentno':2013047,'status':2,'remarks':null},
            {'date':'20161231,clubid:1,'studentno':2013048,'status':3,'remarks':'death and wait for reborn'},
            {'date':'20161231,clubid:1,'studentno':2013049,'status':4,'remarks':'back home and play cs'},
      ]
     */

    public static JSONArray toJsonArray(ArrayList<Attendancedata> attendancelist) throws JSONException {
        JSONArray JSON_Array = new JSONArray();
        for(int c=0;c<attendancelist.size();c++){
            JSONObject JSON_object= new JSONObject();
            Attendancedata attendancestudent = attendancelist.get(c);

            JSON_object.put("date"       ,attendancestudent.date);
            JSON_object.put("clubid"     ,attendancestudent.clubid);
            JSON_object.put("studentno"  ,attendancestudent.studentno);
            JSON_object.put("status"     ,attendancestudent.status);
            JSON_object.put("remarks"    ,attendancestudent.remarks);

            JSON_Array.put(JSON_object);
        }
        return JSON_Array;
    }
}
