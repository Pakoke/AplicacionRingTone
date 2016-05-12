package app.ringtone.functions.stateServer;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dtos.RingToneRestClient;

/**
 * Created by Javi on 12/05/2016.
 */
public class AsyncStateBell extends AsyncTask<String, Void, Boolean>{

    public JSONObject responsejson;

    public AsyncStateBell(){}
    @Override
    protected Boolean doInBackground(String... params) {
        String direccion="cameramethods/stateRing/";
        Boolean result=false;
        try{
            //RingToneRestClient.post();
            RingToneRestClient.get(direccion, null, new JsonHttpResponseHandler() {
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
        }catch (Exception e){
            Log.d("Excepcion", e.toString());
        }
        Log.d("http",responsejson.toString());

        if(responsejson==null){
            result=false;
        }else{
            try {
                Boolean temp=(Boolean) responsejson.get("statering");
                result = temp;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //At the end of doinBackground call this method to make something more
        if(result==true){
            //intentToFunction.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //startActivity(intentToFunction);
        }else{
            //Toast.makeText(getApplicationContext(), "La aplicacion no puede encontrar el servidor.", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
}
