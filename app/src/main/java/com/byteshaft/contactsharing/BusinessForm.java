package com.byteshaft.contactsharing;

import android.Manifest;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BusinessForm extends AppCompatActivity implements View.OnClickListener {

    private EditText mName;
    private EditText mJobTitle;
    private EditText mContactNumber;
    private EditText mEmailAddress;
    private EditText mOrganization;
    private EditText mAddress;
    private EditText mJobzyId;
    private Button mSaveButton;
    private CardsDatabase cardsDatabase;
    private String name;
    private String contactNumber;
    private int id;
    private int defaultValue = 0;
    private Button selectImage;
    private CircularImageView logo;
    private static final int REQUEST_CAMERA = 1212;
    private static final int SELECT_FILE = 1245;
    private File destination;
    private String imageUrl = "";
    private Bitmap logoPicture;
    private Uri selectedImageUri;
    public static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;

    private RelativeLayout layout_name;
    private RelativeLayout layout_address;
    private RelativeLayout layout_email;
    private RelativeLayout layout_job_title;
    private RelativeLayout layout_jobzi_id;
    private RelativeLayout layout_contact;
    private RelativeLayout layout_company;
    private AlphaAnimation clickEffect = new AlphaAnimation(10F, 0.1F);

    private TextView mNameTitle;
    private TextView mNameAction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_card_form);
        cardsDatabase = new CardsDatabase(AppGlobals.getContext());
        layout_name = (RelativeLayout) findViewById(R.id.main_relayout_layout_name);
        layout_address = (RelativeLayout) findViewById(R.id.main_relayout_layout_address);
        layout_email = (RelativeLayout) findViewById(R.id.main_relayout_layout_email);
        layout_job_title = (RelativeLayout) findViewById(R.id.main_relayout_layout_job_title);
        layout_jobzi_id = (RelativeLayout) findViewById(R.id.main_relayout_layout_jobzi_id);
        layout_contact = (RelativeLayout) findViewById(R.id.main_relayout_layout_contact_number);
        layout_company = (RelativeLayout) findViewById(R.id.main_relayout_layout_company);
        layout_name.setOnClickListener(this);
        layout_address.setOnClickListener(this);
        layout_email.setOnClickListener(this);
        layout_job_title.setOnClickListener(this);
        layout_jobzi_id.setOnClickListener(this);
        layout_contact.setOnClickListener(this);
        layout_company.setOnClickListener(this);
//        mSaveButton = (Button) findViewById(R.id.save_button);
//        selectImage = (Button) findViewById(R.id.select_design);
//        selectImage.setOnClickListener(this);
//        Intent idIntent = getIntent();
//        id = idIntent.getIntExtra("id", defaultValue);
//        if (id != defaultValue) {
//            mSaveButton.setText("Update Card");
//        }
//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (id == defaultValue) {
//                    if (validateEditTexts()) {
//                        String jobTitle = mJobTitle.getText().toString();
//                        String emailAddress = mEmailAddress.getText().toString();
//                        String organization = mOrganization.getText().toString();
//                        String address = mAddress.getText().toString();
//                        String jobzyId = mJobzyId.getText().toString();
//                        contactNumber = mContactNumber.getText().toString();
//                        Log.i("First Log", "splash_background one");
//                        cardsDatabase.createNewEntry(name, address, jobTitle, contactNumber, emailAddress,
//                                organization, jobzyId, "", 0, AppGlobals.sSelectedDesign, imageUrl);
////                        CardDetailsTask cardDetailsTask = new CardDetailsTask(
////                                BusinessForm.this,
////                                address,
////                                contactNumber,
////                                emailAddress,
////                                0,
////                                jobTitle,
////                                name,
////                                organization,
////                                "", AppGlobals.sSelectedDesign, imageUrl);
////                        cardDetailsTask.execute();
//                        Log.i("Third", "splash_background 3");
////                        AppGlobals.sNewEntryCreated = true;
//                    }
//                } else {
//                    validateEditTexts();
//                    Log.i("Second Log", "splash_background else part");
//
//                    String jobTitle = mJobTitle.getText().toString();
//                    String emailAddress = mEmailAddress.getText().toString();
//                    String organization = mOrganization.getText().toString();
//                    String address = mAddress.getText().toString();
//                    String jobzyId = mJobzyId.getText().toString();
//                    cardsDatabase.updateEntries(id, name, address, jobTitle,
//                            contactNumber, emailAddress, organization, jobzyId);
//                }
//
//                finish();
//            }
//
//
//        });
    }

    private boolean validateEditTexts() {
        boolean valid = true;
        name = mName.getText().toString();

        if (name.trim().isEmpty() || name.length() < 4) {
            mName.setError("must contain 4 character");
            valid = false;
        } else {
            mName.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),NewBusinessFormActivity.class);
        switch (view.getId()) {
//            case R.id.select_design:
//                startActivity(new Intent(getApplicationContext(),
//                        SelectDesignActivity.class));
//                break;
            case R.id.main_relayout_layout_name:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 0);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_address:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 1);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_email:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 2);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_job_title:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 3);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_jobzi_id:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 4);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_contact_number:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 5);
                startActivity(intent);
                break;
            case R.id.main_relayout_layout_company:
                view.startAnimation(clickEffect);
                intent.putExtra("data", 6);
                startActivity(intent);
                break;
//            case R.id.logo:
//                    if (ContextCompat.checkSelfPermission(BusinessForm.this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(BusinessForm.this,
//                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
//                    } else {
//                        selectImage();
//                    }
//                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission granted");
                    selectImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied!"
                            , Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Remove photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessForm.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Remove photo")) {
                    logo.setImageResource(
                            android.R.drawable.ic_menu_camera);
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                imageUrl = destination.getAbsolutePath();
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logoPicture = Helpers.getBitMapOfProfilePic(destination.getAbsolutePath());
                logo.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                logoPicture = Helpers.getBitMapOfProfilePic(selectedImagePath);
                Log.e(AppGlobals.getLogTag(getClass()), String.valueOf(logoPicture == null));
                logo.setImageBitmap(getResizedBitmap(logoPicture, 20));
                imageUrl = String.valueOf(selectedImagePath);
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
