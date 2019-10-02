package com.gym.dhc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.Adapter.Receipt_Adapter;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.gym.dhc.model.Receipt_Model;
import com.gym.dhc.utils.ItemAnimation;
import com.gym.dhc.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt extends AppCompatActivity {

    private View parent_view;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    private RecyclerView recyclerView;
    private Receipt_Adapter mAdapter;
    List<Receipt_Model> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
        showDialog();
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);*/
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Receipt List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
//  Tools.setSystemBarColor(this, R.color.colorPrimary);

    }
    private void showDialog() {
        progressDialog = new ProgressDialog(Receipt.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Please wait while loading Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.booked_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        rowListItem=new ArrayList<Receipt_Model>();
        get_ALL_Booked_PackService();

        //showSingleChoiceDialog();
    }
    public void get_ALL_Booked_PackService(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_RECEIPT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getCategory(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String mem_id= sharedPreferences.getString("mem_id",null);
                Log.e("mem_id data",mem_id);

                params.put("mem_id",mem_id);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Receipt.this);
        queue.add(stringRequest);
    }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Receipt_Model(json.getString("id"),
                        json.getString("payment_id"),
                        json.getString("pack_id"),
                        json.getString("pack_name"),
                        json.getString("pack_price"),
                        json.getString("added_date"),
                        json.getString("payment_type"),
                        json.getString("personal_traning"),
                        json.getString("traning_price"),
                        json.getString("total")


                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(rowListItem.size()>0){
            progressDialog.dismiss();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //rowListItem = get_ALL_CATEGORY();
        mAdapter =new Receipt_Adapter(Receipt.this,rowListItem);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new Receipt_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Receipt_Model obj, int position) {

                Intent i = new Intent( Receipt.this, Invoice.class );

                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("pay_id", obj.getId());
                editor.putString("razori_pay_id", obj.getPayment_id());
                editor.putString("pack_id", obj.getPack_id());
                editor.putString("payment_type", obj.getPayment_type());
                editor.commit();

                startActivity( i );
                finish();
            }
        });

        }
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent( Receipt.this, MainActivity.class );
        startActivity( i );
    }

}
