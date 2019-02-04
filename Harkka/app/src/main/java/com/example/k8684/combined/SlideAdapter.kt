package com.example.k8684.combined

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

public class SlideAdapter(private val context: Context, private val imageModelArrayList: ArrayList<String>, private var index: Int) : PagerAdapter() {
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        var count = 0
        if (index != 0) {
            count = index
            index = 0
        }
        else {
            count = position
        }
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!
        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView
        Glide.with(context)
            .asBitmap()
            .load(imageModelArrayList[count])
            .apply(RequestOptions().override(imageView.width, imageView.height).fitCenter())
            .into(imageView)

        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}