package com.windhike.tuto.easytouch.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by Aria on 2017/7/19.
 */

public class FileUtil {

    //系统保存截图的路径
    public static final String SCREENCAPTURE_PATH = "Screenshots" + File.separator;
//    public static final String SCREENCAPTURE_PATH = "UM" + File.separator + "Screenshots" + File.separator;

    public static final String SCREENSHOT_NAME = "Screenshot";

    public static String getAppPath(Context context) {
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            return Environment.getExternalStorageDirectory().toString();
//        } else {
        return context.getExternalFilesDir(DIRECTORY_PICTURES).toString();
//        }

    }


    public static String getScreenShots(Context context) {

        StringBuffer stringBuffer = new StringBuffer(getAppPath(context));
        stringBuffer.append(File.separator);

//        stringBuffer.append(SCREENCAPTURE_PATH);

        File file = new File(stringBuffer.toString());

        if (!file.exists()) {
            file.mkdirs();
        }

        return stringBuffer.toString();

    }

    public static String getScreenShotsName(Context context) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        String date = simpleDateFormat.format(new Date());

        StringBuffer stringBuffer = new StringBuffer(getScreenShots(context));
        stringBuffer.append(SCREENSHOT_NAME);
        stringBuffer.append("_");
        stringBuffer.append(date);
        stringBuffer.append(".png");

        return stringBuffer.toString();

    }


}
