package com.manacher.hammer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;
import com.manacher.hammer.Activities.LoginActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.services.GoogleAuthService;
import com.manacher.phoneauthentication.services.PhoneAuthentication;


public class LoginFragment extends Fragment {

    private EditText editText;
    private Activity activity;
    private Button submit;
    private CountryCodePicker codePicker;

    private SignInButton googleSign;
    private GoogleAuthService googleAuthService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.initialized(view);
        this.listener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((LoginActivity)activity).navController = Navigation.findNavController(view);
    }

    private void initialized(View view){
        this.activity = getActivity();
        editText = view.findViewById(R.id.editText);
        submit = view.findViewById(R.id.mContinue);
        codePicker = view.findViewById(R.id.codePicker);
        googleSign = view.findViewById(R.id.googleSign);
        googleAuthService = new GoogleAuthService(activity, this);
        ((LoginActivity)activity).authentication = new PhoneAuthentication(activity);
    }

    private void listener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "+"+codePicker.getSelectedCountryCode();
                String number = editText.getText().toString().trim();
                ((LoginActivity)activity).authentication.sendVerificationCode(code + number);

                ((LoginActivity)activity).navController.navigate(R.id.action_loginFragment_to_oneTimePasswordFragment);

            }
        });

        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleAuthService.signIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAuthService.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleAuthService.firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
            }
        }

    }
}