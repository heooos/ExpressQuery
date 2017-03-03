package com.jkxy.expressquery.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by zh on 2017/2/22.
 */
// TODO: 2017/3/3 添加异步处理
public class AutoGetNumberUtils {

    static String PATH = Environment.getExternalStorageDirectory().getPath() + "/tesseract/";
    //训练数据路径，必须包含tesseract文件夹
    static final String TESSBASE_PATH = "/storage/emulated/0/Download/tesseract/";
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";

    public static String getNumber(Bitmap bmp) {
        Log.d("路径信息", PATH);
        final TessBaseAPI baseApi = new TessBaseAPI();
        //初始化OCR的训练数据路径与语言
        baseApi.init(PATH, DEFAULT_LANGUAGE);
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bmp);
        String text = baseApi.getUTF8Text();
        Log.d("识别的内容", text);
        baseApi.clear();
        baseApi.end();

        return text;
    }

}
