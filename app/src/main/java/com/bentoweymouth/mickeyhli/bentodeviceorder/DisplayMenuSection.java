package com.bentoweymouth.mickeyhli.bentodeviceorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects.MenuObject;
import com.bentoweymouth.mickeyhli.bentodeviceorder.dbobjects.MenuSectionObject;
import com.bentoweymouth.mickeyhli.bentodeviceorder.util.HTTPRequestProcessor;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayMenuSection extends AppCompatActivity {
    private MenuObject menu;
    private MenuSectionObject menuSection;
    private ArrayList<String> list;
    private int section_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            section_id = extras.getInt("section_id");
            menu = extras.getParcelable("menu");
            Log.w("DisplayMenu", "Section_id set to: " + section_id);
        }else{
            Log.e("DisplayMenu","Error, no extras");
        }

        new RequestItemsServiceTask().execute();

        ListView sections = (ListView) findViewById(R.id.menu_Item_choice);
        sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("Choose Section",
                        String.format("Chosen: %d Section, SectionName: %s",
                                position + 1, list.get(position)));

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
        savedInstanceState.putParcelable("Section",menuSection);
        savedInstanceState.putStringArrayList("List",list);
        savedInstanceState.putInt("menu_id",section_id);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        menu = savedInstanceState.getParcelable("Menu");
        menuSection = savedInstanceState.getParcelable("Section");
        list = savedInstanceState.getStringArrayList("List");
        section_id = savedInstanceState.getInt("menu_id");
    }

    private class RequestItemsServiceTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog =
                new ProgressDialog(DisplayMenuSection.this);


        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {

            for(MenuSectionObject ms : menu.getMenu_sections()){
                System.out.println(ms);
            }

            menuSection = menu.getMenuSection(section_id);
            if(menuSection != null) {
                list = menuSection.toStringArray();
            }else
            {
                list = new ArrayList<>();
                list.add("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(menuSection == null || list == null){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                return;
            }
            //Display Menu here
            //Also Create name

            TextView txt = (TextView) findViewById(R.id.Menu_Item_title);
            txt.setText(menuSection.getName());

            ListView menu = (ListView) findViewById(R.id.menu_Item_choice);

            menu.setAdapter(
                    new ArrayAdapter<>(DisplayMenuSection.this,
                            R.layout.support_simple_spinner_dropdown_item,
                            list));

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
