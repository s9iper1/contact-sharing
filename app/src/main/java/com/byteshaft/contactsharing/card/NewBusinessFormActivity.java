package com.byteshaft.contactsharing.card;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.contactsharing.R;
import com.github.siyamed.shapeimageview.CircularImageView;

public class NewBusinessFormActivity extends AppCompatActivity {

    private TextView mEditText;
    private EditText mEditText2;
    private CircularImageView mCircularImageView;
    private String editTextString;
    private String editTextString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_business_form_activity);
        mEditText = (TextView) findViewById(R.id.new_business_card_text_view);
        mEditText2 = (EditText) findViewById(R.id.new_business_card_edit_text);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_business_card_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_button:
                editTextString = mEditText.getText().toString();
                editTextString2 = mEditText.getText().toString();
                break;
        }
        return false;
    }

    private int getIntentValues(int value) {
        switch (value) {
            case 0:
                mEditText.setHint("Full Name");
                return R.mipmap.male;
            case 1:
                return R.drawable.address;
            case 2:
                return R.drawable.email;
            case 3:
                return R.drawable.job_title;
            case 4:
                return R.drawable.jobzi_id;
            case 5:
                return R.drawable.contact_number;
            case 6:
                return R.drawable.company;
            default:
                return R.mipmap.male;
        }
    }
}
