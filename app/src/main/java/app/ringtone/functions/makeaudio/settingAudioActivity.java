package app.ringtone.functions.makeaudio;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.RadioButton;

import java.io.File;
import java.io.IOException;

import app.pacoke.aplicacionringtone.R;
import app.ringtone.FunctionsSystem;
import factories.IntentFactory;
import settingsApp.SharedSettings;

public class settingAudioActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private ImageView imageplaystop;
    private ImageView imagerecord;
    private RadioButton buttonrecord;
    private Button button_audio_send;
    private CheckBox send_checkbox;
    private static Boolean stoped = false;
    private static Boolean recording = true;
    private static Boolean send=true;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private ProgressView progressbar;
    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    private float mProgressStatus = 0;
    private Thread audio_bar_thread;
    private Boolean finish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_audio);
        imageplaystop = (ImageView) findViewById(R.id.audio_play_stop);
        //imagerecord = (ImageView) findViewById(R.id.audio_record);
        buttonrecord = (RadioButton) findViewById(R.id.record_press);
        button_audio_send = (Button) findViewById(R.id.audio_send_file);
        send_checkbox = (CheckBox) findViewById(R.id.audio_checkbox_send);
        progressbar = (ProgressView) findViewById(R.id.audio_progressbar);
    }

    public void onClickImageAudio(View view) {
        if (!stoped) {
            imageplaystop.setImageResource(R.drawable.stop_audio);
        }else{
            imageplaystop.setImageResource(R.drawable.play_audio);
        }
        progressbar = (ProgressView) findViewById(R.id.audio_progressbar);
        // Start lengthy operation in a background thread
        /*
        if(stoped){
            mProgressStatus = 0;
            progressbar.setProgress(mProgressStatus);
        }else{
            audio_bar_thread = new Thread(new Runnable() {
                public void run() {
                    float i = 0.0f;
                    while (mProgressStatus < 100.0f) {
                        // Update the progress bar
                        View mHandler = findViewById(R.id.audio_activity_view);
                        Runnable run = new Runnable() {
                            public void run() {
                                progressbar.setProgress(mProgressStatus);
                            }
                        };
                        mHandler.post(run);
                        mProgressStatus =i + 1.0f;
                        try{
                            this.wait(1000);
                        }catch (Exception e){}
                    }
                }
            });
            audio_bar_thread.start();
        }*/
        onPlay(stoped);
        stoped = !stoped;
    }

    public void onClickAudioRecord(View view) {
        if(recording){
            buttonrecord.setChecked(true);
        }else{
            buttonrecord.setChecked(false);
        }
        onRecord(recording);
        recording=!recording;
    }

    public void onClickAudioSend(View view){
        if(send){
            if(mRecorder==null && new File(mFileName).exists())
            {
                sendFile ss = new sendFile(mFileName,getApplicationContext());
                ss.execute();
            }
            send_checkbox.setChecked(true);
        }else{
            send_checkbox.setChecked(false);
        }
        send = !send;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            try {
                mPlayer.setDataSource(mFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepare();
            mPlayer.start();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public settingAudioActivity() {
        mFileName = SharedSettings.FolderInternal;
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "audiorecordtest.aac";
        Log.d(LOG_TAG,mFileName);
    }
/*
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
    }
*/
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = IntentFactory.getFactory(FunctionsSystem.class.getName());
        startActivity(intent);
    }
}
