package com.cheng.learn.attendance.setupclub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.datastructure.Clubdata;

import java.util.ArrayList;

/**
 * Created by cheng on 7/9/16.
 */
public class ClubItemListAdapter extends BaseAdapter {
    private ArrayList<Clubdata> clubs_data;
    private Context mContext;

    public ClubItemListAdapter(Context context, ArrayList<Clubdata> clubs_data) {
        mContext = context;
        this.clubs_data = clubs_data;
    }

    @Override
    public int getCount() {
        return clubs_data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Clubdata clubdata = clubs_data.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.club_item, parent, false);
        }

        TextView clubitem_textview = (TextView) convertView.findViewById(R.id.item_textview);
        clubitem_textview.setText(clubdata.clubname);

        //set tag so click listener can get the clubid by tag
        convertView.setTag(clubdata.clubid);

        // Return the completed view to render on screen
        return convertView;
    }
}
