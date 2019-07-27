package me.gindoc.compressor.config;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * Author: GIndoc on 2019/6/12.
 * FOR   : 压缩配置类
 */
public class CompressConfig {

    /**
     * 图片压缩格式
     */
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    /**
     * 长不超过的最大像素，单位px
     */
    private int maxWidth = 1200;

    /**
     * 宽不超过的最大像素，单位px
     */
    private int maxHeight = 1200;

    /**
     * 压缩到的最大大小,单位B
     */
    private int maxSize = 200 * 1024;

    /**
     * 压缩质量
     */
    private int quality = 70;

    /**
     * 是否启用像素压缩
     */
    private boolean enablePixelCompress = true;

    /**
     * 是否启用质量压缩
     * 若为true，则根据设置的quality进行压缩，否则根据设置的maxSize计算quality进行压缩
     * true的效率比较高，因为少了一步计算quality的过程
     */
    private boolean enableQualityCompress = true;

    /**
     * 是否保留源文件
     */
    private boolean enableReserveRaw = true;

    /**
     * 压缩后缓存图片目录，非文件路径
     */
    private String cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

    public static CompressConfig getDefaultConfig() {
        return new CompressConfig();
    }

    private CompressConfig() {
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isEnablePixelCompress() {
        return enablePixelCompress;
    }

    public void setEnablePixelCompress(boolean enablePixelCompress) {
        this.enablePixelCompress = enablePixelCompress;
    }

    public boolean isEnableQualityCompress() {
        return enableQualityCompress;
    }

    public void setEnableQualityCompress(boolean enableQualityCompress) {
        this.enableQualityCompress = enableQualityCompress;
    }

    public boolean isEnableReserveRaw() {
        return enableReserveRaw;
    }

    public void setEnableReserveRaw(boolean enableReserveRaw) {
        this.enableReserveRaw = enableReserveRaw;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private CompressConfig config;

        public Builder() {
            config = new CompressConfig();
        }

        public Builder setMaxWidth(int maxWidth) {
            config.setMaxWidth(maxWidth);
            return this;
        }

        public Builder setMaxHeight(int maxHeight){
            config.setMaxHeight(maxHeight);
            return this;
        }

        public Builder setQuality(int quality) {
            config.quality = quality;
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            config.setMaxSize(maxSize);
            return this;
        }

        public Builder setEnablePixelCompress(boolean enablePixelCompress) {
            config.setEnablePixelCompress(enablePixelCompress);
            return this;
        }

        public Builder setEnableQualityCompress(boolean enableQualityCompress) {
            config.setEnableQualityCompress(enableQualityCompress);
            return this;
        }

        public Builder setEnableReserveRaw(boolean enableReserveRaw) {
            config.setEnableReserveRaw(enableReserveRaw);
            return this;
        }

        public Builder setCacheDir(String cacheDir) {
            config.setCacheDir(cacheDir);
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat format) {
            config.setCompressFormat(format);
            return this;
        }

        public CompressConfig build() {
            return config;
        }

    }
}
