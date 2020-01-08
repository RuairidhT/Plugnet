package com.ruairidh.plugnet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import de.nitri.gauge.Gauge;

public class PlugDetailsFragment extends Fragment implements View.OnClickListener{

    View myView;

    TextView mName, cStatus, temperature, deviceStatus, threshold, exTemp;
    Button plugButton, deviceButton;
    Gauge gauge, exGauge;
    Spinner spinner;

    String id;

    int currentStatus = -1;

    DatabaseReference databaseReferenceTemp;
    DatabaseReference databaseReferenceStatus;
    DatabaseReference databaseReferenceName;
    DatabaseReference databaseReferenceMode;
    DatabaseReference databaseReferenceThreshold;
    DatabaseReference databaseReferenceexTemp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_plug_details, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("My Device");

        Bundle plugs = this.getArguments();
        if (plugs != null) {
            id = plugs.getString("ID");
        }

        databaseReferenceTemp = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Temperature");
        databaseReferenceexTemp = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("External_Temp");
        databaseReferenceStatus = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Plug_Status");
        databaseReferenceName = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Name");
        databaseReferenceMode = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Mode");
        databaseReferenceThreshold = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Threshhold");

        mName = myView.findViewById(R.id.NameTV);
        cStatus = myView.findViewById(R.id.plugStatus);
        plugButton = myView.findViewById(R.id.plugButton);
        threshold = myView.findViewById(R.id.threshTV);
        deviceStatus = myView.findViewById(R.id.deviceStatus);
        plugButton.setOnClickListener(this);
        deviceButton = myView.findViewById(R.id.deviceButton);
        deviceButton.setOnClickListener(this);
        temperature = myView.findViewById(R.id.tempTV);
        gauge = myView.findViewById(R.id.gauge);
        exTemp = myView.findViewById(R.id.exTempTV);
        exGauge = myView.findViewById(R.id.exGauge);


        checkPlugStatus();
        getName();
        checkTemperature();
        checkExTemperature();
        checkMode();
        checkThreshold();

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plugButton:
                if (currentStatus == 1){
                    updateStatus(0);
                }
                if (currentStatus == 0){
                    updateStatus(1);
                }
                break;
            case R.id.deviceButton:
                DevicesFragment fragment = new DevicesFragment();
                Bundle plugs = new Bundle();
                plugs.putString("ID", id);
                fragment.setArguments(plugs);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

                break;
            default:
                break;
        }
    }


    public void checkPlugStatus() {

        databaseReferenceStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                int plugStatus = Integer.valueOf(value);
                if (plugStatus == 1) {
                    cStatus.setText("Plug is currently on");
                    plugButton.setText("Turn off");
                    currentStatus = 1;
                }
                if (plugStatus == 0) {
                    cStatus.setText("Plug is currently off");
                    plugButton.setText("Turn on");
                    currentStatus = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateStatus(int status) {

        databaseReferenceStatus.setValue(status)
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

    public void getName() {
        databaseReferenceName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                mName.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkTemperature() {
        databaseReferenceTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                float f = Float.valueOf(value.trim()).floatValue();
                temperature.setText("Inside Temperature:   "+ value + "°C");
                gauge.moveToValue(f);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkExTemperature() {
        databaseReferenceexTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                float f = Float.valueOf(value.trim()).floatValue();
                exTemp.setText("Outside Temperature:   "+ value + "°C");
                exGauge.moveToValue(f);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkMode() {
        databaseReferenceMode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sValue = dataSnapshot.getValue().toString();
                if (sValue.equals("1")) {
                    deviceStatus.setText("Currently in 'Default' mode");
                }
                if (sValue.equals("2")) {
                    deviceStatus.setText("Currently in 'Fan' mode");
                }
                if (sValue.equals("3")) {
                    deviceStatus.setText("Currently in 'Radiator' mode");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkThreshold() {
        databaseReferenceThreshold.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                threshold.setText("Current threshold is " + value + "°C");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
