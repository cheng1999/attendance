package com.cheng.learn.attendance.configureserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cheng.learn.attendance.R;

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

        //define editText
        editText_ServerUrl = (EditText)v.findViewById(R.id.editText_ServerUrl);

        //setup dialog
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("Loading. Please wait...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);

        //setup button on click
        v.findViewById(R.id.button_ServerUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectingServer();
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
