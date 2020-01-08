package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    View myView;

    TextView location, firmware;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_settings, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Settings");

        location = myView.findViewById(R.id.locationTV);
        firmware = myView.findViewById(R.id.firmwareTV);
        location.setOnClickListener(this);
        firmware.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationTV:
                SettingsLocationFragment fragment = new SettingsLocationFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            case R.id.firmwareTV:
                SettingsFirmwareFragment fragment1 = new SettingsFirmwareFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

}
