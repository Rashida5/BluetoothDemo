package com.example.bluetoothdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //Step 1: create the objects
    Button b1, b2, b3;
    ListView scanListView;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
    //Step 2: Declaring the Constants of Bluetooth Adapter Class
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final String TAG = "BluetoothScan";
    BluetoothLeScanner myScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //step 3 : Fetch the Refernce
        b1 = (Button) findViewById(R.id.TurnON);
        b2 = (Button) findViewById(R.id.TurnOFF);
        b3 = (Button) findViewById(R.id.showList);
        scanListView = (ListView) findViewById(R.id.ListOfNearby);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myAdapter == null) {
                    Toast.makeText(MainActivity.this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
                } else if (!myAdapter.isEnabled()) {
                    // Bluetooth is not enabled, prompt the user to enable it
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
                } else {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    } else {
                       /* Log.d("BluetoothScan","StartDescovery");
                        myAdapter.startDiscovery();*/

                    }
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, intentFilter);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
        scanListView.setAdapter(arrayAdapter);

    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("BluetoothScan",action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, action);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                if (deviceName != null) {
                    // Concatenate the name and address into a single string before adding to the list
                    stringArrayList.add(deviceName + " (" + deviceAddress + ")");
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };
}