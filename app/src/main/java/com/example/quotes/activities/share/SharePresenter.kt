package com.example.quotes.activities.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.quotes.Model.Post
import com.example.quotes.utils.Utils
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class SharePresenter(val view: IShareView) {
    fun createBitmap(p: Post) {
        Utils.createBitmap(view as Context, p) {
            if (it != null) {
                view.displayBitmapShare(it)
            } else {
                view.showError("Có lỗi khi tải ảnh")
            }
        }

    }
    fun shareBitmap(bm: Bitmap?) {
        if (bm == null) {
            view.showError("Không thể tải ảnh")
            return
        }
        val context = view as Context
        if (Utils.checkPermissions(context)) {
            val intent = Intent()
            intent.setAction(Intent.ACTION_SEND)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, getImageUriFromBitmap(context, bm))
            context.startActivity(intent)
        } else {
            view.showError("vui lòng cấp quyền truy cập bộ nhớ!")
        }
    }

    fun saveBitmap(bitmap: Bitmap?) {
        if (bitmap == null) {
            view.showError("Không thể tải ảnh")
            return
        }
        val fileName = System.currentTimeMillis().toString()
        val context = view as Context
        if (Utils.checkPermissions(context)) {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                fileName,
                null
            )
            view.showError("Lưu ảnh thành công!")
            view.finishA()
        } else {
            view.showError("vui lòng cấp quyền truy cập bộ nhớ!")
        }
    }

    private fun getImageUriFromBitmap(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}