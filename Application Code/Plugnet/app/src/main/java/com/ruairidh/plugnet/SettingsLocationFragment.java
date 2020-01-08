package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsLocationFragment extends Fragment {

    View myView;

    private Spinner spinner1;
    private Button btnSubmit;
    private TextView locationtv;

    DatabaseReference databaseReferenceLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_settings_location, container, false);

        (( AppCompatActivity ) getActivity()).getSupportActionBar().setTitle("Location");


        databaseReferenceLocation = FirebaseDatabase.getInstance().getReference("Location").child("Location");

        spinner1 = myView.findViewById(R.id.spinner1);
        btnSubmit = myView.findViewById(R.id.btnSubmit);
        locationtv = myView.findViewById(R.id.locationTV);

        getLocation();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = String.valueOf(spinner1.getSelectedItem());
                updateLocation(location);
                Toast.makeText(getContext(), "Updated",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }

    public void getLocation() {

        databaseReferenceLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                locationtv.setText("Current Location: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateLocation(String location) {

        databaseReferenceLocation.setValue(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
