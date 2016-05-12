package settingsApp;

import android.graphics.Path;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javi on 12/12/2015.
 */
public class SharedSettings {
    public static final List<String> appsToInteract = new ArrayList<String>();
    //public static String FolderInternal = "/sdcard/Pictures";
    public static String FolderInternal = Environment.getExternalStorageDirectory().getAbsolutePath()+"/RingTonesImages/";
    public static String Base_Endpoint = "http://192.168.43.16:8080/Rest_ServiceServerRingTone/RingTone/";
    public static String user="javier";
    public static String pass="javier";
    public static String email="fraruide@us.es";
    public static String phone="657611073";
    public static void initaliceParameters(){
        initialiceAppsInteract();
    }
    private static void initialiceAppsInteract(){
        appsToInteract.add("WhatsApp");
        appsToInteract.add("Correo");
        File folder = new File(FolderInternal);
        if(!folder.exists()){
            folder.mkdirs();
        }
    }

}
