package com.gym.dhc.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.gym.dhc.R;
import com.gym.dhc.classes.BackgroundWorker;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private View parent_view;
    private TextInputEditText UsernameEditText;
    private TextInputEditText passEditText;
    Button login_btn,forgotPassword,Registration_Btn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        parent_view = findViewById(android.R.id.content);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
      /*  Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/


        UsernameEditText = (TextInputEditText) findViewById( R.id.edit_username);
        passEditText = (TextInputEditText) findViewById(R.id.edit_password);

        ConnectivityManager cmm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cmm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cmm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected()))
        {

        }else{
            //no connection
            Intent i = new Intent(Login.this, NoItemInternetIcon.class);
            startActivity(i);
            //Toast toast = Toast.makeText(NoItemInternetIcon.this, "No Internet Connection", Toast.LENGTH_LONG);
            // toast.show();
        }


        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this,Registration1.class));
               // Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
            }
        });



        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wifi

                ((View) findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkLogin(view);
                       // startActivity(new Intent(Login.this,MainActivity.class));

                    }
                });

                ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(Login.this,Forgot_Pwd.class));
                        //Snackbar.make(parent_view, "Forgot Password", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        } else {

            Intent i = new Intent(Login.this, NoItemInternetIcon.class);
            startActivity(i);
            //  Toast.makeText(this,"Connect to Internet First", Toast.LENGTH_LONG).show();
            Snackbar.make(parent_view, "Connect to Internet First", Snackbar.LENGTH_SHORT).show();
        }
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String usernamelog= sharedPreferences.getString("user_name",null);
        String passwordlog= sharedPreferences.getString("password",null);
        // String resultlog="2";
        Log.e("login",usernamelog +"/////"+passwordlog);
        if((!(usernamelog == null) )){
            String data;
            try {
                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String user_name= sharedPreferences.getString("user_name",null);
                Log.e("shared data",user_name);
                HttpClient httpclient = new DefaultHttpClient();
                //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
                HttpPost httppost = new HttpPost( MyConfig.URL_PROFILE);
                nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("Check Data Main Profile", data);
                try
                {
                    JSONArray json = new JSONArray(data);
                    for (int i = 0; i < json.length(); i++)
                    {

                        JSONObject obj = json.getJSONObject(i);
                        String mem_id = String.valueOf( obj.getInt("mem_id") );
                        String isActive = obj.getString("isActive");


                        Log.e(" mem_id", mem_id+"//"+isActive);

                        if(isActive.equalsIgnoreCase("0")){

                            sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
                            editor = sharedPreferences.edit();
                            editor.remove( "user_name" );
                            editor.remove( "password" );
                            editor.commit();
                            Intent i2 = new Intent(Login.this, Login.class );
                            i2.setAction(Intent.ACTION_MAIN);
                            i2.addCategory(Intent.CATEGORY_HOME);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i2);
                            finish();

                        }else{
                            Intent i1 = new Intent(Login.this, MainActivity.class);
                            startActivity(i1);
                            finish();
                        }

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

    }

    public void checkLogin(View arg0) {
        final String username = UsernameEditText.getText().toString();

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passEditText.setError("Password must be greater than 4-digit");
        }
        String type = "login";
        // if(isValidEmail(username) && isValidPassword(pass))
        if(isValidPassword(pass))
        {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, username, pass);
        }
    }
    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public void Exit() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.backbutton))
                .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                })
                .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
