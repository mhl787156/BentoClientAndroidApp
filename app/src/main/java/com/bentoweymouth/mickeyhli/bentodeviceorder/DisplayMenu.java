package com.bentoweymouth.mickeyhli.bentodeviceorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects.MenuObject;
import com.bentoweymouth.mickeyhli.bentodeviceorder.util.HTTPRequestProcessor;

import java.util.ArrayList;

public class DisplayMenu extends AppCompatActivity {
    private static MenuObject menu;
    private static ArrayList<String> list;
    private static int menu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            menu_id = extras.getInt("menu_id");
            Log.w("DisplayMenu","menu_id set to: " + menu_id);
            new RequestItemsServiceTask().execute();
        } else {
            Log.e("DisplayMenu","Error, no extras");
        }

        //Only execute Http get if either menu or list iis null
        /*
        if(menu == null || list == null) {
            new RequestItemsServiceTask().execute();
        }
        */

        ListView menus = (ListView) findViewById(R.id.DisplayMenuSections);
        menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("Choose Menu",
                        String.format("Chosen: %d Section, SectionName: %s",
                                position + 1, list.get(position)));

                Intent intent = new Intent(DisplayMenu.this,
                        DisplayMenuSection.class);
                intent.putExtra("section_id", position+1);
                intent.putExtra("menu",menu);
                Log.w("DisplayMenu",
                        "Put section_id into intent with value: " + position+1);
                startActivity(intent);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelable("Menu",menu);
        savedInstanceState.putStringArrayList("List",list);
        savedInstanceState.putInt("menu_id",menu_id);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        menu = savedInstanceState.getParcelable("Menu");
        list = savedInstanceState.getStringArrayList("List");
        menu_id = savedInstanceState.getInt("menu_id");
    }

    private class RequestItemsServiceTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog =
                new ProgressDialog(DisplayMenu.this);


        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {

            try {
                menu = HTTPRequestProcessor.requestMenuProcess(menu_id);
            } catch (Throwable e) {
                // handle exceptions
            }

            list = (menu==null)
                    ?new ArrayList<String>()
                    :menu.toStringArray();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            //Display Menu here
            //Also Create name
            if(menu!=null) {
                TextView txt = (TextView) findViewById(R.id.Menu_Name);
                txt.setText(menu.getName());
            }

            ListView menu = (ListView) findViewById(R.id.DisplayMenuSections);

            menu.setAdapter(
                    new ArrayAdapter<>(DisplayMenu.this,
                            R.layout.support_simple_spinner_dropdown_item,
                            list));


            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
