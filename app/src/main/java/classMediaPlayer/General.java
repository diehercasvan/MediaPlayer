package classMediaPlayer;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by DIEGO CASALLAS on 17/11/2015.
 */
public class General {
    /**
     * **********
     * Creating static variables general
     */

    public static Activity ACTIVITY;
    public static Context CONTEXT;
    public static String ROUTE;
    public  static String [] EXTENSIONS={"mp3","mp4"};


    public  General(){

        this.ACTIVITY=null;
        this.CONTEXT=null;
        this.ROUTE=null;
    }
    /**
     * *****Creating builder  class*********
     */
    public static void printToast(String sMessage){

        Toast.makeText(ACTIVITY, "General Message" + sMessage, Toast.LENGTH_LONG).show();
    }
}
