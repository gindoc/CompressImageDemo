# CompressImageDemo
图片压缩库，可以同时压缩多张图片

#### 基本用法

##### 可以通过配置CompressConfig来设置压缩的参数

__支持的参数：__

参数|值
-|-
enablePixelCompress		|	是否启用像素压缩
enableQualityCompress	|	是否启用质量压缩。若为true，则根据设置的quality进行压缩，否则根据设置的maxSize计算quality进行压缩。true的效率比较高，因为少了一步计算quality的过程
maxWidth，maxHeight		|	压缩的最大宽度、高度，默认1200，单位：像素
maxSize					|	压缩的最大文件大小，默认200 * 1024，单位：B
quality					|	压缩质量，默认值70
enableReserveRaw		|	是否保留源文件
cacheDir				|	压缩后缓存图片目录，非文件路径

__使用方式：__

1、通过`CompressConfig.getDefaultConfig()`获取默认配置

2、通过构造器自定义配置：
```
CompressConfig.Builder()
            .setEnablePixelCompress(true)
            .setEnableQualityCompress(true)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setEnableReserveRaw(true)
            .setMaxWidth(480)
            .setMaxHeight(800)
            .setMaxSize(300*1024)
            .setQuality(75)
            .setCacheDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)
            .build()
```


#### 压缩方式：

```
val photos = arrayListOf<Photo>()
photos.add(Photo(path))
CompressImageManager.build(this, compressConfig, photos, object :CompressImage.CompressListener{
    override fun onCompressSuccess(images: java.util.ArrayList<Photo>?) {
        Log.e("gindoc>>>>>>", "压缩成功")
    }

    override fun onCompressFailed(images: java.util.ArrayList<Photo>?, vararg error: String?) {
		Log.e("gindoc>>>>>>", "压缩失败")
    }

}).compress()
```

#### 参考：[Compressor](https://github.com/zetbaitsu/Compressor)

