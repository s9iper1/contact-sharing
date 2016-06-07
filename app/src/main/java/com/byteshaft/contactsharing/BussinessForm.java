package com.byteshaft.contactsharing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.byteshaft.contactsharing.database.CardsDatabase;

public class BussinessForm  extends Fragment {

    private View mBaseView;
    private EditText mName;
    private EditText mJobTitle;
    private EditText mContactNumber;
    private EditText mEmailAddress;
    private EditText mOrganization;
    private EditText mAddress;
    private Button mSaveButton;
    private CardsDatabase cardsDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBaseView = inflater.inflate(R.layout.business_card_form, container, false);
        cardsDatabase = new CardsDatabase(AppGlobals.getContext());

        mName = (EditText) mBaseView.findViewById(R.id.et_name);
        mJobTitle = (EditText) mBaseView.findViewById(R.id.et_job_title);
        mContactNumber = (EditText) mBaseView.findViewById(R.id.et_contact_number);
        mEmailAddress = (EditText) mBaseView.findViewById(R.id.et_email);
        mOrganization = (EditText) mBaseView.findViewById(R.id.et_organization);
        mAddress = (EditText) mBaseView.findViewById(R.id.et_address);
        mSaveButton = (Button) mBaseView.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String jobTitle = mJobTitle.getText().toString();
                int contactNumber = Integer.parseInt(mContactNumber.getText().toString());
                String emailAddress = mEmailAddress.getText().toString();
                String organization = mOrganization.getText().toString();
                String address = mAddress.getText().toString();

                cardsDatabase.createNewEntry(name, address, jobTitle, contactNumber, emailAddress,
                        organization);
                System.out.println("Entry added");
            }
        });

        return mBaseView;
    }
}
