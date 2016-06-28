package com.byteshaft.contactsharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.byteshaft.contactsharing.utils.AppGlobals;

public class SelectDesignActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView designOne;
    private ImageView designTwo;
    private ImageView designThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_design);
        designOne = (ImageView) findViewById(R.id.design_one);
        designTwo = (ImageView) findViewById(R.id.design_two);
        designThree = (ImageView) findViewById(R.id.design_three);
        designOne.setOnClickListener(this);
        designTwo.setOnClickListener(this);
        designThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.design_one:
                AppGlobals.sSelectedDesign = 0;
                showToast();
                finish();
                break;
            case R.id.design_two:
                AppGlobals.sSelectedDesign = 1;
                showToast();
                finish();
                break;
            case R.id.design_three:
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
