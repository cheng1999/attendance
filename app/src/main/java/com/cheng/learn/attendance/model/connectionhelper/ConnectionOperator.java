package com.cheng.learn.attendance.model.connectionhelper;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cheng on 7/5/16.
 */
public class ConnectionOperator implements ConnectionI {

    RequestQueue queue;
    String server_url;

    /**
     * setup one time for handler more requests
     * @param context
     */
    public ConnectionOperator(Context context, String server_url){
        queue = Volley.newRequestQueue(context);
        this.server_url = server_url;
    }

    private void checkServerError(final Callback listener,JSONObject response){
        if(response.has("error")){
            listener.onError(new Exception("Server's ERROR: "+response.optString("error")));
        }
    }

    @Override
    public void validateServer(final Callback<String> listener) {

        final String url =
            server_url+"/testserver";

        // Request a json response from the provided URL.
        // json style:
        //  {"version":"attendance.v1"}
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                //valid case
                                if (response.optString("version").equals("attendance.v1")) {
                                    listener.onResponse("attendance.v1");//valid
                                }
                                //invalid cases
                                else {
                                    listener.onError(new Exception("unmatch server:"+url));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.onError(error);
                            }
                        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void downloadClublist(final Callback<JSONArray> listener) {

        String url =
            server_url+"/getclublist";

        // Request a json response from the provided URL.
        // json style:
        /*{
          'clublist':
            {'clubid':1, 'clubname':'five_idiots_club'},
            {'clubid':2, 'clubname':'blahblah'}
          }*/
        JsonObjectRequest jsonObjectRequest =
            new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            checkServerError(listener, response);
                            listener.onResponse(response.getJSONArray("clublist"));//valid

                        }catch(JSONException e){
                            listener.onError(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void downloadNameList(final Callback<JSONArray> listener, int clubid) {

        String url =
                server_url+"/getnamelist/"+clubid;

        // Request a json response from the provided URL.
        // json style:
        /*{
            "namelist":[
             {"studentno":2013045,"studentname":"Idiot Titus"},
             {"studentno":2013046,"studentname":"Genius Cheng"},
             {"studentno":2013047,"studentname":"Miss Ting Yong En"}
             {"studentno":2013048,"studentname":"Silent Mood Oscar"}
            ]
          }*/
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    checkServerError(listener, response);
                                    listener.onResponse(response.getJSONArray("clublist"));//valid

                                }catch(JSONException e){
                                    listener.onError(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.onError(error);
                            }
                        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void attendance(final Callback<String> listener, final ArrayList<Attendancedata> attendancedataList) {

        String url =
                server_url+"/attendance";

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                checkServerError(listener, response);
                                listener.onResponse(response.optString("status"));//valid
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.onError(error);
                            }
                        }){

                    //post method
                    @Override
                    protected HashMap<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap();
                        try {
                            hashMap.put("attendance", Attendancedata.toJsonArray(attendancedataList).toString());
                        }catch(Exception e){
                            listener.onError(e);
                        }
                        return hashMap;
                    }
                };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}
