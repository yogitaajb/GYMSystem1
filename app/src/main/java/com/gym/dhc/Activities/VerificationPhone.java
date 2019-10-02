package com.gym.dhc.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
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
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationPhone extends AppCompatActivity {

    AppCompatEditText phone_number;
    AppCompatButton btn_continue,other_time;
    ProgressDialog progressDialog;
    Dialog dialog;
    private com.android.volley.RequestQueue requestQueue;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    String otp,phone;
    EditText text_otp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
  /*  TextView txt_timer;
    int minutes = 2;
    private boolean isCanceled = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_verification_phone);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Tools.setSystemBarColor(this, R.color.grey_20);

        phone_number = (AppCompatEditText) findViewById( R.id.phone_number );
        btn_continue = (AppCompatButton) findViewById( R.id.bcontinue );
        other_time =  (AppCompatButton) findViewById( R.id.other_time );

        Intent myIntent = getIntent();
        phone = myIntent.getStringExtra("mobile"); // will return "FirstKeyValue"
        final String email= myIntent.getStringExtra("email");

        //String phone= getIntent().getStringExtra("phone_number");
        phone_number.setText( phone );



        requestQueue = Volley.newRequestQueue(this);
        try {
            confirmOtp();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        other_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( VerificationPhone.this, Login.class );
                startActivity( i );
                finish();
            }
        });

    }

    private void confirmOtp() throws JSONException {

        //On the click of the confirm button from alert dialog
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitButtonTask();
                //    final ProgressDialog loading = ProgressDialog.show(Activity_Mobile_verification.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String mobile = phone_number.getText().toString().trim();

                progressDialog = new ProgressDialog(VerificationPhone.this,
                        R.style.Dialog_Theme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.Send_OTP,
                        new Response.Listener<String>() {
                            @Override

                            public void onResponse(String response) {
                                Log.e("response",response);
                                //if the server response is success
                                if(response.equalsIgnoreCase("success")){
                                    progressDialog.dismiss();
                                    dialog = new Dialog(VerificationPhone.this);
                                    dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.custom_dialog);
                                    dialog.show();


                                    text_otp = (EditText) dialog .findViewById(R.id.input_otp);
                                   // txt_timer=(TextView) dialog .findViewById( R.id.txt_timer);
                                    //  tv_message.setText(message);

                                    Button bt_yes = (Button)dialog.findViewById(R.id.btn_next);
                                    Button bt_no = (Button)dialog.findViewById(R.id.cancle);
                                    bt_no.setVisibility(View.INVISIBLE);

                                   /* new CountDownTimer(60*minutes*1000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            if(isCanceled)
                                            {
                                                //If the user request to cancel or paused the
                                                //CountDownTimer we will cancel the current instance
                                                cancel();
                                            }else {
                                                long millis = millisUntilFinished;
                                                //Convert milliseconds into hour,minute and seconds
                                                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                                                txt_timer.setText(hms);//set text
                                            }

                                        }
                                        public void onFinish() {
                                            txt_timer.setText("TIME'S UP , Resend OTP!!");
                                            OTP_EMPTY();
                                            dialog.dismiss();//On finish change timer text
                                        }
                                    }.start();
*/

                                    bt_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // CheckYes();
                                            otp=text_otp.getText().toString();
                                            phone=phone_number.getText().toString();
                                            nameValuePairs.add(new BasicNameValuePair("otp", otp));
                                            nameValuePairs.add(new BasicNameValuePair("phone", phone));
                                            try {
                                                HttpClient httpclient = new DefaultHttpClient();
                                                HttpPost httppost = new HttpPost(MyConfig.OTP_Verification);
                                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                                HttpResponse response = httpclient.execute(httppost);
                                                HttpEntity entity = response.getEntity();
                                                //  is = entity.getContent();
                                                String data = EntityUtils.toString(entity);
                                                Log.e("Register", data);
                                                if(data.matches( "verify successfully" )) {
                                                    Log.e( "pass 1", "connection success " );
                                                    Toast.makeText( VerificationPhone.this, "Verify Done Successfully", Toast.LENGTH_SHORT ).show();

                                                   // isCanceled = true;
                                                    Intent i = new Intent( VerificationPhone.this, Login.class );
                                                    startActivity( i );
                                                    finish();

                                                }if(data.matches( "otp not match" )) {
                                                    Log.e( "pass 1", "connection success " );
                                                    Toast.makeText( VerificationPhone.this, "Please Enter Correct OTP!", Toast.LENGTH_SHORT ).show();

                                                }
                                            } catch (ClientProtocolException e) {
                                                Log.e("Fail 1", e.toString());
                                                Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                            //   Intent intent = new Intent(getApplicationContext(), Activity_signup1.class);
                                            //   startActivity(intent);
                                        }
                                    });
                                    bt_no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           // dialog.dismiss();
                                        }
                                    });
                                    //dismissing the progressbar
                                    //  loading.dismiss();
                                    //Starting a new activity
                                    //startActivity(new Intent(Activity_Mobile_verification.this, LoginActivity.class));
                                }
                                /*if(response.equalsIgnoreCase("success update")){
                                    progressDialog.dismiss();
                                    dialog = new Dialog(VerificationPhone.this);
                                    dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.custom_dialog);
                                    dialog.show();


                                    text_otp = (EditText) dialog .findViewById(R.id.input_otp);

                                    //  tv_message.setText(message);

                                    Button bt_yes = (Button)dialog.findViewById(R.id.btn_next);
                                    Button bt_no = (Button)dialog.findViewById(R.id.cancle);

                                    bt_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // CheckYes();
                                            otp=text_otp.getText().toString();
                                            phone=phone_number.getText().toString();
                                            nameValuePairs.add(new BasicNameValuePair("otp", otp));
                                            nameValuePairs.add(new BasicNameValuePair("phone", phone));
                                            try {
                                                HttpClient httpclient = new DefaultHttpClient();
                                                HttpPost httppost = new HttpPost(MyConfig.OTP_Verification);
                                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                                HttpResponse response = httpclient.execute(httppost);
                                                HttpEntity entity = response.getEntity();
                                                //  is = entity.getContent();
                                                String data = EntityUtils.toString(entity);
                                                Log.e("Register", data);
                                                if(data.matches( "verify successfully" )) {
                                                    Log.e( "pass 1", "connection success " );
                                                    Toast.makeText( VerificationPhone.this, "Verify Done Successfully", Toast.LENGTH_SHORT ).show();
                                                    // emptyInputEditText();
                                                    Intent i = new Intent( VerificationPhone.this, LoginSimpleLight.class );
                                                    startActivity( i );
                                                    finish();


                                                }if(data.matches( "otp not match" )) {
                                                    Log.e( "pass 1", "connection success " );
                                                    Toast.makeText( VerificationPhone.this, "Please Enter Correct OTP!", Toast.LENGTH_SHORT ).show();

                                                }
                                            } catch (ClientProtocolException e) {
                                                Log.e("Fail 1", e.toString());
                                                Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                            //   Intent intent = new Intent(getApplicationContext(), Activity_signup1.class);
                                            //   startActivity(intent);
                                        }
                                    });
                                    bt_no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                              *//*      Toast.makeText( Activity_Mobile_verification.this, "phone updated Successfully", Toast.LENGTH_SHORT ).show();
                                    Intent i = new Intent( Activity_Mobile_verification.this, Activity_signup1.class );
                                    startActivity( i );*//*
                                }*/
                                if(response.equalsIgnoreCase("failure")){
                                    progressDialog.dismiss();
                                    //Displaying a toast if the mobile entered is wrong
                                    Toast.makeText(VerificationPhone.this,"Wrong Mobile Number Please Try Again",Toast.LENGTH_LONG).show();
                                    //   loading.dismiss();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(response.equalsIgnoreCase("not send")){
                                    progressDialog.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(VerificationPhone.this);
                                    alert.setTitle("Status");
                                    alert.setMessage("Sms not Send !!");
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                            // finish();

                                        }
                                    });

                                    alert.show();
                                    //Toast.makeText(VerificationPhone.this,"Mobile Number Already Verified !!",Toast.LENGTH_LONG).show();

                                }
                                if(response.equalsIgnoreCase("already_verify")){
                                    progressDialog.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(VerificationPhone.this);
                                    alert.setTitle("Status");
                                    alert.setMessage("Mobile Number Already Verified !!");
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent( VerificationPhone.this, Login.class );
                                            startActivity( i );
                                            dialog.dismiss();
                                            // finish();

                                        }
                                    });

                                    alert.show();
                                    //Toast.makeText(VerificationPhone.this,"Mobile Number Already Verified !!",Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //   alertDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(VerificationPhone.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        //Adding the parameters otp and username
                        params.put("mobile", mobile);

                        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("verify_mobile", mobile);

                        editor.commit();

                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }

   /* public void OTP_EMPTY(){
        phone=phone_number.getText().toString();
        nameValuePairs.add( new BasicNameValuePair( "phone", phone ) );

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_OTP_EMPTY_PHONE );
            httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
            HttpResponse response = httpclient.execute( httppost );
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString( entity ).trim();
            Log.e( "Register", data );
            if (data.matches( "success" )) {

            }
            if (data.matches( "failure" )) {
                // Log.e( "pass 1", "connection success " );
            }
        } catch (ClientProtocolException e) {
            //Log.e( "Fail 1", e.toString() );
            Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
