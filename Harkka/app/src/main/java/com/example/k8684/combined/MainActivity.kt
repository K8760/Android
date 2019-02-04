package com.example.k8684.combined

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var parsedArray : ArrayList<String> = arrayListOf()


    lateinit var getBtn : Button
    var url : String? = null

    var document : org.jsoup.nodes.Document? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getBtn = findViewById<Button>(R.id.loadImage)
        var urlFromField = findViewById<EditText>(R.id.webSiteField)


        getBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                url = urlFromField.text.toString()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(webSiteField.getWindowToken(), 0)
                ParseImages().execute()
                Toast.makeText(this@MainActivity, url, Toast.LENGTH_SHORT).show()
                TimeUnit.SECONDS.sleep(3L)
                val parsedFromListArray = ArrListToarray(parsedArray) as Array<String>
                usage_example_gridview.adapter = ImageListAdapter(this@MainActivity, parsedFromListArray)

                usage_example_gridview.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        println(position)
                        val intent = Intent(this@MainActivity, SingleImage::class.java)
                        intent.putExtra("index", position)
                        intent.putExtra("list", parsedArray)
                        startActivity(intent)
                    }
                }
            }
        })
    }

    // Parsing ArrayList containing images to basic Array

    fun ArrListToarray(myParsedArray: ArrayList<String>): Array<String>{
        val myArray = arrayOfNulls<String>(myParsedArray.size)
        myParsedArray.toArray(myArray)
        val myArrayToReturn = myArray as Array<String>
        return myArrayToReturn
    }

    //Image parser, uses JSOUP library, to get all images what cat be found on page,
    //Stores them in ArrayList
    inner class ParseImages() : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void): Void? {
            try {
                document = Jsoup.connect(url).get() as org.jsoup.nodes.Document
                var links = document?.select("img[src]") as Elements
                for (url in links) {
                    System.out.println("* " + url.attr("abs:src"))
                    parsedArray.add(url.attr("abs:src"))
                }

            }
            catch(e: IOException)  {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }

    }

}
