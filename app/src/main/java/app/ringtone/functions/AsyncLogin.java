package app.ringtone.functions;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dtos.RingToneRestClient;

/**
 * Created by Javi on 03/01/2016.
 */
public class AsyncLogin extends AsyncTask<String, Void, Integer> {

    public JSONObject responsejson=null;
    public static String ds="";
    public AsyncLogin(){}

    @Override
    protected Integer doInBackground(String... params) {
        //userAdd/{name}&{password}&{phone}&{email}
        String direccion="mainmethods/userAdd/"+params[0]+"&"+params[1]+"&"+params[2]+"&"+params[3];
        Integer result=0;
        try{
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
            //intentToFunction.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //startActivity(intentToFunction);
        }else{
            //Toast.makeText(getApplicationContext(), "La aplicacion no puede encontrar el servidor.", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
}
