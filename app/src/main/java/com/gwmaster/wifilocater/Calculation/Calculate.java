package com.gwmaster.wifilocater.Calculation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.gwmaster.wifilocater.MainActivity;
import com.gwmaster.wifilocater.SQLiteDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Calculate extends MainActivity {
    List<Point> points = new ArrayList<>();
    Point point;
    int num;
    public SQLiteDbManager sdm;
    public String[] rooms = new String[35];
    public int[] distances= new int[35];
    private TextView tv1;
//    private TextView tv2;
    public String room = "";
    public String str,str1;
    HashMap<String, Integer> allList = new HashMap<>();

    public Calculate(){

//        rooms[0] = "3051";
//        rooms[1] = "3052";
//        rooms[2] = "3053";
//        rooms[3] = "3071";
//        rooms[4] = "3072";
//        rooms[5] = "3073";
//        rooms[6] = "306";
//        rooms[7] = "3081";
//        rooms[8] = "3082";
//        rooms[9] = "3083";
//        rooms[10] = "3041";
//        rooms[11] = "3042";
//        rooms[12] = "3043";
//        rooms[13] = "3091";
//        rooms[14] = "3092";
//        rooms[15] = "3093";
//        rooms[16] = "3101";
//        rooms[17] = "3102";
//        rooms[18] = "3103";
//        rooms[19] = "3031";
//        rooms[20] = "3032";
//        rooms[21] = "3033";
//        rooms[22] = "3111";
//        rooms[23] = "3021";
//        rooms[24] = "3022";
//        rooms[25] = "3023";
//        rooms[26] = "3121";
//        rooms[27] = "3122";
//        rooms[28] = "3123";
//        rooms[29] = "3011";
//        rooms[30] = "3012";
//        rooms[31] = "3013";
//        rooms[32] = "3131";
//        rooms[33] = "3132";
//        rooms[34] = "3133";
    }
    public void getwifi(HashMap allList){
        this.allList = allList;
    }

    public void search(TextView tv1,String[] rooms){
        this.tv1 = tv1;
        for(int i=0;i<35;i++) {
            distances[i]=getDistance(rooms[i]);
        }
        int min = Integer.MAX_VALUE;
        int j = 0;
        for(int i =0;i<35;i++){
            if(distances[i]<min){
                min = distances[i];
                j = i;
            }
            Log.d("33hao", String.valueOf(distances[33]));
        }
        if(min<400) {
            tv1.setText(rooms[j]);
        }
        else{
            tv1.setText("测量区域外");
        }
    }

    public int getDistance(String room){
        sdm = new SQLiteDbManager();
        String pow1="";
        String pow2="";
        String pow3="";
        String pow4="";
        String pow5="";
        String pow6="";

        String id1="";
        String id2="";
        String id3="";
        String id4="";
        String id5="";
        String id6="";

        int i=0;
        int dis=0;

        SQLiteDatabase sqLiteDatabase = sdm.openDatabase(this);
        try (Cursor cursor = sqLiteDatabase.query("data", null, "position = ?", new String[]{room}, null, null, null)) {
            while (cursor.moveToNext()) {
                if(i==0) {
                    pow1 = cursor.getString(4);
                    id1 = cursor.getString(3);
                }
                else if(i==1) {
                    pow2 = cursor.getString(4);
                    id2 = cursor.getString(3);
                }
                else if(i==2) {
                    pow3 = cursor.getString(4);
                    id3 = cursor.getString(3);
                }
                else if(i==3) {
                    pow4 = cursor.getString(4);
                    id4 = cursor.getString(3);
                }
                else if(i==4) {
                    pow5 = cursor.getString(4);
                    id5 = cursor.getString(3);
                }
                else if(i==5) {
                    pow6 = cursor.getString(4);
                    id6 = cursor.getString(3);
                }
                i++;
            }


            int wifipow1=-200;
            int wifipow2=-200;
            int wifipow3=-200;
            int wifipow4=-200;
            int wifipow5=-200;
            int wifipow6=-200;

            if(allList.containsKey(id1)){
                wifipow1=allList.get(id1);
                Log.d("id1", id1);
            }
            if(allList.containsKey(id2)){
                wifipow2=allList.get(id2);
                Log.d("id2", id2);
            }
            if(allList.containsKey(id3)){
                wifipow3=allList.get(id3);
                Log.d("id3", id3);
            }
            if(allList.containsKey(id4)){
                wifipow4=allList.get(id4);
                Log.d("id4", id4);
            }
            if(allList.containsKey(id5)){
                wifipow5=allList.get(id5);
                Log.d("id5", id5);
            }
            if(allList.containsKey(id6)){
                wifipow6=allList.get(id6);
                Log.d("id6", id6);
            }


            dis+=(int) Math.pow(wifipow1-strToInt(pow1),2)+(int) Math.pow(wifipow2-strToInt(pow2),2);
            dis+=(int) Math.pow(wifipow3-strToInt(pow3),2)+(int) Math.pow(wifipow4-strToInt(pow4),2);
            dis+=(int) Math.pow(wifipow5-strToInt(pow5),2)+(int) Math.pow(wifipow6-strToInt(pow6),2);

            dis= (int) Math.sqrt(dis);


//            tv2.setText(id1);
//            tv2.append(id2);
//            tv2.append(id3);
//            tv2.append(id4);
//            tv2.append(id5);
//            tv2.append(id6);
            sqLiteDatabase.close();
        }
        return dis;
    }

    public int strToInt(String str) throws NumberFormatException{
        if (str == null || str.contentEquals("")) { // 如果传入的字符串为空对象或者传入的字符串为空字符串，则抛出异常
            throw new NumberFormatException("null or empty string"); // 这里直接利用java封装好的异常类，当然我们也可以自己封装异常类，面试官要考察的不是对异常类的封装，而是你要知道要处理异常情况
        }
        boolean negative = false; // negative为true表示是负数，反之为正数
        int pos = 0;
        if (str.charAt(0) == '-') { // 如果为负数
            negative = true;
            pos++; // 调过第一位符号位
        } else if (str.charAt(0) == '+') {
            pos++; // 调过第一位符号位
        }
        int number = 0;
        while (pos < str.length()-1) {
            number *= 10;
            number += (str.charAt(pos) - '0');
            pos++;
        }
        return negative ? -number : number; // 如果为负数则返回对应的负数
    }

    public String changeString(String str){
        String sfin="";
        for(int i = 0; i < str.length() ; i++){
            String ss = String.valueOf(str.charAt(i));
            sfin=sfin+ss;
        }
        return sfin;
    }


}
