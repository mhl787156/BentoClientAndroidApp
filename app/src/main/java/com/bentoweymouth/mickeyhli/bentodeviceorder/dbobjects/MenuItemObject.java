package com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mickeyhli on 18/10/15.
 */
public class MenuItemObject implements DBObject,Parcelable{
    private int id;
    private String item_id;
    private String item_name;
    private String price;

    public MenuItemObject(){}

    protected MenuItemObject(Parcel in) {
        id = in.readInt();
        item_id = in.readString();
        item_name = in.readString();
        price = in.readString();
    }

    public static final Creator<MenuItemObject> CREATOR = new Creator<MenuItemObject>() {
        @Override
        public MenuItemObject createFromParcel(Parcel in) {
            return new MenuItemObject(in);
        }

        @Override
        public MenuItemObject[] newArray(int size) {
            return new MenuItemObject[size];
        }
    };

    @Override
    public void JsonParser(JSONObject obj) {
        try {
            id = obj.getInt("id");
            item_id = obj.getString("item_id");
            item_name = obj.getString("item_name");
            price = obj.getString("price");

        }catch (JSONException e) {
            // handle exception
            Log.e("JsonObject_MenuObject","Exception has been thrown");
        }
        System.out.println("Finished MenuItem");
    }

    @Override
    public ArrayList<String> toStringArray() {
        return null;
    }

    public int getId() {
        return id;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(item_id);
        dest.writeString(item_name);
        dest.writeString(price);
    }
    public String toString(){
        return "MenuItem> id: "+item_id+" name: "+item_name;
    }
}
