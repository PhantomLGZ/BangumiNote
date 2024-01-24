package com.phantom.banguminote

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CutOffLogo : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap =
        if (toTransform.width >= toTransform.height) {
            Bitmap.createBitmap(
                toTransform,
                (toTransform.width - toTransform.height) / 2,
                0,
                toTransform.height,
                toTransform.height
            )
        } else {
            Bitmap.createBitmap(
                toTransform,
                0,
                0,
                toTransform.width,
                toTransform.width
            )
        }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}