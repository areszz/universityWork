package com.gwmaster.wifilocater.Calculation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AP extends AppCompatActivity {
    Point point;
    Calculate cal;
    int level1;
    int level2;
    int level3;
    int level4;
    private static final String TAG = "MainActivity---wifi";
    HashMap<String, Integer> allList = new HashMap<>();
    private TextView showTv;
    StringBuffer sb=new StringBuffer();

    public void aps(HashMap list) {
        Log.d("ap", "daole");
        cal=new Calculate();
    }

//获取wifi 列biao
public List<Integer> getWifiList() {
//    Toast.makeText(this, "初始化成功！", Toast.LENGTH_SHORT).show();


    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    //判断wifi 是否开启

    if (wifiManager.isWifiEnabled()) {
        Log.e(TAG, " wifi 打开");
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<ScanResult> wifiList = new ArrayList<>();
        List<Integer> levelList = new ArrayList<Integer>();

        if (scanWifiList != null && scanWifiList.size() > 0) {
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                sb.append(scanResult.SSID + "---" + scanResult.BSSID + "\n" +scanResult.level+"\n");
                Log.d("list", "daole");
                allList.put(scanResult.BSSID,scanResult.level);
            }
            return levelList;
        } else {
            Log.e(TAG, "非常遗憾搜索到wifi");
        }
    } else {
        Log.e(TAG, " wifi 关闭");
    }




    return null;
}

}