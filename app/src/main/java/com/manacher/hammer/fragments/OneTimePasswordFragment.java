package com.manacher.hammer.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.manacher.hammer.Activities.LoginActivity;
import com.manacher.hammer.R;


public class OneTimePasswordFragment extends Fragment {

    private Activity activity;
    private EditText editText;
    private Button continueButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_time_password, container, false);

        this.activity = getActivity();
        ((LoginActivity)activity).phoneAuth = true;
        editText = view.findViewById(R.id.editText);
        continueButton = view.findViewById(R.id.continueButton);

        this.listener();

        return view;
    }

    private void listener(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = String.valueOf(editText.getText());

                if(otp.isEmpty()){
                    editText.setError("enter a valid code");
                    return;
                }

                ((LoginActivity)activity).authentication.verifyCode(otp);
                continueButton.setEnabled(false);
            }
        });
    }


}