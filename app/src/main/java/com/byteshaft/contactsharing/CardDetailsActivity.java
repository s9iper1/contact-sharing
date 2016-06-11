package com.byteshaft.contactsharing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;

import java.util.HashMap;


public class CardDetailsActivity extends Activity {

    private int cardId;
    private CardsDatabase cardsDatabase;
    private HashMap<String, String> carddata;
    private TextView personName;
    private TextView jobTitle;
    private TextView phoneNumber;
    private TextView emailAddress;
    private TextView address;
    private TextView organization;
    private TextView jobzyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_business_card);
        cardsDatabase = new CardsDatabase(getApplicationContext());
        cardId = getIntent().getIntExtra(AppGlobals.CARD_ID, 0);
        if (cardId == 0) {
            finish();
        } else {
            Log.i("TAG", "" +cardsDatabase.getSingleBusinessCard(cardId));
            carddata = cardsDatabase.getSingleBusinessCard(cardId);
        }
        personName = (TextView) findViewById(R.id.tv_name);
        jobTitle = (TextView) findViewById(R.id.job_title);
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        emailAddress = (TextView) findViewById(R.id.email_address);
        address = (TextView) findViewById(R.id.location);
        organization = (TextView) findViewById(R.id.tv_organization);
        jobzyId = (TextView) findViewById(R.id.tv_jobzy_id);

        personName.setText(carddata.get(AppGlobals.NAME));
        jobTitle.setText(carddata.get(AppGlobals.JOB_TITLE));
        phoneNumber.setText(carddata.get(AppGlobals.NUMBER));
        emailAddress.setText(carddata.get(AppGlobals.EMAIL));
        address.setText(carddata.get(AppGlobals.ADDRESS));
        organization.setText(carddata.get(AppGlobals.ORG));
        jobzyId.setText(carddata.get(AppGlobals.JOBZY_ID));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
