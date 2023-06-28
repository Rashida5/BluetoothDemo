package com.example.bluetoothdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BluetoothCallback extends AppCompatActivity {
    //Step 1: create the objects
    Button b1, b2, b3;
    ListView scanListView;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothLeScanner myScanner;
    //Step 2: Declaring the Constants of Bluetooth Adapter Class
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final String TAG = "BluetoothScan";
    private static final String TAGBluetooth="BluetoothCallbackScan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //step 3 : Fetch the Refernce
        b1 = (Button) findViewById(R.id.TurnON);
      //  b2 = (Button) findViewById(R.id.TurnOFF);
       // b3 = (Button) findViewById(R.id.showList);
        scanListView = (ListView) findViewById(R.id.ListOfNearby);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myAdapter == null) {
                    Toast.makeText(BluetoothCallback.this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
                } else if (!myAdapter.isEnabled()) {
                    // Bluetooth is not enabled, prompt the user to enable it
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
                } else {
                    if (ActivityCompat.checkSelfPermission(BluetoothCallback.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BluetoothCallback.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    } else {
                        Log.d("BluetoothScan","StartScanning");
                        myScanner = myAdapter.getBluetoothLeScanner();
                        ScanSettings settings = new ScanSettings.Builder()
                                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                                .build();
                        myScanner.startScan(null, settings, myScanCallback);
                    }
                }
            }
        });
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
        scanListView.setAdapter(arrayAdapter);
    }

    private ScanCallback myScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAGBluetooth, "Result "+result.toString());
            BluetoothDevice device = result.getDevice();
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            if (deviceName != null) {
                // Concatenate the name and address into a single string before adding to the list
                stringArrayList.add(deviceName + " (" + deviceAddress + ")");
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAGBluetooth, String.valueOf(results.size()));
            for (ScanResult result : results) {
                BluetoothDevice device = result.getDevice();
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                if (deviceName != null) {
                    // Concatenate the name and address into a single string before adding to the list
                    stringArrayList.add(deviceName + " (" + deviceAddress + ")");
                }
            }
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Scan failed with error code " + errorCode);
           Log.d( TAGBluetooth,"ScanFailure");
            Log.d( TAGBluetooth, String.valueOf(errorCode));
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (myScanner != null) {
            myScanner.stopScan(myScanCallback);
        }
    }
}