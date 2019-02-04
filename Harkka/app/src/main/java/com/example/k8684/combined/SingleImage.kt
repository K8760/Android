package com.example.k8684.combined

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.viewpagerindicator.CirclePageIndicator
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build



class SingleImage: Activity() {
    lateinit var saveBtn : Button
    private var index = 0
    private var myImageList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_layout)
        saveBtn = findViewById<Button>(R.id.download)
        val bundle: Bundle? = intent.extras
        if (bundle != null){
            index = intent.getIntExtra("index", 0)

            myImageList = intent.getStringArrayListExtra("list")
        }
        init()


        saveBtn.setOnClickListener()
        {
            if (isWriteStoragePermissionGranted() == false) {
                println("boo, false")
            }
            SaveImage(this).execute(myImageList[currentPage])
            var toast: Toast = Toast(this)
            toast.createToast(this, "Image is donwloaded!")
        }

    }

    private fun init() {
        mPager = findViewById(R.id.pager) as ViewPager
        mPager!!.adapter = SlideAdapter(this@SingleImage, myImageList!!, index)

        val indicator = findViewById(R.id.indicator) as CirclePageIndicator
        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density
        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = myImageList!!.size

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(pos: Int) {
            }
        })

    }

    fun Toast.createToast(context: Context, message:String){
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        /*first parameter is the layout you made
        second parameter is the root view in that xml
         */
        val layout = inflater.inflate(R.layout.toast_layout, (context as Activity).findViewById<ViewGroup>(R.id.custom_toast_container))

        layout.findViewById<TextView>(R.id.text).text = message
        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        setDuration(Toast.LENGTH_SHORT);
        setView(layout);
        show()
    }

    fun isWriteStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                println("Permission is granted2")
                return true
            } else {

                println("Permission is revoked2")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            println("Permission is granted2")
            return true
        }
    }

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

}