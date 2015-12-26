package com.earbits.musicplayer.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.earbits.musicplayer.listener.ApiCallListener;
import com.earbits.musicplayer.model.SongObject;
import com.earbits.musicplayer.network.FetchData;
import com.earbits.musicplayer.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ApiCallListener {

    private  DataCall task = new DataCall();
    SongObject mObject;
    Uri songFile;
    MediaPlayer mediaPlayer;
    int songNumber = -1;
    int songPLayed = 1;
    ImageButton bPaused, bPlay;
    boolean isPlaying =false;
    HashMap<Integer,SongObject> listSong;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bPaused = (ImageButton)findViewById(R.id.bPaused);
        bPlay = (ImageButton)findViewById(R.id.bPlay);
        bPlay.setVisibility(View.VISIBLE);
        bPaused.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        task.execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
     * Fetches data from the URL
     * */
    private void fetchData(boolean forceFetch){
        FetchData fetch = new FetchData();
        fetch.setApiCallListener(this);
        fetch.fetchSongData(MainActivity.this);

    }

    @Override
    public void onSuccess(SongObject object) {

        if(listSong == null){
            listSong = new HashMap<Integer,SongObject>();
        }
        mObject = object;
        listSong.put(++songNumber,mObject);
    }

    @Override
    public void onFailure(String messege) {
        messege = "failed to connect to the server";
        Toast.makeText(MainActivity.this,messege,Toast.LENGTH_SHORT).show();
    }
      /*
    * AsyncTask class to chnage song, from hashmap Array;
    * */
    private  class ChangeSong extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            setUpMediaPlayer();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //set up the UI
            setUpTheUI();
            progressBar.setVisibility(View.GONE);
            if(isPlaying == true){
                mediaPlayer.start();
            }
        }
    }
    /*
    * AsyncTask class to retrieve the data from the URL
    * */
    private class DataCall extends AsyncTask<String,Void,String>{
        private boolean forceFetch = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        public void setForceFetch(boolean forceFetch){
            this.forceFetch = forceFetch;
        }

        @Override
        protected String doInBackground(String... params) {
            fetchData(forceFetch);
            return "Executed";
        }
        /*
        * Set up view widgets when data is accessible
        * */
        @Override
        protected void onPostExecute(String result) {
              /* set up the Media Player that will parse
               The URI and play the song*/
            setUpMediaPlayer();

            //set up the UI
            setUpTheUI();

            progressBar.setVisibility(View.GONE);

            if(isPlaying ==true){
                mediaPlayer.start();
            }
        }
    }

    public void setUpTheUI(){

        ImageView ivCoverImage = (ImageView)findViewById(R.id.ivCoverImage);
        TextView tvTrackName = (TextView)findViewById(R.id.tvTrackName);
        TextView tvSingerName = (TextView)findViewById(R.id.tvSingerName);
        ImageButton bSkipforward = (ImageButton)findViewById(R.id.bSkipforward);
        ImageButton bSkipBackwards = (ImageButton)findViewById(R.id.bSkipBackwards);

        if(mObject.getSongName() !=null ){
            tvTrackName.setText(mObject.getSongName());
        }

        if(mObject.getArtistName() !=null ){
            tvSingerName.setText(mObject.getArtistName());
        }

        if(mObject.getCoverImage() !=null ){
            Picasso.with(MainActivity.this)
                    .load(mObject.getCoverImage())
                    .fit()
                    .tag(MainActivity.this)
                    .into(ivCoverImage);
        }

        //set Up The on Click Button that will controller the player
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                bPlay.setVisibility(View.GONE);
                bPaused.setVisibility(View.VISIBLE);
                isPlaying = true;

            }
        });

        bPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                bPlay.setVisibility(View.VISIBLE);
                bPaused.setVisibility(View.GONE);
                isPlaying=false;
            }
        });

        bSkipforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSong !=null && listSong.size()>songPLayed){
                    mediaPlayer.stop();
                    mObject = listSong.get(songNumber +1);
                    ++songNumber;
                    ++songPLayed;
                    new ChangeSong().execute();
                }else{
                    ++songPLayed;
                    mediaPlayer.stop();
                    skipforward();
                }
            }
        });

        bSkipBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSong !=null &&  listSong.size()>1 && songNumber !=0){
                    --songPLayed;
                    mediaPlayer.stop();
                    mObject = listSong.get(songNumber -1);
                    --songNumber;
                    new ChangeSong().execute();
                }else{
                    Toast.makeText(MainActivity.this,"No song is found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        if(mObject.getMediaFile() != null){
            try{
                songFile = Uri.parse(mObject.getMediaFile());
                mediaPlayer.setDataSource(MainActivity.this, songFile);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void skipforward() {
        if (task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
        if (null != task) {
            task = null;
            task = new DataCall();
            task.setForceFetch(true);
            task.execute("");
        }
    }
}
