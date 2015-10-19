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
public class MenuSectionObject implements DBObject,Parcelable {

    private int id;
    private String name;
    private int num_groups;
    private boolean visible;
    private int staggered_service_order;
    private int total_items;
    private ArrayList<MenuItemObject> section_items = new ArrayList<>();
    private ArrayList<MenuSectionObject> subsections = new ArrayList<>();

    public MenuSectionObject(){}

    protected MenuSectionObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        num_groups = in.readInt();
        visible = in.readByte() != 0;
        staggered_service_order = in.readInt();
        total_items = in.readInt();
        section_items = in.createTypedArrayList(MenuItemObject.CREATOR);
        subsections = in.createTypedArrayList(MenuSectionObject.CREATOR);
    }

    public static final Creator<MenuSectionObject> CREATOR = new Creator<MenuSectionObject>() {
        @Override
        public MenuSectionObject createFromParcel(Parcel in) {
            return new MenuSectionObject(in);
        }

        @Override
        public MenuSectionObject[] newArray(int size) {
            return new MenuSectionObject[size];
        }
    };

    @Override
    public void JsonParser(JSONObject obj) {
        //Parses all MenuSection models and adds to the array list
        try {

            id = obj.getInt("id");
            name = obj.getString("section_name");
            num_groups = obj.getInt("number_of_groups");
            total_items = obj.getInt("total_number_of_items");
            //visible = obj.getBoolean("visibility");
            //staggered_service_order = obj.getInt("staggered_service_order");
        }catch(JSONException e) {
            // handle exception
            Log.e("MenuSectionObject","InitException: " + e );
        }
        try {

            //One of these exists
            JSONArray items = obj.getJSONArray("section_items");
            for (int k = 0; items != null
                    && k < items.length(); k++) {
                JSONObject itemObj = items.getJSONObject(k);

                MenuItemObject mio = new MenuItemObject();
                mio.JsonParser(itemObj);
                section_items.add(mio);
            }
        }catch(JSONException e) {
            // handle exception
            Log.w("MenuSectionObject", "Section_itemException: " + e);
        }
        System.out.println("Finished MenuSection - section_items");
        try {

            JSONArray ss = obj.getJSONArray("subsections");
            for (int k = 0; ss != null
                    && k < ss.length(); k++) {
                JSONObject ssObj = ss.getJSONObject(k);

                MenuSectionObject mso = new MenuSectionObject();
                mso.JsonParser(ssObj);
                subsections.add(mso);
            }
        }catch(JSONException e) {
            // handle exception
            Log.w("MenuSectionObject","SubSectionException: " + e );
        }

        System.out.println("Finished MenuSection");
    }

    @Override
    public ArrayList<String> toStringArray() {
        ArrayList<String> child = new ArrayList<>();
        if(!subsections.isEmpty()){
            //Display the first subsection's items
            for(MenuItemObject mi : subsections.get(0).section_items){
                child.add(mi.getItem_name());
            }
        }
        else if(!section_items.isEmpty()){
            for(MenuItemObject mi : section_items){
                child.add(mi.getItem_name());
            }
        }
        return child;
    }

    /*
    @Override
    public ArrayList<String> toStringArray() {
        ArrayList<Object> child = new ArrayList<>();
        if(!subsections.isEmpty()){
            //Display the first subsection's items
            for(MenuItemObject mi : subsections.get(0).section_items){
                child.add(mi.getItem_name());
            }

        }
        else if(!section_items.isEmpty()){
            for(MenuItemObject mi : section_items){
                child.add(mi.getItem_name());
            }
        }
        return child;
    }
    */
    public MenuItemObject getMenuItem(int i){
        if(section_items != null &&i < section_items.size()){
            return section_items.get(i);
        }
        return null;
    }

    public MenuSectionObject getMenuSection(int i){
        if(subsections != null && i < subsections.size()){
            return subsections.get(i);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNum_groups() {
        return num_groups;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getStaggered_service_order() {
        return staggered_service_order;
    }

    public int getTotal_items() {
        return total_items;
    }

    public ArrayList<MenuItemObject> getSection_items() {
        return section_items;
    }

    public ArrayList<MenuSectionObject> getSubsections() {
        return subsections;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(num_groups);
        dest.writeByte((byte) (visible ? 1 : 0));
        dest.writeInt(staggered_service_order);
        dest.writeInt(total_items);
        dest.writeTypedList(section_items);
        dest.writeTypedList(subsections);
    }

    public String toString(){
        return "Menusection> id: "+id+" name: "+name;
    }
}
