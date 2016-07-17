package com.cheng.learn.attendance.model.datastructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheng on 7/9/16.
 */
public class Clubdata {

    public int clubid;
    public String clubname;

    public Clubdata(int clubid, String clubname){
        this.clubid = clubid;
        this.clubname = clubname;
    }

    // Constructor to convert JSON object into a Java class instance
    public Clubdata(JSONObject object) throws JSONException{
        try {
            this.clubid = object.getInt("clubid");
            this.clubname = object.getString("clubname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Factory method to convert an array of JSON objects into a list of objects
     * Clubdata.fromJsonArray(jsonArray);
     *
     * --the json:--
     *  {'clubid':1, 'clubname':'five_idiots_club'},
     *  {'clubid':2, 'clubname':'blahblah'}
     */
    public static ArrayList<Clubdata> fromJsonArray(JSONArray jsonObjects) throws JSONException {
        ArrayList<Clubdata> clubs_data = new ArrayList<Clubdata>();
        for (int c = 0; c < jsonObjects.length(); c++) {
            clubs_data.add(new Clubdata(jsonObjects.getJSONObject(c)));
        }
        return clubs_data;
    }
}
