package com.byteshaft.contactsharing.card;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.github.siyamed.shapeimageview.CircularImageView;

public class NewBusinessFormActivity extends AppCompatActivity {

    private TextView keyTextView;
    private EditText valueEditText;
    private CircularImageView mCircularImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_business_form_activity);
        keyTextView = (TextView) findViewById(R.id.new_business_card_text_view);
        valueEditText = (EditText) findViewById(R.id.new_business_card_edit_text);
        mCircularImageView = (CircularImageView) findViewById(R.id.new_business_card_image);
        int data = getIntent().getIntExtra("data", 0);
        System.out.println(getSupportActionBar() == null);
        if (getSupportActionBar() != null) {
            System.out.println(getSupportActionBar() == null);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.delete3);
        }
        mCircularImageView.setBackground(getResources().getDrawable(getIntentValues(data)));
        setValueForSaving(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_business_card_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setValueForSaving(int value) {
        switch (value) {
            case 0:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedCardName = editable.toString();

                    }
                });
                break;
            case 1:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedAddress = editable.toString();

                    }
                });
                break;
            case 2:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedJobTitle = editable.toString();

                    }
                });
                break;
            case 3:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedjobzyId = editable.toString();

                    }
                });
                break;
            case 4:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedcontactNumber = editable.toString();

                    }
                });
                break;
            case 5:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedEmail = editable.toString();

                    }
                });
                break;
            case 6:
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        AppGlobals.toBeCreatedOrg = editable.toString();

                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_button:
                CardElements.keysList.add(keyTextView.getText().toString());
                CardElements.cardData.put(keyTextView.getText().toString(),
                        valueEditText.getText().toString());
                BusinessForm.getInstance().finish();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return false;
    }

    private int getIntentValues(int value) {
        switch (value) {
            case 0:
                keyTextView.setHint("Full Name");
                if (AppGlobals.toBeCreatedCardName != null) {
                    valueEditText.setText(AppGlobals.toBeCreatedCardName);
                }
                return R.mipmap.male;
            case 1:
                keyTextView.setHint("Address");
                return R.drawable.address;
            case 2:
                keyTextView.setHint("Email");
                return R.drawable.email;
            case 3:
                keyTextView.setHint("Job Title");
                return R.drawable.job_title;
            case 4:
                keyTextView.setHint("Jobzy id");
                return R.drawable.jobzi_id;
            case 5:
                keyTextView.setHint("Contact Number");
                return R.drawable.contact_number;
            case 6:
                keyTextView.setHint("Organization");
                return R.drawable.company;
            default:
                keyTextView.setHint("Full Name");
                return R.mipmap.male;
        }
    }
}
