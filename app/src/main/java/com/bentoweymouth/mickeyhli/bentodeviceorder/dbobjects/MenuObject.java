package com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mickeyhli on 18/10/15.
 */
public class MenuObject implements DBObject, Parcelable {

    private int id;
    private String name;
    private String date = "";
    private int total_sections = 0;
    private int total_numbers = 0;
    private ArrayList<MenuSectionObject> menu_sections = new ArrayList<>();

    public MenuObject(){}

    protected MenuObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        date = in.readString();
        total_sections = in.readInt();
        total_numbers = in.readInt();
        menu_sections = in.createTypedArrayList(MenuSectionObject.CREATOR);
    }

    public static final Creator<MenuObject> CREATOR = new Creator<MenuObject>() {
        @Override
        public MenuObject createFromParcel(Parcel in) {
            return new MenuObject(in);
        }

        @Override
        public MenuObject[] newArray(int size) {
            return new MenuObject[size];
        }
    };

    @Override
    public void JsonParser(JSONObject obj) {
        try {

            id = obj.getInt("id");
            name = obj.getString("menu_name");
            date = obj.getString("last_date_changed");
            total_sections = obj.getInt("total_number_of_sections");
            total_numbers = obj.getInt("total_number_of_items");

            JSONArray sections = obj.getJSONArray("menu_sections");
            for (int k = 0; k < sections.length(); k++) {
                JSONObject sectionobj = sections.getJSONObject(k);
                MenuSectionObject mso = new MenuSectionObject();
                mso.JsonParser(sectionobj);
                menu_sections.add(mso);
            }
            System.out.println("Finished Menu");

        }catch (JSONException e) {
            // handle exception
            Log.w("HTTPRequest_MenuProcess","Exception has been thrown");
        }
    }

    @Override
    public ArrayList<String> toStringArray() {
        ArrayList<String> header = new ArrayList<>();
        for(MenuSectionObject ms : menu_sections){
            header.add(ms.getName());
        }
        return header;
    }

    /*
    public Tuple<ArrayList<String>,ArrayList<Object>> toArrayAdapterFormat(){
        ArrayList<String> header = new ArrayList<>();
        for(MenuSectionObject ms : menu_sections){
            header.add(ms.getName());
        }
        return new Tuple<>(header,toStringArray());
    }
    */
    /*
    @Override
    public ArrayList<Object> toStringArray() {
        ArrayList<Object> childItem = new ArrayList<>();
        for (MenuSectionObject ms : menu_sections) {
            childItem.add(ms.toStringArray());
        }
        return childItem;
    }
    */

    public MenuSectionObject getMenuSection(int i){
        if(i < menu_sections.size()){
            return menu_sections.get(i);
        }
        return null;
    }

    public ArrayList<MenuSectionObject> getMenu_sections(){
        return menu_sections;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getTotal_sections() {
        return total_sections;
    }

    public String getDate() {
        return date;
    }

    public int getTotal_numbers() {
        return total_numbers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeInt(total_sections);
        dest.writeInt(total_numbers);
        dest.writeTypedList(menu_sections);
    }

    public String toString(){
        return "Menu> id: "+id+" name: "+name;
    }
}
