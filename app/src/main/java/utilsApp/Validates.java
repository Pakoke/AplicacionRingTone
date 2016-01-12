package utilsApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Javi on 12/12/2015.
 */
public class Validates {

    private static CharSequence inputStr;
    public static Boolean validatePhone(String phoneNumber){
        Boolean isValid = false;
        //Initialize reg ex for phone number.
        String expression = "\\d{9}";
        inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
    public static Boolean validateUser(String username){
        Boolean isValid = false;
        //Initialize reg ex for phone number.
        String expression = "[a-zA-Z]+";
        inputStr = username;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
    public static Boolean validateEmail(String email){
        Boolean isValid = false;
        //Initialize reg ex for phone number.
        String expression = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
        inputStr = email;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
    public static Boolean validateIp(String ipAdress){
        Boolean isValid = false;
        //Initialize reg ex for phone number.
        String expression = "(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))";
        inputStr = ipAdress;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
    public static Boolean validatePort(String portAdress){
        Boolean isValid = false;
        //Initialize reg ex for phone number.
        String expression = "\\d{2,4}";
        inputStr = portAdress;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }

}
