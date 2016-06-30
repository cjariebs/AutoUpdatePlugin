package org.apache.cordova.plugin.AutoUpdatePlugin;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataOutputStream;

import android.util.Log;

public class AutoUpdatePlugin extends CordovaPlugin {
    // Constructor
    public AutoUpdatePlugin() {}

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("updateApp")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    updateApp(callbackContext);
                }
            });
            return true;
        }
       
        return false;
        
    }

    ////

    private void updateApp(CallbackContext callbackContext) {
        final String libs = "LD_LIBRARY_PATH=/vendor/lib:/system/lib ";
        final String[] commands = {
            libs + "pm install -r /sdcard/Download/android-debug.apk",
            libs + "am start -n com.posthaste.cod.mobileclient/.MainActivity"
        };
        Log.d("Posthaste Autoupdate", "ATTEMPTING AUTOUPDATE");
        su_exec(commands);
    }

    private void su_exec(String[] commands) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            InputStream errStream = process.getErrorStream();
            DataOutputStream outStream = new DataOutputStream(process.getOutputStream());

            for (String command : commands) {
                outStream.writeBytes(command + "\n");
            }

            outStream.writeBytes("exit\n");
            outStream.flush();

            int read;
            byte[] buffer = new byte[4096];
            String output = new String();
            while ((read = errStream.read(buffer)) > 0) {
                output += new String(buffer, 0, read);
            }

            process.waitFor();
        } catch (IOException e) {
            Log.e("Posthaste Autoupdate", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("Posthaste Autoupdate", e.getMessage());
        }
    }
}

