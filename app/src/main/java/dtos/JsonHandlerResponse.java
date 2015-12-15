package dtos;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Javi on 03/01/2016.
 */
public class JsonHandlerResponse extends JsonHttpResponseHandler{

    private JSONObject responsejson=null;

    public JsonHandlerResponse(){}

    public JSONObject getResponseJson(){
        return responsejson;
    }

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

}
