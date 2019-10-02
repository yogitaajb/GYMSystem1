package com.gym.dhc.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
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
import java.util.Map;

public class Offline_Payment extends AppCompatActivity {

    EditText et_price,et_trainer;
    Button bt_next;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String pack_id,pack_name,pack_price,pack_desc,pack_duration,personal_traning,coupon_code,traning_price,traning_discount_price;

    private String[] trainer_array;

    ArrayList<String> sub_list=new ArrayList<>();
    private JSONArray result_sub;
    public static final String JSON_ARRAY_sub = "result";
    int traning_price1,traning_discount_price1,pack_price1,include_discount_price,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline__payment);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();

        Intent myIntent = getIntent(); // gets the previously created intent
        pack_id = myIntent.getStringExtra("pack_id");
        pack_name= myIntent.getStringExtra("pack_name");
        pack_price = myIntent.getStringExtra("pack_price");
        pack_desc= myIntent.getStringExtra("pack_desc");
        pack_duration= myIntent.getStringExtra("pack_duration");

        personal_traning= myIntent.getStringExtra("personal_traning");
        coupon_code= myIntent.getStringExtra("coupon_code");
        traning_price= myIntent.getStringExtra("traning_price");
        traning_discount_price= myIntent.getStringExtra("traning_discount_price");

        getTrainerlist();
        et_price=(EditText) findViewById(R.id.et_price);

        if(personal_traning.equalsIgnoreCase("Not Required")){

            traning_price1=0;
            traning_discount_price1=0;
            pack_price1=Integer.parseInt(pack_price);
            include_discount_price=0;
            total=pack_price1;
            et_price.setText("Pack Price - INR " +total );
        }
        else{
            //if (traning_discount_price != null && !traning_discount_price.isEmpty() && !traning_discount_price.equals("null")) {
            traning_price1=Integer.parseInt(traning_price);
            traning_discount_price1=Integer.parseInt(traning_discount_price);
            pack_price1=Integer.parseInt(pack_price);

            include_discount_price=traning_price1-traning_discount_price1;
            total=pack_price1+include_discount_price;
            et_price.setText("  Pack Price - INR  " +pack_price+" \n   Personal Traning Price - INR  " +traning_price+" \n   Discount Price - INR  " +traning_discount_price);
            //}
        }




        et_trainer=(EditText) findViewById(R.id.et_trainer);
        et_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrainerDialog(v);
            }
        });

        bt_next=(Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(Registration1.this,Registration2.class);
                // startActivity(i);
                if(validate()){
                    OfflineStore();
                }

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Offline Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

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

    private void showTrainerDialog(final View v) {
       /* final String[] array = new String[]{
                "Single", "Couple", "Group", "3 Month", "6 Monthly", "Annual"
        };*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Trainers List");
        builder.setSingleChoiceItems(trainer_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(trainer_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean validate() {
        boolean valid = true;

        String price = et_price.getText().toString();
        String trainer = et_trainer.getText().toString();


        if (price.isEmpty()) {
            et_price.setError("Price Not Filled !!");
            valid = false;
        } else {
            et_price.setError(null);
        }



        if (trainer.isEmpty()) {
            et_trainer.setError("Select one Trainer !");
            valid = false;
        } else {
            et_trainer.setError(null);
        }

        return valid;
    }
public void OfflineStore(){

    sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
    sharedPreferences.edit();
    String mem_id= sharedPreferences.getString("mem_id",null);
    String mobile_number= sharedPreferences.getString("mobile_number",null);
     String trainer=et_trainer.getText().toString();
    Log.e("pack duration",pack_duration);
    nameValuePairs.add(new BasicNameValuePair("trainer", trainer));
    nameValuePairs.add(new BasicNameValuePair("pack_id", pack_id));
    nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
    nameValuePairs.add(new BasicNameValuePair("pack_price", pack_price));
    nameValuePairs.add(new BasicNameValuePair("status", "1"));
    nameValuePairs.add(new BasicNameValuePair("pack_duration", pack_duration));

    nameValuePairs.add(new BasicNameValuePair("traning_total", String.valueOf(include_discount_price)));
    nameValuePairs.add(new BasicNameValuePair("total", String.valueOf(total)));
    nameValuePairs.add(new BasicNameValuePair("personal_traning", personal_traning));
    nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));

    try { String data1;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost( MyConfig.URL_OffliePayment_detail);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        //  is = entity.getContent();
        data1 = EntityUtils.toString(entity);
        Log.e("Register", data1);
        if(data1.matches( "success" )) {
            Log.e( "pass 1", "connection success " );

            final AlertDialog.Builder builder = new AlertDialog.Builder(Offline_Payment.this);
            builder.setCancelable(false);
            builder.setMessage("Payment Done Successfully  ");
            //builder.setMessage("Payment Done Successfully by paymentId =  "+razorpayPaymentID);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent( Offline_Payment.this, MainActivity.class );
                    startActivity( i );
                    finish();

                }
            });
            builder.show();

        }if(data1.matches( "failure" )) {
            Log.e( "pass 1", "connection success " );
            final AlertDialog.Builder builder = new AlertDialog.Builder(Offline_Payment.this);
            builder.setCancelable(true);
            builder.setMessage("Payment Failure");

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

    public void getTrainerlist(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_Trainer_list,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result_sub = j.getJSONArray(JSON_ARRAY_sub);
                            getTrainer(result_sub);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
        /*{
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String user_id= sharedPreferences.getString("idtag",null);

                Log.e("user_id",user_id);
                params.put("user_id",user_id);
                return params;
            }
        };*/
        //  RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    private void getTrainer(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                // sub_list.add("Select Subject");
                sub_list.add(json.getString("full_name"));
                trainer_array= sub_list.toArray(new String[sub_list.size()]);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


      /*  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_textcolor,sub_list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textcolor);
        subject_spinner.setAdapter(spinnerArrayAdapter);
        // spin_temp.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItems));
        subject_spinner.setSelection(0);*/

    }



}
