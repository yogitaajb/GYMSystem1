package com.gym.dhc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Forgot_Pwd extends AppCompatActivity implements View.OnClickListener{

    private static TextInputEditText mobileText;
    private static AppCompatButton submit, back;
    private com.android.volley.RequestQueue requestQueue;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__pwd);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mobileText = (TextInputEditText) findViewById(R.id.reg_phone);
        submit = (AppCompatButton) findViewById(R.id.forgot_button);
        back = (AppCompatButton) findViewById(R.id.backToLoginBtn);
        back.setOnClickListener(this);
        //  submit.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        try {
            confirmOtp();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void confirmOtp() throws JSONException {

        //On the click of the confirm button from alert dialog
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitButtonTask();
                final ProgressDialog loading = ProgressDialog.show(Forgot_Pwd.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String mobile = mobileText.getText().toString().trim();

                StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.ForgotPassword,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                Log.e("responce",response);
                                if(response.equalsIgnoreCase("success")){
                                    //dismissing the progressbar
                                    loading.dismiss();
                                    //Starting a new activity
                                    startActivity(new Intent(Forgot_Pwd.this, Login.class));
                                    finish();
                                }if(response.equalsIgnoreCase("failure")){
                                    //Displaying a toast if the mobile entered is wrong
                                    Toast.makeText(Forgot_Pwd.this,"Wrong Mobile Number Please Try Again",Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(response.equalsIgnoreCase("not send")){
                                    //Displaying a toast if the mobile entered is wrong
                                    Toast.makeText(Forgot_Pwd.this,"Message not Send Retrying....",Toast.LENGTH_LONG).show();
                                    loading.dismiss();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //   alertDialog.dismiss();
                                Toast.makeText(Forgot_Pwd.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        //Adding the parameters otp and username
                        params.put("mobile", mobile);
                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                Intent i = new Intent(this, Login.class);
                startActivity(i);
                finish();
                break;


        }

    }

}
