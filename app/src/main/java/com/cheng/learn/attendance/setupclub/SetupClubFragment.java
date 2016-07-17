package com.cheng.learn.attendance.setupclub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.configureserver.ConfigureServerActivity;
import com.cheng.learn.attendance.model.datastructure.Clubdata;

import java.util.ArrayList;

public class SetupClubFragment extends Fragment implements SetupClubContract.View{

    SetupClubContract.Presenter mPresenter;
    ListView clublist_ListView;

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

        clublist_ListView = (ListView)v.findViewById(R.id.club_listView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void showClubList(ArrayList<Clubdata> clubs_data){
        final ClubItemListAdapter adapter = new ClubItemListAdapter(getContext(), clubs_data);
        clublist_ListView.setAdapter(adapter);

        clublist_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int clubid = (int)view.getTag();
                mPresenter.setMainClub(clubid);
                mPresenter.downloadNameList();
            }
        });
    }

    @Override
    public void finishActivity(){
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    /**
     * functions to start Activity
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
        if(requestCode==SERVER_CONFIGURE_CODE && resultCode == Activity.RESULT_OK){
            mPresenter.downloadClubList();
        }
    }
}
