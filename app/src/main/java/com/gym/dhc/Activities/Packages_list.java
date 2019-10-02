package com.gym.dhc.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.Adapter.AdapterList;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.gym.dhc.model.Packages_model;
import com.gym.dhc.utils.Tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Packages_list extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterList mAdapter;

    List<Packages_model> listitem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String traning_id,traning_name,traning_price,traning_discount_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_list);

        initToolbar();
        initComponent();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Packages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        listitem=new ArrayList<Packages_model>();
        PackageList();

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


    public void PackageList(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_Package_List,
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

        RequestQueue queue = Volley.newRequestQueue(Packages_list.this);
        queue.add(stringRequest);
    }
    private void getStatmentList(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                listitem.add(new Packages_model(json.getString("id"),
                        json.getString("pack_name"),
                        json.getString("price"),
                        json.getString("description"),
                        json.getString("duration")
                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //set data and list adapter
        mAdapter = new AdapterList(this, listitem);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Packages_model obj, int position) {
                //Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();

                showDialogImageQuotes(obj);

               /* Intent i = new Intent( Packages_list.this, Make_Payment.class );
                i.putExtra("pack_id",obj.getId());
                i.putExtra("pack_name",obj.getPack_name());
                i.putExtra("pack_price",obj.getPrice());
                i.putExtra("pack_desc",obj.getDescription());
                startActivity( i );
                finish();*/

            }
        });
    }
   /* private void showDialogImageQuotes(final Packages_model p) {
        final Dialog dialog = new Dialog(Packages_list.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_pack_desc);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView txt_pack_name = (TextView) dialog.findViewById(R.id.txt_pack_name);
        TextView txt_price = (TextView) dialog.findViewById(R.id.txt_price);
        TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
        TextView txt_duration = (TextView) dialog.findViewById(R.id.txt_duration);
        Button btn_make_payment = (Button) dialog.findViewById(R.id.btn_make_payment);
        Button btn_offline_payment = (Button) dialog.findViewById(R.id.btn_offline_payment);
        EditText et_personal_traning= (EditText) dialog.findViewById(R.id.et_personal_traning);
        final EditText et_coupon_code= (EditText) dialog.findViewById(R.id.et_coupon_code);

        txt_pack_name.setText(p.getPack_name());
        txt_price.setText("Rs. "+p.getPrice());
        txt_description.setText(p.getDescription());
        txt_duration.setText(p.getDuration()+" Days ");

        et_personal_traning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // showPersonalTraningDialog(v);
                final String[] array = new String[]{
                        "Not Required", "Individual","Group Of 3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                builder.setTitle("Personal Traning Type");
                builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ((EditText) v).setText(array[i]);
                        if(array[i].equalsIgnoreCase("Group Of 3"))
                        {
                            et_coupon_code.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            et_coupon_code.setVisibility(View.GONE);
                            et_coupon_code.setText(null);
                        }


                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(p.getDescription().equalsIgnoreCase("Trail 3 Days")){

                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Packages_list.this);
                    alert.setTitle("Status");
                    alert.setMessage("You have registered for 3 days Trial Package. Kindly upgrade package by clicking Upgrade package tab.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    alert.show();

                }else{
                    Intent i = new Intent( Packages_list.this, Make_Payment.class );
                    i.putExtra("pack_id",p.getId());
                    i.putExtra("pack_name",p.getPack_name());
                    i.putExtra("pack_price",p.getPrice());
                    i.putExtra("pack_desc",p.getDescription());
                    i.putExtra("pack_duration",p.getDuration());
                    startActivity( i );
                    finish();
                    dialog.cancel();
                }

            }
        });
        btn_offline_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getDescription().equalsIgnoreCase("Trail 3 Days")){

                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Packages_list.this);
                    alert.setTitle("Status");
                    alert.setMessage("You have registered for 3 days Trial Package. Kindly upgrade package by clicking Upgrade package tab.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    alert.show();
                }else {
                    Intent i = new Intent(Packages_list.this, Offline_Payment.class);
                    i.putExtra("pack_id", p.getId());
                    i.putExtra("pack_name", p.getPack_name());
                    i.putExtra("pack_price", p.getPrice());
                    i.putExtra("pack_desc", p.getDescription());
                    i.putExtra("pack_duration", p.getDuration());
                    startActivity(i);
                    finish();
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }*/

    private void showDialogImageQuotes(final Packages_model p) {
        final Dialog dialog = new Dialog(Packages_list.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_pack_desc);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView txt_pack_name = (TextView) dialog.findViewById(R.id.txt_pack_name);
        TextView txt_price = (TextView) dialog.findViewById(R.id.txt_price);
        TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
        TextView txt_duration = (TextView) dialog.findViewById(R.id.txt_duration);
        Button btn_make_payment = (Button) dialog.findViewById(R.id.btn_make_payment);
        Button btn_offline_payment = (Button) dialog.findViewById(R.id.btn_offline_payment);
        final EditText et_personal_traning= (EditText) dialog.findViewById(R.id.et_personal_traning);
        final EditText et_coupon_code= (EditText) dialog.findViewById(R.id.et_coupon_code);

        txt_pack_name.setText(p.getPack_name());
        txt_price.setText("Rs. "+p.getPrice());
        txt_description.setText(p.getDescription());
        txt_duration.setText(p.getDuration()+" Days ");

        et_personal_traning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // showPersonalTraningDialog(v);
                final String[] array = new String[]{
                        "Not Required", "Individual","Group Of 3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                builder.setTitle("Personal Traning Type");
                builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ((EditText) v).setText(array[i]);

                        String data;

                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost( MyConfig.URL_Personal_Traning);
                            nameValuePairs.add(new BasicNameValuePair("personal_traning_type", array[i]));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            data = EntityUtils.toString(entity);
                            Log.e("Check Data Main Profile", data);
                            try
                            {
                                JSONArray json = new JSONArray(data);
                                for (int i1 = 0; i1 < json.length(); i1++)
                                {

                                    JSONObject obj = json.getJSONObject(i1);
                                    traning_id = String.valueOf( obj.getInt("id") );
                                    traning_name = obj.getString("name");
                                    traning_price = obj.getString("price");
                                    traning_discount_price = obj.getString("discount_amount");


                                    Log.e("coupon data",traning_id+"="+traning_price+"="+traning_name+"="+traning_discount_price);


                                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("traning_id", traning_id);
                                    editor.putString("traning_name", traning_name);
                                    editor.putString("traning_price", traning_price);
                                    editor.putString("traning_discount_price", traning_discount_price);
                                    editor.commit();


                                }

                            }
                            catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } catch (ClientProtocolException e) {
                            Log.e("Fail 1", e.toString());
                            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(array[i].equalsIgnoreCase("Group Of 3"))
                        {
                            et_coupon_code.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            et_coupon_code.setVisibility(View.GONE);
                            et_coupon_code.setText(null);
                        }


                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getDescription().equalsIgnoreCase("Trail 3 Days")){

                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Packages_list.this);
                    alert.setTitle("Status");
                    alert.setMessage("You have registered for 3 days Trial Package. Kindly upgrade package by clicking Upgrade package tab.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    alert.show();

                }else {
                    String coupon_code= et_coupon_code.getText().toString();
                    String personal_traning= et_personal_traning.getText().toString();
                    if (personal_traning != null && !personal_traning.isEmpty() && !personal_traning.equals("null")) {
                        if (coupon_code != null && !coupon_code.isEmpty() && !coupon_code.equals("null")) {
                            String data;
                            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                            sharedPreferences.edit();
                            String mem_id= sharedPreferences.getString("mem_id",null);

                            nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));
                            nameValuePairs.add(new BasicNameValuePair("mem_id",mem_id));


                            try {
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost( MyConfig.URL_Match_Coupon);
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity entity = response.getEntity();
                                //  is = entity.getContent();
                                data = EntityUtils.toString(entity).trim();
                                Log.e("Register", data);
                                if(data.matches( "success" )) {
                                    Log.e( "pass 1", "connection success " );
                                    Intent i = new Intent(Packages_list.this, Make_Payment.class);
                                    i.putExtra("pack_id", p.getId());
                                    i.putExtra("pack_name", p.getPack_name());
                                    i.putExtra("pack_price", p.getPrice());
                                    i.putExtra("pack_desc", p.getDescription());
                                    i.putExtra("pack_duration", p.getDuration());
                                    i.putExtra("personal_traning", et_personal_traning.getText().toString());
                                    i.putExtra("coupon_code", et_coupon_code.getText().toString());
                                    i.putExtra("traning_price", traning_price);
                                    i.putExtra("traning_discount_price", traning_discount_price);
                                    startActivity(i);
                                    finish();
                                    dialog.cancel();
                                }if(data.matches( "failure" )) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                                    builder.setCancelable(false);
                                    builder.setMessage("Sorry ,Entered Coupon code is not Valid Coupon or Already Used by 3 members !!!");

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else{

                            if(personal_traning.equalsIgnoreCase("Group Of 3")){
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                                builder.setCancelable(false);
                                builder.setMessage("Enter Coupon Code First then Continue !!!");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }else {
                                Intent i = new Intent(Packages_list.this, Make_Payment.class);
                                i.putExtra("pack_id", p.getId());
                                i.putExtra("pack_name", p.getPack_name());
                                i.putExtra("pack_price", p.getPrice());
                                i.putExtra("pack_desc", p.getDescription());
                                i.putExtra("pack_duration", p.getDuration());
                                i.putExtra("personal_traning", et_personal_traning.getText().toString());
                                i.putExtra("coupon_code", et_coupon_code.getText().toString());
                                i.putExtra("traning_price", traning_price);
                                i.putExtra("traning_discount_price", traning_discount_price);
                                startActivity(i);
                                finish();
                                dialog.cancel();
                            }
                        }
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                        builder.setCancelable(false);
                        builder.setMessage("Select at least on option from Personal Traning dropdown !!!");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }

                }
            }
        });
        btn_offline_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getDescription().equalsIgnoreCase("Trail 3 Days")){

                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Packages_list.this);
                    alert.setTitle("Status");
                    alert.setMessage("You have registered for 3 days Trial Package. Kindly upgrade package by clicking Upgrade package tab.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    alert.show();

                }else {
                    String coupon_code= et_coupon_code.getText().toString();
                    String personal_traning= et_personal_traning.getText().toString();
                    if (personal_traning != null && !personal_traning.isEmpty() && !personal_traning.equals("null")) {
                    if (coupon_code != null && !coupon_code.isEmpty() && !coupon_code.equals("null")) {
                        String data;
                        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                        sharedPreferences.edit();
                        String mem_id= sharedPreferences.getString("mem_id",null);

                        nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));
                        nameValuePairs.add(new BasicNameValuePair("mem_id",mem_id));


                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost( MyConfig.URL_Match_Coupon);
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            //  is = entity.getContent();
                            data = EntityUtils.toString(entity).trim();
                            Log.e("Register", data);
                            if(data.matches( "success" )) {
                                Log.e( "pass 1", "connection success " );


                                Intent i = new Intent(Packages_list.this, Offline_Payment.class);
                                i.putExtra("pack_id", p.getId());
                                i.putExtra("pack_name", p.getPack_name());
                                i.putExtra("pack_price", p.getPrice());
                                i.putExtra("pack_desc", p.getDescription());
                                i.putExtra("pack_duration", p.getDuration());
                                i.putExtra("personal_traning",personal_traning );
                                i.putExtra("coupon_code", coupon_code);
                                i.putExtra("traning_price", traning_price);
                                i.putExtra("traning_discount_price", traning_discount_price);
                                startActivity(i);
                                finish();
                                dialog.cancel();

                            }if(data.matches( "failure" )) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                                builder.setCancelable(false);
                                builder.setMessage("Sorry ,Entered Coupon code is not Valid Coupon or Already Used by 3 members !!!");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        } catch (ClientProtocolException e) {
                            Log.e("Fail 1", e.toString());
                            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else{
                        if(personal_traning.equalsIgnoreCase("Group Of 3")){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                            builder.setCancelable(false);
                            builder.setMessage("Enter Coupon Code First then Continue !!!");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }else {
                            Intent i = new Intent(Packages_list.this, Offline_Payment.class);
                            i.putExtra("pack_id", p.getId());
                            i.putExtra("pack_name", p.getPack_name());
                            i.putExtra("pack_price", p.getPrice());
                            i.putExtra("pack_desc", p.getDescription());
                            i.putExtra("pack_duration", p.getDuration());
                            i.putExtra("personal_traning", personal_traning);
                            i.putExtra("coupon_code", coupon_code);
                            i.putExtra("traning_price", traning_price);
                            i.putExtra("traning_discount_price", traning_discount_price);
                            startActivity(i);
                            finish();
                            dialog.cancel();
                        }
                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Packages_list.this);
                    builder.setCancelable(false);
                    builder.setMessage("Select at least on option from Personal Traning dropdown !!!");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }

                }
            }
        });
        dialog.show();
    }
}
