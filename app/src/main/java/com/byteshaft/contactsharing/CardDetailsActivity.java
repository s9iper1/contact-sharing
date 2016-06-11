package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byteshaft.contactsharing.bluetooth.BluetoothActivity;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class CardDetailsActivity extends Activity implements View.OnClickListener {

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
    private FrameLayout frameLayout;
    private ImageButton editButton;
    private ImageButton shareButton;
    private RelativeLayout mainLayout;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_business_card);
        cardsDatabase = new CardsDatabase(getApplicationContext());
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        cardId = getIntent().getIntExtra(AppGlobals.CARD_ID, 0);
        color = getIntent().getStringExtra(AppGlobals.CURRENT_COLOR);
        if (cardId == 0) {
            finish();
        } else {
            Log.i("TAG", "" +cardsDatabase.getSingleBusinessCard(cardId));
            carddata = cardsDatabase.getSingleBusinessCard(cardId);
        }
        mainLayout.setBackgroundColor(Color.parseColor(color));
        personName = (TextView) findViewById(R.id.tv_name);
        jobTitle = (TextView) findViewById(R.id.job_title);
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        emailAddress = (TextView) findViewById(R.id.email_address);
        address = (TextView) findViewById(R.id.location);
        organization = (TextView) findViewById(R.id.tv_organization);
        jobzyId = (TextView) findViewById(R.id.tv_jobzy_id);
        frameLayout = (FrameLayout) findViewById(R.id.buttons);
        editButton = (ImageButton) findViewById(R.id.edit_button);
        shareButton = (ImageButton) findViewById(R.id.share_button);
        editButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        personName.setText(carddata.get(AppGlobals.NAME));
        jobTitle.setText(carddata.get(AppGlobals.JOB_TITLE));
        phoneNumber.setText(carddata.get(AppGlobals.NUMBER));
        emailAddress.setText(carddata.get(AppGlobals.EMAIL));
        address.setText(carddata.get(AppGlobals.ADDRESS));
        organization.setText(carddata.get(AppGlobals.ORG));
        jobzyId.setText(carddata.get(AppGlobals.JOBZY_ID));

        address.setTypeface(AppGlobals.regularTypeface);
        personName.setTypeface(AppGlobals.regularTypeface);
        jobTitle.setTypeface(AppGlobals.regularTypeface);
        phoneNumber.setTypeface(AppGlobals.regularTypeface);
        emailAddress.setTypeface(AppGlobals.regularTypeface);
        organization.setTypeface(AppGlobals.regularTypeface);
        jobzyId.setTypeface(AppGlobals.regularTypeface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:

                break;
            case R.id.share_button:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(AppGlobals.NAME, personName.getText().toString());
                    jsonObject.put(AppGlobals.ADDRESS, address.getText().toString());
                    jsonObject.put(AppGlobals.EMAIL, emailAddress.getText().toString());
                    jsonObject.put(AppGlobals.JOB_TITLE, jobTitle.getText().toString());
                    jsonObject.put(AppGlobals.ORG, organization.getText().toString());
                    jsonObject.put(AppGlobals.JOBZY_ID, jobzyId.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                intent.putExtra(AppGlobals.DATA_TO_BE_SENT, jsonObject.toString());
                startActivity(intent);
                break;
        }

    }
}
