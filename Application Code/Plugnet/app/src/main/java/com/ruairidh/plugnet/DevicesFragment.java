package com.ruairidh.plugnet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DevicesFragment extends Fragment implements View.OnClickListener{

    Button none, fan, radiator, update;
    EditText threshold, deviceName;
    View myView;

    String id;

    DatabaseReference databaseReferenceMode;
    DatabaseReference databaseReferenceThreshold;
    DatabaseReference databaseReferenceName;

    public static boolean button1pressed = false;
    public static boolean button2pressed = false;
    public static boolean button3pressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView =  inflater.inflate(R.layout.fragment_devices, container, false);

        (( AppCompatActivity )getActivity()).getSupportActionBar().setTitle("My Devices");

        Bundle plugs = this.getArguments();
        if (plugs != null) {
            id = plugs.getString("ID");
        }

        databaseReferenceMode = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Mode");
        databaseReferenceThreshold = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Threshhold");
        databaseReferenceName = FirebaseDatabase.getInstance().getReference("Plugs").child(id).child("Name");

        none = myView.findViewById(R.id.nonebtn);
        fan = myView.findViewById(R.id.fanbtn);
        radiator = myView.findViewById(R.id.radiatorbtn);
        update = myView.findViewById(R.id.updatebtn);
        threshold = myView.findViewById(R.id.thresholdet);
        deviceName = myView.findViewById(R.id.deviceNameET);

        none.setOnClickListener(this);
        fan.setOnClickListener(this);
        radiator.setOnClickListener(this);
        update.setOnClickListener(this);

        getName();
        getThreshold();

        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button1pressed = false;
        button2pressed = false;
        button3pressed = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nonebtn:
                if (button1pressed == true) {
                    none.setBackgroundResource(R.drawable.defaultwhite_background);
                    none.setTextColor(Color.BLACK);
                    button1pressed = false;
                    break;
                }
                if (button1pressed == false) {
                    setToDefault();
                    none.setBackgroundResource(R.drawable.defaultblue_background);
                    none.setTextColor(Color.WHITE);
                    button1pressed = true;
                    break;
                }
            case R.id.fanbtn:
                if (button2pressed == true) {
                    fan.setBackgroundResource(R.drawable.defaultwhite_background);
                    fan.setTextColor(Color.BLACK);
                    button2pressed = false;
                    break;
                }
                if (button2pressed == false) {
                    setToDefault();
                    fan.setBackgroundResource(R.drawable.defaultblue_background);
                    fan.setTextColor(Color.WHITE);
                    button2pressed = true;
                    break;
                }
            case R.id.radiatorbtn:
                if (button3pressed == true) {
                    radiator.setBackgroundResource(R.drawable.defaultwhite_background);
                    radiator.setTextColor(Color.BLACK);
                    button3pressed = false;
                    break;
                }
                if (button3pressed == false) {
                    setToDefault();
                    radiator.setBackgroundResource(R.drawable.defaultblue_background);
                    radiator.setTextColor(Color.WHITE);
                    button3pressed = true;
                    break;
                }
            case R.id.updatebtn:
                if (button1pressed == false && button2pressed == false && button3pressed == false) {
                    Toast.makeText(getContext(), "Please select a device", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (threshold.getText().toString().trim().length() == 0){
                    Toast.makeText(getContext(), "Please enter a threshold temperature", Toast.LENGTH_SHORT).show();
                }
                if (deviceName.getText().toString().trim().length() == 0){
                    Toast.makeText(getContext(), "Please enter a name for your plug", Toast.LENGTH_SHORT).show();
                }
                else {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()){
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    update();
                    break;
                }

            default:
                break;
        }
    }


    public void setToDefault(){
        none.setBackgroundResource(R.drawable.defaultwhite_background);
        none.setTextColor(Color.BLACK);
        button1pressed = false;
        fan.setBackgroundResource(R.drawable.defaultwhite_background);
        fan.setTextColor(Color.BLACK);
        button2pressed = false;
        radiator.setBackgroundResource(R.drawable.defaultwhite_background);
        radiator.setTextColor(Color.BLACK);
        button3pressed = false;
    }


    public void update(){
        if (button1pressed == true) {
            updateMode(1);
        }
        if (button2pressed == true) {
            updateMode(2);
        }
        if (button3pressed == true) {
            updateMode(3);
        }

        int thresholdtemp = Integer.parseInt(threshold.getText().toString().trim());
        updateThreshold(thresholdtemp);

        String name = deviceName.getText().toString().trim();
        updateName(name);

        HomeFragment fragment = new HomeFragment();
//        Bundle plugs = new Bundle();
//        plugs.putString("ID", id);
//        fragment.setArguments(plugs);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }



    public void updateMode(int mode) {

        databaseReferenceMode.setValue(mode)
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

    public void getThreshold() {
        databaseReferenceThreshold.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                threshold.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateThreshold(int threshold) {

        databaseReferenceThreshold.setValue(threshold)
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
                deviceName.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updateName(String name) {

        databaseReferenceName.setValue(name)
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
