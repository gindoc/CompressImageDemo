package me.gindoc.compressimagedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import me.gindoc.compressor.utils.UriParseUtils;

import java.io.File;

/**
 * Author: GIndoc on 2019/6/12.
 * FOR   :
 */
public class CommonUtils {


    /**
     * 判断是否有摄像头
     *
     * @param context 上下文
     * @return true 有摄像头   false 没有摄像头
     */
    public static boolean hasCamera(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context 为null");
        }
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Camera.getNumberOfCameras() > 0;
    }


    /**
     * 获取拍照intent
     *
     * @param outputUri 拍照后图片的输出uri
     * @return 返回intent，方便封装跳转
     */
    public static Intent getCameraIntent(Uri outputUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        return intent;
    }


    public static String startCameraPage(Activity activity) {
        File file = CachePathUtils.getCameraCacheFile();
        Uri outputUri = UriParseUtils.getUriForFile(activity, file);
        if (hasCamera(activity)) {
            activity.startActivityForResult(getCameraIntent(outputUri), Constants.CAMERA_CODE);
            return file.getAbsolutePath();
        }
        return null;
    }

    public static void startAlbumPage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, Constants.ALBUM_CODE);
    }
}
