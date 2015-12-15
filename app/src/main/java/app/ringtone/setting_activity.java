package app.ringtone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.pacoke.aplicacionringtone.R;
import app.ringtone.functions.AsyncLogin;
import cz.msebera.android.httpclient.Header;
import dtos.JsonHandlerResponse;
import dtos.RingToneRestClient;
import factories.IntentFactory;
import settingsApp.SharedSettings;

public class setting_activity extends AppCompatActivity {

    public com.rey.material.widget.EditText text_user;
    public com.rey.material.widget.EditText text_address;
    public com.rey.material.widget.EditText text_email;
    public com.rey.material.widget.EditText text_phone;
    public com.rey.material.widget.EditText text_pass;
    public com.rey.material.widget.Spinner list_app;
    public com.rey.material.widget.Button button_send;
    public com.rey.material.widget.ProgressView progress_send;
    public static Intent intentToFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inizialite_fields();
        addListenerTofields();
        fillfields();
    }

    private void fillfields() {
        if(SharedSettings.user!=null){
            text_user.setText(SharedSettings.user);
        }
        if(SharedSettings.pass!=null){
            text_pass.setText(SharedSettings.pass);
        }
        if(SharedSettings.email!=null){
            text_email.setText(SharedSettings.email);
        }
        if(SharedSettings.phone!=null){
            text_phone.setText(SharedSettings.phone);
        }
        if(SharedSettings.Base_Endpoint!=null){
            text_address.setText(SharedSettings.Base_Endpoint);
        }
    }

    private void inizialite_fields(){

        text_user = (EditText) findViewById(R.id.s_user);
        text_address = (EditText) findViewById(R.id.s_address);
        text_email = (EditText) findViewById(R.id.s_email);
        text_phone = (EditText) findViewById(R.id.s_phone);
        text_pass = (EditText) findViewById(R.id.s_pass);
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
        text_address.append(SharedSettings.Base_Endpoint);

    }

    private void addListenerTofields(){
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToFunction = IntentFactory.createIntent(v.getContext(),FunctionsSystem.class);
                Boolean good = true;
                if(text_user.getText().toString().isEmpty()){
                    good=false;
                    text_user.setError(getString(R.string.error_user));
                }
                if(text_address.getText().toString().isEmpty()){
                    good=false;
                    text_address.setError(getString(R.string.error_address));
                }
                if(text_email.getText().toString().isEmpty()){
                    good=false;
                    text_email.setError(getString(R.string.error_email));
                }
                if(text_phone.getText().toString().isEmpty()){
                    good=false;
                    text_phone.setError(getString(R.string.error_phone));
                }
                if(text_pass.getText().toString().isEmpty()){
                    good=false;
                    text_pass.setError(getString(R.string.error_pass));
                }
                if(good){
                    progress_send.start();
                    SharedSettings.user = text_user.getText().toString();
                    SharedSettings.pass = text_pass.getText().toString();
                    SharedSettings.email = text_email.getText().toString();
                    SharedSettings.phone = text_phone.getText().toString();
                    SharedSettings.Base_Endpoint = text_address.getText().toString();
                    new AsyncLogin().execute(
                                text_user.getText().toString(),text_pass.getText().toString(),text_phone.getText().toString(),text_email.getText().toString());

                }
                //Wait to response and start the activity it's correct.
            }
        });
    }

    public class AsyncLogin extends AsyncTask<String, Void, Integer> {
        //public static String ds="";
        public AsyncLogin(){}

        @Override
        protected Integer doInBackground(String... params) {
            JSONObject responsejson=null;
            //userAdd/{name}&{password}&{phone}&{email}
            String direccion="mainmethods/userAdd/"+params[0]+"&"+params[1]+"&"+params[2]+"&"+params[3];
            Integer result=0;
            Calendar current;
            Calendar future=DateFormat.getDateInstance().getCalendar();
            JsonHandlerResponse handler=new JsonHandlerResponse();
            future.add(Calendar.MILLISECOND, 50);
            try{
                RingToneRestClient.get(direccion, null, handler);
                while(true){
                    current=DateFormat.getDateInstance().getCalendar();
                    if(handler.getResponseJson()!=null){
                        responsejson=handler.getResponseJson();
                        break;
                    }
                    if(current.equals(future)){
                        break;
                    }
                }
            }catch (Exception e){
                Log.d("Excepcion", e.toString());
            }
            if(responsejson==null){
                result=0;
            }else{
                try {
                    Boolean temp=(Boolean) responsejson.get("registered");
                    if(temp)
                        result=1;
                    else
                        result=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result==1){
                intentToFunction.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intentToFunction);
            }else{
                Toast.makeText(getApplicationContext(), "La aplicacion no puede encontrar el servidor.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        if(getIntent().getStringExtra("comeFrom")!=null){
            Intent intent = IntentFactory.getFactory(getIntent().getStringExtra("comeFrom"));
            startActivity(intent);
        }
    }

}
