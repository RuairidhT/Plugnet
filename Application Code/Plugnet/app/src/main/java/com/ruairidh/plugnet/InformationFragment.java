package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InformationFragment extends Fragment implements View.OnClickListener{

    View myView;

    TextView modes, threshold;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView =  inflater.inflate(R.layout.fragment_information, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Information");


        modes = myView.findViewById(R.id.modesTV);
        threshold = myView.findViewById(R.id.thresholdTV);
        modes.setOnClickListener(this);
        threshold.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modesTV:
                InformationModesFragment fragment = new InformationModesFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            case R.id.thresholdTV:
                InformationThresholdFragment fragment1 = new InformationThresholdFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

}
