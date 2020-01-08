package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HelpFragment extends Fragment implements View.OnClickListener {

    View myView;

    TextView faq, account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_help, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Help");

        faq = myView.findViewById(R.id.faqTV);
        account = myView.findViewById(R.id.accountTV);
        faq.setOnClickListener(this);
        account.setOnClickListener(this);


        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faqTV:
                HelpFAQFragment fragment = new HelpFAQFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            case R.id.accountTV:
                HelpAccountFragment fragment1 = new HelpAccountFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }
}
