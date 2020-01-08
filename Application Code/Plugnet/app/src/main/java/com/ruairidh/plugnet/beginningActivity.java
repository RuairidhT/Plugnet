package com.ruairidh.plugnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class beginningActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignIn;
    private Button btnCreateAccount;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            openApp();
        }

        btnSignIn = findViewById(R.id.signIn);
        btnSignIn.setOnClickListener(this);
        btnCreateAccount = findViewById(R.id.createAccount);
        btnCreateAccount.setOnClickListener(this);
    }

    public void openApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.createAccount:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }


}
