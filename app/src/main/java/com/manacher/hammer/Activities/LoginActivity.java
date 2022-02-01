package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.manacher.hammer.R;
import com.manacher.phoneauthentication.services.AuthListener;
import com.manacher.phoneauthentication.services.PhoneAuthentication;

public class LoginActivity extends AppCompatActivity implements AuthListener {

    private PhoneAuthentication phoneAuthentication;
    private EditText editText;
    private TextView status;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phoneAuthentication = new PhoneAuthentication(this, "Aviraj", Uri.parse(""));

        phoneAuthentication.sendVerificationCode("+918126460072");

        this.initialized();
        this.listener();
    }

    private void initialized(){
        editText = findViewById(R.id.editText);
        status = findViewById(R.id.status);
        submit = findViewById(R.id.submit);
    }

    private void listener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = String.valueOf(editText.getText());
                phoneAuthentication.verifyCode(code);
            }
        });
    }

    @Override
    public void onSuccess(String userId) {
        Log.d("YYAG7", "onSuccess: "+userId);
        status.setText("onSuccess: "+userId);
    }

    @Override
    public void onFailure() {
        Log.d("YYAG7", "onFailure: ");
        status.setText("onFailure");
    }
}