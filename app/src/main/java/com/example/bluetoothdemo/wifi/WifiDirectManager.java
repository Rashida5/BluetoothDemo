package com.example.bluetoothdemo.wifi;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WifiDirectManager {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private Context context;
    private WifiP2pManager.PeerListListener peerListListener;
    private List<WifiP2pDevice> deviceList = new ArrayList<>();

    public WifiDirectManager(Context context) {
        this.context = context;
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(context, context.getMainLooper(), null);
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                deviceList.clear();
                Log.d("DiscoverPeers",peers.getDeviceList().toString());
                deviceList.addAll(peers.getDeviceList());
                // Update the UI with the list of nearby devices
            }
        };
    }

    public void discoverPeers() {
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Wi-Fi Direct discovery started successfully
                Log.d("DiscoverPeers","Successfull");
            }

            @Override
            public void onFailure(int reason) {
                // Wi-Fi Direct discovery failed
                Log.d("DiscoverPeers", String.valueOf(reason));
            }
        });
    }

    public List<WifiP2pDevice> getDeviceList() {
        return deviceList;
    }
}
