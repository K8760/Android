package com.example.k8684.combined
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

open class ImageListAdapter(context: Context, private val imageUrls: Array<String>) :
    ArrayAdapter<Any>(context, R.layout.adapter_layout, imageUrls) {

    private val inflater: LayoutInflater

    init {

        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

            var holder : ViewHolder
            var retView : View?
            retView = convertView
            if (null == convertView) {

                retView = inflater.inflate(R.layout.adapter_layout, parent, false)
                //holder = ViewHolder()



                    //retView.tag = holder
            }
                Picasso
                    .get()
                    .load(imageUrls[position])
                    .fit() // will explain later
                    .into(retView as ImageView)
            /*else {
                    //holder = convertView.tag as ViewHolder
                    retView = convertView
            }*/

            return retView
    }
    open class ViewHolder {
        var image: ImageView? = null
    }
}
//convertView = inflater.inflate(R.layout.adapter_layout, parent, false)
