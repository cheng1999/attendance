package com.cheng.learn.attendance.model.connectionhelper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheng.learn.attendance.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void validateServer(final Callback<String> listener) {

        final String url_validate =
        server_url+"/testserver";

        // Request a json response from the provided URL.
        // json style:
        //  {"version":"attendance.v1"}
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url_validate, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                //valid case
                                if (response.optString("version").equals("attendance.v1")) {
                                    listener.onResponse("ok");//valid
                                }
                                //invalid cases
                                else {
                                    listener.onError(new Exception("unmatch server:"+url_validate));
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
        String url_downloadClublist =
        server_url+"/getclublist";

        // Request a json response from the provided URL.
        // json style:
        /*{
          'clublist':
            {'clubid':1, 'clubname':'five_idiots_club'},
            {'clubid':2, 'clubname':'blahblah'}
          }*/
        JsonObjectRequest jsonObjectRequest =
            new JsonObjectRequest(Request.Method.GET, url_downloadClublist, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //valid case
                            if (response.has("clublist")) {
                                listener.onResponse(response.getJSONArray("clublist"));//valid
                            }
                            //invalid cases
                            if (response.has("error")) {
                                listener.onError(new Exception(response.optString("error")));
                            }
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

        String url_downloadNameList =
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
                new JsonObjectRequest(Request.Method.GET, url_downloadNameList, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //valid case
                                    if (response.has("namelist")) {
                                        listener.onResponse(response.getJSONArray("namelist"));//valid
                                    }
                                    //invalid cases
                                    if (response.has("error")) {
                                        listener.onError(new Exception(response.optString("error")));
                                    }
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
}
