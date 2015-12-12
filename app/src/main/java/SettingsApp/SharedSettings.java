package SettingsApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javi on 12/12/2015.
 */
public class SharedSettings {
    public static final List<String> appsToInteract = new ArrayList<String>();

    public static void initaliceParameters(){
        initialiceAppsInteract();
    }
    private static void initialiceAppsInteract(){
        appsToInteract.add("WhatsApp");
        appsToInteract.add("Correo");
    }
}
