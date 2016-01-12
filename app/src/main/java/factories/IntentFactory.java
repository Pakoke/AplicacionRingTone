package Factories;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Javi on 12/12/2015.
 */
public class IntentFactory {

    private static HashMap<String,Intent> intents = new HashMap<String,Intent>();

    public static Intent createIntent(Context contexto,Class clase){
        Intent intent = new Intent(contexto,clase);
        intents.put(clase.getName(),intent);
        return intent;
    }

    public static Intent getFactory(String nameclass){
        return intents.get(nameclass);
    }
}
