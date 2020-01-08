package com.ruairidh.plugnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PasswordResetFragment extends Fragment implements View.OnClickListener{
    private View myView;

    private EditText oldPswd;
    private EditText newPswd1;

    private Button save;
    private Button cancel;

    private TextView forgotPswd;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_password_reset, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("Reset Password");

        oldPswd = myView.findViewById(R.id.oldPassword);
        newPswd1 = myView.findViewById(R.id.newPassword1);

        save = myView.findViewById(R.id.updatedPassword);
        save.setOnClickListener(this);
        cancel = myView.findViewById(R.id.cancelChanges);
        cancel.setOnClickListener(this);
        forgotPswd = myView.findViewById(R.id.forgottenPassword);
        forgotPswd.setOnClickListener(this);


        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatedPassword:
                updatePassword();
                break;
            case R.id.cancelChanges:
                ProfileFragment fragment = new ProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.forgottenPassword:
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void updatePassword() {

        final String email = user.getEmail();
        final String oldPassword = oldPswd.getText().toString().trim();
        final String newPass = newPswd1.getText().toString().trim();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Password changed", Toast.LENGTH_SHORT).show();
                                        ProfileFragment fragment = new ProfileFragment();
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
