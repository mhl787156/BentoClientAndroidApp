package com.bentoweymouth.mickeyhli.bentodeviceorder.util;

import android.os.Build;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mickeyhli on 17/10/15.
 */
public class RestHTTPClient {
    private static final int CONNECTION_TIMEOUT = 100;
    private static final int DATARETRIEVAL_TIMEOUT = 100;
    private static final String ROOTURL = "http://192.168.0.5:5000/api";

    public static JSONObject tryRequestWebService(String serviceUrl){
        JSONObject o = null;
        for(int i = 0; o == null && i < 3 ; i++) {
            o = requestWebService(serviceUrl);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    public static JSONObject requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();
        serviceUrl = ROOTURL + serviceUrl;
        Log.w("HTTPClient","Attempting to connect to: "+serviceUrl);

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.e("HTTPClient","Malformed URL:" + e);
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.e("HTTPClient","Connection time out:" + e);
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.e("HTTPClient","Input stream not opened:" + e);
        } catch (JSONException e) {
            // response body is no valid JSON string
            Log.e("HTTPClient","JSON exception:" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }




}
