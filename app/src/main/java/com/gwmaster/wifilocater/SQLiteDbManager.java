package com.gwmaster.wifilocater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,
 * 如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 *
 */
public class SQLiteDbManager<TAG> {
    private static final String TAG = "MainActivity---wifisdm";
    //数据库存储路径
    String filePath = "data" + Environment.getDataDirectory().getAbsolutePath() +"/com.gwmaster.wifilocater/databases/WIFILocaterDB.db";

    // 数据库存放的文件夹 data/data/com.zwinsoft.voice 下面
    String pathStr = "data" + Environment.getDataDirectory().getAbsolutePath() +"/com.gwmaster.wifilocater/databases";


    /*String filePath = "data" + Environment.getDataDirectory().getAbsolutePath() +"/ZGY/WIFILocaterDB.db";
    // 数据库存放的文件夹 data/data/com.zwinsoft.voice 下面
    String pathStr = "data" + Environment.getDataDirectory().getAbsolutePath() +"/ZGY";*/

    SQLiteDatabase database;

    public SQLiteDatabase openDatabase(Context context) {

        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        // 查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("Voice", "存在数据库");
            // 存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            // 不存在先创建文件夹
            File path = new File(pathStr);
            Log.i("Voice", "pathStr=" + path);
            if (path.mkdir()) {
                Log.i("Voice", "创建成功");
            } else {
                Log.i("Voice", "创建失败");
            }
            try {
                // 得到资源
                AssetManager am = context.getAssets();
                // 得到数据库的输入流
                InputStream is = am.open("WIFILocaterDB.db");
                Log.i("Voice", is + "");
                // 用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("Voice", "fos=" + fos);
                Log.i("Voice", "jhPath=" + jhPath);
                // 创建byte数组 用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("Voice", "得到");
                    fos.write(buffer, 0, count);
                }
                // 最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            // 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

}
