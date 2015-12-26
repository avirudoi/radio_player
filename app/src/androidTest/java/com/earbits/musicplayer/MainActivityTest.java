package com.earbits.musicplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.earbits.musicplayer.activity.MainActivity;
import com.earbits.musicplayer.listener.ApiCallListener;
import com.earbits.musicplayer.model.SongObject;
import com.earbits.musicplayer.network.FetchData;
import com.squareup.picasso.Picasso;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.HashMap;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    ImageButton playButton, pauseButton;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        playButton = (ImageButton)activity.findViewById(R.id.bPlay);
        pauseButton = (ImageButton)activity.findViewById(R.id.bPaused);
    }

    @SmallTest
    public void testProgressBarNotNull(){
        ProgressBar progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        assertNotNull(progressBar);
    }

    @UiThreadTest
    public void testPlayButton(){
        playButton.performClick();
        assertTrue(playButton.getVisibility() == View.GONE);
        assertTrue(pauseButton.getVisibility() == View.VISIBLE);
    }

    @UiThreadTest
    public void testPausedButton(){
        pauseButton.performClick();
        assertTrue(playButton.getVisibility() == View.VISIBLE);
        assertTrue(pauseButton.getVisibility() == View.GONE);

    }
}
