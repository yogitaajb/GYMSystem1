package com.gym.dhc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import com.gym.dhc.utils.ViewAnimation;

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
import java.util.Random;

public class Invoice extends AppCompatActivity {

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ImageButton bt_toggle_items, bt_toggle_address, bt_toggle_description,bt_toggle_ptraning;
    private View lyt_expand_items, lyt_expand_address, lyt_expand_description,lyt_expand_ptraning;
    private NestedScrollView nested_scroll_view;
     TextView top_total_price,tv_payment_id,payment_datetime,pack_name,pack_price,pack_description,user_name,
             mobile_number,user_weight,email,address,txt_type_note,txt_startdate,txt_expirydate,
             txt_traning_price,txt_coupon_code,ptraning_price,ptraning;


    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initToolbar();
        initComponent();
       // Get_Payment_Detail();
        InvoiceList();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
    }

    private void initComponent() {

        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        // section items
        bt_toggle_items = (ImageButton) findViewById(R.id.bt_toggle_items);
        lyt_expand_items = (View) findViewById(R.id.lyt_expand_items);
        bt_toggle_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_items);
            }
        });

        // section address
        bt_toggle_address = (ImageButton) findViewById(R.id.bt_toggle_address);
        lyt_expand_address = (View) findViewById(R.id.lyt_expand_address);
        bt_toggle_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_address);
            }
        });

        // section description
        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });
        // section Traning
        bt_toggle_ptraning = (ImageButton) findViewById(R.id.bt_toggle_ptraning);
        lyt_expand_ptraning = (View) findViewById(R.id.lyt_expand_ptraning);
        bt_toggle_ptraning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_ptraning);
            }
        });
        // copy to clipboard
        tv_payment_id = (TextView) findViewById(R.id.tv_payment_id);
        top_total_price = (TextView) findViewById(R.id.top_total_price);
        payment_datetime = (TextView) findViewById(R.id.payment_datetime);
        pack_name = (TextView) findViewById(R.id.pack_name);
        txt_type_note = (TextView) findViewById(R.id.txt_type_note);
        pack_price = (TextView) findViewById(R.id.pack_price);
        user_name = (TextView) findViewById(R.id.user_name);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        user_weight = (TextView) findViewById(R.id.user_weight);
        pack_description = (TextView) findViewById(R.id.pack_description);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        txt_startdate= (TextView) findViewById(R.id.txt_startdate);
        txt_expirydate= (TextView) findViewById(R.id.txt_expirydate);
        txt_traning_price = (TextView) findViewById(R.id.txt_traning_price);
        txt_coupon_code = (TextView) findViewById(R.id.txt_coupon_code);
        ptraning_price= (TextView) findViewById(R.id.ptraning_price);
        ptraning= (TextView) findViewById(R.id.ptraning);

        ImageButton bt_copy_code = (ImageButton) findViewById(R.id.bt_copy_code);
        bt_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.copyToClipboard(getApplicationContext(), tv_payment_id.getText().toString());
            }
        });

    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent( Invoice.this, Receipt.class );
            startActivity( i );
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent( Invoice.this, Receipt.class );
        startActivity( i );
        finish();
    }


    private void Get_Payment_Detail() { String data;

        try {
            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
            sharedPreferences.edit();
            String mem_id= sharedPreferences.getString("mem_id",null);
            String pay_id= sharedPreferences.getString("pay_id",null);
            String razori_pay_id= sharedPreferences.getString("razori_pay_id",null);
            String pack_id= sharedPreferences.getString("pack_id",null);

           // Log.e("shared data",user_name);
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
            HttpPost httppost = new HttpPost( MyConfig.URL_INVOICE);
            nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
            nameValuePairs.add(new BasicNameValuePair("pay_id", pay_id));
            nameValuePairs.add(new BasicNameValuePair("razori_pay_id", razori_pay_id));
            nameValuePairs.add(new BasicNameValuePair("pack_id", pack_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);
            Log.e("Invoice", data);
            try
            {
                JSONArray json = new JSONArray(data);
                for (int i = 0; i < json.length(); i++)
                {

                    JSONObject obj = json.getJSONObject(i);
                   String payment_id1 = String.valueOf( obj.getInt("payment_id") );
                    String pack_id1 = obj.getString("pack_id");
                    String pack_name1 = obj.getString("pack_name");
                    String description1 = obj.getString("description");
                    //password = obj.getString("password");
                    String pack_price1 = obj.getString("pack_price");
                    String payment_datetime1 = obj.getString("payment_datetime");
                    String surname = obj.getString("surname");
                    String firstname=obj.getString("firstname");
                    String mobile_number1 = obj.getString("mobile_number");
                    String address1 = obj.getString("address1");
                    String address2 = obj.getString("address2");
                    String email1 = obj.getString("email");
                    String weight1 = obj.getString("weight");

                    Log.e("profile data",mem_id+"="+surname+"="+firstname+"="+email+"="+mobile_number);

                    top_total_price.setText(pack_price1);
                    tv_payment_id.setText(payment_id1);
                    payment_datetime.setText(payment_datetime1);
                    pack_name.setText(pack_name1);
                    pack_price.setText(pack_price1);
                    pack_description.setText(description1);
                    user_name.setText(firstname+" "+surname);
                    mobile_number.setText(mobile_number1);
                    user_weight.setText(weight1);
                    email.setText(email1);
                    address.setText(address1+"  "+address2);

                   // top_total_price.setText();




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

    }

    public void InvoiceList(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_INVOICE,
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
                String pay_id= sharedPreferences.getString("pay_id",null);
                String razori_pay_id= sharedPreferences.getString("razori_pay_id",null);
                String pack_id= sharedPreferences.getString("pack_id",null);

                params.put("mem_id",mem_id);
                params.put("pay_id",pay_id);
                params.put("razori_pay_id",razori_pay_id);
                params.put("pack_id",pack_id);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Invoice.this);
        queue.add(stringRequest);
    }
    private void getStatmentList(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
               // JSONObject json = j.getJSONObject(i);
                JSONObject obj = j.getJSONObject(i);
                String payment_id1 = String.valueOf( obj.getString("payment_id") );
                String pack_id1 = obj.getString("pack_id");
                String pack_name1 = obj.getString("pack_name");
                String description1 = obj.getString("description");
                //password = obj.getString("password");
                String pack_price1 = obj.getString("pack_price");
                String payment_datetime1 = obj.getString("payment_datetime");
                String surname = obj.getString("surname");
                String firstname=obj.getString("firstname");
                String mobile_number1 = obj.getString("mobile_number");
                String address1 = obj.getString("address1");
                String address2 = obj.getString("address2");
                String email1 = obj.getString("email");
                String membership_type = obj.getString("membership_type");
                String start_date = obj.getString("start_date");
                String expiry_date = obj.getString("expiry_date");
                String admin_email=obj.getString("admin_email");

                String personal_traning=obj.getString("personal_traning");
                String personal_traning_price=obj.getString("personal_traning_price");
                String coupon_code=obj.getString("coupon_code");
                String traning_price=obj.getString("traning_price");
                String total=obj.getString("total");


                Log.e("profile data",surname+"="+firstname+"="+email+"="+mobile_number);

                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String payment_type= sharedPreferences.getString("payment_type",null);
                if (payment_type.equalsIgnoreCase("1"))
                {
                    tv_payment_id.setText("Cash Payment");
                   // txt_type_note.setVisibility(View.INVISIBLE);
                    txt_type_note.setText("Firm EmailId : "+admin_email);
                }else if (payment_type.equalsIgnoreCase("0")){
                    tv_payment_id.setText(payment_id1);
                    txt_type_note.setText("\nRazori Payment ID : "+"\n\n"+ "Firm EmailId : "+admin_email);
                }


                top_total_price.setText("INR "+total);

                payment_datetime.setText(payment_datetime1);
                pack_name.setText(pack_name1);
                pack_price.setText("RS. "+pack_price1);
                pack_description.setText(description1);
                user_name.setText(firstname+" "+surname);
                mobile_number.setText(mobile_number1);
                user_weight.setText("Membership Type : "+membership_type);
                email.setText("Email : "+email1);
                address.setText(address1+"  "+address2);
                txt_startdate.setText(start_date);
                txt_expirydate.setText(expiry_date);
                if (traning_price != null && !traning_price.isEmpty() && !traning_price.equals("null")) {
                    txt_traning_price.setText("RS. " + traning_price);
                }else{
                    txt_traning_price.setText("RS. " + "0");
                }
                txt_coupon_code.setText(coupon_code);
                ptraning_price.setText("RS. " +personal_traning_price);
                ptraning.setText(personal_traning);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
