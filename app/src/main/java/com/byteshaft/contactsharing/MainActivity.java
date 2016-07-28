package com.byteshaft.contactsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.bluetooth.BluetoothActivity;
import com.byteshaft.contactsharing.card.BusinessCardsList;
import com.byteshaft.contactsharing.utils.AppGlobals;

import com.byteshaft.contactsharing.utils.BitmapWithCharacter;
import com.byteshaft.contactsharing.utils.Helpers;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity sInstance;
    private View navHeader;
    private TextView navUserName;
    private CircularImageView circularImageView;
    private TextView navEmail;
    public static MainActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        loadFragment(new BusinessCardsList());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navUserName = (TextView) navHeader.findViewById(R.id.nav_username);
        circularImageView = (CircularImageView) navHeader.findViewById(R.id.nav_image);
        navEmail = (TextView) navHeader.findViewById(R.id.nav_email);

        if (!Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME).equals("")) {
            if (navUserName != null) {
                navUserName.setText(Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME));
            }
        } else {
            navUserName.setText("Username");
        }
        if (!Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL).equals("")) {
            if (navEmail != null) {
                navEmail.setText(Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));
            }
        } else {
            navEmail.setText("email@domain.com");
        }

        if (!Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME).equals("")) {
            if (navUserName != null) {
                String letter = Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME);
                System.out.println(letter.charAt(0));
                int[] array = getResources().getIntArray(R.array.letter_tile_colors);
                final BitmapWithCharacter tileProvider = new BitmapWithCharacter();
                final Bitmap letterTile = tileProvider.getLetterTile(Helpers.
                        getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME),
                        Integer.parseInt(String.valueOf(array[new Random().nextInt(array.length)])), 100, 100);
                circularImageView.setImageBitmap(letterTile);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_cards:
                loadFragment(new BusinessCardsList());
                break;
//            case R.id.nav_create_card:
//                loadFragment(new CreateBusinessCard());
//                break;
            case R.id.nav_logout:
                if (Helpers.isUserLoggedIn()) {
                    showLogoutDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "You are not loggedIn", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_bluetooth:
                startActivity(new Intent(getApplicationContext(), BluetoothActivity.class));
                break;
            default:
                loadFragment(new CreateBusinessCard());
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedpreferences = Helpers.getPreferenceManager();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        if (!Helpers.isUserLoggedIn()) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            Helpers.saveUserLogin(false);
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void closeApplication() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(startMain);
        finish();
    }
}
