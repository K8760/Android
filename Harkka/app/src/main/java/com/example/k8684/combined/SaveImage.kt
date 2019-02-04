package com.example.k8684.combined

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SaveImage(context: Context) : AsyncTask<String, Unit, Unit>() {
    private var mContext: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg params: String?) {
        val url = params[0]
        val requestOptions = RequestOptions().override(100)
            .downsample(DownsampleStrategy.CENTER_INSIDE)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        mContext.get()?.let {
            val bitmap = Glide.with(it)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .submit()
                .get()

            try {
                var file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                if (!file.exists()) {
                    file.mkdir()
                }
                val date = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                val stamp = date.format(formatter) + ".png"
                file = File(file, stamp)
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                println("boo, done")
            } catch (e: Exception) {
                println("boo, fail" + e)
            }
        }
    }
}