package com.example.bluetoothdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.bluetoothdemo.wifi.WifiDirectManager;

public class WifiDirect extends AppCompatActivity {
    private ListView deviceListView;
    private Button scanButton;
    private WifiDirectManager wifiDirectManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_direct);
        deviceListView = findViewById(R.id.device_list);
        scanButton = findViewById(R.id.scan_button);
        wifiDirectManager = new WifiDirectManager(this);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiDirectManager.discoverPeers();
            }
        });
    }
}