package tutho.com.newsapiapp.http;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * Created by dennis on 2/2/16.
 */
public class RestClient {
    public static final String TAG = "RestClient";
    private static final int READTIMEOUT = 10000;
    private static final int CONNECTTIMEOUT = 15000;
    private static final String GETREQUEST = "GET";

    private static String data;
    private InputStream is = null;

    public static String makeRestRequest(int method, String myUrl, String params) throws Exception {
        InputStream is = null;
        Log.d(TAG,"URL IS "+myUrl);
        URL url;
         if(method == CommonTasks.GET){
            url = new URL(myUrl +"?"+params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setReadTimeout(READTIMEOUT);
                conn.setConnectTimeout(CONNECTTIMEOUT);
                conn.setRequestMethod(GETREQUEST);
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
               // String responseMsg = (String) conn.getErrorStream();
                Log.d(TAG, "The response is: " + response);

                is = conn.getInputStream();

                data = readIt(is);
                Log.d(TAG,data);
            }catch (IOException e){
                is = conn.getErrorStream();
                data = readIt(is);
                Log.d(TAG,data);
                e.printStackTrace();
            }finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return data;
    }

    public static String readIt(InputStream is) {
        StringBuilder sb = null;
        if (is != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        return "error:";
    }
}
