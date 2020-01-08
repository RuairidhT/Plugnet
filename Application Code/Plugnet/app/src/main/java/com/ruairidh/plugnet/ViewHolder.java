package com.ruairidh.plugnet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    DatabaseReference databaseReferenceOverride;

    public ViewHolder(View itemView) {
        super((itemView));

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

    }

    public void setDetails(Context ctx, String name, int mode, int plug_Status, float temperature, final int Override, String ID) {
        TextView mName = mView.findViewById(R.id.rName);
        ImageView mDevice = mView.findViewById(R.id.rDevice);
        ImageView mStatus = mView.findViewById(R.id.rStatus);
        TextView temp = mView.findViewById(R.id.temperatureTV);
        Switch overrideSwitch = mView.findViewById(R.id.switch1);
        View v = mView.findViewById(R.id.linerLayout);

        databaseReferenceOverride = FirebaseDatabase.getInstance().getReference("Plugs").child(ID).child("Override");


        if (Override == 1) {
            overrideSwitch.setChecked(true);
            v.setBackgroundColor(0xFFF5F5F5);
        }
        if (Override == 0) {
            overrideSwitch.setChecked(false);
            v.setBackgroundColor(0xFFDCDCDC);
        }

        overrideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Override == 0) {
                    updateMode(1);
                }
                if (Override == 1) {
                    updateMode(0);
                }
            }
        });

        mName.setText(name);

        temp.setText("Temperature: " + temperature + "°C");

        if (mode == 1) {
            mDevice.setImageResource(R.drawable.plug);
        }
        if (mode == 2) {
            mDevice.setImageResource(R.drawable.ventilator);
        }
        if (mode == 3) {
            mDevice.setImageResource(R.drawable.radiator);
        }

        if (plug_Status == 0) {
            mStatus.setImageResource(R.drawable.poweroff);
        }
        if (plug_Status == 1) {
            mStatus.setImageResource(R.drawable.poweron);
        }

    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }


    public void updateMode(int value) {

        databaseReferenceOverride.setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
