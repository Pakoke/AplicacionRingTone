package app.ringtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.pacoke.aplicacionringtone.R;
import factories.IntentFactory;

public class setting_activity extends AppCompatActivity {

    public com.rey.material.widget.EditText text_user;
    public com.rey.material.widget.EditText text_ip;
    public com.rey.material.widget.EditText text_port;
    public com.rey.material.widget.EditText text_email;
    public com.rey.material.widget.EditText text_phone;
    public com.rey.material.widget.Spinner list_app;
    public com.rey.material.widget.Button button_send;
    public com.rey.material.widget.ProgressView progress_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inizialite_widget();
        addListenerToWidget();

    }
    private void inizialite_widget(){

        text_user = (EditText) findViewById(R.id.s_user);
        text_ip = (EditText) findViewById(R.id.s_ip);
        text_port = (EditText) findViewById(R.id.s_port);
        text_email = (EditText) findViewById(R.id.s_email);
        text_phone = (EditText) findViewById(R.id.s_phone);
        list_app = (Spinner) findViewById(R.id.s_spinner);
        button_send = (Button) findViewById(R.id.s_sinformation);
        progress_send =(ProgressView) findViewById(R.id.s_progressbar);


        List<String> list = new ArrayList<String>();
        list.add("Correo");
        list.add("Whatsapp");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        list_app.setAdapter(dataAdapter);



    }

    private void addListenerToWidget(){
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_send.start();

                Intent intent = IntentFactory.createIntent(v.getContext(),FunctionsSystem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        /*
        text_user.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
                Log.i(this.getClass().toString(), "Accion de error onEditorAction");
                if (text_user.getText().toString().contains("a")) {
                    v.setError(getString(R.string.error_user));
                }
                return false;
            }
        });
        text_user.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (text_user.getText().toString().contains("a")) {
                        text_user.setError(getString(R.string.error_user));
                    } else {
                        text_user.setError(null);
                    }
                }
                return false;
            }
        });*/
    }

}
