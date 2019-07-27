package me.gindoc.compressimagedemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import me.gindoc.compressimagedemo.utils.CachePathUtils
import me.gindoc.compressimagedemo.utils.CommonUtils
import me.gindoc.compressimagedemo.utils.CommonUtils.getCameraIntent
import me.gindoc.compressimagedemo.utils.CommonUtils.hasCamera
import me.gindoc.compressimagedemo.utils.Constants
import me.gindoc.compressor.CompressImageManager
import me.gindoc.compressor.bean.Photo
import me.gindoc.compressor.config.CompressConfig
import me.gindoc.compressor.listener.CompressImage
import me.gindoc.compressor.utils.UriParseUtils

class MainActivity : AppCompatActivity(), CompressImage.CompressListener {

    private val REQUEST_CAMERA = 0X1000
    private val REQUEST_ALBUM = 0X1001

    private var cameraCachePath: String? = null                         // 拍照后，源文件路径

    private var compressConfig: CompressConfig? = null                  // 压缩配置

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compressConfig = CompressConfig.getDefaultConfig()

        compressConfig?.isEnableQualityCompress =false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            Constants.CAMERA_CODE -> {
                cameraCachePath?.let { preCompress(it) }
            }
            Constants.ALBUM_CODE -> {
                if (data != null) {
                    val uri = data.data
                    val path = UriParseUtils.getPath(this, uri)
                    preCompress(path)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeCamera()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        AlertDialog.Builder(this)
                            .setMessage("请前往设置页面开启摄像头权限")
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setMessage("拍照需要摄像头权限")
                            .setPositiveButton("确定") { dialog, which ->
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.CAMERA),
                                    REQUEST_CAMERA
                                )
                            }
                            .show()
                    }
                }
            }

            REQUEST_ALBUM -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeAlbum()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        AlertDialog.Builder(this)
                            .setMessage("请前往设置页面开启读取存储卡权限")
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setMessage("选取图片需要读取存储卡权限")
                            .setPositiveButton("确定") { dialog, which ->
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    REQUEST_ALBUM
                                )
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun preCompress(path: String) {
        val photos = arrayListOf<Photo>()
        photos.add(Photo(path))
        if (photos.isNotEmpty()) compress(photos)

    }

    private fun compress(photos: ArrayList<Photo>) {
        CompressImageManager.build(this, compressConfig, photos, this).compress()
    }

    override fun onCompressSuccess(images: java.util.ArrayList<Photo>?) {
        Log.e("gindoc>>>>>>", "压缩成功")
        Toast.makeText(this, "压缩结束", Toast.LENGTH_SHORT).show()
    }

    override fun onCompressFailed(images: java.util.ArrayList<Photo>?, vararg error: String?) {
        Log.e("gindoc>>>>>>", "压缩失败")
        Toast.makeText(this, "压缩结束", Toast.LENGTH_SHORT).show()

    }

    fun takeFromCamera(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setMessage("请前往设置页面开启摄像头权限")
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
            }
        } else {
            takeCamera()
        }

    }

    private fun takeCamera() {
        cameraCachePath = CommonUtils.startCameraPage(this)
    }

    fun takeFromAlbum(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(this)
                    .setMessage("请前往设置页面开启读取存储卡权限")
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_ALBUM)
            }
        } else {
            takeAlbum()
        }
    }

    private fun takeAlbum() {
        CommonUtils.startAlbumPage(this)
    }


}
