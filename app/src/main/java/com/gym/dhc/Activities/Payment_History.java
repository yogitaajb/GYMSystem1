package com.gym.dhc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.Adapter.PaymentHistory_Adapter;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.gym.dhc.model.payment_History_model;
import com.gym.dhc.utils.ItemAnimation;
import com.gym.dhc.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Payment_History extends AppCompatActivity {

    RecyclerView recyclerView;
    private PaymentHistory_Adapter mAdapter;
    List<payment_History_model> list_stmt;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    private Toolbar toolbar;
    private int animation_type = ItemAnimation.BOTTOM_UP;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AutoCompleteTextView edit_search;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__history);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        edit_search=(AutoCompleteTextView)findViewById( R.id.et_search);
        initComponent();
        showDialog();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);*/
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        list_stmt=new ArrayList<payment_History_model>();
        getStatementList();

    }
    private void showDialog() {
        progressDialog = new ProgressDialog(Payment_History.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Please wait while loading Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }
    public void getStatementList(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_PaymentHistory_List,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getStatmentList(result);
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

        RequestQueue queue = Volley.newRequestQueue(Payment_History.this);
        queue.add(stringRequest);
    }
    private void getStatmentList(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                list_stmt.add(new payment_History_model(json.getString("id"),
                        json.getString("payment_id"),
                        json.getString("payment_price"),
                        json.getString("pack_id"),
                        json.getString("pack_name"),
                        json.getString("pack_price"),
                        json.getString("payment_datetime"),
                        json.getString("status"),
                        json.getString("payment_type"),
                        json.getString("trainer_name")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
                if(list_stmt.size()>0){
                    progressDialog.dismiss();
                    mAdapter = new PaymentHistory_Adapter(Payment_History.this, list_stmt);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Payment_History.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    // recyclerView.setLayoutManager( new LinearLayoutManager(Activity_Stmt.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    // Capture Text in EditText
                    edit_search.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String text = edit_search.getText().toString().toLowerCase( Locale.getDefault());
                            mAdapter.filter(text);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1,
                                                      int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                            // TODO Auto-generated method stub
                        }
                    });
                }/*else{
                    progressDialog.dismiss();
                }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent( Payment_History.this, MainActivity.class );
        startActivity( i );
    }

}
