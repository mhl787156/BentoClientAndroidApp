package com.bentoweymouth.mickeyhli.bentodeviceorder.util;

import android.util.Log;
import com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects.MenuObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mickeyhli on 18/10/15.
 */
public class HTTPRequestProcessor {
    //Contains all the static methods for get, put etc to the server

    public static  Tuple<Map<Integer,String>,Integer> requestAllMenus(){
        String URL = "/menu";
        int greatestkey = 0;
        Map<Integer,String> listOfMenus = new HashMap<>();
        JSONObject serviceResult = RestHTTPClient.tryRequestWebService(URL);

        try {
            JSONArray items = serviceResult.getJSONArray("json_list");

            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                Log.w("JsonObject",i + "  " + obj.toString());

                int id = obj.getInt("id");
                if(id > greatestkey){
                    greatestkey = id;
                }
                String name = obj.getString("menu_name");

                listOfMenus.put(id,name);
                System.out.println(listOfMenus.get(id));

                Log.w("Find All Items", id + "  " + name);
            }

        } catch (JSONException e) {
            // handle exception
            Log.w("FindAllItems","Exception has been thrown");
        }
      return new Tuple(listOfMenus,greatestkey);
    }


    public static MenuObject requestMenuProcess(int i){
        //Parses the Menu Object from the menu Choice 'i'
        //All Menu States are recorded here.

        MenuObject menu = new MenuObject();

        JSONObject serviceResult =
                RestHTTPClient.tryRequestWebService("/menu/" + i);

        try {
            JSONArray items = serviceResult.getJSONArray("json_list");
            for (int k = 0; k < items.length(); k++) {
                JSONObject obj = items.getJSONObject(k);
                Log.w("HTTPRequestProcessor", k + "  " + obj.toString());
                menu.JsonParser(obj);
                Log.w("HTTPRequestProcessor", "The Menu 1: " + menu.toString());
            }

        } catch (JSONException e) {
            // handle exception
            Log.e("HTTPRequestProcessor","Exception has been thrown");
        }

        Log.w("HTTPRequestProcessor","The Menu 2: " + menu.toString());

        return menu;
    }
}
