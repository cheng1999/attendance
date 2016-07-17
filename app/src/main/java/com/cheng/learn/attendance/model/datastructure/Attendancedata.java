package com.cheng.learn.attendance.model.datastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        this.date = Integer.getInteger(ft.format(date)); //date in yyyyMMdd format to store into database
        this.clubid = clubid;
        this.studentno = studentno;
        this.status = status;
        this.remarks = remarks;
    }

    /**
     * Factory method to export JSON objects
     * Attendance.toJsonArray(jsonArray);
     * <p>
     * --the json:--
      {
        'date':'20161231',
        'clubid':1,
        'attendance':[
            {'studentno':2013045,'status':1,'remarks':null},
            {'studentno':2013046,'status':1,'remarks':null},
            {'studentno':2013047,'status':2,'remarks':null},
            {'studentno':2013048,'status':3,'remarks':'death and wait for reborn'},
            {'studentno':2013049,'status':4,'remarks':'back home and play cs'},
        ]
      }
     */

    public static JSONObject toJson(ArrayList<Attendancedata> attendancelist) throws JSONException {

        int date = attendancelist.get(0).date;
        int clubid = attendancelist.get(0).clubid;

        JSONObject JSON = new JSONObject();
        JSON.put("date",date);
        JSON.put("clubid",clubid);

        JSONArray attendance_students_JSONArray = new JSONArray();
        for(int c=0;c<attendancelist.size();c++){
            JSONObject attendance_student_obj= new JSONObject();
            Attendancedata attendancestudent = attendancelist.get(c);

            attendance_student_obj.put("studentno",attendancestudent.studentno);
            attendance_student_obj.put("status",attendancestudent.status);
            attendance_student_obj.put("remarks",attendancestudent.remarks);

            attendance_students_JSONArray.put(attendance_student_obj);
        }
        JSON.put("attendance",attendance_students_JSONArray);

        return JSON;
    }
}
