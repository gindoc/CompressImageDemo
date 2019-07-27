package me.gindoc.compressor.listener;


/**
 * 单张图片压缩时的监听
 */
public interface CompressResultListener {

    /**
     * 压缩成功
     *
     * @param imgPath
     */
    void onCompressSuccess(String imgPath);

    /**
     * 压缩失败
     *
     * @param imgPath
     * @param error
     */
    void onCompressFailed(String imgPath, String error);

}