package me.gindoc.compressimagedemo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: GIndoc on 2019/6/12.
 * FOR   :
 */
public class CachePathUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);


    /**
     * 创建拍照路径
     *
     * @param fileName 图片名
     * @return 缓存文件路径
     */
    private static File getCameraCacheDir(String fileName) {
        File cache = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!cache.mkdirs() && (!cache.exists() || !cache.isDirectory())) {
            return null;
        } else {
            return new File(cache, fileName);
        }
    }

    /**
     * 获取拍照缓存文件
     *
     * @return 缓存文件
     */
    public static File getCameraCacheFile() {
        String fileName = "camera_" + getBaseFileName() + ".jpg";
        return getCameraCacheDir(fileName);
    }


    /**
     * 获取图片文件名
     *
     * @return 图片文件名
     */
    public static String getBaseFileName() {
        return dateFormat.format(new Date());
    }
}
