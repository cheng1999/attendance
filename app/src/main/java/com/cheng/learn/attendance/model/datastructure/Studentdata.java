package com.cheng.learn.attendance.model.datastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheng on 7/12/16.
 */
public class Studentdata {

    public int clubid;//member of which club
    public int studentno;
    public String studentname;

    public Studentdata(int clubid, int studentno, String studentname){
        this.clubid = clubid;
        this.studentno = studentno;
        this.studentname = studentname;
    }

    // Constructor to convert JSON object into a Java class instance
    public Studentdata(JSONObject object) throws JSONException{
        this.clubid = object.getInt("clubid");
        this.studentno = object.getInt("studentno");
        this.studentname = object.getString("studentname");
    }


    /**
     * Factory method to convert an array of JSON objects into a list of objects
     * Studentdata.fromJsonArray(jsonArray);
     *
     * --the json:--
     * {"clubid":1,"studentno":2013045,"studentname":"Idiot Titus"},
     * {"clubid":1,"studentno":2013046,"studentname":"Genius Cheng"},
     * {"clubid":1,"studentno":2013047,"studentname":"Miss Ting Yong En"}
     * {"clubid":1,"studentno":2013048,"studentname":"Silent Mood Oscar"}
     */
    public static ArrayList<Studentdata> fromJsonArray(JSONArray jsonObjects) throws JSONException {
        ArrayList<Studentdata> students_data = new ArrayList<Studentdata>();
        for (int c = 0; c < jsonObjects.length(); c++) {
            students_data.add(new Studentdata(jsonObjects.getJSONObject(c)));
        }
        return students_data;
    }
}
