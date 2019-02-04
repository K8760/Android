package fi.jamk.k8760.imagelibaries;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        ImageView img1 = (ImageView) findViewById(R.id.imageView1);
        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        Picasso.with(context).load(imageUri).resize(300, 300).centerCrop().into(img1);
        Picasso.with(context).load(R.drawable.cat).resize(300, 300).centerCrop().into(img2);
    }
}
