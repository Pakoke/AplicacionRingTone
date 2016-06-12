package app.ringtone.functions.makeaudio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.RadioButton;
import com.yqritc.recyclerviewmultipleviewtypesadapter.EnumListBindAdapter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import app.pacoke.aplicacionringtone.R;
import app.ringtone.FunctionsSystem;
import app.ringtone.functions.makeaudio.soundfile.SoundFile;
import binders.Sample2Binder;
import binders.SampleData;
import binders.SampleFileBinder;
import dtos.SampleFileData;
import factories.IntentFactory;
import settingsApp.SharedSettings;
import utilsApp.SampleEnumListAdapter;

public class settingAudioActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private ImageView imageplaystop;
    private ImageView imagerecord;
    private RadioButton buttonrecord;
    private Button button_audio_send;
    private CheckBox send_checkbox;
    private CheckBox ring_checkbox;
    private static Boolean stoped = false;
    private static Boolean recording = true;
    private static Boolean send=true;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private Handler recordingHandler;
    private ProgressView progressbar;
    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    private float mProgressStatus = 0;
    private Thread audio_bar_thread;
    private Boolean finish = false;
    public BottomSheetDialog mDialog;
    SampleEnumListAdapter adapter;
    private ProgressDialog mProgressDialog;
    private Thread mSaveSoundFileThread;
    private SoundFile mSoundFile;
    private Handler mHandler;
    private String mInfoContent;
    private CountDownTimer t;
    private long timeCounter;
    private Thread mLoadSoundFileThread;
    private List<File> filestosend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_audio);
        buttonrecord = (RadioButton) findViewById(R.id.record_press);
        button_audio_send = (Button) findViewById(R.id.audio_send_file);
        send_checkbox = (CheckBox) findViewById(R.id.audio_checkbox_send);
        ring_checkbox = (CheckBox) findViewById(R.id.audio_checkbox_ring);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_audio_list);
        adapter = new SampleEnumListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        File directory = new File(SharedSettings.FolderInternal);
        File[] filestoshow = null;
        filestosend=new ArrayList<>();
        mHandler = new Handler();
        filestoshow = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().matches(".*((.mp3)|(.ogg)|(.aac)|(.3gp)|(.wav)|(.amr))");
            }
        });
        Arrays.sort(filestoshow, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    return -1;
                }
                if (lhs.lastModified() < rhs.lastModified()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        List<SampleFileData> sampleData = getSampleData(filestoshow);
        adapter.setSample2Data(sampleData);

    }
    private static class SampleEnumListAdapter extends EnumListBindAdapter<SampleEnumListAdapter.SampleViewType> {

        enum SampleViewType {
            //SAMPLE1,
            SAMPLE2
            //,SAMPLE3
        }

        public SampleEnumListAdapter() {
            addAllBinder(//new Sample1Binder(this),
                    new SampleFileBinder(this)
                    //,new Sample3Binder(this)
            );
        }

        public void setSample2Data(List<SampleFileData> dataSet) {
            ((SampleFileBinder) getDataBinder(SampleViewType.SAMPLE2)).addAll(dataSet);
        }
        public void addSampleData(SampleFileData data){
            ((SampleFileBinder) getDataBinder(SampleViewType.SAMPLE2)).add(data);
        }
    }


    private List<SampleFileData> getSampleData(File[] filestoshow) {


        List<SampleFileData> dataSet = new ArrayList<>();

        mDialog = new BottomSheetDialog(this);

        for(int i=0;i<filestoshow.length;i++) {
            Log.i(this.getClass().getName(),filestoshow[i].toString());
            SampleFileData data = new SampleFileData();
            data.mTitle = filestoshow[i].getName();
            data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_audio_list), "drawable", getPackageName());
            data.mListener = new ListenerClickOnSoundFile(this.findViewById(R.id.grid_layout_music), filestoshow[i]);
            data.mLongListener = new ListenerLongClickOnSoundFile(this.findViewById(R.id.grid_layout_music),filestoshow[i]);
            data.fileData = filestoshow[i];
            dataSet.add(data);
        }
        return dataSet;
    }

    private class ListenerLongClickOnSoundFile implements View.OnLongClickListener{

        View view;
        File fileToClick;
        Boolean check;
        public ListenerLongClickOnSoundFile(View view,File fichero){
            this.view=view;
            fileToClick = fichero;
            check = false;
        }
        @Override
        public boolean onLongClick(View v) {
            filestosend.add(fileToClick);
            if(check){
                v.setBackgroundColor(Color.WHITE);
                check = false;
            }else{
                v.setBackgroundColor(Color.BLUE);
                mFileName = fileToClick.getAbsolutePath();
                check = true;
            }
            return true;
        }
    }


    private class ListenerClickOnSoundFile implements View.OnClickListener{

        View view;
        File fileToClick;
        public ListenerClickOnSoundFile(View view,File fichero){this.view=view;fileToClick = fichero;}

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileToClick.getAbsolutePath()));
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(fileToClick.getAbsolutePath());
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            intent.setDataAndType(Uri.parse(fileToClick.getAbsolutePath()), type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                Toast.makeText(v.getContext(), "Se ha pulsado el fichero " + fileToClick.getName(), Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }else{
                Toast.makeText(v.getContext(), "No esta instalada la aplicacion necesaria para reproducir el archivo " + fileToClick.getName(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void onClickImageAudio(View view) {
        if (!stoped) {
            imageplaystop.setImageResource(R.drawable.stop_audio);
        }else{
            imageplaystop.setImageResource(R.drawable.play_audio);
        }
        //progressbar = (ProgressView) findViewById(R.id.audio_progressbar);
        // Start lengthy operation in a background thread
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
//        if(finished){

            if(mRecorder==null && new File(mFileName).exists())
            {
                try{
                    //abslute path of mFileName
                    sendFile ss = new sendFile(mFileName,getApplicationContext());
                    ss.setSpeaker(!ring_checkbox.isChecked());
                    ss.sendCheckBox = this.send_checkbox;
                    ss.sendCheckBox.setChecked(false);
                    Log.i(LOG_TAG, Boolean.toString(ring_checkbox.isChecked()));
                    ss.execute();
                    //send_checkbox.setChecked(true);
                }catch (Exception e){
                    Log.i(LOG_TAG,e.getMessage());
                }
            }else{

            }
//        }else{
//            send_checkbox.setChecked(false);
//        }
//        send = !send;
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



//        Start recording with a sample rate of 44KHz, 2 channels (stereo), at 0.2 quality


        mFileName = SharedSettings.FolderInternal;
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        Calendar c = Calendar.getInstance();
        mFileName += "audiorecordtest"+c.get(Calendar.HOUR)+"_"+c.get(Calendar.MINUTE)+"_"+c.get(Calendar.SECOND)+".wav";
        Log.d(LOG_TAG,mFileName);

        mRecorder = new MediaRecorder();
        File fi = new File(mFileName);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(2);
        mRecorder.setAudioSamplingRate(64);

        Log.i(LOG_TAG, "Grabando " + mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();



        t = new CountDownTimer( Long.MAX_VALUE , 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int cnt=0;
                cnt++;
                String time = new Integer(cnt).toString();
                timeCounter = cnt;
                long millis = cnt;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

//                txtcount.setText(String.format("%d:%02d:%02d", minutes, seconds,millis));

            }

            @Override
            public void onFinish() {            }
        };

        t.start();
    }

    private void stopRecording() {

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        t.cancel();

        loadFromFile();
        saveRingtone(mFileName);

        //Insert in the list of musics
        SampleFileData data = new SampleFileData();
        File tempfile = new File(mFileName);
        data.mTitle = tempfile.getName();
        data.mDrawableResId = getResources().getIdentifier(getString(R.string.drawable_audio_list), "drawable", getPackageName());
        data.mListener = new ListenerClickOnSoundFile(this.findViewById(R.id.grid_layout_music), tempfile);
        data.mLongListener = new ListenerLongClickOnSoundFile(this.findViewById(R.id.grid_layout_music),tempfile);
        data.fileData = tempfile;
        adapter.addSampleData(data);

        Log.i(LOG_TAG, "Grabado " + mFileName);
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

    }
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

    private void loadFromFile() {
        File mFile = new File(mFileName);
        final SoundFile.ProgressListener listener =
                new SoundFile.ProgressListener() {
                    public boolean reportProgress(double elapsedTime) {
//                        long now = getCurrentTime();
//                        if (now - mRecordingLastUpdateTime > 5) {
//                            mRecordingTime = elapsedTime;
//                            // Only UI thread can update Views such as TextViews.
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    int min = (int)(mRecordingTime/60);
//                                    float sec = (float)(mRecordingTime - 60 * min);
//                                    mTimerTextView.setText(String.format("%d:%05.2f", min, sec));
//                                }
//                            });
//                            mRecordingLastUpdateTime = now;
//                        }
                        return true;
                    }
                };

        try {
            mSoundFile = SoundFile.create(mFile.getAbsolutePath(),listener);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SoundFile.InvalidInputException e) {
            e.printStackTrace();
        }
    }

    private void saveRingtone(final CharSequence title) {
//        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
//        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
//        final int startFrame = mWaveformView.secondsToFrames(startTime);
//        final int endFrame = mWaveformView.secondsToFrames(endTime);

        final int duration = (int)(timeCounter + 0.5);

        // Create an indeterminate progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(R.string.progress_dialog_saving);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Save the sound file in a background thread
        mSaveSoundFileThread = new Thread() {
            public void run() {
                // Try AAC first.
                String outPath = makeRingtoneFilename(title, ".m4a");
                if (outPath == null) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            showFinalAlert(new Exception(), R.string.no_unique_filename);
                        }
                    };
                    mHandler.post(runnable);
                    return;
                }
                File outFile = new File(outPath);
                Boolean fallbackToWAV = false;
                try {
                    // Write the new file
                    mSoundFile.WriteFile(outFile,  0, timeCounter);
                } catch (Exception e) {
                    // log the error and try to create a .wav file instead
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Log.e("Ringdroid", "Error: Failed to create " + outPath);
                    Log.e("Ringdroid", writer.toString());
                    fallbackToWAV = true;
                }

                // Try to create a .wav file if creating a .m4a file failed.
                if (fallbackToWAV) {
                    outPath = makeRingtoneFilename(title, ".wav");
                    if (outPath == null) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                showFinalAlert(new Exception(), R.string.no_unique_filename);
                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                    outFile = new File(outPath);
                    try {
                        // create the .wav file
                        mSoundFile.WriteWAVFile(outFile, 0, timeCounter);
                    } catch (Exception e) {
                        // Creating the .wav file also failed. Stop the progress dialog, show an
                        // error message and exit.
                        mProgressDialog.dismiss();
                        if (outFile.exists()) {
                            outFile.delete();
                        }
//                        mInfoContent = e.toString();
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                mInfo.setText(mInfoContent);
//                            }
//                        });

                        CharSequence errorMessage;
                        if (e.getMessage() != null
                                && e.getMessage().equals("No space left on device")) {
                            errorMessage = getResources().getText(R.string.no_space_error);
                            e = null;
                        } else {
                            errorMessage = getResources().getText(R.string.write_error);
                        }
                        final CharSequence finalErrorMessage = errorMessage;
                        final Exception finalException = e;
                        Runnable runnable = new Runnable() {
                            public void run() {
                                showFinalAlert(finalException, finalErrorMessage);
                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                }

                // Try to load the new file to make sure it worked
                try {
                    final SoundFile.ProgressListener listener =
                            new SoundFile.ProgressListener() {
                                public boolean reportProgress(double frac) {
                                    // Do nothing - we're not going to try to
                                    // estimate when reloading a saved sound
                                    // since it's usually fast, but hard to
                                    // estimate anyway.
                                    return true;  // Keep going
                                }
                            };
                    SoundFile.create(outPath, listener);
                } catch (final Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    mInfoContent = e.toString();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            mInfo.setText(mInfoContent);
//                        }
//                    });

//                    Runnable runnable = new Runnable() {
//                        public void run() {
//                            showFinalAlert(e, getResources().getText(R.string.write_error));
//                        }
//                    };
//                    mHandler.post(runnable);
                    return;
                }

                mProgressDialog.dismiss();

                final String finalOutPath = outPath;
                Runnable runnable = new Runnable() {
                    public void run() {
                        afterSavingRingtone(title,
                                finalOutPath,
                                duration);
                    }
                };
                mHandler.post(runnable);
            }
        };
        mSaveSoundFileThread.start();
    }

    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private void showFinalAlert(Exception e, CharSequence message) {
        CharSequence title;
        if (e != null) {
            Log.e("Ringdroid", "Error: " + message);
            Log.e("Ringdroid", getStackTrace(e));
            title = getResources().getText(R.string.alert_title_failure);
            setResult(RESULT_CANCELED, new Intent());
        } else {
            Log.v("Ringdroid", "Success: " + message);
            title = getResources().getText(R.string.alert_title_success);
        }

        new AlertDialog.Builder(settingAudioActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    private void showFinalAlert(Exception e, int messageResourceId) {
        showFinalAlert(e, getResources().getText(messageResourceId));
    }

    private void afterSavingRingtone(CharSequence title,
                                     String outPath,
                                     int duration) {
        File outFile = new File(outPath);
        long fileSize = outFile.length();
        if (fileSize <= 512) {
            outFile.delete();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_title_failure)
                    .setMessage(R.string.too_small_error)
                    .setPositiveButton(R.string.alert_ok_button, null)
                    .setCancelable(false)
                    .show();
            return;
        }

        // Create the database record, pointing to the existing file path
        String mimeType;
        if (outPath.endsWith(".m4a")) {
            mimeType = "audio/mp4a-latm";
        } else if (outPath.endsWith(".wav")) {
            mimeType = "audio/wav";
        } else {
            // This should never happen.
            mimeType = "audio/mpeg";
        }

        String artist = "" + getResources().getText(R.string.artist_name);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, outPath);
        values.put(MediaStore.MediaColumns.TITLE, title.toString());
        values.put(MediaStore.MediaColumns.SIZE, fileSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

        values.put(MediaStore.Audio.Media.ARTIST, artist);
        values.put(MediaStore.Audio.Media.DURATION, duration);


        values.put(MediaStore.Audio.Media.IS_MUSIC,0);

        // Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
        final Uri newUri = getContentResolver().insert(uri, values);
        setResult(RESULT_OK, new Intent().setData(newUri));

        // If Ringdroid was launched to get content, just return
//        if (mWasGetContentIntent) {
//            finish();
//            return;
//        }

        // There's nothing more to do with music or an alarm.  Show a
        // success message and then quit.

            Toast.makeText(this,
                    R.string.save_success_message,
                    Toast.LENGTH_SHORT)
                    .show();
//            finish();
            return;


        // If it's a notification, give the user the option of making
        // this their default notification.  If they say no, we're finished.
//        if (mNewFileKind == FileSaveDialog.FILE_KIND_NOTIFICATION) {
//            new AlertDialog.Builder(RingdroidEditActivity.this)
//                    .setTitle(R.string.alert_title_success)
//                    .setMessage(R.string.set_default_notification)
//                    .setPositiveButton(R.string.alert_yes_button,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int whichButton) {
//                                    RingtoneManager.setActualDefaultRingtoneUri(
//                                            RingdroidEditActivity.this,
//                                            RingtoneManager.TYPE_NOTIFICATION,
//                                            newUri);
//                                    finish();
//                                }
//                            })
//                    .setNegativeButton(
//                            R.string.alert_no_button,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    finish();
//                                }
//                            })
//                    .setCancelable(false)
//                    .show();
//            return;
//        }

        // If we get here, that means the type is a ringtone.  There are
        // three choices: make this your default ringtone, assign it to a
        // contact, or do nothing.

//        final Handler handler = new Handler() {
//            public void handleMessage(Message response) {
//                int actionId = response.arg1;
//                switch (actionId) {
//                    case R.id.button_make_default:
//                        RingtoneManager.setActualDefaultRingtoneUri(
//                                RingdroidEditActivity.this,
//                                RingtoneManager.TYPE_RINGTONE,
//                                newUri);
//                        Toast.makeText(
//                                RingdroidEditActivity.this,
//                                R.string.default_ringtone_success_message,
//                                Toast.LENGTH_SHORT)
//                                .show();
//                        finish();
//                        break;
//                    case R.id.button_choose_contact:
//                        chooseContactForRingtone(newUri);
//                        break;
//                    default:
//                    case R.id.button_do_nothing:
//                        finish();
//                        break;
//                }
//            }
//        };
//        Message message = Message.obtain(handler);
//        AfterSaveActionDialog dlog = new AfterSaveActionDialog(
//                this, message);
//        dlog.show();
    }
    private String makeRingtoneFilename(CharSequence title, String extension) {
        String subdir;
//        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
//        if (!externalRootDir.endsWith("/")) {
//            externalRootDir += "/";
//        }
//
//        switch(mNewFileKind) {
//            default:
//            case FileSaveDialog.FILE_KIND_MUSIC:
//                // TODO(nfaralli): can directly use Environment.getExternalStoragePublicDirectory(
//                // Environment.DIRECTORY_MUSIC).getPath() instead
//                subdir = "media/audio/music/";
//                break;
//            case FileSaveDialog.FILE_KIND_ALARM:
//                subdir = "media/audio/alarms/";
//                break;
//            case FileSaveDialog.FILE_KIND_NOTIFICATION:
//                subdir = "media/audio/notifications/";
//                break;
//            case FileSaveDialog.FILE_KIND_RINGTONE:
//                subdir = "media/audio/ringtones/";
//                break;
//        }
        String parentdir = SharedSettings.FolderInternal;

        // Create the parent directory
        File parentDirFile = new File(parentdir);
        parentDirFile.mkdirs();

        // If we can't write to that special path, try just writing
        // directly to the sdcard
//        if (!parentDirFile.isDirectory()) {
//            parentdir = externalRootDir;
//        }

        // Turn the title into a filename
        String filename = "";
        for (int i = 0; i < title.length(); i++) {
            if (Character.isLetterOrDigit(title.charAt(i))) {
                filename += title.charAt(i);
            }
        }

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0)
                testPath = parentdir + filename + i + extension;
            else
                testPath = parentdir + filename + extension;

            try {
                RandomAccessFile f = new RandomAccessFile(new File(testPath), "r");
                f.close();
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }

        return path;
    }


}
