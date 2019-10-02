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

public class Registration2 extends AppCompatActivity {

    EditText et_heart,et_bp,et_chest_pain,et_breathing,et_stomach,et_liver,et_diabetes,et_hernia,
            et_fits,et_attack,et_backpain,et_asthma;
    Button bt_next;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        initComponent();

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Member Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

    }

    private void initComponent() {

        et_heart= (findViewById(R.id.et_heart));
        et_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_bp= (findViewById(R.id.et_bp));
        et_bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_chest_pain= (findViewById(R.id.et_chest_pain));
        et_chest_pain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_breathing= (findViewById(R.id.et_breathing));
        et_breathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_stomach= (findViewById(R.id.et_stomach));
        et_stomach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_liver= (findViewById(R.id.et_liver));
        et_liver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_diabetes= (findViewById(R.id.et_diabetes));
        et_diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_hernia= (findViewById(R.id.et_hernia));
        et_hernia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_fits= (findViewById(R.id.et_fits));
        et_fits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_attack= (findViewById(R.id.et_attack));
        et_attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_backpain= (findViewById(R.id.et_backpain));
        et_backpain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        et_asthma= (findViewById(R.id.et_asthma));
        et_asthma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showANSDialog(v);
            }
        });

        bt_next= (findViewById(R.id.bt_next));
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(Registration2.this,Registration3.class);
                startActivity(i);*/
                if(validate()){
                    RegistrationAA2();
                }

            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onPause();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showANSDialog(final View v) {
        final String[] array = new String[]{
                "Yes", "No"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean validate() {
        boolean valid = true;

        String heart = et_heart.getText().toString();
        String bp = et_bp.getText().toString();
        String chest_pain = et_chest_pain.getText().toString();
        String breathing = et_breathing.getText().toString();
        String stomach = et_stomach.getText().toString();
        String liver = et_liver.getText().toString();
        String diabetes = et_diabetes.getText().toString();
        String hernia = et_hernia.getText().toString();
        String fits = et_fits.getText().toString();
        String attack = et_attack.getText().toString();
        String backpain = et_backpain.getText().toString();
        String asthma = et_asthma.getText().toString();

        if (heart.isEmpty()) {
            et_heart.setError("Select One Option");
            valid = false;
        } else {
            et_heart.setError(null);
        }
        if (bp.isEmpty()) {
            et_bp.setError("Select One Option");
            valid = false;
        } else {
            et_bp.setError(null);
        }
        if (chest_pain.isEmpty()) {
            et_chest_pain.setError("Select One Option ");
            valid = false;
        } else {
            et_chest_pain.setError(null);
        }
        if (breathing.isEmpty()) {
            et_breathing.setError("Select One Option ");
            valid = false;
        } else {
            et_breathing.setError(null);
        }

        if (stomach.isEmpty()) {
            et_stomach.setError("Select One Option");
            valid = false;
        } else {
            et_stomach.setError(null);
        }
        if (liver.isEmpty() ) {
            et_liver.setError("Select One Option");
            valid = false;
        } else {
            et_liver.setError(null);
        }
        if (diabetes.isEmpty()) {
            et_diabetes.setError("Select One Option ");
            valid = false;
        } else {
            et_diabetes.setError(null);
        }

        if (hernia.isEmpty()) {
            et_hernia.setError("Select One Option");
            valid = false;
        } else {
            et_hernia.setError(null);
        }
        if (fits.isEmpty() ) {
            et_fits.setError("Select One Option");
            valid = false;
        } else {
            et_fits.setError(null);
        }
        if (attack.isEmpty()) {
            et_attack.setError("Select One Option ");
            valid = false;
        } else {
            et_attack.setError(null);
        }

        if (backpain.isEmpty()) {
            et_backpain.setError("Select One Option");
            valid = false;
        } else {
            et_backpain.setError(null);
        }
        if (asthma.isEmpty() ) {
            et_asthma.setError("Select One Option");
            valid = false;
        } else {
            et_asthma.setError(null);
        }

        return valid;
    }


    private void RegistrationAA2() { String data;

        String heart = et_heart.getText().toString();
        String bp = et_bp.getText().toString();
        String chest_pain = et_chest_pain.getText().toString();
        String breathing = et_breathing.getText().toString();
        String stomach = et_stomach.getText().toString();
        String liver = et_liver.getText().toString();
        String diabetes = et_diabetes.getText().toString();
        String hernia = et_hernia.getText().toString();
        String fits = et_fits.getText().toString();
        String attack = et_attack.getText().toString();
        String backpain = et_backpain.getText().toString();
        String asthma = et_asthma.getText().toString();

        Intent myIntent = getIntent(); // gets the previously created intent
        String mobile = myIntent.getStringExtra("mobile"); // will return "FirstKeyValue"
        String email= myIntent.getStringExtra("email");

        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("heart", heart));
        nameValuePairs.add(new BasicNameValuePair("bp", bp));
        nameValuePairs.add(new BasicNameValuePair("chest_pain", chest_pain));
        nameValuePairs.add(new BasicNameValuePair("breathing", breathing));
        nameValuePairs.add(new BasicNameValuePair("stomach", stomach));
        nameValuePairs.add(new BasicNameValuePair("liver", liver));
        nameValuePairs.add(new BasicNameValuePair("diabetes", diabetes));
        nameValuePairs.add(new BasicNameValuePair("hernia", hernia));
        nameValuePairs.add(new BasicNameValuePair("fits", fits));
        nameValuePairs.add(new BasicNameValuePair("attack", attack));
        nameValuePairs.add(new BasicNameValuePair("backpain", backpain));
        nameValuePairs.add(new BasicNameValuePair("asthma", asthma));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_REGISTRATION2);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            data = EntityUtils.toString(entity);
            Log.e("Register", data);
            if(data.matches( "success" )) {
                Log.e( "pass 1", "connection success " );

                Intent i = new Intent( Registration2.this, Registration3.class );
                i.putExtra("mobile",mobile);
                i.putExtra("email",email);
                startActivity( i );
                finish();

            }if(data.matches( "failure" )) {
                Log.e( "pass 1", "connection success " );
                Toast.makeText( Registration2.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
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
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Registration2.this);
        builder.setCancelable(false);
        builder.setMessage("Sorry ,can't perform this action !!!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
