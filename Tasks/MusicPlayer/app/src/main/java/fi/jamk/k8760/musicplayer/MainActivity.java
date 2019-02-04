package fi.jamk.k8760.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // list view
    private ListView listview;
    // path to mp3-files
    private String mediaPath;
    // List of Strings to hold mp3-filenames
    private List<String> songs = new ArrayList<String>();
    // Mediaplayer for playing music
    private MediaPlayer mediaPlayer = new MediaPlayer();
    // use AsyncTask to load filenames
    private LoadSongsTask task;
    // permission to read external storage
    private final int REQUEST_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find listview
        listview = (ListView) findViewById(R.id.listView);
        // define mediaPath to root of external memory
        mediaPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // item listener for listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // reset, prepare and start playing
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songs.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Cannot start audio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // check runtime permission to read external memory
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // ask permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            // Permission has already been granted, load MP3 files from external memory
            task = new LoadSongsTask();
            task.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // if request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, load MP3 files from external memory
                    task = new LoadSongsTask();
                    task.execute();
                } else {
                    Toast.makeText(getBaseContext(), "Read External Memory - permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // Use AsyncTask to read all mp3 file names
    private class LoadSongsTask extends AsyncTask<Void, String, Void> {
        private List<String> loadedSongs = new ArrayList<String>();

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading...",
                    Toast.LENGTH_LONG).show();
        }

        protected Void doInBackground(Void... url) {
            updateSongListRecursive(new File(mediaPath));
            return null;
        }

        public void updateSongListRecursive(File path) {
            if (path.isDirectory()) {
                for (int i = 0; i < path.listFiles().length; i++) {
                    File file = path.listFiles()[i];
                    updateSongListRecursive(file);
                }
            } else {
                String name = path.getAbsolutePath();
                publishProgress(name);
                if (name.endsWith(".mp3")) {
                    loadedSongs.add(name);
                }
            }
        }

        protected void onPostExecute(Void args) {
            ArrayAdapter<String> songList = new
                    ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, loadedSongs);
            listview.setAdapter(songList);
            songs = loadedSongs;

            Toast.makeText(getApplicationContext(), "Songs=" + songs.size(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }
}