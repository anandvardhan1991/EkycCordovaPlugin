package cordova.plugin.ekyc;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
   
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;


/**
 * This class echoes a string called from JavaScript.
 */
public class EkycCordovaPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        PersonalDetailsModelClass personalDetailsModelClass = new PersonalDetailsModelClass();
        personalDetailsModelClass.setQuotationNumber(args.getJSONObject(0).getString("quotationNumber"));
        personalDetailsModelClass.setProposerName(args.getJSONObject(0).getString("proposerName"));
        personalDetailsModelClass.setAdhaarNumber(args.getJSONObject(0).getString("adhaarNumber"));
        personalDetailsModelClass.setPlanName(args.getJSONObject(0).getString("planName"));
        if (action.equals("otpVerification")) {
            this.otpVerification(personalDetailsModelClass, callbackContext);
            return true;
        } else if(action.equals("irisVerification")) {
            this.irisVerification(personalDetailsModelClass, callbackContext);
            return true;
        } else if(action.equals("fingerPrintVerification")) {
            this.fingerPrintVerification(personalDetailsModelClass, callbackContext);
            return true;
        }
        return false;
    }

    private void otpVerification(PersonalDetailsModelClass personalDetailsModelClass, CallbackContext callbackContext) {
        try{
        callbackContext.success(personalDetailsModelClass.getQuotationNumber());        
        } catch(Exception e){
            callbackContext.error("Something went wrong"+ e);
        }
    }

    private void irisVerification(PersonalDetailsModelClass personalDetailsModelClass, CallbackContext callbackContext) {
        try{
        callbackContext.success(personalDetailsModelClass.getQuotationNumber());
        } catch(Exception e){
            callbackContext.error("Something went wrong"+ e);
        }
    }

    private void fingerPrintVerification(PersonalDetailsModelClass personalDetailsModelClass, CallbackContext callbackContext) {
        try{
        callbackContext.success(personalDetailsModelClass.getQuotationNumber());
        } catch(Exception e){
            callbackContext.error("Something went wrong"+ e);
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
