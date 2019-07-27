package me.gindoc.compressor.utils;

import android.os.Environment;

/**
 * Author: GIndoc on 2019/6/12.
 * FOR   :
 */
public class Constants {

    /**
     * 获取manifest.xml中定义的
     * <meta-data
     *  android:name="provider_auth"
     *  android:value="${AUTHORITIES}" />，其中AUTHORITIES在build.gradle中定义
     */
    public static final String PROVIDER_AUTH_KEY = "provider_auth";


    // 不一定正确，具体的以build.gradle中定义的为准，当获取不到build.gradle中定义的值时才取这个
    public static final String PROVIDER_AUTH_UNKNOWN = "me.gindoc.compressor.provider";


    // 缓存根目录
    public static final String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";

    // 压缩后缓存目录
    public static final String COMPRESS_CACHE = "compress_cache";

}
