package com.jkxy.expressquery;

import android.os.Environment;

/**
 * Created by zh on 2016/11/3.
 */

public class Constants {

    public static final int REQUEST_ACTION_IMAGE_CAPTURE = 2;

    public static final int REQUEST_ACTION_GET_CONTENT = 1;
    public static final int REQUEST_CROP_CODE = 3;

    public static final int REQUEST_LOAD_ACTIVITY = 4;


    //缓存图片路径
    public static final String IMAGE_CACHE_PATH = Environment
            .getExternalStorageDirectory()
            .getPath()
            + "/"
            + "temp.jpg";

}
