package app.ringtone.functions.makeaudio;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import dtos.RingToneRestClient;

/**
 * Created by Javi on 02/04/2016.
 */
public class sendFile extends AsyncTask<String, Void, Integer>{

    private static final String LOG_TAG = "AudioRecordSendToServer";
    private JSONObject responsejson=new JSONObject();
    private String directionFileToUpload="";
    private Context contextWhereCall = null;
    private Boolean speaker = true;
    public CheckBox sendCheckBox;

    public sendFile(String directionFileToUpload)
    {
        this.directionFileToUpload = directionFileToUpload;
    }
    public sendFile(String directionFileToUpload, Context contextWhereCall)
    {
        this.directionFileToUpload = directionFileToUpload;
        this.contextWhereCall = contextWhereCall;
    }
    
    public void setSpeaker(Boolean speaker){
        this.speaker = speaker;
    }



    @Override
    protected Integer doInBackground(String... params) {
        String direccion="cameramethods/playAudio/";
        Integer result=0;

        File fileToUpload = new File(directionFileToUpload);
        Log.i(LOG_TAG, "Grabando " + directionFileToUpload);

        RequestParams paramsa = new RequestParams();
        if(fileToUpload.exists())
        {
            try {
                paramsa.put("file", fileToUpload);
                paramsa.put("name",fileToUpload.getName());
                paramsa.put("speaker",String.valueOf(speaker));
                //paramsa.put("filenamedfdf",directionFileToUpload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(contextWhereCall, "La aplicacion no puede encontrar el archivo.", Toast.LENGTH_SHORT).show();
        }

        try{
            RingToneRestClient.post(direccion,paramsa,new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    responsejson = response;

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
                @Override
                public boolean getUseSynchronousMode() {
                    return false;
                }
            });
            result = 1;
        }catch (Exception e){
            Log.d("Excepcion", e.toString());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if(result==1){
            this.sendCheckBox.setChecked(true);
        }else{
            this.sendCheckBox.setChecked(false);
            Toast.makeText(contextWhereCall, "Se ha producido un error al mandar el fichero.", Toast.LENGTH_SHORT).show();
        }
    }
}
