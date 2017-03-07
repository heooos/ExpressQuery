package com.jkxy.expressquery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zh on 2017/3/7.
 */

public class ImageLoader {


    public static Bitmap getBitmapFromFile(String file){
        Bitmap image = null;
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];//读取
        int len;
        try {
            InputStream is = new FileInputStream(file);
            while ((len=is.read(bytes))!=-1){
                outputStream.write(bytes, 0, len);//写入
            }
            byte[] result=outputStream.toByteArray();
            image = BitmapFactory.decodeByteArray(result, 0, result. length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
