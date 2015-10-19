package com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mickeyhli on 18/10/15.
 */
public interface DBObject {

    void JsonParser(JSONObject obj);

    ArrayList<String> toStringArray();

}
