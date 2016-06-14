package com.byteshaft.contactsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.contactsharing.utils.AppGlobals;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginButton;
    private EditText mEmailAddress;
    private EditText mPassword;
    private TextView mSignUpText;


    private String mEmail;
    private String mPasswordEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailAddress = (EditText) findViewById(R.id.email_address);
        mPassword = (EditText) findViewById(R.id.password_entry);
        mLoginButton = (Button) findViewById(R.id.login);
        mSignUpText = (TextView) findViewById(R.id.signup_text);
        mLoginButton.setOnClickListener(this);
        mSignUpText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                // TODO: 09/06/2016
                validateEditText();
                System.out.println("one");
                break;
            case R.id.signup_text:
                // TODO: 09/06/2016
                System.out.println("sign up");
                startActivity(new Intent(AppGlobals.getContext(), RegisterActivity.class));
                break;
        }
    }

    private boolean validateEditText() {

        boolean valid = true;
        mPasswordEntry = mPassword.getText().toString();
        mEmail = mEmailAddress.getText().toString();

        if (mPasswordEntry.trim().isEmpty() || mPasswordEntry.length() < 4) {
            mPassword.setError("must contain 4 character");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailAddress.setError("invalid email");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }
}
