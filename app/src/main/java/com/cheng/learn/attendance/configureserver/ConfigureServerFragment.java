package com.cheng.learn.attendance.configureserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.learn.attendance.R;
import com.cheng.learn.attendance.model.LoadingDialog;

public class ConfigureServerFragment extends Fragment implements ConfigureServerContract.View{

    private ConfigureServerContract.Presenter mConfigureServerPresenter;
    EditText editText_ServerUrl;
    ProgressDialog loadingDialog;


    public ConfigureServerFragment() {
        // Required empty public constructor
    }

    public static ConfigureServerFragment newInstance() {
        ConfigureServerFragment fragment = new ConfigureServerFragment();
        return fragment;
    }

    @Override
    public void setPresentor(ConfigureServerContract.Presenter presenter) {
        mConfigureServerPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.configure_server_frag, container, false);

        // define editText
        editText_ServerUrl = (EditText)v.findViewById(R.id.editText_ServerUrl);

        // setup dialog
        loadingDialog = new LoadingDialog(getContext()).loadingDialog;

        // setup button on click
        v.findViewById(R.id.button_ServerUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectingServer();
            }
        });
        // edit text enter key listener
        editText_ServerUrl.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    connectingServer();
                    return true;
                }
                return false;
            }
        });

        return v;
    }


    /**
     *
     * part contain methods which will call from presenter
     */
    @Override
    public void connectingServer(){
        String url = editText_ServerUrl.getText().toString();
        mConfigureServerPresenter.validateServer(url);
        loadingDialog.show();
    }

    @Override
    public void retryConfigure(String errormsg){
        Toast.makeText(getContext(), "failed, please retry again: =\n"+errormsg, Toast.LENGTH_LONG).show();
        loadingDialog.dismiss();
    }


    /**
     * internal method
     */
    @Override
    public void finishedActivity() {
        //loadingDialog.dismiss();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }



}
