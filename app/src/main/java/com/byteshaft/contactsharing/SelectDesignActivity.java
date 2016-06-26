package com.byteshaft.contactsharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.byteshaft.contactsharing.utils.AppGlobals;

public class SelectDesignActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout designOne;
    private RelativeLayout designTwo;
    private RelativeLayout designThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_design);
        designOne = (RelativeLayout) findViewById(R.id.design_one_layout);
        designTwo = (RelativeLayout) findViewById(R.id.design_two_layout);
        designThree = (RelativeLayout) findViewById(R.id.design_three_layout);
        designOne.setOnClickListener(this);
        designTwo.setOnClickListener(this);
        designThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.design_one_layout:
                AppGlobals.sSelectedDesign = 0;
                showToast();
                finish();
                break;
            case R.id.design_two_layout:
                AppGlobals.sSelectedDesign = 1;
                showToast();
                finish();
                break;
            case R.id.design_three_layout:
                AppGlobals.sSelectedDesign = 2;
                showToast();
                finish();
                break;
        }

    }

    private void showToast() {
        Toast.makeText(SelectDesignActivity.this, "Design selected", Toast.LENGTH_SHORT).show();
    }
}
