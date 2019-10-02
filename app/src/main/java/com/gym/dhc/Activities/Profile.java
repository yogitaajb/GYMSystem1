package com.gym.dhc.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.gym.dhc.utils.Tools;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Profile extends AppCompatActivity {

    private ViewPager view_pager;
    private TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        initComponent();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PersonalFragment.newInstance(1), "PERSONAL INFORMATION");
        adapter.addFragment(HealthFragment.newInstance(2), "HEALTH INFORMATION");
        adapter.addFragment(PhotoFragment.newInstance(3), "PHOTO");
        viewPager.setAdapter(adapter);
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Profile.this,MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PersonalFragment extends Fragment {

                    private static final String ARG_SECTION_NUMBER = "section_number";
                    EditText et_surname,et_firtname,et_mobile,et_weight,bt_birth_date,et_address1,et_address2,
                                    et_email,et_emergency_mobile,et_membership_type,et_month_membership,et_locker_number;
                    Button bt_next;
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    SharedPreferences sharedPreferences;
                    SharedPreferences.Editor editor;

                     String mem_id,surname,firstname,mobile_number,dob,address1,address2,email,weight,emergency_contact
                          ,membership_type,month_membership,locker_no;

                private String[] duration_array;
                ArrayList<String> duration_list=new ArrayList<>();
                private JSONArray result_duration;
                public static final String JSON_ARRAY_duration = "result";

                private String[] package_array;
                ArrayList<String> pack_list=new ArrayList<>();
                private JSONArray result_pack;
                public static final String JSON_ARRAY_pack = "result";
ProgressDialog progressDialog;
                String select_duration;

                    public PersonalFragment() {
                    }

                    public static PersonalFragment newInstance(int sectionNumber) {
                        PersonalFragment fragment = new PersonalFragment();
                        Bundle args = new Bundle();
                        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                        fragment.setArguments(args);
                        return fragment;
                    }

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);
                       // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        bt_birth_date= (EditText) ( rootView.findViewById(R.id.bt_birth_date));
                        bt_birth_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogDatePickerLight(v);
                            }
                        });

                        et_surname= (EditText)( rootView.findViewById(R.id.et_surname));
                        et_firtname= (EditText) ( rootView.findViewById(R.id.et_firtname));
                        et_mobile= (EditText) ( rootView.findViewById(R.id.et_mobile));
                        et_weight= (EditText) ( rootView.findViewById(R.id.et_weight));
                        bt_birth_date= (EditText) ( rootView.findViewById(R.id.bt_birth_date));
                        et_address1= (EditText) ( rootView.findViewById(R.id.et_address1));
                        et_address2= (EditText) ( rootView.findViewById(R.id.et_address2));
                        et_email= (EditText) ( rootView.findViewById(R.id.et_email));
                        et_emergency_mobile=(EditText) ( rootView.findViewById(R.id.et_emergency_mobile));
                        et_locker_number=(EditText) (rootView.findViewById(R.id.et_locker_number));

                       // getDurationlist();
                        et_month_membership= (EditText) ( rootView.findViewById(R.id.et_month_membership));
                        et_membership_type= (EditText)( rootView.findViewById(R.id.et_membership_type));
                      //  et_membership_type.setVisibility(View.GONE);
                       /* et_membership_type.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showMemTypeDialog(v);
                            }
                        });


                        et_month_membership.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showMonthMemDialog(v);
                            }
                        });*/

                        Get_USER_DATA();

                        bt_next= (Button) ( rootView.findViewById(R.id.bt_next));
                        bt_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(validate()){
                                    showDialog();
                                    UPDATE_Personal();
                                }

                            }
                        });



                        return rootView;
                    }


                    private void showMemTypeDialog(final View v) {
                   /* final String[] array = new String[]{
                            "Single", "Couple", "Group", "3 Month", "6 Monthly", "Annual"
                    };*/
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Duration");
                        builder.setSingleChoiceItems(duration_array, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                select_duration=duration_array[i];
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
                        datePicker.show(getActivity().getFragmentManager(), "Birth Date");
                    }

                    public boolean validate() {
                        boolean valid = true;

                        String surname = et_surname.getText().toString();
                        String firstname = et_firtname.getText().toString();
                        String mobile = et_mobile.getText().toString();
                        String weight = et_weight.getText().toString();
                        String birthdate = bt_birth_date.getText().toString();
                        String address1 = et_address1.getText().toString();
                        String address2 = et_address2.getText().toString();
                        String email = et_email.getText().toString();
                        String emr_mobile = et_emergency_mobile.getText().toString();
                        String mem_type = et_membership_type.getText().toString();
                        String month_mem = et_month_membership.getText().toString();
                        String locker_num= et_locker_number.getText().toString();

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
                            et_membership_type.setError("Select your Membership Type");
                            valid = false;
                        } else {
                            et_membership_type.setError(null);
                        }
                        if (month_mem.isEmpty()) {
                            et_month_membership.setError("Select your Monthly Membership");
                            valid = false;
                        } else {
                            et_month_membership.setError(null);
                        }

                        return valid;
                    }
                    private void UPDATE_Personal() { String data;

                        String surname = et_surname.getText().toString();
                        String firstname = et_firtname.getText().toString();
                        String mobile = et_mobile.getText().toString();
                        String weight = et_weight.getText().toString();
                        //String password = et_password.getText().toString();
                        String birthdate = bt_birth_date.getText().toString();
                        String address1 = et_address1.getText().toString();
                        String address2 = et_address2.getText().toString();
                        String email = et_email.getText().toString();
                        String emr_mobile = et_emergency_mobile.getText().toString();
                        String mem_type = et_membership_type.getText().toString();
                        String month_mem = et_month_membership.getText().toString();
                        String locker_num= et_locker_number.getText().toString();

                        nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
                        nameValuePairs.add(new BasicNameValuePair("surname", surname));
                        nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
                        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
                        nameValuePairs.add(new BasicNameValuePair("weight", weight));
                        //nameValuePairs.add(new BasicNameValuePair("password", password));
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
                            HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_PERSONAL);
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            //  is = entity.getContent();
                            data = EntityUtils.toString(entity);
                            Log.e("Register", data);
                            if(data.matches( "success" )) {
                                Log.e( "pass 1", "connection success " );
                                progressDialog.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(true);
                                builder.setMessage(" Updated Successfully !!");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();

                            }if(data.matches( "failure" )) {
                                progressDialog.dismiss();
                                Log.e( "pass 1", "connection success " );
                                Toast.makeText( getContext(), "Something went wrong", Toast.LENGTH_SHORT ).show();
                            }
                        } catch (ClientProtocolException e) {
                            Log.e("Fail 1", e.toString());
                            Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

        private void showDialog() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Update data");
            progressDialog.setMessage("Please wait while update Data ...  ");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
        }

                        private void Get_USER_DATA() { String data;

                            try {
                                sharedPreferences=getContext().getSharedPreferences("Mydata",MODE_PRIVATE);
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
                                        mem_id = String.valueOf( obj.getInt("mem_id") );
                                        surname = obj.getString("surname");
                                        firstname = obj.getString("firstname");
                                        mobile_number = obj.getString("mobile_number");
                                        //password = obj.getString("password");
                                        dob = obj.getString("dob");
                                        address1 = obj.getString("address1");
                                        address2 = obj.getString("address2");
                                        email = obj.getString("email");
                                        weight = obj.getString("weight");
                                        emergency_contact = obj.getString("emergency_contact");
                                        membership_type=obj.getString("membership_type");
                                        month_membership = obj.getString("month_membership");
                                        locker_no = obj.getString("locker_no");

                                        Log.e("profile data",mem_id+"="+surname+"="+firstname+"="+email+"="+mobile_number);


                                        et_surname.setText( surname );
                                        et_firtname.setText( firstname );
                                        et_mobile.setText( mobile_number );
                                        bt_birth_date.setText(dob);
                                        et_address1.setText(address1);
                                        et_address2.setText( address2 );
                                        et_email.setText( email );
                                        et_weight.setText( weight );
                                        et_emergency_mobile.setText( emergency_contact );
                                        et_membership_type.setText( membership_type );
                                        et_month_membership.setText( month_membership );
                                        et_locker_number.setText(locker_no);
                                        sharedPreferences = getContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.putString("mem_id", mem_id);
                                        editor.putString("mobile_number", mobile_number);
                                        editor.commit();


                                    }

                                }
                                catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
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
                            RequestQueue queue = Volley.newRequestQueue(getContext());
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
                            RequestQueue queue = Volley.newRequestQueue(getContext());
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

    public static class HealthFragment extends Fragment {
                        private static final String ARG_SECTION_NUMBER = "section_number";
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;
                        String mem_id,heart,bp,chest_pain,breathing,stomach,liver,diabetes,harnia,fits,
                                attack,backpain,asthma;
                        EditText et_heart,et_bp,et_chest_pain,et_breathing,et_stomach,et_liver,et_diabetes,et_hernia,
                                et_fits,et_attack,et_backpain,et_asthma;
                        Button bt_next;
        ProgressDialog progressDialog;

                        public HealthFragment() {
                        }

                        public static HealthFragment newInstance(int sectionNumber) {
                            HealthFragment fragment = new HealthFragment();
                            Bundle args = new Bundle();
                            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                            fragment.setArguments(args);
                            return fragment;
                        }

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                            View rootView = inflater.inflate(R.layout.fragment_health, container, false);
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                          //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));



                            et_heart= (rootView.findViewById(R.id.et_heart));
                            et_heart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_bp= (rootView.findViewById(R.id.et_bp));
                            et_bp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_chest_pain= (rootView.findViewById(R.id.et_chest_pain));
                            et_chest_pain.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_breathing= (rootView.findViewById(R.id.et_breathing));
                            et_breathing.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_stomach= (rootView.findViewById(R.id.et_stomach));
                            et_stomach.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_liver= (rootView.findViewById(R.id.et_liver));
                            et_liver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_diabetes= (rootView.findViewById(R.id.et_diabetes));
                            et_diabetes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_hernia= (rootView.findViewById(R.id.et_hernia));
                            et_hernia.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_fits= (rootView.findViewById(R.id.et_fits));
                            et_fits.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_attack= (rootView.findViewById(R.id.et_attack));
                            et_attack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_backpain= (rootView.findViewById(R.id.et_backpain));
                            et_backpain.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            et_asthma= (rootView.findViewById(R.id.et_asthma));
                            et_asthma.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showANSDialog(v);
                                }
                            });

                            Get_USER_DATA();

                            bt_next= (rootView.findViewById(R.id.bt_next));
                            bt_next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                               /* Intent i = new Intent(Registration2.this,Registration3.class);
                                startActivity(i);*/
                                    if(validate()){
                                        showDialog();
                                        Update_Health();
                                    }

                                }
                            });



                            return rootView;
                        }
        private void showDialog() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Update data");
            progressDialog.setMessage("Please wait while update Data ...  ");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
        }

                        private void Get_USER_DATA() { String data;

                            try {
                                sharedPreferences=getContext().getSharedPreferences("Mydata",MODE_PRIVATE);
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
                                        mem_id = String.valueOf( obj.getInt("mem_id") );
                                        heart = obj.getString("heart");
                                        bp = obj.getString("bp");
                                        chest_pain = obj.getString("chest_pain");
                                        breathing = obj.getString("breathing");
                                        stomach = obj.getString("stomach");
                                        liver = obj.getString("liver");
                                        diabetes= obj.getString("diabetes");
                                        harnia = obj.getString("harnia");
                                        fits = obj.getString("fits");
                                        attack = obj.getString("attack");
                                        backpain = obj.getString("backpain");
                                        asthma = obj.getString("asthma");


                                        Log.e("Health data",mem_id+"="+heart+"="+asthma);

                                        et_heart.setText( heart );
                                        et_bp.setText( bp );
                                        et_chest_pain.setText( chest_pain );
                                        et_breathing.setText(breathing);
                                        et_stomach.setText(stomach);
                                        et_liver.setText( liver );
                                        et_diabetes.setText( diabetes );
                                        et_hernia.setText( harnia );
                                        et_fits.setText( fits );
                                        et_attack.setText( attack );
                                        et_backpain.setText( backpain );
                                        et_asthma.setText( asthma );




                                    }

                                }
                                catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        private void showANSDialog(final View v) {
                            final String[] array = new String[]{
                                    "Yes", "No"
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


                        private void Update_Health() { String data;

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

                         /*
                            nameValuePairs.add(new BasicNameValuePair("mobile", mobile));*/
                            nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
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
                                HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_HEALTH);
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity entity = response.getEntity();
                                //  is = entity.getContent();
                                data = EntityUtils.toString(entity);
                                Log.e("Register", data);
                                if(data.matches( "success" )) {
                                    Log.e( "pass 1", "connection success " );
                                    progressDialog.dismiss();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setMessage(" Updated Successfully !!");

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();

                                }if(data.matches( "failure" )) {
                                    progressDialog.dismiss();
                                    Log.e( "pass 1", "connection success " );
                                    Toast.makeText( getContext(), "Something went wrong", Toast.LENGTH_SHORT ).show();
                                }
                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


    }

    public static class PhotoFragment extends Fragment {
                        private static final String ARG_SECTION_NUMBER = "section_number";
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;
                        ImageView img_profile,imgview_idproof;
                        FloatingActionButton fab_camera,fab_idproof;
        ProgressDialog progressDialog;

                        String profile_image1,profile_image,id_proof,id_proof1;
                        String mem_id,hear_about,declare_status,terms_status;
                        Button bt_submit;

                       /* Uri picUri;

                        private ArrayList<String> permissionsToRequest;
                        private ArrayList<String> permissionsRejected = new ArrayList<>();
                        private ArrayList<String> permissions = new ArrayList<>();

                        private final static int ALL_PERMISSIONS_RESULT = 107;
                        private final static int IMAGE_RESULT = 200;*/

                    private Uri mImageCaptureUri;
                    private android.app.AlertDialog dialog;
                    private static final int PICK_FROM_CAMERA = 1;
                    private static final int CROP_FROM_CAMERA = 2;
                    private static final int PICK_FROM_FILE = 3;

                    private Uri mImageCaptureUriBack;
                    private android.app.AlertDialog dialogg;
                    private static final int PICK_FROM_CAMERABack = 4;
                    private static final int CROP_FROM_CAMERABack = 5;
                    private static final int PICK_FROM_FILEBack = 6;

                        public PhotoFragment() {
                        }

                        public static PhotoFragment newInstance(int sectionNumber) {
                            PhotoFragment fragment = new PhotoFragment();
                            Bundle args = new Bundle();
                            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                            fragment.setArguments(args);
                            return fragment;
                        }

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                            View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
                            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            img_profile=(ImageView) rootView.findViewById( R.id.img_profile) ;
                            fab_camera= (rootView.findViewById(R.id.fab_camera));

                            imgview_idproof=(ImageView) rootView.findViewById( R.id.imgview_idproof) ;
                            fab_idproof= (rootView.findViewById(R.id.fab_idproof));

                            Get_USER_DATA();


                            bt_submit= (rootView.findViewById(R.id.bt_submit));
                            bt_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog();
                                    Update_Photo();
                               /* Intent i = new Intent(Registration3.this,Login.class);
                                startActivity(i);*/
                                   /* if (profile_image != null && !profile_image.isEmpty() && !profile_image.equals("null")) {

                                        if (id_proof != null && !id_proof.isEmpty() && !id_proof.equals("null")) {

                                            Update_Photo();

                                        }else{
                                            //profile_image =profile_image1;
                                            id_proof =id_proof1;
                                            Update_Photo();
                                        }


                                    }else{
                                        if (id_proof != null && !id_proof.isEmpty() && !id_proof.equals("null")) {

                                            profile_image =profile_image1;
                                            Update_Photo();


                                        }else{
                                            profile_image =profile_image1;
                                            id_proof =id_proof1;
                                            Update_Photo();
                                        }


                                    }*/
                                }
                            });

                            captureImageInitialization();
                            fab_camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                                    dialog.show();
                                }
                            });
                           /* permissions.add(CAMERA);
                            permissions.add(WRITE_EXTERNAL_STORAGE);
                            permissions.add(READ_EXTERNAL_STORAGE);
                            permissionsToRequest = findUnAskedPermissions(permissions);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                if (permissionsToRequest.size() > 0)
                                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                            }
*/
                            captureImageInitializationBack();
                            fab_idproof.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                                    dialogg.show();
                                }
                            });


                            return rootView;
                        }
        private void showDialog() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Update Photo");
            progressDialog.setMessage("Please wait while update Data ...  ");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
        }
                        private void Get_USER_DATA() { String data;

                            try {
                                sharedPreferences=getContext().getSharedPreferences("Mydata",MODE_PRIVATE);
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
                                        mem_id = String.valueOf( obj.getInt("mem_id") );
                                        profile_image1 = obj.getString("profile_image");
                                        id_proof1 = obj.getString("id_proof");
                                        hear_about = obj.getString("hear_about");
                                        declare_status= obj.getString("declare_status");
                                        terms_status = obj.getString("terms_status");

                                        Log.e("profile data",profile_image1+"="+hear_about);

                                       /* byte[] decodedString2 = Base64.decode(profile_image1, Base64.DEFAULT);
                                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                                        img_profile.setImageBitmap(decodedByte2);
                                        Log.e("decodedByte2", String.valueOf(decodedByte2));*/
                                       String profileing= MyConfig.Base_url+""+profile_image1;
                                        InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                        try {
                                            in = (InputStream) new URL(profileing).getContent();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                        img_profile.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                        try {
                                            if(in != null)
                                            in.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e(" image", String.valueOf(profileing));

                                        String idproofimg= MyConfig.Base_url+""+id_proof1;
                                        InputStream in1 = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                        try {
                                            in1 = (InputStream) new URL(idproofimg).getContent();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Bitmap bitmap1 = BitmapFactory.decodeStream(in1); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                        imgview_idproof.setImageBitmap(bitmap1); //Sets the Bitmap to ImageView
                                        try {
                                            if(in1 != null)
                                            in1.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e(" idproofimg", String.valueOf(idproofimg));


                                    }

                                }
                                catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        public String getBase64(String path) throws IOException {
                            String encodedImage = null;

                            try
                            {
                                Bitmap bm = BitmapFactory.decodeFile(path);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bm = resize(bm, 600, 600);
                                bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.d("Tag","Inside getBase64(): "+e.getMessage());
                            }

                            Log.d("Mytag","getBase64 :"+encodedImage);
                            return encodedImage;

                        }

                        private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
                            if (maxHeight > 0 && maxWidth > 0) {
                                int width = image.getWidth();
                                int height = image.getHeight();
                                float ratioBitmap = (float) width / (float) height;
                                float ratioMax = (float) maxWidth / (float) maxHeight;

                                int finalWidth = maxWidth;
                                int finalHeight = maxHeight;
                                if (ratioMax > 1) {
                                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                                } else {
                                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                                }
                                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                                return image;
                            } else {
                                return image;
                            }
                        }

                        //new code for camera gallary
/*
                        public Intent getPickImageChooserIntent() {

                            Uri outputFileUri = getCaptureImageOutputUri();

                            List<Intent> allIntents = new ArrayList<>();
                            PackageManager packageManager = getContext().getPackageManager();

                            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                            for (ResolveInfo res : listCam) {
                                Intent intent = new Intent(captureIntent);
                                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                                intent.setPackage(res.activityInfo.packageName);
                                if (outputFileUri != null) {
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                }
                                allIntents.add(intent);
                            }

                            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.setType("image/*");
                            List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
                            for (ResolveInfo res : listGallery) {
                                Intent intent = new Intent(galleryIntent);
                                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                                intent.setPackage(res.activityInfo.packageName);
                                allIntents.add(intent);
                            }

                            Intent mainIntent = allIntents.get(allIntents.size() - 1);
                            for (Intent intent : allIntents) {
                                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                                    mainIntent = intent;
                                    break;
                                }
                            }
                            allIntents.remove(mainIntent);

                            Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

                            return chooserIntent;
                        }


                        private Uri getCaptureImageOutputUri() {
                            Uri outputFileUri = null;
                            File getImage = getContext().getExternalFilesDir("");
                            if (getImage != null) {
                                outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
                            }
                            return outputFileUri;
                        }

                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {


                            if (resultCode == Activity.RESULT_OK) {

                                //  ImageView imageView = findViewById(R.id.imageView);

                                if (requestCode == IMAGE_RESULT) {

                                    String filePath = getImageFilePath(data);
                                    try {
                                        profile_image=getBase64(filePath);
                                        Log.e("profile_image",profile_image);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (filePath != null) {
                                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                        img_profile.setImageBitmap(selectedImage);

                                        byte[] decodedString2 = Base64.decode(profile_image, Base64.DEFAULT);
                                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                                        img_profile.setImageBitmap(decodedByte2);

                                    }
                                    byte[] decodedString2 = Base64.decode(profile_image, Base64.DEFAULT);
                                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                                    img_profile.setImageBitmap(decodedByte2);


                                }

                            }

                        }


                        private String getImageFromFilePath(Intent data) {
                            boolean isCamera = data == null || data.getData() == null;

                            if (isCamera) return getCaptureImageOutputUri().getPath();
                            else return getPathFromURI(data.getData());

                        }

                        public String getImageFilePath(Intent data) {
                            return getImageFromFilePath(data);
                        }

                        private String getPathFromURI(Uri contentUri) {
                            String[] proj = {MediaStore.Audio.Media.DATA};
                            Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                            cursor.moveToFirst();
                            return cursor.getString(column_index);
                        }

                        @Override
                        public void onSaveInstanceState(Bundle outState) {
                            super.onSaveInstanceState(outState);

                                outState.putParcelable("pic_uri", picUri);

                        }

                        @Override
                        public void onActivityCreated(Bundle savedInstanceState) {
                            super.onActivityCreated(savedInstanceState);

                            // get the file url
                            if (savedInstanceState != null){
                            picUri = savedInstanceState.getParcelable("pic_uri");
                            }
                        }

                        private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
                            ArrayList<String> result = new ArrayList<String>();

                            for (String perm : wanted) {
                                if (!hasPermission(perm)) {
                                    result.add(perm);
                                }
                            }

                            return result;
                        }

                        private boolean hasPermission(String permission) {
                            if (canMakeSmores()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    return (getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
                                }
                            }
                            return true;
                        }

                        private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
                            new AlertDialog.Builder(getContext())
                                    .setMessage(message)
                                    .setPositiveButton("OK", okListener)
                                    .setNegativeButton("Cancel", null)
                                    .create()
                                    .show();
                        }

                        private boolean canMakeSmores() {
                            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
                        }

                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

                            switch (requestCode) {

                                case ALL_PERMISSIONS_RESULT:
                                    for (String perms : permissionsToRequest) {
                                        if (!hasPermission(perms)) {
                                            permissionsRejected.add(perms);
                                        }
                                    }

                                    if (permissionsRejected.size() > 0) {


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                                }
                                                            }
                                                        });
                                                return;
                                            }
                                        }

                                    }

                                    break;
                            }

                        }*/

        private void captureImageInitialization() {
            /**
             * a selector dialog to display two image source options, from camera
             * ‘Take from camera’ and from existing files ‘Select from gallery’
             */
        /*final String[] items = new String[] { "Take from camera",
                "Select from gallery" };*/
            final String[] items = new String[] {"Select from gallery" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.select_dialog_item, items);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) { // pick from
                    // camera
                    /*if (item == 0) {
                     *//**
                     * To take a photo from camera, pass intent action
                     * ‘MediaStore.ACTION_IMAGE_CAPTURE‘ to open the camera app.
                     *//*

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                   *//* mImageCaptureUri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "tmp_avatar_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg"));*//*

                    mImageCaptureUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".my.package.name.provider",new File(Environment
                            .getExternalStorageDirectory(), "tmp_avatar_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg"));


                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        intent.putExtra("return-data", true);
                        //  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {*/
                    if (item == 0) {
                        // pick from file
                        /**
                         * To select an image from existing files, use
                         * Intent.createChooser to open image chooser. Android will
                         * automatically display a list of supported applications,
                         * such as image gallery or file manager.
                         */
                        Intent intent = new Intent();

                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivityForResult(Intent.createChooser(intent,
                                "Complete action using"), PICK_FROM_FILE);
                    }
                }
            });

            dialog = builder.create();
        }

        public class CropOptionAdapter extends ArrayAdapter<CropOption> {
            private ArrayList<CropOption> mOptions;
            private LayoutInflater mInflater;

            public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
                super(context, R.layout.crop_selector, options);

                mOptions = options;

                mInflater = LayoutInflater.from(context);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup group) {
                if (convertView == null)
                    convertView = mInflater.inflate(R.layout.crop_selector, null);

                CropOption item = mOptions.get(position);

                if (item != null) {
                    ((ImageView) convertView.findViewById(R.id.iv_icon))
                            .setImageDrawable(item.icon);
                    ((TextView) convertView.findViewById(R.id.tv_name))
                            .setText(item.title);

                    return convertView;
                }

                return null;
            }
        }

        public class CropOption {
            public CharSequence title;
            public Drawable icon;
            public Intent appIntent;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {


       /* if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }
*/
            if (resultCode != RESULT_OK)
                return;

            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    /**
                     * After taking a picture, do the crop
                     */
                    doCrop();

                    break;

                case PICK_FROM_FILE:
                    /**
                     * After selecting image from files, save the selected path
                     */
                    mImageCaptureUri = data.getData();

                    doCrop();

                    break;

                case CROP_FROM_CAMERA:
                    Bundle extras = data.getExtras();
                    /**
                     * After cropping the image, get the bitmap of the cropped image and
                     * display it on imageview.
                     */
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo = resize(photo, 1200, 1200);
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        profile_image = Base64.encodeToString(b, Base64.DEFAULT);
                        Log.e("Image",profile_image);
                        img_profile.setImageBitmap(photo);
                    }

                    File f = new File(mImageCaptureUri.getPath());
                    /**
                     * Delete the temporary image
                     */
                    if (f.exists())
                        f.delete();

                    break;

                case PICK_FROM_CAMERABack:
                    /**
                     * After taking a picture, do the crop
                     */
                    doCropBack();

                    break;

                case PICK_FROM_FILEBack:
                    /**
                     * After selecting image from files, save the selected path
                     */
                    mImageCaptureUriBack = data.getData();

                    doCropBack();

                    break;

                case CROP_FROM_CAMERABack:
                    Bundle extrasBack = data.getExtras();
                    /**
                     * After cropping the image, get the bitmap of the cropped image and
                     * display it on imageview.
                     */
                    if (extrasBack != null) {
                        Bitmap photo = extrasBack.getParcelable("data");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo = resize(photo, 1200, 1200);
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        id_proof = Base64.encodeToString(b, Base64.DEFAULT);

                        imgview_idproof.setImageBitmap(photo);

                    }

                    File fBack = new File(mImageCaptureUriBack.getPath());
                    /**
                     * Delete the temporary image
                     */
                    if (fBack.exists())
                        fBack.delete();

                    break;

            }
        }

        private void doCrop() {
            final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
            /**
             * Open image crop app by starting an intent
             * ‘com.android.camera.action.CROP‘.
             */
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            /**
             * Check if there is image cropper app installed.
             */
            List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(
                    intent, 0);

            int size = list.size();

            /**
             * If there is no image cropper app, display warning message
             */
            if (size == 0) {

                Toast.makeText(getContext(), "Can not find image crop app",
                        Toast.LENGTH_SHORT).show();

                return;
            } else {
                /**
                 * Specify the image path, crop dimension and scale
                 */
                intent.setData(mImageCaptureUri);

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                /**
                 * There is posibility when more than one image cropper app exist,
                 * so we have to check for it first. If there is only one app, open
                 * then app.
                 */

                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = list.get(0);

                    i.setComponent(new ComponentName(res.activityInfo.packageName,
                            res.activityInfo.name));

                    startActivityForResult(i, CROP_FROM_CAMERA);
                } else {
                    /**
                     * If there are several app exist, create a custom chooser to
                     * let user selects the app.
                     */
                    for (ResolveInfo res : list) {
                        final CropOption co = new CropOption();

                        co.title = getContext().getPackageManager().getApplicationLabel(
                                res.activityInfo.applicationInfo);
                        co.icon = getContext().getPackageManager().getApplicationIcon(
                                res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);

                        co.appIntent
                                .setComponent(new ComponentName(
                                        res.activityInfo.packageName,
                                        res.activityInfo.name));

                        cropOptions.add(co);
                    }

                    CropOptionAdapter adapter = new CropOptionAdapter(
                            getContext(), cropOptions);

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Choose Crop App");
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    startActivityForResult(
                                            cropOptions.get(item).appIntent,
                                            CROP_FROM_CAMERA);
                                }
                            });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                getContext().getContentResolver().delete(mImageCaptureUri, null,
                                        null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    android.app.AlertDialog alert = builder.create();

                    alert.show();
                }
            }
        }

        ///For Back ADhar Images

        private void captureImageInitializationBack() {
            /**
             * a selector dialog to display two image source options, from camera
             * ‘Take from camera’ and from existing files ‘Select from gallery’
             */
            final String[] items = new String[] { "Select from gallery" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.select_dialog_item, items);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) { // pick from
                    if (item == 0) {
                        // pick from file
                        /**
                         * To select an image from existing files, use
                         * Intent.createChooser to open image chooser. Android will
                         * automatically display a list of supported applications,
                         * such as image gallery or file manager.
                         */
                        Intent intent = new Intent();

                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivityForResult(Intent.createChooser(intent,
                                "Complete action using"), PICK_FROM_FILEBack);
                    }
                }
            });

            dialogg = builder.create();
        }

        private void doCropBack() {
            final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
            /**
             * Open image crop app by starting an intent
             * ‘com.android.camera.action.CROP‘.
             */
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            /**
             * Check if there is image cropper app installed.
             */
            List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(
                    intent, 0);

            int size = list.size();

            /**
             * If there is no image cropper app, display warning message
             */
            if (size == 0) {

                Toast.makeText(getContext(), "Can not find image crop app",
                        Toast.LENGTH_SHORT).show();

                return;
            } else {
                /**
                 * Specify the image path, crop dimension and scale
                 */
                intent.setData(mImageCaptureUriBack);

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                /**
                 * There is posibility when more than one image cropper app exist,
                 * so we have to check for it first. If there is only one app, open
                 * then app.
                 */

                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = list.get(0);

                    i.setComponent(new ComponentName(res.activityInfo.packageName,
                            res.activityInfo.name));

                    startActivityForResult(i, CROP_FROM_CAMERABack);
                } else {
                    /**
                     * If there are several app exist, create a custom chooser to
                     * let user selects the app.
                     */
                    for (ResolveInfo res : list) {
                        final CropOption co = new CropOption();

                        co.title = getContext().getPackageManager().getApplicationLabel(
                                res.activityInfo.applicationInfo);
                        co.icon = getContext().getPackageManager().getApplicationIcon(
                                res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);

                        co.appIntent
                                .setComponent(new ComponentName(
                                        res.activityInfo.packageName,
                                        res.activityInfo.name));

                        cropOptions.add(co);
                    }

                    CropOptionAdapter adapter = new CropOptionAdapter(
                            getContext(), cropOptions);

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Choose Crop App");
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    startActivityForResult(
                                            cropOptions.get(item).appIntent,
                                            CROP_FROM_CAMERABack);
                                }
                            });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUriBack != null) {
                                getContext().getContentResolver().delete(mImageCaptureUriBack, null,
                                        null);
                                mImageCaptureUriBack = null;
                            }
                        }
                    });

                    android.app.AlertDialog alert = builder.create();

                    alert.show();
                }
            }
        }


                        private void Update_Photo() { String data;

                            nameValuePairs.add(new BasicNameValuePair("mem_id", mem_id));
                            nameValuePairs.add(new BasicNameValuePair("profile_image", profile_image));
                            nameValuePairs.add(new BasicNameValuePair("id_proof_image", id_proof));
                            try {
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_PHOTO);
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity entity = response.getEntity();
                                //  is = entity.getContent();
                                data = EntityUtils.toString(entity);
                                Log.e("Register", data);
                                if(data.matches( "success" )) {
                                    Log.e( "pass 1", "connection success " );
                                    progressDialog.dismiss();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setMessage("Profile Updated Successfully !!");

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();

                                }if(data.matches( "failure" )) {
                                    Log.e( "pass 1", "connection success " );
                                    progressDialog.dismiss();
                                    Toast.makeText( getContext(), "Something went wrong", Toast.LENGTH_SHORT ).show();
                                }
                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    @Override
    public void onBackPressed() {

       /* i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        Intent i = new Intent(Profile.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}
