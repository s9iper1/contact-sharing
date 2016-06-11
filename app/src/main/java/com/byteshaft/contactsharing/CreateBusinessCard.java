package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byteshaft.contactsharing.utils.AppGlobals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateBusinessCard extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private Button formButton;
    private Button picButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.creat_bussiness_card, container, false);
        formButton = (Button) mBaseView.findViewById(R.id.button_form);
        picButton = (Button) mBaseView.findViewById(R.id.button_pic);
        formButton.setOnClickListener(this);
        picButton.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppGlobals.sNewEntryCreated) {
            MainActivity.getInstance().loadFragment(new BusinessCardsList());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_form:
                startActivity(new Intent(getActivity(), BusinessForm.class));
                break;
            case R.id.button_pic:
                dispatchTakePictureIntent();
                break;

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Uri uriSavedImage = Uri.fromFile(createDirectoryAndSaveFile());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
        }
    }

    private File createDirectoryAndSaveFile() {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String internalFolder = Environment.getExternalStorageDirectory()+
                File.separator + "Android/data" + File.separator + AppGlobals.getContext().getPackageName();
        File file = new File(internalFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        internalFolder = internalFolder + File.separator +  fileName + ".jpg";
        return new File(internalFolder);
    }

}
