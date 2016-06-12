package app.ringtone.functions.videostreamer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

import app.pacoke.aplicacionringtone.BuildConfig;

public class Settings extends Activity {
    String imagesize;
    boolean realclick;

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.1 */
    class C00761 implements OnCheckedChangeListener {
        C00761() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean z;
            String commandline;
            z = !isChecked;
            /*Boolean onUSB = Boolean.valueOf(z);
            Spinner spinner = (Spinner) Settings.this.findViewById(C0075R.id.effectsspinner);
            spinner.getSelectedView();
            spinner.setEnabled(onUSB.booleanValue());
            spinner = (Spinner) Settings.this.findViewById(C0075R.id.awbspinner);
            spinner.getSelectedView();
            spinner.setEnabled(onUSB.booleanValue());
            spinner = (Spinner) Settings.this.findViewById(C0075R.id.exposurespinner);
            spinner.getSelectedView();
            spinner.setEnabled(onUSB.booleanValue());
            spinner = (Spinner) Settings.this.findViewById(C0075R.id.meteringspinner);
            spinner.getSelectedView();
            spinner.setEnabled(onUSB.booleanValue());
            EditText editText = (EditText) Settings.this.findViewById(C0075R.id.commandline);
            if (onUSB.booleanValue()) {
                commandline = "commandline";
            } else {
                commandline = "usbcommandline";
            }
            editText.setText(Settings.this.getSharedPreferences("Raspicam", 0).getString(commandline, BuildConfig.VERSION_NAME));*/
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.2 */
    class C00772 implements OnItemSelectedListener {
        final /* synthetic */ String[] val$imagesizes;

        C00772(String[] strArr) {
            this.val$imagesizes = strArr;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (Settings.this.realclick) {
                FullscreenActivity.imagewidth = Integer.valueOf(this.val$imagesizes[i].split(" ")[0]).intValue();
                int h = (FullscreenActivity.imagewidth * 3) / 4;
                FullscreenActivity.imagesize = String.format(Locale.US, " -w %d -h %d ", Integer.valueOf(FullscreenActivity.imagewidth), Integer.valueOf(h));
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.3 */
    class C00783 implements OnItemSelectedListener {
        final /* synthetic */ String[] val$effects;

        C00783(String[] strArr) {
            this.val$effects = strArr;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (Settings.this.realclick) {
                FullscreenActivity.effect = this.val$effects[i];
            } else {
                Settings.this.realclick = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.4 */
    class C00794 implements OnItemSelectedListener {
        final /* synthetic */ String[] val$awbs;

        C00794(String[] strArr) {
            this.val$awbs = strArr;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (Settings.this.realclick) {
                FullscreenActivity.awb = this.val$awbs[i];
            } else {
                Settings.this.realclick = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.5 */
    class C00805 implements OnItemSelectedListener {
        final /* synthetic */ String[] val$exposures;

        C00805(String[] strArr) {
            this.val$exposures = strArr;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (Settings.this.realclick) {
                FullscreenActivity.exposure = this.val$exposures[i];
            } else {
                Settings.this.realclick = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.Settings.6 */
    class C00816 implements OnItemSelectedListener {
        final /* synthetic */ String[] val$meterings;

        C00816(String[] strArr) {
            this.val$meterings = strArr;
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (Settings.this.realclick) {
                FullscreenActivity.metering = this.val$meterings[i];
            } else {
                Settings.this.realclick = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public Settings() {
        this.realclick = false;
    }

    int ArrayPos(String[] array, String current) {
        int position = 0;
        String[] arr$ = array;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$ && arr$[i$].indexOf(current) == -1) {
            position++;
            i$++;
        }
        if (position >= array.length) {
            return 0;
        }
        return position;
    }

    protected void onCreate(Bundle savedInstanceState) {
        /*String commandline;
        super.onCreate(savedInstanceState);
        setContentView(C0075R.layout.activity_settings);
        SharedPreferences settings = getSharedPreferences("Raspicam", 0);
        CheckBox checkbox = (CheckBox) findViewById(C0075R.id.USBcam);
        Boolean usbcam = Boolean.valueOf(settings.getBoolean("USBcam", false));
        checkbox.setChecked(usbcam.booleanValue());
        EditText editText = (EditText) findViewById(C0075R.id.commandline);
        if (usbcam.booleanValue()) {
            commandline = "usbcommandline";
        } else {
            commandline = "commandline";
        }
        editText.setText(settings.getString(commandline, BuildConfig.VERSION_NAME));
        ((EditText) findViewById(C0075R.id.txtDelay)).setText(String.valueOf(Double.valueOf(((double) settings.getInt("delayTime", 100)) / 1000.0d)));
        checkbox.setOnCheckedChangeListener(new C00761());
        ((CheckBox) findViewById(C0075R.id.livezoom)).setChecked(settings.getBoolean("livezoom", true));
        ((CheckBox) findViewById(C0075R.id.h264)).setChecked(settings.getBoolean("h264", false));
        Resources res = getResources();
        String[] imagesizes = res.getStringArray(C0075R.array.size_array);
        Spinner spinner = (Spinner) findViewById(C0075R.id.sizespinner);
        spinner.setSelection(ArrayPos(imagesizes, FullscreenActivity.imagesize.split(" ")[2]));
        spinner.setOnItemSelectedListener(new C00772(imagesizes));
        String[] effects = res.getStringArray(C0075R.array.effect_array);
        spinner = (Spinner) findViewById(C0075R.id.effectsspinner);
        spinner.setSelection(ArrayPos(effects, FullscreenActivity.effect));
        spinner.setOnItemSelectedListener(new C00783(effects));
        spinner.getSelectedView();
        spinner.setEnabled(!usbcam.booleanValue());
        String[] awbs = res.getStringArray(C0075R.array.awb_array);
        spinner = (Spinner) findViewById(C0075R.id.awbspinner);
        spinner.setSelection(ArrayPos(awbs, FullscreenActivity.awb));
        spinner.setOnItemSelectedListener(new C00794(awbs));
        spinner.getSelectedView();
        spinner.setEnabled(!usbcam.booleanValue());
        String[] exposures = res.getStringArray(C0075R.array.exposure_array);
        spinner = (Spinner) findViewById(C0075R.id.exposurespinner);
        spinner.setSelection(ArrayPos(exposures, FullscreenActivity.exposure));
        spinner.setOnItemSelectedListener(new C00805(exposures));
        spinner.getSelectedView();
        spinner.setEnabled(!usbcam.booleanValue());
        String[] meterings = res.getStringArray(C0075R.array.metering_array);
        spinner = (Spinner) findViewById(C0075R.id.meteringspinner);
        spinner.setSelection(ArrayPos(meterings, FullscreenActivity.metering));
        spinner.setOnItemSelectedListener(new C00816(meterings));
        spinner.getSelectedView();
        spinner.setEnabled(!usbcam.booleanValue());*/
    }

    public void reviewonmarket(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.pibits.raspberrypiremotecam")));
    }

    public void savebutton(View view) {
        /*double ddelay;
        SharedPreferences settings = getSharedPreferences("Raspicam", 0);
        boolean USBcam = ((CheckBox) findViewById(C0075R.id.USBcam)).isChecked();
        settings.edit().putBoolean("USBcam", USBcam).commit();
        String text = ((EditText) findViewById(C0075R.id.commandline)).getText().toString().trim();
        text.replaceAll("-t 0", BuildConfig.VERSION_NAME);
        text = " " + text + " ";
        if (USBcam) {
            settings.edit().putString("usbcommandline", text).commit();
        } else {
            settings.edit().putString("commandline", text).commit();
        }
        try {
            ddelay = Double.parseDouble(((EditText) findViewById(C0075R.id.txtDelay)).getText().toString());
        } catch (NumberFormatException e) {
            ddelay = 0.1d;
        }
        if (ddelay < 0.1d) {
            ddelay = 0.1d;
        }
        settings.edit().putInt("delayTime", (int) (1000.0d * ddelay)).commit();
        settings.edit().putBoolean("livezoom", ((CheckBox) findViewById(C0075R.id.livezoom)).isChecked()).commit();
        settings.edit().putBoolean("h264", ((CheckBox) findViewById(C0075R.id.h264)).isChecked()).commit();
        finish();*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(C0075R.menu.settings, menu);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        savebutton(null);
        finish();
        return true;
    }
}
