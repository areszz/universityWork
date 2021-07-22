package com.gwmaster.wifilocater;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gwmaster.wifilocater.Calculation.AP;
import com.gwmaster.wifilocater.Calculation.Calculate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity---wifi";
    private TextView showTv;
    private EditText editText;
    private TextView tv1;
    private TextView tv2;
    public String str;
    public String str1;
    public Calculate cal;
    public AP ap1;
    public SQLiteDbManager sdm;
    public String[] rooms = new String[35];
    public String room;

    public boolean start = false;

    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showTv = (TextView) findViewById(R.id.showID);
        showTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv1 = (TextView) findViewById(R.id.testword);
        str = "";
        sdm = new SQLiteDbManager();
        SQLiteDatabase sqLiteDatabase = sdm.openDatabase(this);
        try (Cursor cursor = sqLiteDatabase.query("room", null, null, null, null, null, null)) {
            int x = 0;
            while (cursor.moveToNext()) {
                String stt = cursor.getString(0);
                rooms[x] = stt;
                x++;
            }
        }
        sqLiteDatabase.close();
        Toast.makeText(this, "初始化成功！", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CHANGE_NETWORK_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            };
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }


        }



//        //getWifiList();
//        showTv.setText(sb);
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t2=new Thread()
                {
                    public void run() {
//                        String url ="http://47.93.11.41:8080/testBoot/insert?id=" + "556" +"?userName=" + "xyk"+ "?passWord="+ "9999999";
                        String url ="http://47.93.11.41:8080/testBoot/insert/557/xyk/123";
                        System.out.println(sendRequest(url,"GET"));
                    }
                };
                t2.start();
//                String url ="http://47.93.11.41:8080/testBoot/insert?id=" + "556" +"?userName=" + "xyk"+ "?passWord="+ "9999999";
//                if (android.os.Build.VERSION.SDK_INT > 9) {
//                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                    StrictMode.setThreadPolicy(policy);
//                }
//                System.out.println(sendRequest(url,"POST"));
            }
        });


        //刷新按钮
        Button rb = findViewById(R.id.rbutton);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb=new StringBuffer();
                cal=new Calculate();
                cal.getwifi(getWifiList());
                cal.search(tv1,rooms);
                showTv.setText(sb);
                start = true;
                Thread t = new Thread(){
                    public void run() {
                        while (start) {
                            sb=new StringBuffer();
                            cal.getwifi(getWifiList());
                            cal.search(tv1, rooms);
                            showTv.setText(sb);
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                };
                t.start();
            }
        });

    }

    public static String sendRequest(String urlParam,String requestType) {

        HttpURLConnection con = null;

        BufferedReader buffer = null;
        StringBuffer resultBuffer = null;

        try {
            URL url = new URL(urlParam);
            //得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod(requestType);
            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=GBK");
            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            //得到响应码
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = con.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                return resultBuffer.toString();
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }





    public HashMap<String, Integer> getWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        HashMap<String, Integer> allList = new HashMap<>();
        //判断wifi 是否开启
        if (wifiManager.isWifiEnabled()) {
            Log.e(TAG, " wifi 打开");
            List<ScanResult> scanWifiList = wifiManager.getScanResults();
            List<ScanResult> wifiList = new ArrayList<>();
            List<Integer> levelList = new ArrayList<Integer>();
            if (scanWifiList != null && scanWifiList.size() > 0) {
                for (int i = 0; i < scanWifiList.size(); i++) {
                    ScanResult scanResult = scanWifiList.get(i);

                    sb.append(scanResult.SSID + "---" + scanResult.BSSID + "@" +scanResult.level+"\n");
                    Log.d("list", "daole");
                    allList.put(String.valueOf(scanResult.BSSID),scanResult.level);
                }
                return allList;
            } else {
                Log.e(TAG, "非常遗憾搜索到wifi");
            }
        } else {
            Log.e(TAG, " wifi 关闭");
        }
        return null;
    }


                    //获取wifi 列表

}
