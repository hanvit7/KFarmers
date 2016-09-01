package org.qblex.QbleRemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by VIT_HOME on 2016-05-30.
 */
public class MyWifi extends Thread {
    private static final String TAG = MyWifi.class.getSimpleName();

    public enum WifiState {
        WAIT, CHECK_WIFI_ENABLE, CHECK_WIFI_CONNECTION, SEARCH_WIFI, WAIT_SEARCH_WIFI, CHECK_FILM_INFO,
        WAIT_CONNECT_FILM, CONNECT_FILM, CONNECT_SUCCESS, CONNECT_FAIL
    }

    volatile boolean running = false;
    private int cnt_;
    private int delay_;
    private String ssid_;
    private String password_;

    WifiState wifistate;
    String tempSsid;

    WifiConfiguration wifiConfig;
    ConnectivityManager manager;
    NetworkInfo wifi;
    IntentFilter filter;
    BroadcastReceiver wifiReceiver;

    WifiManager wifiManager;
    ScanResult scanResult;
    List apList;

    ProgressListner progressListner;
    Context context;

    public MyWifi(Context context, ProgressListner progressListner) {
        this.context = context;
        wifistate = WifiState.WAIT;
        this.progressListner = progressListner;
    }

    public interface ProgressListner {
        void progressDialog(WifiState wifiState, String string);
    }

    WifiState temp;

    @Override
    public void run() {
        wifistate = WifiState.WAIT;
        while (running) {
            try {
                if (temp != wifistate) {
                    Log.d(TAG, "WIFISTATE ----------" + wifistate + "----------");
                    temp = wifistate;
                }
                progressListner.progressDialog(wifistate, null);
                switch (wifistate) {
                    case WAIT:
                        if (cnt_ > 0) cnt_--;
                        else wifistate = WifiState.CHECK_WIFI_ENABLE;
                        break;

                    case CHECK_WIFI_ENABLE:
                        if (manager == null)
                            manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

                        if (wifiManager == null)
                            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                        if (wifiManager.isWifiEnabled()) {
                            Log.d(TAG, "isWifiEnabled : true");
                            wifistate = WifiState.CHECK_WIFI_CONNECTION;
                        } else {
                            Log.d(TAG, "isWifiEnabled : false");
                            wifiManager.setWifiEnabled(true);
                            while (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) ;
                            Log.d(TAG, "WifiEnabled");
                            wifistate = WifiState.SEARCH_WIFI;
                        }
                        break;

                    case CHECK_WIFI_CONNECTION:
                        progressListner.progressDialog(wifistate, "Check WiFi Connection");
                        wifi = manager.getActiveNetworkInfo(); // 와이파이에 연결된 상태
                        Log.d(TAG, "wifi : " + String.valueOf(wifi));

                        if (wifi != null) {
                            if (wifi.isConnected()) {
                                Log.d(TAG, "isConnected : true");
                                wifistate = WifiState.CHECK_FILM_INFO;
                            } else {
                                Log.d(TAG, "isConnected : false");
                                wifistate = WifiState.SEARCH_WIFI;
                            }
                        } else {
                            Log.d(TAG, "isConnected : true");
                            wifistate = WifiState.SEARCH_WIFI;
                        }
                        break;

                    case CHECK_FILM_INFO:
                        progressListner.progressDialog(wifistate, "Check Connect with Film WiFi");
                        Log.d(TAG, "connected wifi ssid : " + String.valueOf(wifi.getExtraInfo()));
                        Log.d(TAG, "getConnectionInfo : " + String.valueOf(wifiManager.getConnectionInfo()));
                        Log.d(TAG, "getConnectionInfo.ssid : " + String.valueOf(wifiManager.getConnectionInfo().getSSID()));
//                        if (wifi.getExtraInfo().contains(ssid_)) {
                        if (wifiManager.getConnectionInfo().getSSID().contains(ssid_)) {
                            Log.d(TAG, "wifi.getExtraInfo().contains(ssid_) : true");
                            wifistate = WifiState.WAIT_CONNECT_FILM;
                        } else {
                            Log.d(TAG, "wifi.getExtraInfo().contains(ssid_) : false");
                            wifiManager.disconnect();
                            wifistate = WifiState.SEARCH_WIFI;
                        }
                        break;

                    case SEARCH_WIFI:
                        progressListner.progressDialog(wifistate, "Searching WiFi");
//                        if (wifiManager.isWifiEnabled()) {
//                        wifi = manager.getActiveNetworkInfo(); // 와이파이에 연결된 상태
//                        Log.d(TAG, "wifi : " + wifi);
//                        if (wifi == null) {
//                                if (!wifi.isConnected()) {
                        if (wifiReceiver == null)
                            wifiReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    if (wifistate == WifiState.WAIT_SEARCH_WIFI &
                                            intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                                        Log.d(TAG, "searchWifi");
                                        searchWifi();
                                    }
                                }
                            };

                        wifiManager.startScan();
                        wifistate = WifiState.WAIT_SEARCH_WIFI;
//                            }

                        if (filter == null) filter = new IntentFilter();
                        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                        context.registerReceiver(wifiReceiver, filter);

//                        }
                        break;

                    case WAIT_SEARCH_WIFI:
                        break;

                    case CONNECT_FILM:
                        progressListner.progressDialog(wifistate, "Connect to Film WiFi");
                        if (wifiConfig == null) wifiConfig = new WifiConfiguration();
//                        wifiConfig.SSID = String.format("\"%s\"", tempSsid);//USER SET 수정
                        wifiConfig.SSID = "\"".concat(tempSsid).concat("\"");
                        wifiConfig.status = WifiConfiguration.Status.DISABLED;
//                        wifiConfig.priority = 40;
                        //wpa나 wpa2일때
                        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//                        wifiConfig.preSharedKey = String.format("\"%s\"", password_);
                        wifiConfig.preSharedKey = "\"".concat(password_).concat("\"");

                        int netId = wifiManager.addNetwork(wifiConfig);
                        if (netId != -1) {
                            Log.d(TAG, "enableNetwork : ");
                            wifiManager.enableNetwork(netId, true);
                        } else {
                            Log.d(TAG, "enableNetwork : netId == -1");
                        }
                        Log.d(TAG, "SSID : " + wifiConfig.SSID + ", preSharedKey : " + wifiConfig.preSharedKey);
                        Log.d(TAG, "wifiConfig : " + String.valueOf(wifiConfig));
                        wifiManager.updateNetwork(wifiConfig);
                        Log.d(TAG, "getConnectionInfo : " + String.valueOf(wifiManager.getConnectionInfo()));
//                        wifiManager.saveConfiguration();
//                        wifiManager.reconnect();

                        wifistate = WifiState.WAIT_CONNECT_FILM;
                        break;

                    case WAIT_CONNECT_FILM:
                        wifi = manager.getActiveNetworkInfo(); // 와이파이에 연결된 상태
                        if (wifi != null) {
//                            if (wifi.getExtraInfo().contains(ssid_)) {
                            if (wifiManager.getConnectionInfo().getSSID().contains(ssid_)) {
                                Log.d(TAG, "isConnected : true");
                                Log.d(TAG, "getConnectionInfo : " + String.valueOf(wifiManager.getConnectionInfo()));
                                progressListner.progressDialog(wifistate, "Connecting");
//                                if (!socket.isConnected()) socketStart();
//                                Log.d(TAG, "saveConfiguration : " + String.valueOf(wifiManager.saveConfiguration()));
                                wifistate = WifiState.CONNECT_SUCCESS;
                            }
//                            }
                        }
                        break;

                    case CONNECT_SUCCESS:
                        progressListner.progressDialog(wifistate, null);
                        running = false;
                        wifiConfig = null;
                        manager = null;
                        wifi = null;
                        if (wifiReceiver != null) {
                            context.unregisterReceiver(wifiReceiver);
                            wifiReceiver = null;
                            filter = null;
                        }
                        break;

                    case CONNECT_FAIL:
                        progressListner.progressDialog(wifistate, null);
                        running = false;
                        wifiConfig = null;
                        manager = null;
                        wifi = null;
                        if (wifiReceiver != null) {
                            context.unregisterReceiver(wifiReceiver);
                            wifiReceiver = null;
                            filter = null;
                        }
                        break;
                }
                sleep(delay_);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (Exception e) {

            }
        }
    }

    void setRunning(boolean b, int cnt, int delay) {
        running = b;
        cnt_ = cnt;
        delay_ = delay;
    }

    public void setWifiInfo(String ssid, String password) {
        ssid_ = ssid;
        password_ = password;
    }

    public void searchWifi() {
        apList = wifiManager.getScanResults();
        if (wifiManager.getScanResults() != null) {
            int size = apList.size();
            Log.d(TAG, "SSID : " + ssid_);
            for (int i = 0; i < size; i++) {
                scanResult = (ScanResult) apList.get(i);
                Log.d(TAG, "scanResult.SSID : " + scanResult.SSID);
                if (scanResult.SSID.contains(ssid_)) {
                    tempSsid = scanResult.SSID;
                    wifistate = WifiState.CONNECT_FILM;
                    return;
                }
            }
        }
        //찾을수가 없습니다.
        wifistate = WifiState.CONNECT_FAIL;
    }

}
