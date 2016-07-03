package app.ringtone.functions.makephoto;

import android.content.Context;
import android.widget.Toast;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import settingsApp.SharedSettings;
import utilsApp.UtilsMethods;

/**
 * Created by Javi on 02/01/2016.
 */
public class RetrievePhotoManually extends FileAsyncHttpResponseHandler{

    private SweetAlertDialog pDialog;
    Context currentcontext;

    public RetrievePhotoManually(Context context) {
        super(context);
        currentcontext=context;
    }
    public RetrievePhotoManually(Context context,SweetAlertDialog pDialog) {
        super(context);
        currentcontext=context;
        this.pDialog = pDialog;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//        Toast.makeText(currentcontext,"Error en la conexion",Toast.LENGTH_SHORT).show();
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Fallo en la conexion");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, File file) {
        if (file == null || !file.exists()) {
//            Toast.makeText(currentcontext,"Error en el archivo",Toast.LENGTH_SHORT).show();
            pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Error en el archivo");
            return;
        }
        try{
            File tempFile;
            FileInputStream input = new FileInputStream(file);
            String nameFile="example";
            File cDir = currentcontext.getCacheDir();
            /** Getting a reference to temporary file, if created earlier */
            tempFile = new File(cDir.getPath() + "/" + file.getName()) ;
            for(Header h:headers){
                if(h.getName().equals("Content-Disposition")){
                    nameFile=h.getValue().split("\"")[1];
                }
            }
            UtilsMethods.copyFile(cDir.getPath()+"/",tempFile.getName(),SharedSettings.FolderInternal,nameFile);
            if(new File(SharedSettings.FolderInternal + nameFile).exists()){
//                Toast.makeText(currentcontext, "El archivo se ha movido con exito!", Toast.LENGTH_SHORT).show();
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("El archivo se ha creado con exito!");
            }else{
//                Toast.makeText(currentcontext, "Error al mover el archivo", Toast.LENGTH_SHORT).show();
                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Error al mover el archivo");

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRetry(int retryNo) {
        //Toast.makeText(currentcontext, "Error en el archivo "+retryNo, Toast.LENGTH_SHORT).show();
        pDialog.setTitleText("Reintento "+retryNo);
//        if(retryNo >= 3) {
//            Header[] head = new Header[1];
//            this.onFailure(0,head,new TimeoutException(),null);
//            try {
//                this.finalize();
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }
    }

}
