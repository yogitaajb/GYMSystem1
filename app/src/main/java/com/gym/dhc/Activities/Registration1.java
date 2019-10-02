package com.gym.dhc.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registration1 extends AppCompatActivity {

    EditText et_surname,et_firtname,et_mobile,et_weight,et_password,bt_birth_date,et_address1,et_address2,
            et_email,et_emergency_mobile,et_membership_type,et_month_membership,et_locker_number;
    Button bt_next;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String[] duration_array;
    ArrayList<String> duration_list=new ArrayList<>();
    private JSONArray result_duration;
    public static final String JSON_ARRAY_duration = "result";

    private String[] package_array;
    ArrayList<String> pack_list=new ArrayList<>();
    private JSONArray result_pack;
    public static final String JSON_ARRAY_pack = "result";

    String select_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

    }

    private void initComponent() {
        bt_birth_date= (EditText) (findViewById(R.id.bt_birth_date));
        bt_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);
            }
        });

        et_surname= (EditText)(findViewById(R.id.et_surname));
        et_firtname= (EditText) (findViewById(R.id.et_firtname));
        et_mobile= (EditText) (findViewById(R.id.et_mobile));
        et_weight= (EditText) (findViewById(R.id.et_weight));
        et_password= (EditText) (findViewById(R.id.et_password));
        bt_birth_date= (EditText) (findViewById(R.id.bt_birth_date));
        et_address1= (EditText) (findViewById(R.id.et_address1));
        et_address2= (EditText) (findViewById(R.id.et_address2));
        et_email= (EditText) (findViewById(R.id.et_email));
        et_emergency_mobile=(EditText) (findViewById(R.id.et_emergency_mobile));
        et_locker_number=(EditText) (findViewById(R.id.et_locker_number));

        getDurationlist();

        et_membership_type= (EditText)(findViewById(R.id.et_membership_type));
        et_membership_type.setVisibility(View.GONE);
        et_membership_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemTypeDialog(v);
            }
        });

        et_month_membership= (EditText) (findViewById(R.id.et_month_membership));

        et_month_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthMemDialog(v);
            }
        });

        bt_next= (Button) (findViewById(R.id.bt_next));
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(Registration1.this,Registration2.class);
               // startActivity(i);
                if(validate()){
                   RegistrationDATA1();
               }

            }
        });
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

    private void showMemTypeDialog(final View v) {
       /* final String[] array = new String[]{
                "Single", "Couple", "Group", "3 Month", "6 Monthly", "Annual"
        };*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Packages");
        builder.setSingleChoiceItems(package_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(package_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showMonthMemDialog(final View v) {
        /*final String[] array = new String[]{
                "Trail 7 Days", "3 Month", "6 Month", "12 Month", "Other"};*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duration");
        builder.setSingleChoiceItems(duration_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                select_duration=duration_array[i];
                pack_list=new ArrayList<>();
                getPackagelist();
                ((EditText) v).setText(duration_array[i]);

                et_membership_type.setVisibility(View.VISIBLE);

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void dialogDatePickerLight(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date = calendar.getTimeInMillis();
                       // bt_birth_date.setText(Tools.getFormattedDateShort(date));
                        int month = monthOfYear+1;
                        bt_birth_date.setText(year +"-"+month+"-"+ dayOfMonth);
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMaxDate(cur_calender);
        datePicker.show(getFragmentManager(), "Birth Date");
    }

    public boolean validate() {
        boolean valid = true;

        String surname = et_surname.getText().toString();
        String firstname = et_firtname.getText().toString();
        String mobile = et_mobile.getText().toString();
        String weight = et_weight.getText().toString();
        String password = et_password.getText().toString();
        String birthdate = bt_birth_date.getText().toString();
        String address1 = et_address1.getText().toString();
        String address2 = et_address2.getText().toString();
        String email = et_email.getText().toString();
        String emr_mobile = et_emergency_mobile.getText().toString();
        String mem_type = et_membership_type.getText().toString();
        String month_mem = et_month_membership.getText().toString();
        String locker_num= et_locker_number.getText().toString();

       /* if (locker_num.isEmpty()) {
            et_locker_number.setError("Enter Locker Number");
            valid = false;
        } else {
            et_locker_number.setError(null);
        }*/
        if (address1.isEmpty()) {
            et_address1.setError("Enter Your Current Address");
            valid = false;
        } else {
            et_address1.setError(null);
        }

        if (surname.isEmpty() || surname.length() < 2) {
            et_surname.setError("at least 2 characters");
            valid = false;
        } else {
            et_surname.setError(null);
        }
        if (firstname.isEmpty() || firstname.length() < 2) {
            et_firtname.setError("at least 2 characters");
            valid = false;
        } else {
            et_firtname.setError(null);
        }

        if (weight.isEmpty()) {
            et_weight.setError("Enter Your Current Weight");
            valid = false;
        } else {
            et_weight.setError(null);
        }
        if (birthdate.isEmpty()) {
            bt_birth_date.setError("enter your Birth date ");
            valid = false;
        } else {
            bt_birth_date.setError(null);
        }
      /*  if (password.isEmpty() || password.length() < 5) {
            et_password.setError("enter valid and strong password ");
            valid = false;
        } else {
            et_password.setError(null);
        }*/
        if (!password.matches(".*\\d.*")){
            et_password.setError("no digits found");
            valid = false;
        }else
        if (!password.matches(".*[a-z].*")) {
            et_password.setError("no lowercase letters found");
            valid = false;
        }else
        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            et_password.setError("no special chars found");
            valid = false;
        }else
        if (password.isEmpty() || password.length() < 6) {
            et_password.setError("at least 6 characters password which contain lowercase letter ,digit and special character");
            valid = false;
        } else {
            et_password.setError(null);
        }
        if (mobile.isEmpty() || !Patterns.PHONE.matcher(mobile).matches()) {
            et_mobile.setError("enter a valid Phone address");
            valid = false;
        } else {
            et_mobile.setError(null);
        }

        if (emr_mobile.isEmpty() || !Patterns.PHONE.matcher(emr_mobile).matches()) {
            et_emergency_mobile.setError("enter a valid Phone address");
            valid = false;
        } else {
            et_emergency_mobile.setError(null);
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("enter a valid Email address");
            valid = false;
        } else {
            et_email.setError(null);
        }
        if (mem_type.isEmpty()) {
            et_membership_type.setError("Select your Membership Package");
            valid = false;
        } else {
            et_membership_type.setError(null);
        }
        if (month_mem.isEmpty()) {
            et_month_membership.setError("Select your Duration");
            valid = false;
        } else {
            et_month_membership.setError(null);
        }

        return valid;
    }


    private void RegistrationDATA1() { String data;

        String surname = et_surname.getText().toString();
        String firstname = et_firtname.getText().toString();
        String mobile = et_mobile.getText().toString();
        String weight = et_weight.getText().toString();
        String password = et_password.getText().toString();
        String birthdate = bt_birth_date.getText().toString();
        String address1 = et_address1.getText().toString();
        String address2 = et_address2.getText().toString();
        String email = et_email.getText().toString();
        String emr_mobile = et_emergency_mobile.getText().toString();
        String mem_type = et_membership_type.getText().toString();
        String month_mem = et_month_membership.getText().toString();
        String locker_num= et_locker_number.getText().toString();

        nameValuePairs.add(new BasicNameValuePair("surname", surname));
        nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
        nameValuePairs.add(new BasicNameValuePair("weight", weight));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("birthdate", birthdate));
        nameValuePairs.add(new BasicNameValuePair("address1", address1));
        nameValuePairs.add(new BasicNameValuePair("address2", address2));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("emr_mobile", emr_mobile));
        nameValuePairs.add(new BasicNameValuePair("mem_type", mem_type));
        nameValuePairs.add(new BasicNameValuePair("month_mem", month_mem));
        nameValuePairs.add(new BasicNameValuePair("locker_num", locker_num));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_REGISTRATION1);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            data = EntityUtils.toString(entity).trim();
            Log.e("Register", data);
            if(data.matches( "success" )) {
                Log.e( "pass 1", "connection success " );

                Intent i = new Intent( Registration1.this, Registration2.class );
                i.putExtra("mobile",mobile);
                i.putExtra("email",email);
                startActivity( i );
                finish();

            }if(data.matches( "failure" )) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Registration1.this);
                alert.setTitle("Status");
                alert.setMessage("Something went wrong");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                alert.show();

            }
            if(data.matches( "number already register" )) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Registration1.this);
                alert.setTitle("Status");
                alert.setMessage("Mobile Number Already Registered ,try Other Number!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                alert.show();

            }
            if(data.matches( "locker number already assign" )) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Registration1.this);
                alert.setTitle("Status");
                alert.setMessage("Locker Number Already Assign to other Member ,try Other Number!");
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


    public void getDurationlist(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_Duration_list,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result_duration = j.getJSONArray(JSON_ARRAY_duration);
                            getTrainer(result_duration);
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
                duration_list.add(json.getString("description"));
                duration_array= duration_list.toArray(new String[duration_list.size()]);

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
    public void getPackagelist(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_Duration_pack_list,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result_pack = j.getJSONArray(JSON_ARRAY_pack);
                            getPackage(result_pack);
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

               /* sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String user_id= sharedPreferences.getString("idtag",null);*/

                Log.e("select_duration",select_duration);
                params.put("duration",select_duration);
                return params;
            }
        };
        //  RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    private void getPackage(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                // sub_list.add("Select Subject");
                pack_list.add(json.getString("pack_name"));
                package_array= pack_list.toArray(new String[pack_list.size()]);
               String ssd= pack_list.get(i).toString();
               Log.e("jhgj",ssd);
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
