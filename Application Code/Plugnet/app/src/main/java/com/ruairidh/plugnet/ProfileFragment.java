package com.ruairidh.plugnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private TextView fName;
    private View myView;
    private Button logoutBtn;
    private TextView changePassword;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_profile, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();

        fName = myView.findViewById(R.id.tvFName);

        loadUserInformation();

        changePassword = myView.findViewById(R.id.passwordReset);
        changePassword.setOnClickListener(this);

        logoutBtn = myView.findViewById(R.id.logout);
        logoutBtn.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                if (mAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), beginningActivity.class);
                    startActivity(intent);
                    this.getActivity().finish();
                }
                break;
            case R.id.passwordReset:
                PasswordResetFragment fragment = new PasswordResetFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    public void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            fName.setText(user.getEmail());
        }
    }
}
