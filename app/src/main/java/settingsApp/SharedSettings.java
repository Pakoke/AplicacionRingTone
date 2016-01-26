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
    public static String Base_Endpoint = "http://10.0.2.2:8080/Rest_ServiceServerRingTone/RingTone/";
    public static String user=null;
    public static String pass=null;
    public static String email=null;
    public static String phone=null;
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
