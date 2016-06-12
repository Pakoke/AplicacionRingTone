package app.ringtone.functions.videostreamer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import app.pacoke.aplicacionringtone.BuildConfig;
import app.pacoke.aplicacionringtone.R;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utilsApp.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FullscreenActivity extends Activity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 20000;
    private static final int HIDER_FLAGS = 6;
    public static String awb;
    static String defaultimagesize;
    public static String effect;
    public static String exposure;
    public static String imagesize;
    public static int imagewidth;
    public static String metering;
    public static int threeD;
    int angle;
    String command;
    String delaytime;
    long lastrunTime;
    Bitmap latestpic;
    Boolean livezoom;
    OnTouchListener mDelayHideTouchListener;
    Handler mHideHandler;
    Runnable mHideRunnable;
    private SystemUiHider mSystemUiHider;
    Boolean mjpeg;
    public OnTouchListener onTableTouched;
    long presstime;
    String raspiend;
    String raspioptions;
    String raspistill;
    long runTime;
    TimerTask scanTask;
    ImageView screenImage;
    public Session session;
    Timer f7t;
    Boolean usbcam;
    Boolean video;

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.1 */
    class C00591 implements OnCancelListener {
        C00591() {
        }

        public void onCancel(DialogInterface dialog) {
            try {
                FullscreenActivity.this.sshChannel(FullscreenActivity.this.session, "sudo pkill raspi*; sudo pkill vlc;");
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.2 */
    class C00612 implements Runnable {
        final /* synthetic */ ProgressDialog val$progressDialog;

        /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.2.1 */
        class C00601 implements OnClickListener {
            C00601() {
            }

            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case PagerAdapter.POSITION_NONE /*-2*/:
                        Intent i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("http://" + StreamerSetting.host + ":8080"), "video/h264");
                        FullscreenActivity.this.startActivity(i);
                    case ViewDragHelper.INVALID_POINTER /*-1*/:
                        FullscreenActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=org.videolan.vlc.betav7neon")));
                    default:
                }
            }
        }

        C00612(ProgressDialog progressDialog) {
            this.val$progressDialog = progressDialog;
        }

        public void run() {
            this.val$progressDialog.dismiss();
            String appid = "org.videolan.vlc.betav7neon";
            Intent i = new Intent("android.intent.action.VIEW");
            i.setDataAndType(Uri.parse("http://" + StreamerSetting.host + ":8080"), "video/*");
            try {
                FullscreenActivity.this.startActivityForResult(i, 1);
            } catch (Exception e) {
                OnClickListener dialogClickListener = new C00601();
                new Builder(FullscreenActivity.this).setMessage("VLC or other player required to stream h264\n\n install VLC from the play store ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.3 */
    class C00623 implements OnSeekBarChangeListener {
        final /* synthetic */ String val$adjust;
        final /* synthetic */ int val$min;

        C00623(int i, String str) {
            this.val$min = i;
            this.val$adjust = str;
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int value = progress + this.val$min;
            FullscreenActivity.this.raspioptions = " --" + this.val$adjust + " " + value;
            FullscreenActivity.this.command = FullscreenActivity.this.raspistill + FullscreenActivity.this.raspioptions + FullscreenActivity.this.raspiend;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.4 */
    class C00634 implements OnMenuItemClickListener {
        C00634() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            SharedPreferences settings = FullscreenActivity.this.getSharedPreferences("Raspicam", 0);
            String options = BuildConfig.VERSION_NAME;
            FullscreenActivity.effect = (String) item.getTitle();
            Toast.makeText(FullscreenActivity.this.getApplicationContext(), "Applying effect " + FullscreenActivity.effect, Toast.LENGTH_SHORT).show();
            if (FullscreenActivity.effect == "flip") {
                options = "--flip v";
            }
            if (FullscreenActivity.effect == "mirror") {
                options = "--flip h";
            }
            if (FullscreenActivity.effect == "negative") {
                options = "--invert";
            }
            if (FullscreenActivity.effect == "greyscale") {
                options = "--greyscale";
            }
            if (FullscreenActivity.effect == "timestamp") {
                options = "--no-banner";
            }
            if (FullscreenActivity.effect == "pi camera") {
                settings.edit().putBoolean("USBcam", false).commit();
                FullscreenActivity.effect = "none";
                FullscreenActivity.this.onResume();
            } else {
                FullscreenActivity.this.command = "fswebcam " + options + " -q -";
            }
            if (!FullscreenActivity.this.video.booleanValue()) {
                FullscreenActivity.this.updateimage(FullscreenActivity.this.findViewById(R.id.fullscreen_content));
            }
            return FullscreenActivity.AUTO_HIDE;
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.5 */
    class C00645 implements OnMenuItemClickListener {
        final /* synthetic */ View val$view;

        C00645(View view) {
            this.val$view = view;
        }

        public boolean onMenuItemClick(MenuItem item) {
            SharedPreferences settings = FullscreenActivity.this.getSharedPreferences("Raspicam", 0);
            FullscreenActivity.effect = (String) item.getTitle();
            if (FullscreenActivity.effect == "3D") {
                FullscreenActivity.threeD = 1 - FullscreenActivity.threeD;
            }
            if (FullscreenActivity.effect == "flip") {
                FullscreenActivity.this.raspioptions = FullscreenActivity.this.toggleoption(FullscreenActivity.this.raspioptions, " -vf ");
                FullscreenActivity.effect = "none";
            }
            if (FullscreenActivity.effect == "mirror") {
                FullscreenActivity.this.raspioptions = FullscreenActivity.this.toggleoption(FullscreenActivity.this.raspioptions, " -hf ");
                FullscreenActivity.effect = "none";
            }
            if (FullscreenActivity.effect == "zoom") {
                FullscreenActivity.this.raspioptions = FullscreenActivity.this.toggleoption(FullscreenActivity.this.raspioptions, "-roi 0.2,0.2,0.2,0.2");
                FullscreenActivity.effect = "none";
            }
            if (FullscreenActivity.effect == "rotate") {
                FullscreenActivity.this.angle += 90;
                FullscreenActivity.this.angle = (FullscreenActivity.this.angle / 90) * 90;
                if (FullscreenActivity.this.angle == 360) {
                    FullscreenActivity.this.angle = 0;
                }
                settings.edit().putInt("angle", FullscreenActivity.this.angle).commit();
                FullscreenActivity.effect = "none";
            }
            if (FullscreenActivity.effect == "brightness") {
                FullscreenActivity.this.MakeSliderDialog(this.val$view, FullscreenActivity.effect, 0, 100);
                FullscreenActivity.this.video = Boolean.valueOf(FullscreenActivity.AUTO_HIDE);
                FullscreenActivity.effect = "skip";
            }
            if (FullscreenActivity.effect == "contrast") {
                FullscreenActivity.this.MakeSliderDialog(this.val$view, FullscreenActivity.effect, -100, 100);
                FullscreenActivity.effect = "skip";
            }
            if (FullscreenActivity.effect == "sharpness") {
                FullscreenActivity.this.MakeSliderDialog(this.val$view, FullscreenActivity.effect, -100, 100);
                FullscreenActivity.effect = "skip";
            }
            if (FullscreenActivity.effect == "saturation") {
                FullscreenActivity.this.MakeSliderDialog(this.val$view, FullscreenActivity.effect, -100, 100);
                FullscreenActivity.effect = "skip";
            }
            if (FullscreenActivity.effect == "ev") {
                FullscreenActivity.this.MakeSliderDialog(this.val$view, FullscreenActivity.effect, -25, 25);
                FullscreenActivity.effect = "skip";
            }
            if (FullscreenActivity.effect == "MJPEG") {
                FullscreenActivity.this.mjpeg = Boolean.valueOf(!FullscreenActivity.this.mjpeg.booleanValue() ? FullscreenActivity.AUTO_HIDE : false);
            }
            if (FullscreenActivity.effect == "USB") {
                settings.edit().putBoolean("USBcam", FullscreenActivity.AUTO_HIDE).commit();
                FullscreenActivity.this.onResume();
            } else {
                if (!(FullscreenActivity.effect == "none" || FullscreenActivity.effect == "skip")) {
                    Toast.makeText(FullscreenActivity.this.getApplicationContext(), "Applying effect " + FullscreenActivity.effect, Toast.LENGTH_SHORT).show();
                }
                FullscreenActivity.this.command = FullscreenActivity.this.raspistill + "-ifx " + FullscreenActivity.effect + " " + FullscreenActivity.this.raspioptions + FullscreenActivity.this.raspiend;
            }
            if (!(FullscreenActivity.this.video.booleanValue() || FullscreenActivity.effect == "skip")) {
                FullscreenActivity.this.updateimage(FullscreenActivity.this.findViewById(R.id.fullscreen_content));
            }
            return FullscreenActivity.AUTO_HIDE;
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.7 */
    class C00657 implements OnTouchListener {
        static final int DRAG = 1;
        static final int NONE = 0;
        static final int ZOOM = 2;
        float f6d;
        boolean first;
        float[] lastEvent;
        Matrix matrix;
        PointF mid;
        int mode;
        float newRot;
        float oldDist;
        Matrix savedMatrix;
        PointF start;

        C00657() {
            this.matrix = new Matrix();
            this.savedMatrix = new Matrix();
            this.mode = NONE;
            this.start = new PointF();
            this.mid = new PointF();
            this.oldDist = 1.0f;
            this.lastEvent = null;
            this.f6d = 0.0f;
            this.newRot = 0.0f;
            this.first = FullscreenActivity.AUTO_HIDE;
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
                case NONE /*0*/:
                    this.savedMatrix.set(this.matrix);
                    if (this.first) {
                        this.first = false;
                        this.start.set(event.getX(), event.getY() / 2.0f);
                    } else {
                        this.start.set(event.getX(), event.getY());
                    }
                    this.mode = DRAG;
                    this.lastEvent = null;
                    FullscreenActivity.this.presstime = System.currentTimeMillis();
                    break;
                case DRAG /*1*/:
                case FullscreenActivity.HIDER_FLAGS /*6*/:
                    this.mode = NONE;
                    this.lastEvent = null;
                    FullscreenActivity.this.mSystemUiHider.show();
                    FullscreenActivity.this.runTime = System.currentTimeMillis() - FullscreenActivity.this.presstime;
                    if (FullscreenActivity.this.runTime < 500) {
                        FullscreenActivity.this.mSystemUiHider.toggle();
                        break;
                    }
                    break;
                case ZOOM /*2*/:
                    FullscreenActivity.this.mSystemUiHider.hide();
                    if (this.mode != DRAG) {
                        if (this.mode == ZOOM && event.getPointerCount() == ZOOM) {
                            float newDist = FullscreenActivity.this.spacing(event);
                            this.matrix.set(this.savedMatrix);
                            if (newDist > 10.0f) {
                                float scale = newDist / this.oldDist;
                                if (FullscreenActivity.this.withinbounds(this.matrix, scale, 20.0f, 1.0f)) {
                                    this.matrix.postScale(scale, scale, (float) (FullscreenActivity.this.screenImage.getMeasuredWidth() / ZOOM), (float) (FullscreenActivity.this.screenImage.getMeasuredHeight() / ZOOM));
                                }
                            }
                            if (this.lastEvent != null) {
                                this.newRot = FullscreenActivity.this.rotation(event);
                                this.matrix.postRotate(this.newRot - this.f6d, (float) (FullscreenActivity.this.screenImage.getMeasuredWidth() / ZOOM), (float) (FullscreenActivity.this.screenImage.getMeasuredHeight() / ZOOM));
                                break;
                            }
                        }
                    }
                    this.matrix.set(this.savedMatrix);
                    this.matrix.postTranslate(event.getX() - this.start.x, event.getY() - this.start.y);
                    break;
                case ChannelSftp.SSH_FX_BAD_MESSAGE /*5*/:
                    this.oldDist = FullscreenActivity.this.spacing(event);
                    this.savedMatrix.set(this.matrix);
                    FullscreenActivity.this.midPoint(this.mid, event);
                    this.mode = ZOOM;
                    this.lastEvent = new float[4];
                    this.lastEvent[NONE] = event.getX(NONE);
                    this.lastEvent[DRAG] = event.getX(DRAG);
                    this.lastEvent[ZOOM] = event.getY(NONE);
                    this.lastEvent[3] = event.getY(DRAG);
                    this.f6d = FullscreenActivity.this.rotation(event);
                    break;
            }
            if (FullscreenActivity.this.livezoom.booleanValue()) {
                FullscreenActivity.this.screenImage.setScaleType(ScaleType.MATRIX);
                FullscreenActivity.this.screenImage.setImageMatrix(this.matrix);
            } else {
                FullscreenActivity.this.screenImage.setScaleType(ScaleType.FIT_CENTER);
            }
            if (FullscreenActivity.threeD == DRAG) {
                FullscreenActivity.this.screenImage.setScaleType(ScaleType.MATRIX);
                FullscreenActivity.this.screenImage.setImageMatrix(this.matrix);
            }
            FullscreenActivity.this.updateImage(v);
            return FullscreenActivity.AUTO_HIDE;
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.8 */
    class C00668 implements OnTouchListener {
        C00668() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            FullscreenActivity.this.delayedHide(FullscreenActivity.AUTO_HIDE_DELAY_MILLIS);
            return false;
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.9 */
    class C00679 implements Runnable {
        C00679() {
        }

        public void run() {
            FullscreenActivity.this.mSystemUiHider.hide();
        }
    }

    public class ReadMjpeg extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... url) {
            Bitmap bm = null;
            String TAG = "raspi";
            try {
                HttpResponse res = new DefaultHttpClient().execute(new HttpGet(URI.create(url[0])));
                if (res.getStatusLine().getStatusCode() == 401) {
                    return null;
                }
                bm = new mjpegStream(res.getEntity().getContent()).readMjpegFrame();
                return bm;
            } catch (ClientProtocolException e) {
            } catch (IOException e2) {
            }
            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            FullscreenActivity.this.latestpic = result;
        }
    }

    public class sshUpdate extends AsyncTask<Void, Void, Boolean> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... params) {
            try {
                FullscreenActivity.this.sshChannel(FullscreenActivity.this.session, FullscreenActivity.this.command);
            } catch (Exception e) {
            }
            return Boolean.valueOf(FullscreenActivity.AUTO_HIDE);
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            FullscreenActivity.this.updateImage(FullscreenActivity.this.findViewById(R.id.fullscreen_content));
            if (FullscreenActivity.this.video.booleanValue()) {
                new sshUpdate().execute();
            }
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.FullscreenActivity.6 */
    class C01036 implements SystemUiHider.OnVisibilityChangeListener {
        int mControlsHeight;
        int mShortAnimTime;
        final /* synthetic */ View val$controlsView;

        C01036(View view) {
            this.val$controlsView = view;
        }

        @TargetApi(13)
        public void onVisibilityChange(boolean visible) {
            if (VERSION.SDK_INT >= 13) {
                float f;
                if (this.mControlsHeight == 0) {
                    this.mControlsHeight = this.val$controlsView.getHeight();
                }
                if (this.mShortAnimTime == 0) {
                    //this.mShortAnimTime = FullscreenActivity.this.getResources().getInteger(17694720);
                }
                ViewPropertyAnimator animate = this.val$controlsView.animate();
                if (visible) {
                    f = 0.0f;
                } else {
                    f = (float) this.mControlsHeight;
                }
                animate.translationY(f).setDuration((long) this.mShortAnimTime);
            } else {
                this.val$controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
            if (visible) {
                FullscreenActivity.this.delayedHide(FullscreenActivity.AUTO_HIDE_DELAY_MILLIS);
            }
        }
    }

    public FullscreenActivity() {
        this.delaytime = " -t 200 ";
        this.raspistill = "raspistill -n " + this.delaytime + defaultimagesize;
        this.raspioptions = BuildConfig.VERSION_NAME;
        this.raspiend = " -o -";
        this.command = this.raspistill + this.raspioptions + this.raspiend;
        this.livezoom = Boolean.valueOf(AUTO_HIDE);
        this.usbcam = Boolean.valueOf(false);
        this.mjpeg = Boolean.valueOf(false);
        this.latestpic = null;
        this.angle = 0;
        this.lastrunTime = 1000;
        this.video = Boolean.valueOf(false);
        this.onTableTouched = new C00657();
        this.mDelayHideTouchListener = new C00668();
        this.mHideHandler = new Handler();
        this.mHideRunnable = new C00679();
    }

    static {
        defaultimagesize = " -w 640 -h 480 ";
        effect = "none";
        awb = "auto";
        metering = "average";
        exposure = "auto";
        imagesize = defaultimagesize;
        imagewidth = 640;
        threeD = 0;
    }

    public void updateimage(View view) {
        try {
            new sshUpdate().execute();
        } catch (Exception e) {
            Log.e(this.getClass().getName(),e.getMessage());
        }
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        } catch (Throwable th) {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    @SuppressLint({"NewApi"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            sshChannel(this.session, "sudo pkill raspi*;sudo pkill vlc");
        } catch (Exception e) {
        }
    }

    public void clickplay(View view) {
        if (getSharedPreferences("Raspicam", 0).getBoolean("h264", false)) {
            try {
                StringBuilder append = new StringBuilder().append(" -rot ");
                int i = (this.angle / 90) * 90;
                this.angle = i;
                String rotation = append.append(Integer.toString(i)).append(" ").toString();
                if (false){//this.usbcam.booleanValue()) {
                    sshChannel(this.session, "cvlc v4l2:// :v4l2-vdev=/dev/video0 --sout '#transcode{vcodec=mp2v}:duplicate{dst=std{access=http,mux=ts,dst=:8080}}' --no-sout-audio >/dev/null &");
                } else {
                    sshChannel(this.session, "/opt/vc/bin/raspivid -o - -n -t 0 -fps 25 " + rotation + defaultimagesize + this.raspioptions + " |cvlc stream:///dev/stdin --sout '#standard{access=http,mux=ts,dst=:8080}' :demux=h264 >/dev/null &");
                }
                ProgressDialog progressDialog = ProgressDialog.show(this, "Streaming h264", BuildConfig.VERSION_NAME, AUTO_HIDE);
                progressDialog.setCancelable(AUTO_HIDE);
                progressDialog.setOnCancelListener(new C00591());
                new Handler().postDelayed(new C00612(progressDialog), 3000);
                //return;
            } catch (Exception error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
                return;
            }
        }
        String message;
        ImageButton myButton = (ImageButton) findViewById(R.id.play_streamer_fullscreen);
        if (this.video.booleanValue()) {
            message = "Continuous mode disabled";
            myButton.setColorFilter(Color.argb(0, 0, 0, 0));
            this.video = Boolean.valueOf(false);
        } else {
            message = "Continuous mode on \n " + String.valueOf(((double) Integer.parseInt(this.delaytime.split(" ")[2])) / 1000.0d) + "s";
            myButton.setColorFilter(Color.argb(TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT));
            this.video = Boolean.valueOf(AUTO_HIDE);
        }
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
        try {
            new sshUpdate().execute();
        } catch (Exception error2) {
            Log.d("Raspicam", BuildConfig.VERSION_NAME + error2.toString());
        }
    }

    public void clickoldplay(View view) {
        if ((getSharedPreferences("Raspicam", 0).getBoolean("h264", false))){
            int myangle = (this.angle / 90) * 90;
            Toast.makeText(getApplicationContext(), "Grabbing video", Toast.LENGTH_LONG).show();
            try {
                sshChannel(this.session, "raspivid -t 3000 -w 480 -h 360 -rot " + String.valueOf(myangle) + " -o - ");
                File from = new File(getCacheDir() + "/image.jpg");
                File to = new File("/sdcard/video.h264");
                copyFile(from, to);
                Uri uri = Uri.fromFile(to);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
                return;
            } catch (Exception error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String message;
        ImageButton myButton = (ImageButton) findViewById(R.id.play_streamer_fullscreen);
        if (this.video.booleanValue()) {
            message = "Continuous mode disabled";
            myButton.setColorFilter(Color.argb(0, 0, 0, 0));
            this.video = Boolean.valueOf(false);
        } else {
            message = "Continuous mode on \n " + String.valueOf(((double) Integer.parseInt(this.delaytime.split(" ")[2])) / 1000.0d) + "s";
            myButton.setColorFilter(Color.argb(TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT, TransportMediator.FLAG_KEY_MEDIA_NEXT));
            this.video = Boolean.valueOf(AUTO_HIDE);
        }
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
        try {
            new sshUpdate().execute();
        } catch (Exception error2) {
            Log.d("Raspicam", BuildConfig.VERSION_NAME + error2.toString());
        }
    }

    public void MakeSliderDialog(View view, String effect, int min, int max) {
        Builder alert = new Builder(view.getContext());
        String adjust = effect;
        alert.setMessage("Set " + adjust);
        max -= min;
        LinearLayout linear = new LinearLayout(view.getContext());
        linear.setOrientation(LinearLayout.VERTICAL);
        SeekBar seek = new SeekBar(view.getContext());
        seek.setMax(0);
        seek.setMax(max);
        seek.setProgress(max / 2);
        seek.setOnSeekBarChangeListener(new C00623(min, adjust));
        linear.addView(seek);
        alert.setView(linear);
        Window window = alert.show().getWindow();
        window.clearFlags(2);
        LayoutParams wlp = window.getAttributes();
        wlp.gravity = 80;
        window.setAttributes(wlp);
    }

    public void saveimage(View view) {
        Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(getCacheDir() + "/image.jpg"), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME);
        Toast.makeText(getApplicationContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show();
    }

    String toggleoption(String options, String option) {
        if (options.contains(option)) {
            return this.raspioptions.replace(option, BuildConfig.VERSION_NAME);
        }
        return options + option;
    }

    @SuppressLint({"NewApi"})
    public void configclick(View view) {
        if (VERSION.SDK_INT >= 11) {
            PopupMenu menu = new PopupMenu(this, view);
            if (this.usbcam.booleanValue()) {
                menu.getMenu().add("none");
                menu.getMenu().add("pi camera");
                menu.getMenu().add("flip");
                menu.getMenu().add("mirror");
                menu.getMenu().add("3D");
                menu.getMenu().add("negative");
                menu.getMenu().add("greyscale");
                menu.getMenu().add("timestamp");
                menu.setOnMenuItemClickListener(new C00634());
            } else {
                menu.getMenu().add("none");
                menu.getMenu().add("USB");
                menu.getMenu().add("flip");
                menu.getMenu().add("mirror");
                menu.getMenu().add("rotate");
                menu.getMenu().add("3D");
                menu.getMenu().add("zoom");
                menu.getMenu().add("brightness");
                menu.getMenu().add("contrast");
                menu.getMenu().add("saturation");
                menu.getMenu().add("sharpness");
                menu.getMenu().add("ev");
                /*String[] effects = getResources().getStringArray(C0075R.array.effect_array);
                for (int i = 1; i < effects.length; i++) {
                    menu.getMenu().add(effects[i]);
                }*/
                menu.setOnMenuItemClickListener(new C00645(view));
            }
            menu.show();
            return;
        }
        startActivity(new Intent(this, Settings.class));
    }

    private Bitmap getResizedBitmap(Bitmap bm, int angle) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_videostreamer, menu);
        return true;

//        getMenuInflater().inflate(, menu);
//        //return super.onCreateOptionsMenu(menu);
//        return AUTO_HIDE;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0075R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return AUTO_HIDE;
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 82) {
        }
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        this.video = Boolean.valueOf(false);
        if (this.session != null) {
            this.session.disconnect();
        }
        finish();
        return AUTO_HIDE;
    }

    public void onPause() {
        super.onPause();
        this.video = Boolean.valueOf(false);

        ((ImageButton) findViewById(R.id.play_streamer_fullscreen)).setColorFilter(Color.argb(0, 0, 0, 0));
    }

    public void onResume() {
        SharedPreferences settings = getSharedPreferences("Raspicam", 0);
        this.raspioptions = " " ;//+ settings.getString("commandline", BuildConfig.VERSION_NAME).trim() + " ";
        Integer delay = Integer.valueOf(settings.getInt("delayTime", 200));
        this.delaytime = " -t " + String.valueOf(delay) + " ";
        this.raspistill = "/opt/vc/bin/raspistill -n " + this.delaytime + imagesize;
        this.livezoom = Boolean.valueOf(settings.getBoolean("livezoom", AUTO_HIDE));
        this.angle = settings.getInt("angle", 0);
        this.command = this.raspistill + "-ifx " + effect + " -awb " + awb + " -mm " + metering + " -ex " + exposure + " " + this.raspioptions + this.raspiend;
        this.usbcam = Boolean.valueOf(settings.getBoolean("USBcam", false));
        if (this.usbcam.booleanValue()) {
            setTitle("Raspicam - " + StreamerSetting.host + " USB1");
            String sleeptime = String.valueOf(delay.intValue() / 1000);
            this.command = "sleep " + sleeptime + ";fswebcam " + settings.getString("usbcommandline", BuildConfig.VERSION_NAME) + " -q -r " + String.format(Locale.US, "%dx%d", Integer.valueOf(imagewidth), Integer.valueOf((imagewidth * 4) / 3)) + " -";
        } else {
            setTitle("Raspicam - " + StreamerSetting.host);
        }
        updateimage(findViewById(R.id.fullscreen_content));
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        //Save the session
        this.session = StreamerSetting.session;

        this.f7t = new Timer();
        View controlsView = findViewById(R.id.fullscreen_content_controls);
        View contentView = findViewById(R.id.fullscreen_content);
        try {
            sshChannel(this.session, "sudo pkill raspi*");
        } catch (Exception e) {
            Log.e(this.getClass().getName(),e.getMessage());
        }
        this.mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        this.mSystemUiHider.setup();
        this.mSystemUiHider.setOnVisibilityChangeListener(new C01036(controlsView));
        //findViewById(R.id.update_button).setOnTouchListener(this.mDelayHideTouchListener);
    }

    private void limitDrag(Matrix m, ImageView view) {
        float[] values = new float[9];
        m.getValues(values);
        float transX = values[2];
        float transY = values[5];
        Rect bounds = view.getDrawable().getBounds();
        int viewWidth = getResources().getDisplayMetrics().widthPixels;
        int viewHeight = getResources().getDisplayMetrics().heightPixels;
        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;
        float minX = (float) (-(viewWidth / 2));
        float minY = (float) (-(viewHeight / 2));
        if (transX > ((float) viewWidth)) {
            transX = (float) viewWidth;
        } else if (transX < minX) {
            transX = minX;
        }
        if ((-transX) > ((float) viewWidth)) {
            transX = (float) (-viewWidth);
        } else if ((-transX) < minX) {
            transX = -(30.0f + minX);
        }
        if (transY > ((float) viewHeight)) {
            transY = (float) viewHeight;
        } else if (transY < minY) {
            transY = minY;
        }
        if ((-transY) > ((float) viewHeight)) {
            transY = (float) (-viewHeight);
        } else if ((-transY) < minY) {
            transY = -(170.0f + minY);
        }
        values[2] = transX;
        values[5] = transY;
        m.setValues(values);
    }

    private boolean withinbounds(Matrix matrix, float newscale, float max, float min) {
        float[] values = new float[9];
        matrix.getValues(values);
        float scalex = values[0];
        float skewy = values[3];
        float currentzoom = (float) Math.sqrt((double) ((scalex * scalex) + (skewy * skewy)));
        if (currentzoom > max && newscale > 1.0f) {
            return false;
        }
        if (currentzoom >= min || newscale >= 1.0f) {
            return AUTO_HIDE;
        }
        return false;
    }

    private float rotation(MotionEvent event) {
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(0) - event.getY(1)), (double) (event.getX(0) - event.getX(1))));
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.session = StreamerSetting.session;
        this.screenImage = (ImageView) findViewById(R.id.fullscreen_content);
        this.screenImage.setOnTouchListener(this.onTableTouched);
        setTitle("RaspiCAM - " + StreamerSetting.host);
        this.mjpeg = Boolean.valueOf(getSharedPreferences("Raspicam", 0).getBoolean("mjpeg", false));
        this.video = this.mjpeg;
    }

    public void updateImage(View v) {
        if (this.mjpeg.booleanValue()) {
            new ReadMjpeg().execute("http://" + StreamerSetting.host + ":8081");
        }
        File file = new File(getCacheDir() + "/image.jpg");
        if (file.length() > 1024 || this.mjpeg.booleanValue()) {
            Bitmap bitmap;
            ImageView jpgView = (ImageView) findViewById(R.id.fullscreen_content);
            if (this.mjpeg.booleanValue()) {
                bitmap = this.latestpic;
            } else {
                bitmap = BitmapFactory.decodeFile(getCacheDir() + "/image.jpg");
                if (this.angle != 0) {
                    bitmap = getResizedBitmap(bitmap, this.angle);
                }
            }
            Bitmap finalBitmap = bitmap;
            if (threeD == 1) {
                Bitmap bitmap2 = bitmap.copy(bitmap.getConfig(), AUTO_HIDE);
                try {
                    finalBitmap = Bitmap.createBitmap(this.screenImage.getMeasuredWidth(), this.screenImage.getMeasuredHeight(), Config.ARGB_8888);
                    Canvas canvas = new Canvas(finalBitmap);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
                    canvas.drawBitmap(bitmap2, 0.0f, (float) (this.screenImage.getMeasuredHeight() / 2), null);
                } catch (Exception e) {
                }
            }
            jpgView.setImageBitmap(finalBitmap);
            return;
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String error = in.readLine();
            in.close();
            if (this.usbcam.booleanValue()) {
                getSharedPreferences("Raspicam", 0).edit().putBoolean("USBcam", false).commit();
                onResume();
            }
        } catch (Exception e2) {
        }
    }

    public void sshChannel(Session session, String command) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        InputStream in = channel.getInputStream();
        channel.connect();
        FileOutputStream outputFile = new FileOutputStream(getCacheDir() + "/image.jpg");
        byte[] tmp = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
        while (true) {
            if (in.available() > 0) {
                int i = in.read(tmp, 0, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
                if (i >= 0) {
                    outputFile.write(tmp, 0, i);
                }
            }
            if (channel.isClosed()) {
                channel.disconnect();
                outputFile.close();
                return;
            }
        }
    }

    public byte[] sshCommand(Session session, String command) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] tmp = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
        while (true) {
            if ((in.available() <= 0 || in.read(tmp, 0, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) < 0) && channel.isClosed()) {
                channel.disconnect();
                return tmp;
            }
        }
    }

    private void delayedHide(int delayMillis) {
        this.mHideHandler.removeCallbacks(this.mHideRunnable);
        this.mHideHandler.postDelayed(this.mHideRunnable, (long) delayMillis);
    }
}
