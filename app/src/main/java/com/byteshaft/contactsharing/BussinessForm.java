package com.byteshaft.contactsharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.byteshaft.contactsharing.database.CardsDatabase;

public class BussinessForm  extends AppCompatActivity {
    
    private EditText mName;
    private EditText mJobTitle;
    private EditText mContactNumber;
    private EditText mEmailAddress;
    private EditText mOrganization;
    private EditText mAddress;
    private Button mSaveButton;
    private CardsDatabase cardsDatabase;

    private String name;
    private String contactNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_card_form);
        cardsDatabase = new CardsDatabase(AppGlobals.getContext());
        mName = (EditText) findViewById(R.id.et_name);
        mJobTitle = (EditText) findViewById(R.id.et_job_title);
        mContactNumber = (EditText) findViewById(R.id.et_contact_number);
        mEmailAddress = (EditText) findViewById(R.id.et_email);
        mOrganization = (EditText) findViewById(R.id.et_organization);
        mAddress = (EditText) findViewById(R.id.et_address);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validateEditTexts();
                String jobTitle = mJobTitle.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();
                String organization = mOrganization.getText().toString();
                String address = mAddress.getText().toString();

                cardsDatabase.createNewEntry(name, address, jobTitle, contactNumber, emailAddress,
                            organization);
                System.out.println("Entry added");
            }
        });
    }

    private boolean validateEditTexts() {

        boolean valid = true;
        name = mName.getText().toString();
        contactNumber = mContactNumber.getText().toString();

        if (name.trim().isEmpty() || name.length() < 4) {
            mName.setError("must contain 4 character");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (contactNumber.trim().isEmpty() || contactNumber.length() < 4) {
            mContactNumber.setError("invalid entry");
            valid = false;
        } else {
            mContactNumber.setError(null);
        }
        return valid;
    }
}
