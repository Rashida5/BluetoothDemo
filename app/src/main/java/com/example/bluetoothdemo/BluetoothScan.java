package com.example.bluetoothdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class BluetoothScan extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private boolean mScanning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth.
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startScan();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }
    private void startScan() {
        if (!mScanning) {
            mScanning = true;
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Device found.
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceName = device.getName();
                        String deviceAddress = device.getAddress();
                        Toast.makeText(BluetoothScan.this, "Found device: " + deviceName + " (" + deviceAddress + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(this, "Scanning for devices...", Toast.LENGTH_SHORT).show();
        }
    }
    private void stopScan() {
        if (mScanning) {
            mScanning = false;
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
            mBluetoothAdapter.cancelDiscovery();
            Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
        }
    }
}
