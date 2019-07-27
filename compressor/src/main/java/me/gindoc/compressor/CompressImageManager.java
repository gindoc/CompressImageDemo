package me.gindoc.compressor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import me.gindoc.compressor.bean.Photo;
import me.gindoc.compressor.config.CompressConfig;
import me.gindoc.compressor.core.CompressImageUtil;
import me.gindoc.compressor.listener.CompressImage;
import me.gindoc.compressor.listener.CompressResultListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: GIndoc on 2019/6/12.
 * FOR   :
 */
public class CompressImageManager implements CompressImage {

    private CompressImageUtil compressImageUtil;                    // 压缩工具类
    private ArrayList<Photo> images;                                // 需要压缩的图片集合
    private CompressListener listener;                              // 压缩监听，告知页面Activity
    private CompressConfig config;                                  // 压缩配置
    private Context mContext;

    private CompressImageManager(Context context, CompressConfig config,
                                 ArrayList<Photo> photos, CompressImage.CompressListener listener) {
        mContext = context;
        compressImageUtil = new CompressImageUtil(config);
        this.config = config;
        this.images = photos;
        this.listener = listener;
    }

    public static CompressImage build(Context context, CompressConfig config,
                                      ArrayList<Photo> photos, CompressImage.CompressListener listener) {
        return new CompressImageManager(context, config, photos, listener);
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, "图片集合为空");
            return;
        }
        for (Photo image : images) {
            if (image == null) {
                listener.onCompressFailed(images, "图片集合中某张图片为空");
                return;
            }
        }

        // 开始递归压缩，从第一张开始
        compress(images.get(0));
    }

    // 第一张图片的index==0
    private void compress(final Photo image) {
        if (TextUtils.isEmpty(image.getOriginalPath())) {
            continueCompress(image, false);
            return;
        }
        File file = new File(image.getOriginalPath());
        if (!file.exists() || !file.isFile()) {
            continueCompress(image, false);
            return;
        }
        if (file.length() < config.getMaxSize()) {
            image.setCompressPath(image.getOriginalPath());
            continueCompress(image, true);
            return;
        }

        // 开始真正的压缩（逐张）
        compressImageUtil.compress(image.getOriginalPath(), new CompressResultListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                // 压缩成功，设置该图片对象为已压缩，并递归到下一张需要压缩的图片对象
                image.setCompressPath(imgPath);

                // 不保存原图
                if (!config.isEnableReserveRaw()) {
                    boolean isDelete = new File(image.getOriginalPath()).delete();

                    // 删除后通知系统相册刷新
                    if (isDelete) {
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(image.getOriginalPath()))));
                    }
                }

                continueCompress(image, true);
            }

            @Override
            public void onCompressFailed(String imgPath, String error) {
                continueCompress(image, false, error);

            }
        });
    }

    private void continueCompress(Photo image, boolean isCompress, String... error) {
        image.setCompressed(isCompress);

        int index = images.indexOf(image);

        // 是否为需要压缩的图片集合中的最后一张图片对象
        if (index == images.size() - 1) {
            // 全部压缩完，告知页面Activity
            handleCallback(error);
        } else {
            // 继续压缩下一张
            compress(images.get(index + 1));
        }
    }

    private void handleCallback(String... error) {
        // 如果有错误信息
        if (error.length > 0) {
            listener.onCompressFailed(images, error);
            return;
        }

        for (Photo image : images) {
            // 如果存在没有压缩的图片，或者压缩失败的
            if (!image.isCompressed()) {
                listener.onCompressFailed(images, image.getOriginalPath() + "图片压缩失败");
                return;
            }
        }

        listener.onCompressSuccess(images);
    }

}
