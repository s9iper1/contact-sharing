package com.byteshaft.contactsharing;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CreateBusinessCard extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private Button formButton;
    private Button picButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.creat_bussiness_card, container, false);
        formButton = (Button) mBaseView.findViewById(R.id.button_form);
        picButton = (Button) mBaseView.findViewById(R.id.button_pic);
        formButton.setOnClickListener(this);
        picButton.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_form:
                System.out.println("form Button");
                loadFragment(new BussinessForm());
                break;
            case R.id.button_pic:
                System.out.println("pic Button");
                dispatchTakePictureIntent();
                break;

        }
    }

    public void loadFragment (Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
