package app.ringtone.functions.videostreamer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import app.pacoke.aplicacionringtone.BuildConfig;
import app.pacoke.aplicacionringtone.*;
import app.ringtone.*;
import factories.IntentFactory;

public class StreamerSetting extends AppCompatActivity {

    public static String host;
    public static Session session;
    public Session EXTRA_MESSAGE;
    boolean connected;
    int port;
    String pwd;
    String user;

    /* renamed from: com.pibits.raspberrypiremotecam.StreamerSetting.1 */
    class C00721 implements AdapterView.OnItemClickListener {
        private long lastclick;
        final /* synthetic */ ListView val$listView;

        C00721(ListView listView) {
            this.val$listView = listView;
            this.lastclick = -1;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ((EditText) StreamerSetting.this.findViewById(R.id.ip_videostreamer)).setText((String) this.val$listView.getItemAtPosition(position));
            long now = System.currentTimeMillis();
            if (now - this.lastclick < 250) {
                StreamerSetting.this.ConnectButton(StreamerSetting.this.findViewById(R.id.activity_content_videostreamer ));
            }
            this.lastclick = now;
        }
    }

    /* renamed from: com.pibits.raspberrypiremotecam.StreamerSetting.2 */
    class C00732 implements DialogInterface.OnClickListener {
        C00732() {
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case ViewDragHelper.INVALID_POINTER /*-1*/:
                    ((EditText) StreamerSetting.this.findViewById(R.id.ip_videostreamer)).setText(StreamerSetting.this.getIpAddr());
                    ((EditText) StreamerSetting.this.findViewById(R.id.username_videostreamer)).setText("pi");
                    ((EditText) StreamerSetting.this.findViewById(R.id.password_videostreamer)).setText("raspberry");
                    ((EditText) StreamerSetting.this.findViewById(R.id.port_videostreamer)).setText("22");
                    Toast.makeText(StreamerSetting.this.getApplicationContext(), "Login settings set to defaults", Toast.LENGTH_SHORT).show();
                default:
            }
        }
    }

    public class SShConnect extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;

        /* renamed from: com.pibits.raspberrypiremotecam.StreamerSetting.SShConnect.1 */
        class C00741 implements DialogInterface.OnCancelListener {
            C00741() {
            }


            public void onCancel(DialogInterface dialog) {
                SShConnect.this.cancel(true);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            StreamerSetting.this.connected = false;
            this.progressDialog = ProgressDialog.show(StreamerSetting.this, "Connecting to " + StreamerSetting.host, "Please wait (up to 30 secs)...", true);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener(new C00741());
        }

        protected Boolean doInBackground(Void... params) {
            return Boolean.valueOf(StreamerSetting.this.sshConnected());
        }

        protected void onPostExecute(Boolean result) {
            String text;
            super.onPostExecute(result);
            this.progressDialog.dismiss();
            if (result.booleanValue()) {
                StreamerSetting.this.connected = true;
                text = "Successfully connected to " + StreamerSetting.host;
                StreamerSetting.this.fullscreen(0);
            } else {
                StreamerSetting.this.connected = false;
                text = StreamerSetting.host + ": Connection failed";
            }
            Toast.makeText(StreamerSetting.this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public StreamerSetting() {
        this.user = "pi";
        this.pwd = "raspberry";
        this.port = 22;
        this.connected = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamer_setting);
        SharedPreferences settings = getSharedPreferences("Raspicam", 0);
        EditText editText = (EditText) findViewById(R.id.ip_videostreamer);
        editText.setText(settings.getString("ipaddress", getIpAddr()));
        String ip = getIntent().getStringExtra("ip");
        if (!(ip == null || ip.contains("("))) {
            editText.setText(ip);
            ConnectButton(findViewById(R.id.activity_content_videostreamer));
        }
        ((EditText) findViewById(R.id.username_videostreamer)).setText(settings.getString("user", this.user));
        ((EditText) findViewById(R.id.password_videostreamer)).setText(settings.getString("password", this.pwd));
        ((EditText) findViewById(R.id.port_videostreamer)).setText(settings.getString("port", String.valueOf(this.port)));
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox1_videostreamer);
        Boolean autostart = Boolean.valueOf(settings.getBoolean("autostart", false));
        checkbox.setChecked(autostart.booleanValue());
        ListView listView = (ListView) findViewById(R.id.list_videostreamer);
        String[] values = new String[4];
        for (int i = 0; i < 4; i++) {
            values[i] = settings.getString("recents_" + i, BuildConfig.VERSION_NAME);
        }
        /*listView.setAdapter(new ArrayAdapter(this, 17367043, 16908308, values));
        listView.setOnItemClickListener(new C00721(listView));
        if (autostart.booleanValue()) {
            ConnectButton(findViewById(C0075R.layout.activity_main));
        }*/
    }

    public boolean sshConnected() {
        JSch jsch = new JSch();
        try {
            if (!host.contains(".")) {
                try {
                    host = InetAddress.getByName(host).getHostAddress();
                } catch (UnknownHostException e) {
                }
            }
            session = jsch.getSession(this.user, host, this.port);
            session.setPassword(this.pwd);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            prop.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig(prop);
            session.connect(30000);

            return true;
        } catch (JSchException e2) {
            return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public String getIpAddr() {
        return ((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getIpAddress() != 0 ? String.format(Locale.US, "%d.%d.%d.", Integer.valueOf(((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getIpAddress() & MotionEventCompat.ACTION_MASK), Integer.valueOf((((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getIpAddress() >> 8) & MotionEventCompat.ACTION_MASK), Integer.valueOf((((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getIpAddress() >> 16) & MotionEventCompat.ACTION_MASK), Integer.valueOf((((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getIpAddress() >> 24) & MotionEventCompat.ACTION_MASK)) : BuildConfig.VERSION_NAME;
    }

    public void ConnectButton(View view) {
        EditText editText = (EditText) findViewById(R.id.ip_videostreamer);
        host = editText.getText().toString().replaceAll(",", ".");
        String ip = getIpAddr();
        if (host == null || ip == null) {
            Toast.makeText(getApplicationContext(), "Not an IP Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (host.contentEquals(ip)) {
            Toast.makeText(getApplicationContext(), "Not an IP Address", Toast.LENGTH_SHORT).show();
            return;
        }
        int i;
        editText.setEnabled(false);
        editText.setEnabled(true);
        this.user = ((EditText) findViewById(R.id.username_videostreamer)).getText().toString();
        this.pwd = ((EditText) findViewById(R.id.password_videostreamer)).getText().toString();
        this.port = Integer.parseInt(((EditText) findViewById(R.id.port_videostreamer)).getText().toString());
        CheckBox checkbox = (CheckBox) findViewById((R.id.checkBox1_videostreamer));
        CheckBox chkmjpeg = (CheckBox) findViewById((R.id.chkmjpeg_videostreamer));
        SharedPreferences settings = getSharedPreferences("Raspicam", 0);
        settings.edit().putString("ipaddress", host).commit();
        settings.edit().putString("user", this.user).commit();
        settings.edit().putString("password", this.pwd).commit();
        settings.edit().putString("port", String.valueOf(this.port)).commit();
        settings.edit().putBoolean("autostart", checkbox.isChecked()).commit();
        settings.edit().putBoolean("mjpeg", chkmjpeg.isChecked()).commit();
        settings.edit().putString("imagesize", " -w 640 -h 480 ").commit();
        ArrayList<String> values = new ArrayList();
        for (i = 0; i < 4; i++) {
            values.add(settings.getString("recents_" + i, BuildConfig.VERSION_NAME));
        }
        values.remove(host);
        values.add(0, host);
        for (i = 0; i < 4; i++) {
            String newvalue = values.get(i);
            settings.edit().putString("recents_" + i, newvalue).commit();
        }
        ListView listView = (ListView) findViewById(R.id.list_videostreamer);
        String[] listvalues = new String[4];
        for (i = 0; i < 4; i++) {
            listvalues[i] = settings.getString("recents_" + i, BuildConfig.VERSION_NAME);
        }
        //listView.setAdapter(new ArrayAdapter(this, 17367043, 16908308, listvalues));
        if (chkmjpeg.isChecked()) {
            fullscreen(0);
            return;
        }
        try {
            new SShConnect().execute();
        } catch (Exception error) {
            Log.d("Raspicam", BuildConfig.VERSION_NAME + error.toString());
        }
    }

    public void ResetDefaults(View view) {
        DialogInterface.OnClickListener dialogClickListener = new C00732();
        new AlertDialog.Builder(this).setMessage("Reset Username, Password and port to default settings ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    public void fullscreen(int num) {
        startActivity(new Intent(this, FullscreenActivity.class));
    }

    public void imageclicked(View view) {
        if (this.connected) {
            fullscreen(0);
        } else {
            ConnectButton(view);
        }
    }

    public void openimage(View view) {
        File f = new File(getCacheDir() + "/image.jpg");
        f.setReadable(true, false);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(f), "image/*");
        startActivity(intent);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == 2 || newConfig.orientation != 1) {

        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        session.disconnect();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = IntentFactory.getFactory(FunctionsSystem.class.getName());
        startActivity(intent);
    }

}
