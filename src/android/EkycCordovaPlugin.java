package cordova.plugin.ekyc;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
   
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;

import java.net.URI;

import com.ecs.rdlibrary.ECSBioCaptureActivity;
import com.ecs.rdlibrary.response.PidData;


/**
 * This class echoes a string called from JavaScript.
 */
public class EkycCordovaPlugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private final int FINGERPRINT_CAPTURE_ACTIVITY = 100;
    private final int IRIS_CAPTURE_ACTIVITY = 200;
    private Context context;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public void setTheGlobalCallBackContext(CallbackContext callbackContext){
        this.callbackContext = callbackContext;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.context = this.cordova.getActivity().getApplicationContext();
        PersonalDetailsModelClass personalDetailsModelClass = new PersonalDetailsModelClass();
        personalDetailsModelClass.setQuotationNumber(args.getJSONObject(0).getString("quotationNumber"));
        personalDetailsModelClass.setProposerName(args.getJSONObject(0).getString("proposerName"));
        personalDetailsModelClass.setAdhaarNumber(args.getJSONObject(0).getString("adhaarNumber"));
        personalDetailsModelClass.setPlanName(args.getJSONObject(0).getString("planName"));
        this.setTheGlobalCallBackContext(callbackContext);
        if(action.equals("irisVerification")) {
            this.irisVerification(personalDetailsModelClass, context, callbackContext);
            return true;
        } else if(action.equals("fingerPrintVerification")) {
            this.fingerPrintVerification(personalDetailsModelClass, context, callbackContext);
            return true;
        }
        return false;
    }

    private void irisVerification(PersonalDetailsModelClass personalDetailsModelClass, Context context, CallbackContext callbackContext) {
        try{
        callbackContext.success("" +personalDetailsModelClass.getQuotationNumber());
        } catch(Exception e){
            callbackContext.error("Something went wrong"+ e);
        }
    }

    private void fingerPrintVerification(PersonalDetailsModelClass personalDetailsModelClass, Context context, CallbackContext callbackContext) {
        try{
        startFingerPrintCapture(context, callbackContext);
        } catch(Exception e){
            callbackContext.error("Something went wrong"+ e);
        }
    }


    private void startFingerPrintCapture(Context context, CallbackContext callbackContext){
        try{
        Intent intentStartFingerPrintCapture = new Intent(context, ECSBioCaptureActivity.class);
        intentStartFingerPrintCapture.putExtra("OPERATION", "K");
        intentStartFingerPrintCapture.putExtra("DEVICETYPE", "F");
        intentStartFingerPrintCapture.putExtra("UIDAIENVIRONMENT", "PP");
        intentStartFingerPrintCapture.putExtra("PFR", true);
        intentStartFingerPrintCapture.putExtra("DE", false);
        intentStartFingerPrintCapture.putExtra("LR", true);
        intentStartFingerPrintCapture.putExtra("KYCVER", "2.5");
        this.cordova.setActivityResultCallback(this);
        this.cordova.getActivity().startActivityForResult(intentStartFingerPrintCapture,this.FINGERPRINT_CAPTURE_ACTIVITY);
        } catch(Exception e){
            callbackContext.error("Cannot Open Activity"+e);
        }
    }

    private void registerTheFingerPrintDevice(Context context) throws Exception{
        Intent registerationIntent = new Intent("android.intent.action.SCL_RDSERVICE_OTP_RECIEVER");
        registerationIntent.putExtra("OTP", "SCL-9999-8888-777");
        registerationIntent.setPackage("com.scl.rdservice");
        try{
            this.cordova.getActivity().sendBroadcast(registerationIntent);
        }catch(Exception e){
            e.printStackTrace();
            this.cordova.getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    Toast.makeText(context, "RD Service not installed", Toast.LENGTH_LONG);
                }
            });
        }
    }

    private void showToastForGooglePlay(String messageToSend){
        this.cordova.getActivity().runOnUiThread(new Runnable(){
           public void run(){
            Toast.makeText(context, messageToSend, Toast.LENGTH_LONG);
           }
        });
        this.openPlayStoreRDApp(context);
    }


    private void openPlayStoreRDApp(Context context){
        Intent openPlayStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.scl.rdservice"));
        openPlayStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openPlayStoreIntent);
    }

    private void sendSuccessPIDData(int errorCode, String messageToSend) throws JSONException{
        final JSONObject result = new JSONObject();
        result.put("ErrorCode",errorCode);
        result.put("ErrorMsg",messageToSend);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
        pluginResult.setKeepCallback(true);
        this.callbackContext.sendPluginResult(pluginResult);
    }

    private void sendErrorPluginResult(int errorCode,String messageToSend) throws JSONException {
        final JSONObject result = new JSONObject();
        result.put("ErrorCode",errorCode);
        result.put("ErrorMsg",messageToSend);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, result);
        pluginResult.setKeepCallback(true);
        this.callbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == this.FINGERPRINT_CAPTURE_ACTIVITY){
            if(resultCode != Activity.RESULT_OK){
                if(data.getStringExtra("ERROR_MESSAGE").contains("Device not registered")){
                    this.cordova.getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(context, "wait Registeration Process will Start", Toast.LENGTH_LONG).show();
                        }
                    });
                    try{
                        registerTheFingerPrintDevice(context);
                    } catch(Exception e){
                        try{
                        sendErrorPluginResult(-2,"RD Service not installed");
                        } catch(JSONException jsonException){
                            jsonException.printStackTrace();
                        }
                    }
                } else if(data.getStringExtra("ERROR_MESSAGE").contains("device from google playstore")){
                        showToastForGooglePlay("Please install the Morpho RD service for your device from google play store");
                } else {
                    try{
                    sendErrorPluginResult(-1, data.getStringExtra("ERROR_MESSAGE"));
                    } catch(JSONException jsonException){
                        jsonException.printStackTrace();
                    }
                }
            } else {
                if(data.getStringExtra("PID_DATA") == null){
                    try{
                    sendErrorPluginResult(-4,"Unable to capture fingerprint. pidData is Null");
                    } catch(JSONException jsonException){
                        jsonException.printStackTrace();
                    }
                }
                try{
                sendSuccessPIDData(1,data.getStringExtra("PID_DATA"));
                } catch(JSONException jsonException){
                    jsonException.printStackTrace();
                }
            }
        }
    }

     /**
 * Called when the Activity is being destroyed (e.g. if a plugin calls out to an
 * external Activity and the OS kills the CordovaActivity in the background).
 * The plugin should save its state in this method only if it is awaiting the
 * result of an external Activity and needs to preserve some information so as
 * to handle that result; onRestoreStateForActivityResult() will only be called
 * if the plugin is the recipient of an Activity result
 *
 * @return  Bundle containing the state of the plugin or null if state does not
 *          need to be saved
 */
public Bundle onSaveInstanceState() {
    Bundle state = new Bundle();
    return state;
}

/**
 * Called when a plugin is the recipient of an Activity result after the
 * CordovaActivity has been destroyed. The Bundle will be the same as the one
 * the plugin returned in onSaveInstanceState()
 *
 * @param state             Bundle containing the state of the plugin
 * @param callbackContext   Replacement Context to return the plugin result to
 */
public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
}
}
