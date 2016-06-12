package app.ringtone.functions.makevideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;

import cz.msebera.android.httpclient.Header;
import settingsApp.SharedSettings;
import utilsApp.UtilsMethods;


/**
 * Created by Javi on 01/06/2016.
 */
public class RetrieveVideoManually extends FileAsyncHttpResponseHandler {
    Context currentcontext;

    public RetrieveVideoManually(Context context) {
        super(context);
        currentcontext=context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
        Toast.makeText(currentcontext, "Error en la conexion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(currentcontext,"Error en el archivo",Toast.LENGTH_SHORT).show();
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
            UtilsMethods.copyFile(cDir.getPath() + "/", tempFile.getName(), SharedSettings.FolderInternal, nameFile);
            if(new File(SharedSettings.FolderInternal + nameFile).exists()){
                //Toast.makeText(currentcontext, "El archivo se ha movido con exito!", Toast.LENGTH_SHORT).show();
                String type = null;
                String extension = MimeTypeMap.getFileExtensionFromUrl(SharedSettings.FolderInternal+nameFile);
                if (extension != null) {
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SharedSettings.FolderInternal+nameFile));
                intent.setDataAndType(Uri.parse(SharedSettings.FolderInternal + nameFile), type);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                ((Activity)currentcontext).startActivityForResult(intent, 1);
                Toast.makeText(currentcontext, "El fichero se ha guardado con el nombre: "+nameFile, Toast.LENGTH_SHORT).show();
                //currentcontext.startActivity(intent);
            }else{
                Toast.makeText(currentcontext, "Error al mover el archivo", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRetry(int retryNo) {
        Toast.makeText(currentcontext, "Error en el archivo "+retryNo, Toast.LENGTH_SHORT).show();
    }
}
