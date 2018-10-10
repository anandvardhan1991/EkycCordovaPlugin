package cordova.plugin.ekyc;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

public class LocalUtils {
    public static Boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }
}