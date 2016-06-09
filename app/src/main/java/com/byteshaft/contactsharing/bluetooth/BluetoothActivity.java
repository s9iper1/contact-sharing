package com.byteshaft.contactsharing.bluetooth;

import android.Manifest;
import android.app.ActionBar;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = BluetoothActivity.class.getSimpleName();
    public static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private ArrayList<String> bluetoothDeviceArrayList;
    private HashMap<String, String> bluetoothMacAddress;
    private Button button;
    private Button send;
    private BluetoothChatService mChatService = null;
    private String mConnectedDeviceName = null;
    public ViewHolder holder;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        button = (Button) findViewById(R.id.button);
        send = (Button) findViewById(R.id.send);
        button.setOnClickListener(this);
        send.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.devicesList);
        bluetoothDeviceArrayList = new ArrayList<>();
        bluetoothMacAddress = new HashMap<>();
        mChatService = new BluetoothChatService(getApplicationContext(), mHandler);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("TAG", ""+ i);
                Log.i("TAG", "" + bluetoothDeviceArrayList.get(i));
                Log.i("TAG", ""+ bluetoothMacAddress.get(bluetoothDeviceArrayList.get(i)));
            }
        });
    }

    private void setStatus(int resId) {
        BluetoothActivity activity = this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        BluetoothActivity activity = this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.i("TAG", writeMessage);
//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i("TAG", readMessage);
//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
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
                            , Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
            // Start the Bluetooth chat services
            mChatService.start();
        }
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
        unregisterReceiver(mReceiver);
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
            discoverDevices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            discoverDevices();
        }
    }

    private void discoverDevices() {
        mBluetoothAdapter.startDiscovery();
//        makeDiscoverAble();
        Log.i(TAG, "Discover");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
//                new ConnectThread(bluetoothDeviceArrayList.get(0));
//                BluetoothDevice bluetoothDevice = bluetoothDeviceArrayList.get(0);
//                connectDevice(bluetoothDevice.getAddress(), true);
                break;
            case R.id.send:
                byte[] message = "this is a test".getBytes();
                mChatService.write(message);
                break;
        }
    }

    private void connectDevice(String macAddress, boolean secure) {
        // Get the device MAC address
        //macAddress
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        // Attempt to connect to the device
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