package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;

import java.io.File;

public class CreateBusinessCard extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private Button formButton;
    private Button picButton;

    private CardsDatabase cardsDatabase;
    private String fileName;
    private EditText input;
    private Uri uriSavedImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardsDatabase = new CardsDatabase(AppGlobals.getContext());
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
                enterNameDialog();
                break;

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            uriSavedImage = Uri.fromFile(createDirectoryAndSaveFile());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            System.out.println(uriSavedImage);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            cardsDatabase.imageEntry(fileName, uriSavedImage.toString());
            cardsDatabase.createNewEntry(fileName, "", "", "", "", "", "",
                    uriSavedImage.toString(), 1);
        }
    }

    private File createDirectoryAndSaveFile() {
        String internalFolder = Environment.getExternalStorageDirectory()+
                File.separator + "Android/data" + File.separator + AppGlobals.getContext().getPackageName();
        File file = new File(internalFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        internalFolder = internalFolder + File.separator +  fileName + ".jpg";
        return new File(internalFolder);
    }

    private void enterNameDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // Setting Dialog Title
        alertDialog.setTitle("Set Name for this Card");
        alertDialog.setMessage("Enter name");

        // outside touch disable

        input = new EditText(getActivity());
        InputMethodManager imm = (InputMethodManager) AppGlobals.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        input.requestFocus();
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        input.setHint("Type here ");
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        // Showing Alert Message
        dialog.setCancelable(false);
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = input.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    fileName = name;
                    dispatchTakePictureIntent();
                    dialog.dismiss();

                }
            }
        });

        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
