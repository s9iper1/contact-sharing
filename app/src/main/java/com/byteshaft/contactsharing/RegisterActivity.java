package com.byteshaft.contactsharing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailAddress;
    private EditText mMobileNumber;
    private EditText mPassword;

    private String mFname;
    private String mLname;
    private String mEmail;
    private String mMobile;
    private String mPasswordEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mEmailAddress = (EditText) findViewById(R.id.email);
        mMobileNumber = (EditText) findViewById(R.id.mobile_number);
        mPassword = (EditText) findViewById(R.id.password);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEditText();

            }
        });
    }

    private boolean validateEditText() {

        boolean valid = true;
        mFname = mFirstName.getText().toString();
        mLname = mLastName.getText().toString();
        mMobile = mMobileNumber.getText().toString();
        mPasswordEntry = mPassword.getText().toString();
        mEmail = mEmailAddress.getText().toString();

        if (mFname.trim().isEmpty() || mFname.length() < 3) {
            mFirstName.setError("enter at least 3 characters");
            valid = false;
        } else {
            mFirstName.setError(null);
        }

        if (mLname.trim().isEmpty() || mLname.length() < 3) {
            mLastName.setError("enter at least 3 characters");
            valid = false;
        } else {
            mLastName.setError(null);
        }

        if (mPasswordEntry.trim().isEmpty() || mPasswordEntry.length() < 4) {
            mPassword.setError("enter at least 4 characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mMobile.trim().isEmpty() || mMobile.length() < 3) {
            mMobileNumber.setError("Enter Mobile Number");
            valid = false;
        } else {
            mMobileNumber.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailAddress.setError("please provide a valid email");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }
}
