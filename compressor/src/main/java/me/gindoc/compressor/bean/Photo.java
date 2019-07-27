package me.gindoc.compressor.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * Author: GIndoc on 2019/6/14.
 * FOR   :
 */
public class Photo implements Serializable {


    /**
     * 图片原始路径
     */
    private String originalPath;


    /**
     * 是否压缩过
     */
    private boolean isCompressed;


    /**
     * 压缩后的路径
     */
    private String compressPath;

    public Photo(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setCompressed(boolean compressed) {
        isCompressed = compressed;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return isCompressed == photo.isCompressed &&
                Objects.equals(originalPath, photo.originalPath) &&
                Objects.equals(compressPath, photo.compressPath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(originalPath, isCompressed, compressPath);
    }
}
