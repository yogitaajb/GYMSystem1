package com.gym.dhc.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Make_Payment extends AppCompatActivity implements PaymentResultListener  {//PaymentResultWithDataListener
    private static final String TAG = Make_Payment.class.getSimpleName();
    String pack_id,pack_name,pack_price,pack_desc,pack_duration,personal_traning,coupon_code,traning_price,traning_discount_price;
    TextView txt_pname,txt_price,txt_description;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int traning_price1,traning_discount_price1,pack_price1,include_discount_price,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__payment);

        Checkout.preload(getApplicationContext());

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



        // Payment button created by you in XML layout
        txt_description= (TextView) findViewById(R.id.description);
        txt_pname = (TextView) findViewById(R.id.txt_pname);
        txt_price = (TextView) findViewById(R.id.txt_price);
        Button button = (Button) findViewById(R.id.btn_pay);

        if(personal_traning.equalsIgnoreCase("Not Required")){

            traning_price1=0;
            traning_discount_price1=0;
            pack_price1=Integer.parseInt(pack_price);
            include_discount_price=0;
            total=pack_price1;
            txt_price.setText("Pack Price - INR " +total );
        }
        else{
            //if (traning_discount_price != null && !traning_discount_price.isEmpty() && !traning_discount_price.equals("null")) {
            traning_price1=Integer.parseInt(traning_price);
            traning_discount_price1=Integer.parseInt(traning_discount_price);
            pack_price1=Integer.parseInt(pack_price);

            include_discount_price=traning_price1-traning_discount_price1;
            total=pack_price1+include_discount_price;
            txt_price.setText("  Pack Price - INR  " +pack_price+" \n   Personal Traning Price - INR  " +traning_price+" \n   Discount Price - INR  " +traning_discount_price);
            //}
        }


        txt_pname.setText(pack_name);
      //  txt_price.setText("INR  " +pack_price);
        txt_description.setText(pack_desc);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
       //int pack_price1= Integer.parseInt(pack_price);
        int pack_price1= total;
        int pack_price2 = 100*pack_price1;
        String pack_price3= String.valueOf(pack_price2);
        Log.e("Price",total+"//"+pack_price1+"//"+pack_price2+"//"+pack_price3);

        try {
            JSONObject options = new JSONObject();
            options.put("name", pack_name);
            options.put("description", pack_desc);
            //options.put("order_id", "6767676");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.dhc_logo);
            options.put("currency", "INR");
            options.put("amount", pack_price3);
            //

            JSONObject preFill = new JSONObject();
           // preFill.put("email", "test@razorpay.com");
           // preFill.put("contact", "9876543210");

            //options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {//, PaymentData data
        try {
           // Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            /*String paymentId = data.getPaymentId();
            String signature = data.getSignature();
            String orderId = data.getOrderId();*/

            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
            sharedPreferences.edit();
            String mem_id= sharedPreferences.getString("mem_id",null);
            String mobile_number= sharedPreferences.getString("mobile_number",null);
Log.e("pack duration",pack_duration);

            nameValuePairs.add(new BasicNameValuePair("payment_id", razorpayPaymentID));
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
                HttpPost httppost = new HttpPost( MyConfig.URL_Payment_detail);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                //  is = entity.getContent();
                data1 = EntityUtils.toString(entity);
                Log.e("Register", data1);
                if(data1.matches( "success" )) {
                    Log.e( "pass 1", "connection success " );

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Make_Payment.this);
                    builder.setCancelable(true);
                    builder.setMessage("Payment Done Successfully by paymentId =  "+razorpayPaymentID);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent( Make_Payment.this, MainActivity.class );
                            startActivity( i );
                            finish();

                        }
                    });
                    builder.show();

                }if(data1.matches( "failure" )) {
                    Log.e( "pass 1", "connection success " );
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Make_Payment.this);
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


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {//, PaymentData data
        try {
           /* Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder builder = new AlertDialog.Builder(Make_Payment.this);
            builder.setCancelable(true);
            builder.setMessage("Payment Failure");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();*/

            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
            sharedPreferences.edit();
            String mem_id= sharedPreferences.getString("mem_id",null);
            String mobile_number= sharedPreferences.getString("mobile_number",null);
            Log.e("pack duration",pack_duration);

            nameValuePairs.add(new BasicNameValuePair("payment_id", "response"));
            nameValuePairs.add(new BasicNameValuePair("pack_id", pack_id));
            nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
            nameValuePairs.add(new BasicNameValuePair("pack_price", pack_price));
            nameValuePairs.add(new BasicNameValuePair("status", "0"));
            nameValuePairs.add(new BasicNameValuePair("pack_duration", pack_duration));
            nameValuePairs.add(new BasicNameValuePair("traning_total", String.valueOf(include_discount_price)));
            nameValuePairs.add(new BasicNameValuePair("total", String.valueOf(total)));
            nameValuePairs.add(new BasicNameValuePair("personal_traning", personal_traning));
            nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));

            try { String data1;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost( MyConfig.URL_Payment_detail);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response1 = httpclient.execute(httppost);
                HttpEntity entity = response1.getEntity();
                //  is = entity.getContent();
                data1 = EntityUtils.toString(entity).trim();
                Log.e("Register", data1);
                if(data1.matches( "success" )) {
                    Log.e( "pass 1", "connection success " );

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Make_Payment.this);
                    builder.setCancelable(true);
                    builder.setMessage("Payment failure "+response );//+"//"+code

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent( Make_Payment.this, MainActivity.class );
                            startActivity( i );
                            finish();

                        }
                    });
                    builder.show();

                }if(data1.matches( "failure" )) {
                    Log.e( "pass 1", "connection success " );
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Make_Payment.this);
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


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


}
