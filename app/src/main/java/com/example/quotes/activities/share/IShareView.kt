package com.example.quotes.activities.share

import android.graphics.Bitmap
import com.example.quotes.base.IMVPView

interface IShareView:IMVPView {
    fun displayBitmapShare(bitmap: Bitmap)
    fun finishA()
}