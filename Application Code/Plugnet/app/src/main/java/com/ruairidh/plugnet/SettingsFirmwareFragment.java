package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SettingsFirmwareFragment extends Fragment {

    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_settings_firmware, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Firmware");

        return myView;
    }


}
