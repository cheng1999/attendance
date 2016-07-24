package com.cheng.learn.attendance.setupclub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.configureserver.ConfigureServerActivity;
import com.cheng.learn.attendance.model.datastructure.Clubdata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.util.ArrayList;

public class SetupClubFragment extends Fragment implements SetupClubContract.View{

    SetupClubContract.Presenter mPresenter;
    ListView listView;
    FloatingActionButton fab;

    public static SetupClubFragment newInstance() {return new SetupClubFragment();}

    public SetupClubFragment() {
        // Required empty public constructor
    }

    @Override
    public void setPresentor(SetupClubContract.Presenter presenter) {mPresenter = presenter;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.setup_club_frag, container, false);

        listView = (ListView)v.findViewById(R.id.listView);

        //on fab clicked
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }


    /**
     *
     * part contain methods which will call from presenter
     */
    @Override
    public void showClubList(ArrayList<Clubdata> club_list){
        final ClubItemListAdapter adapter = new ClubItemListAdapter(getContext(), club_list);

        //now listView is for showing the list of club
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Clubdata clubdata = (Clubdata)view.getTag();

                mPresenter.setMainClub(clubdata.clubid);
                mPresenter.downloadNameList();

                getActivity().setTitle(clubdata.clubname);
            }
        });
    }

    @Override
    public void showNameList(ArrayList<Studentdata> student_list) {
        //disable onclick listener
        //listView.setOnItemClickListener(null);
        listView.setClickable(false);

        final MemberItemListAdapter adapter = new MemberItemListAdapter(getContext(), student_list);

        //now listView is for showing the list of student
        listView.setAdapter(adapter);

        //enable fab
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishActivity(){
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    /*
        functions to start Activity
     */

    public final int SERVER_CONFIGURE_CODE = 1;

    @Override
    public void startServerConfigure() {
        Intent intent = new Intent(getContext(), ConfigureServerActivity.class);
        startActivityForResult(intent, SERVER_CONFIGURE_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //server configure done
        if(requestCode == SERVER_CONFIGURE_CODE && resultCode == Activity.RESULT_OK){
            mPresenter.downloadClubList();
        }
    }
}
