package me.gindoc.compressor.core;

import android.graphics.*;
import android.media.ExifInterface;
import me.gindoc.compressor.config.CompressConfig;
import me.gindoc.compressor.listener.CompressResultListener;

import java.io.*;

/**
 * Author: GIndoc on 2019/7/26.
 * FOR   :
 */
public class CompressImageUtil {

    private CompressConfig config;

    public CompressImageUtil(CompressConfig config) {
        this.config = config == null ? CompressConfig.getDefaultConfig() : config;
    }

    public void compress(String imgPath, CompressResultListener listener) {
        FileOutputStream fileOutputStream = null;
        File parent = new File(config.getCacheDir());
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try {
            File file = new File(config.getCacheDir(), System.currentTimeMillis() + "_compressed.jpg");
            fileOutputStream = new FileOutputStream(file);

            // write the compressed bitmap at the destination specified by destinationPath.
            Bitmap bitmap;
            if (config.isEnablePixelCompress()) {
                bitmap = decodeSampledBitmapFromFile(new File(imgPath), config.getMaxWidth(), config.getMaxHeight());
            } else {
                bitmap = BitmapFactory.decodeFile(imgPath);
            }

            int quality;
            if (config.isEnableQualityCompress()) {
                quality = config.getQuality();
            } else {
                quality = calculateQualityByMaxSize(bitmap, config.getMaxSize());
            }

            bitmap.compress(config.getCompressFormat(), quality, fileOutputStream);

            listener.onCompressSuccess(file.getAbsolutePath());

        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
            listener.onCompressFailed(imgPath, e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onCompressFailed(imgPath, e.getMessage());
                }
            }
        }
    }

    /**
     * 参考 https://github.com/zetbaitsu/Compressor/blob/master/compressor/src/main/java/id/zelory/compressor/ImageUtil.java
     *
     * @param imageFile 源文件
     * @param reqWidth  期望的最大宽度
     * @param reqHeight 期望的最大高度
     * @return 压缩后的bitmap
     */
    private Bitmap decodeSampledBitmapFromFile(File imageFile, float reqWidth, float reqHeight) throws IOException, OutOfMemoryError {
        // First decode with inJustDecodeBounds=true to check dimensions

        Bitmap scaledBitmap, bmp;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = reqWidth / reqHeight;

        if (actualHeight > reqHeight || actualWidth > reqWidth) {
            //If Height is greater
            if (imgRatio < maxRatio) {
                imgRatio = reqHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) reqHeight;

            }  //If Width is greater
            else if (imgRatio > maxRatio) {
                imgRatio = reqWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) reqWidth;
            } else {
                actualHeight = (int) reqHeight;
                actualWidth = (int) reqWidth;
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        bmp.recycle();

        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        } else if (orientation == 3) {
            matrix.postRotate(180);
        } else if (orientation == 8) {
            matrix.postRotate(270);
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
                scaledBitmap.getHeight(), matrix, true);

        return scaledBitmap;


    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            inSampleSize *= 2;
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private int calculateQualityByMaxSize(Bitmap bitmap, int maxSize) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);         // 质量压缩法，这里的100表示不压缩，把压缩后的数据存储到baos中
        while (baos.toByteArray().length > maxSize) {                // 循环判断压缩后图片是否大于maxSize，如果大于继续压缩
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        baos.close();
        return options;
    }
}
