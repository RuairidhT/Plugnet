package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class InformationThresholdFragment extends Fragment {

    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_information_threshold, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Threshold Temperatures");

        return myView;
    }


}
