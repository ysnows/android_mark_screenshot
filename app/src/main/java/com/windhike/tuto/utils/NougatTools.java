package com.windhike.tuto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.windhike.tuto.BuildConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.core.content.FileProvider;

/**
 * Android N 适配工具类
 */
public class NougatTools {
    /**
     * 将普通uri转化成适应7.0的content://形式  针对文件格式
     *
     * @param context    上下文
     * @param file       文件路径
     * @param intent     intent
     * @param intentType intent.setDataAndType
     * @return
     */
    public static Intent formatFileProviderIntent(
            Context context, File file, Intent intent, String intentType) {

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        // 表示文件类型
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, intentType);

        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式  针对图片格式
     *
     * @param context 上下文
     * @param file    文件路径
     * @param intent  intent
     * @return
     */
    public static Intent formatFileProviderPicIntent(
            Context context, File file, Intent intent) {


        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 表示图片类型
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式
     *
     * @return
     */
    public static Uri formatFileProviderUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        return uri;
    }

    public static void openFile(Activity activityFrom, File file) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  此处注意替换包名，
            Uri contentUri = FileProvider.getUriForFile(activityFrom, "com.quansu.heikeng", file);
            intent.setDataAndType(contentUri, getMimeTypeFromFile(file));
//            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), getMimeTypeFromFile(file));//也可使用 Uri.parse("file://"+file.getAbsolutePath());
        }

        //以下设置都不是必须的
        intent.setAction(Intent.ACTION_VIEW);// 系统根据不同的Data类型，通过已注册的对应Application显示匹配的结果。
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task
        //若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
        intent.addCategory(Intent.CATEGORY_DEFAULT);//按照普通Activity的执行方式执行
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activityFrom.startActivity(intent);
    }

    public static String getMimeTypeFromFile(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex > 0) {
            //获取文件的后缀名
            String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            HashMap<String, String> map = MyMimeMap.getMimeMap();
            if (!TextUtils.isEmpty(end) && map.keySet().contains(end)) {
                type = map.get(end);
            }
        }
        return type;
    }


}
