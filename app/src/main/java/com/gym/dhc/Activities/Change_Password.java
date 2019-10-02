package com.gym.dhc.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Change_Password extends AppCompatActivity {

    private View parent_view;
    TextInputEditText oldPass,newPass,re_nrePass;
    Button Submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    Dialog dialog;
    EditText text_otp;
    TextView txt_timer;
    int minutes = 2;
    private boolean isCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        parent_view = findViewById(android.R.id.content);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        oldPass=(TextInputEditText)findViewById(R.id.editOldpass);
        newPass=(TextInputEditText)findViewById(R.id.editNewPass);
        re_nrePass=(TextInputEditText)findViewById(R.id.editRenewpass);

        Submit=(Button)findViewById(R.id.buttonPISubmit);
        Submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    // Toast.makeText(Activity_Change_Password.this,"Enter right data!!!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(parent_view, "Enter right data!!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }else {
                    SavePassWord();
                }
            }
        } );

      /*  ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
            }
        });*/

        Tools.setSystemBarColor(this);


    }
    public boolean validate() {
        boolean valid = true;

        String old_password = oldPass.getText().toString();
        String new_password = newPass.getText().toString();
        String reEnterPassword = re_nrePass.getText().toString();
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String oldpass_tag= sharedPreferences.getString("password",null);
        Log.e("thanks", old_password + "/////" + new_password + "////" + reEnterPassword );
        if (old_password.isEmpty() || !(old_password.equals(oldpass_tag))){
            oldPass.setError("Enter valid old password");
            valid = false;
        } else {
            oldPass.setError(null);
        }

        if (!new_password.matches(".*\\d.*")){
            newPass.setError("no digits found");
            valid = false;
        }else
        if (!new_password.matches(".*[a-z].*")) {
            newPass.setError("no lowercase letters found");
            valid = false;
        }else
        if (!new_password.matches(".*[!@#$%^&*+=?-].*")) {
            newPass.setError("no special chars found");
            valid = false;
        }else
        if (new_password.isEmpty() || new_password.length() < 6) {
            newPass.setError("at least 6 characters password which contain lowercase letter ,digit and special character");
            valid = false;
        } else {
            newPass.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(new_password))) {
            re_nrePass.setError("Password Do not match");
            valid = false;
        } else {
            re_nrePass.setError(null);
        }

        return valid;
    }
    private void emptyInputEditText() {
        oldPass.setText(null);
        newPass.setText(null);
        re_nrePass.setText(null);
    }
    private void SavePassWord() {
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        final String usernamelog= sharedPreferences.getString("user_name",null);
        final String passwordlog= sharedPreferences.getString("password",null);
        final String new_pass = newPass.getText().toString();

        nameValuePairs.add(new BasicNameValuePair("new_pass", new_pass));
        nameValuePairs.add(new BasicNameValuePair("usernamelog", usernamelog));
        nameValuePairs.add(new BasicNameValuePair("passwordlog", passwordlog));

        Log.e("new_pass", new_pass);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_PASSWORD);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString(entity).trim();
            Log.e("Register", data);
            if(data.matches( "success" )) {
                dialog = new Dialog(Change_Password.this);
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();

                text_otp = (EditText) dialog .findViewById(R.id.input_otp);
               // txt_timer=(TextView) dialog .findViewById( R.id.txt_timer);



                Button bt_yes = (Button)dialog.findViewById(R.id.btn_next);
                Button bt_no = (Button)dialog.findViewById(R.id.cancle);


                /*new CountDownTimer(60*minutes*1000, 1000) {
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
                }.start();*/


                bt_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // CheckYes();
                        String otp=text_otp.getText().toString();

                        nameValuePairs.add(new BasicNameValuePair("otp", otp));
                        nameValuePairs.add(new BasicNameValuePair("new_pass", new_pass));
                        nameValuePairs.add(new BasicNameValuePair("usernamelog", usernamelog));
                        nameValuePairs.add(new BasicNameValuePair("passwordlog", passwordlog));
                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost(MyConfig.OTP_Verification_Change_PWD);
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            //  is = entity.getContent();
                            String data = EntityUtils.toString(entity).trim();
                            Log.e("Register", data+"//");
                            if(data.matches("verify")) {
                                Log.e( "pass 1", "connection success " );
                                // Toast.makeText( Activity_Change_Password.this, "Password Change Successfully", Toast.LENGTH_SHORT ).show();
                                AlertDialog.Builder alert = new AlertDialog.Builder(Change_Password.this);
                                alert.setTitle("Status");
                                alert.setMessage("Password Change Successfully !!");
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isCanceled = true;
                                        emptyInputEditText();
                                        dialog.dismiss();
                                        sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
                                        editor = sharedPreferences.edit();
                                        editor.remove( "user_name" );
                                        editor.remove( "password" );
                                        editor.commit();
                                        finish();
                                        Intent i = new Intent( Change_Password.this, Login.class );
                                        startActivity( i );

                                        dialog.dismiss();

                                    }
                                });
                                alert.show();

                            }if(data.matches( "otp not match" )) {
                                Log.e( "pass 1", "connection success " );
                                // Toast.makeText( Activity_Change_Password.this, "Please Enter Correct OTP!", Toast.LENGTH_SHORT ).show();

                                AlertDialog.Builder alert = new AlertDialog.Builder(Change_Password.this);
                                alert.setTitle("Status");
                                alert.setMessage("Please Enter Correct OTP!");
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                                alert.show();

                            }
                            if(data.matches( "Something went wrong" )) {

                                // Toast.makeText( Activity_Change_Password.this, "password not updated,something went wrong", Toast.LENGTH_SHORT ).show();
                                AlertDialog.Builder alert = new AlertDialog.Builder(Change_Password.this);
                                alert.setTitle("Status");
                                alert.setMessage("Password not updated,something went wrong!");
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                                alert.show();
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


            } if(data.matches( "failure" )) {
                //  if(data.matches( "phone number already exits" )) {
                Toast.makeText( Change_Password.this, "something went wrong", Toast.LENGTH_SHORT ).show();
                //   }
            }
            if(data.matches( "not send" )) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Change_Password.this);
                alert.setTitle("Status");
                alert.setMessage("SMS Not Send,Retry...!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                alert.show();

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
   /* public void OTP_EMPTY(){
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        final String usernamelog= sharedPreferences.getString("user_name",null);

        nameValuePairs.add( new BasicNameValuePair( "usernamelog", usernamelog ) );

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_OTP_EMPTY );
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
   @Override
   public void onBackPressed()
   {
       Intent i = new Intent( Change_Password.this, MainActivity.class );
       startActivity( i );
       finish();
   }


}
