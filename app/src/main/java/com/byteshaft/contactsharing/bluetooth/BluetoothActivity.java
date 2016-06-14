package com.byteshaft.contactsharing.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.MainActivity;
import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BluetoothActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = BluetoothActivity.class.getSimpleName();
    public static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private ArrayList<String> bluetoothDeviceArrayList;
    private HashMap<String, String> bluetoothMacAddress;
    private BluetoothChatService mChatService = null;
    private String mConnectedDeviceName = null;
    public ViewHolder holder;
    public ListView listView;
    private MenuItem refreshItem;
    private String dataToBeSent = "";
    private Switch discoverSwitch;
    private TextView currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        dataToBeSent = getIntent().getStringExtra(AppGlobals.DATA_TO_BE_SENT);
        listView = (ListView) findViewById(R.id.devicesList);
        currentState = (TextView) findViewById(R.id.current_state);
        discoverSwitch = (Switch) findViewById(R.id.discovery_switch);
        discoverSwitch.setOnCheckedChangeListener(this);
        bluetoothDeviceArrayList = new ArrayList<>();
        bluetoothMacAddress = new HashMap<>();
        mChatService = new BluetoothChatService(mHandler);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("TAG", "" + i);
                Log.i("TAG", "" + bluetoothDeviceArrayList.get(i));
                Log.i("TAG", "" + bluetoothMacAddress.get(bluetoothDeviceArrayList.get(i)));
                if (dataToBeSent != null) {
                    if (!dataToBeSent.trim().isEmpty()) {
                        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                            connectDevice(bluetoothMacAddress.get(bluetoothDeviceArrayList.get(i)), true);
                        } else {
                            byte[] message = dataToBeSent.getBytes();
                            mChatService.write(message);
                        }
                    }
                }
            }
        });
        if (checkDeviceDiscoverState()) {
            discoverSwitch.setChecked(true);
        } else {
            discoverSwitch.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        refreshItem = menu.findItem(R.id.action_refresh);
        refresh();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            discoverDevices();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        finish();
    }

    public void refresh() {
     /* Attach a rotating ImageView to the refresh item as an ActionView */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_image, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh_animation);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        refreshItem.setActionView(iv);

        //TODO trigger loading
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.i("TAG", "" + checkDeviceDiscoverState() + "  button" + compoundButton.isChecked());
        switch (compoundButton.getId()) {
            case R.id.discovery_switch:
                if (compoundButton.isChecked()) {
                    if (checkDeviceDiscoverState()) {
                        compoundButton.setChecked(true);
                    } else {
                        makeDiscoverAble();
                        compoundButton.setChecked(false);
                    }

                } else {
                    if (checkDeviceDiscoverState()) {
                        compoundButton.setChecked(true);
                    } else {
                        compoundButton.setChecked(false);
                    }

                }
        }
    }

    public void completeRefresh() {
        if (refreshItem.getActionView() != null) {
            refreshItem.getActionView().clearAnimation();
            refreshItem.setActionView(null);
        }
    }

    private boolean checkDeviceDiscoverState() {
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            // device is discoverable & connectable
            return true;
        } else {
            // device is not discoverable & connectable
            return false;
        }
    }

//    private void setStatus(int resId) {
//        BluetoothActivity activity = this;
//        if (null == activity) {
//            return;
//        }
//        Toast.makeText(BluetoothActivity.this, getResources().getString(resId), Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * Updates the status on the action bar.
//     *
//     * @param subTitle status
//     */
//    private void setStatus(CharSequence subTitle) {
//        BluetoothActivity activity = this;
//        if (null == activity) {
//            return;
//        }
//        Toast.makeText(BluetoothActivity.this, String.valueOf(subTitle), Toast.LENGTH_SHORT).show();
//    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            currentState.setText(getString(R.string.title_connected_to, mConnectedDeviceName));
                            if (dataToBeSent != null) {
                                byte[] message = dataToBeSent.getBytes();
                                mChatService.write(message);
                            }
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
                            currentState.setText(getString(R.string.title_connecting));
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
                            currentState.setText(getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    currentState.setText(getString(R.string.writing_data));
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.i("WRITE", writeMessage);
                    mChatService.stop();
                    break;
                case Constants.MESSAGE_READ:
                    currentState.setText(getString(R.string.reading_data));
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i("READ", readMessage);
                    try {
                        JSONObject jsonCard = new JSONObject(readMessage);
                        CardsDatabase cardsData = new CardsDatabase(getApplicationContext());
//                        cardsData.createNewEntry(jsonCard.getString(AppGlobals.NAME),
//                                jsonCard.getString(AppGlobals.ADDRESS), jsonCard.getString(
//                                        AppGlobals.JOB_TITLE), jsonCard.getString(AppGlobals.NUMBER),
//                                jsonCard.getString(AppGlobals.EMAIL), jsonCard.getString(AppGlobals.ORG),
//                                jsonCard.getString(AppGlobals.JOBZY_ID));
                        showNotification();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mChatService.stop();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (this != null) {
                        Toast.makeText(BluetoothActivity.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (this != null) {
                        Toast.makeText(BluetoothActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_cards)
                        .setContentTitle("New Card Received")
                        .setAutoCancel(true)
                        .setContentText("New Business Card Received. Click to open");
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
        int mNotificationId = 2112;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission granted");
                    checkBluetoothAndEnable();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied!"
                            , Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(null);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkBluetoothAndEnable();
        } else {
            if (ContextCompat.checkSelfPermission(BluetoothActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BluetoothActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                checkBluetoothAndEnable();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {

        }
    }

    private void checkBluetoothAndEnable() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Device does not support Bluetooth");
            return;
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();

            }
            discoverDevices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        discoverDevices();
                    }
                }, 3000);
            } else {
            }
        }
    }

    private void discoverDevices() {
        mBluetoothAdapter.startDiscovery();
        Log.i(TAG, "Discover");
        bluetoothDeviceArrayList = new ArrayList<>();
        bluetoothMacAddress = new HashMap<>();
        listView.setAdapter(null);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "receiver");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if (!mBluetoothAdapter.getAddress().equals(device.getAddress())) {
                    if (!bluetoothDeviceArrayList.contains(device.getName()) && device.getName() != null) {
                        bluetoothDeviceArrayList.add(device.getName());
                        bluetoothMacAddress.put(device.getName(), device.getAddress());
                        Log.i(TAG, device.getName() + "\n" + device.getAddress());
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                completeRefresh();
            }
            Adapter adapter = new Adapter(getApplicationContext(), R.layout.bluetooth_delegate,
                    bluetoothDeviceArrayList);
            listView.setAdapter(adapter);
        }
    };

    private void makeDiscoverAble() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkDeviceDiscoverState()) {
                    discoverSwitch.setChecked(true);
                } else {
                    discoverSwitch.setChecked(false);
                }
            }
        }, 3000);

    }

    private void connectDevice(String macAddress, boolean secure) {
        // Get the device MAC address
        //macAddress
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        // Attempt to connect to the device
        mBluetoothAdapter.cancelDiscovery();
        mChatService.connect(device, secure);
    }

    class Adapter extends ArrayAdapter<String> {

        private ArrayList<String> list;
        private Context mContext;

        public Adapter(Context context, int resource, ArrayList<String> list) {
            super(context, resource);
            this.list = list;
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.bluetooth_delegate, parent, false);
                holder = new ViewHolder();
                holder.bluetoothName = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bluetoothName.setText(list.get(position));
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    static class ViewHolder {
        public TextView bluetoothName;
    }

}