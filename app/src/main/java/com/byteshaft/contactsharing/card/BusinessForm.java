package com.byteshaft.contactsharing.card;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BusinessForm extends AppCompatActivity implements View.OnClickListener {

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
    public static BusinessForm sInstance;
    public static BusinessForm getInstance() {
        return sInstance;
    }
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
    private TextView mAddress;
    private TextView mAddressAction;
    private TextView mJobTitle;
    private TextView mJobTitleAction;
    private TextView mJobziId;
    private TextView mJobziIdAction;
    private TextView mContactNumber;
    private TextView mContactNumberAction;
    private TextView mEmail;
    private TextView mEmailAction;
    private TextView mOrganization;
    private TextView mOrganizationAction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_card_form);
        sInstance = this;
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
        mNameTitle = (TextView) findViewById(R.id.name_text_view_title);
        mNameAction = (TextView) findViewById(R.id.name_text_view_action);
        if (CardInfo.cardData.containsKey("Full Name")) {
            mNameAction.setText(CardInfo.cardData.get("Full Name"));
        }
        mAddress = (TextView) findViewById(R.id.address_text_view_title);
        mAddressAction = (TextView) findViewById(R.id.address_text_view_action);
        if (CardInfo.cardData.containsKey("Address")) {
            mAddressAction.setText(CardInfo.cardData.get("Address"));
        }
        mJobTitle = (TextView) findViewById(R.id.job_title_text_view_title);
        mJobTitleAction = (TextView) findViewById(R.id.job_title_text_view_action);
        if (CardInfo.cardData.containsKey("Job Title")) {
            mJobTitleAction.setText(CardInfo.cardData.get("Job Title"));
        }
        mJobziId = (TextView) findViewById(R.id.jobzi_id_text_view_title);
        mJobziIdAction = (TextView) findViewById(R.id.jobzi_id_text_view_action);
        if (CardInfo.cardData.containsKey("Jobzy Id")) {
            mJobziIdAction.setText(CardInfo.cardData.get("Jobzy Id"));
        }
        mContactNumber = (TextView) findViewById(R.id.contact_number_text_view_title);
        mContactNumberAction = (TextView) findViewById(R.id.contact_number_text_view_action);
        if (CardInfo.cardData.containsKey("Contact Number")) {
            mContactNumberAction.setText(CardInfo.cardData.get("Contact Number"));
        }
        mEmail = (TextView) findViewById(R.id.email_text_view_title);
        mEmailAction = (TextView) findViewById(R.id.email_text_view_action);
        if (CardInfo.cardData.containsKey("Email")) {
            mEmailAction.setText(CardInfo.cardData.get("Email"));
        }
        mOrganization = (TextView) findViewById(R.id.company_text_view_title);
        mOrganizationAction = (TextView) findViewById(R.id.company_text_view_action);
        if (CardInfo.cardData.containsKey("Organization")) {
            mOrganizationAction.setText(CardInfo.cardData.get("Organization"));
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NewBusinessFormActivity.class);
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
