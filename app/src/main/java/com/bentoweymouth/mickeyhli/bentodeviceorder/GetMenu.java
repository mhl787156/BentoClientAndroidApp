package com.bentoweymouth.mickeyhli.bentodeviceorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bentoweymouth.mickeyhli.bentodeviceorder.util.HTTPRequestProcessor;
import com.bentoweymouth.mickeyhli.bentodeviceorder.util.Tuple;

import java.util.Map;

public class GetMenu extends AppCompatActivity {

    private Map<Integer,String> listOfMenus;
    private int numberOfMenus;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_menu);

        new RequestItemsServiceTask().execute();

        ListView menus = (ListView) findViewById(R.id.DisplayCurrentMenus);
        menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("Choose Menu",
                        String.format("Position: %d,id: %d,menu name:", position, id, listOfMenus.get(position - 1)));


                Intent intent = new Intent(GetMenu.this, DisplayMenu.class);
                intent.putExtra("menu_id", position);
                Log.w("GetMenu", "Put menu_id into intent with value: " + position);
                startActivity(intent);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private class RequestItemsServiceTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog =
                new ProgressDialog(GetMenu.this);


        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {
            Tuple<Map<Integer,String>,Integer> ms = null;

            try {
                ms = HTTPRequestProcessor.requestAllMenus();

                listOfMenus = ms.getFirst();
                numberOfMenus = ms.getSecond();

                list = new String[numberOfMenus + 1];
                for(int i = 0; i < list.length;i++){
                    String x = listOfMenus.get(i);
                    list[i] = (x == null) ? "": x;
                    System.out.println(list[i]);
                }
            } catch (Throwable e) {
                    listOfMenus = null;
                    numberOfMenus = 0;
                    list = null;
            }


            if(ms == null){
                Log.e("AsyncTaskBackground","ms is null");
            }
            if(listOfMenus == null){
                Log.e("AsyncTaskBackground","listOfMenus is null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(listOfMenus == null || list == null) {
                Log.w("GetMenu","On post execute failed");
                return;
            }

            ListView menus = (ListView) findViewById(R.id.DisplayCurrentMenus);
            menus.setAdapter(
                    new ArrayAdapter<>(GetMenu.this,
                            R.layout.support_simple_spinner_dropdown_item,
                            list));

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }



}
