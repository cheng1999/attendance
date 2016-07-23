package com.cheng.learn.attendance.attendancecamera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.util.ArrayList;

/**
 * Created by cheng on 7/23/16.
 */
public class NamelistItemListAdapter extends BaseAdapter {
    private ArrayList<Studentdata> student_list;
    private Context mContext;

    public NamelistItemListAdapter(Context context, ArrayList<Studentdata> student_list) {
        mContext = context;
        this.student_list = student_list;
    }

    @Override
    public int getCount() {
        return student_list.size();
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
        Studentdata studentdata = student_list.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_item, parent, false);
        }

        TextView item_textview = (TextView) convertView.findViewById(R.id.item_textview);
        item_textview.setText(studentdata.studentname);

        // Return the completed view to render on screen
        return convertView;
    }

    public void add(Studentdata studentdata){
        student_list.add(studentdata);
        notifyDataSetChanged();
    }

    public void remove(Studentdata studentdata){
        student_list.remove(studentdata);
        notifyDataSetChanged();
    }
}
