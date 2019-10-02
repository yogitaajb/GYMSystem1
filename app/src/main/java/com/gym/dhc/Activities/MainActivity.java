package com.gym.dhc.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.gym.dhc.R;
import com.gym.dhc.classes.MyConfig;
import com.gym.dhc.model.Banner_model;
import com.gym.dhc.model.Packages_model;
import com.gym.dhc.utils.CommonUtils;
import com.gym.dhc.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    LinearLayout lyt_profile,lyt_make_payment,lyt_upgrade_package,lyt_receipt,lyt_payment_history,lyt_help,
            lyt_trainers,lyt_reach_us,lyt_contact_us;
Button btn_make_payment;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String mem_id,surname,firstname,mobile_number,dob,address1,address2,email,weight,emergency_contact
            ,membership_type,month_membership,profile_image;
    SliderLayout sliderLayout ;
    HashMap<String, String> HashMapForLocalRes ;
  //  HashMap<String, Integer> HashMapForLocalRes ;
    CircularImageView profile_view;
    TextView txt_user_name;

    List<Banner_model> listitem=new ArrayList<Banner_model>();;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {

                if(CommonUtils.getNetworkType(MainActivity.this).equalsIgnoreCase("2g") ){
                    Log.e("Range", CommonUtils.getNetworkType(MainActivity.this));
                }else{
                    Log.e("Range1",CommonUtils.getNetworkType(MainActivity.this));
                }
                // connected to wif

            }
        } else {

            Intent i = new Intent(MainActivity.this, NoItemInternetIcon.class);
            startActivity(i);
        }

        profile_view=(CircularImageView) findViewById(R.id.profile_view);
        txt_user_name=(TextView) findViewById(R.id.txt_user_name);
        if(!((Activity) getApplicationContext()).isFinishing())
        {
            //show dialog
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1000);
        }


        Get_USER_DATA();

        sliderLayout = (SliderLayout) findViewById(R.id.slider);


        //listitem=new ArrayList<Banner_model>();

       getBannerImagelist();

        //AddImageUrlFormLocalRes();
      /*  Log.e("sssdddd",String.valueOf(listitem.get(0).getImage_url()));
        Log.e("sss",String.valueOf(HashMapForLocalRes.keySet()));
        for(String name : HashMapForLocalRes.keySet()){
            Log.e("sss11",name);
            TextSliderView textSliderView = new TextSliderView(MainActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType( BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(MainActivity.this);
*/
        btn_make_payment=(Button) findViewById(R.id.btn_make_payment);
        lyt_profile=(LinearLayout) findViewById(R.id.lyt_profile);
        lyt_make_payment=(LinearLayout) findViewById(R.id.lyt_make_payment);
        lyt_upgrade_package=(LinearLayout) findViewById(R.id.lyt_upgrade_package);
        lyt_receipt=(LinearLayout) findViewById(R.id.lyt_receipt);
        lyt_payment_history=(LinearLayout) findViewById(R.id.lyt_payment_history);
        lyt_help=(LinearLayout) findViewById(R.id.lyt_help);
        lyt_trainers=(LinearLayout) findViewById(R.id.lyt_trainers);
        lyt_reach_us=(LinearLayout) findViewById(R.id.lyt_reach_us);
        lyt_contact_us=(LinearLayout) findViewById(R.id.lyt_contact_us);
        Intent myIntent = getIntent(); // gets the previously created intent
        int flag = myIntent.getIntExtra("flag",0);
        if(flag==1){
            showCustomDialog();
        }else{

        }


        lyt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Profile.class);
                startActivity(i);
            }
        });
        lyt_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Packages_list.class);
                startActivity(i);
            }
        });
        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Packages_list.class);
                startActivity(i);
            }
        });
        lyt_upgrade_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Upgrade_Package.class);
                startActivity(i);
            }
        });
        lyt_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Receipt.class);
                startActivity(i);
            }
        });
        lyt_payment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Payment_History.class);
                startActivity(i);
            }
        });
        lyt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(MainActivity.this,Help.class);
                startActivity(i);*/
                Uri uri = Uri.parse("https://www.justdial.com/Nashik/Dynamic-Health-Club-Behind-Petrol-Pump-Ojhar/0253PX253-X253-181008232740-U5S6_BZDET"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        lyt_trainers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost( MyConfig.URL_Dashboard_url);
                    // nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Log.e("Check Data Main Profile", data);
                    try
                    {
                        JSONArray json = new JSONArray(data);
                        for (int i = 0; i < json.length(); i++)
                        {

                            JSONObject obj = json.getJSONObject(i);
                           String trainers_url = obj.getString("trainers_url");

                            Log.e("trainers_url data",trainers_url);

                            Uri uri = Uri.parse(trainers_url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);

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
        });
        lyt_reach_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost( MyConfig.URL_Dashboard_url);
                    // nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                   String data = EntityUtils.toString(entity);
                    Log.e("Check Data Main Profile", data);
                    try
                    {
                        JSONArray json = new JSONArray(data);
                        for (int i = 0; i < json.length(); i++)
                        {

                            JSONObject obj = json.getJSONObject(i);

                            String reach_us_url = obj.getString("reach_us_url");


                            Log.e("reach_us_url data",reach_us_url);

                            Uri uri = Uri.parse(reach_us_url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);


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
        });
        lyt_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost( MyConfig.URL_HELP);
                    // nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Log.e("Check Data Main Profile", data);
                    try
                    {
                        JSONArray json = new JSONArray(data);
                        for (int i = 0; i < json.length(); i++)
                        {

                            JSONObject obj = json.getJSONObject(i);

                            String contact1 = String.valueOf("contact");


                            Log.e("contact_us data",contact1);

                            Uri number = Uri.parse("tel:"+contact1);
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                            startActivity(callIntent);

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
        });



    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
       // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle("");
        //Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }
    private void showDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Please wait while loading Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_90));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_change_password) {
            Intent i = new Intent(MainActivity.this, Change_Password.class );
            startActivity(i);
            finish();
        }
        if (item.getItemId() == R.id.action_logout) {
            sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
            editor = sharedPreferences.edit();
            editor.remove( "user_name" );
            editor.remove( "password" );
            editor.commit();
            Intent i = new Intent(MainActivity.this, Login.class );
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Get_USER_DATA() { String data;

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
                    profile_image = obj.getString("profile_image");

                    Log.e("profile data",mem_id+"="+surname+"="+firstname+"="+email+"="+mobile_number);

                   /* byte[] decodedString2 = Base64.decode(profile_image, Base64.DEFAULT);
                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                    profile_view.setImageBitmap(decodedByte2);*/
                    //Log.e("decodedByte2", String.valueOf(decodedByte2));
                    String profileing= MyConfig.Base_url+""+profile_image;
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(profileing).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    profile_view.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e(" image", String.valueOf(profileing));

                    txt_user_name.setText(firstname +" "+surname);
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
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
            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_rules);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

       // ((TextView) dialog.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /// Toast.makeText(getApplicationContext(), "Button Accept Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        /*((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Button Decline Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




    @Override
    public void onBackPressed() {
        Exit();
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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, String>();

      /*  HashMapForLocalRes.put("1", "http://upturnglobal.in/gym_app/images/banner1.jpg");
        HashMapForLocalRes.put("2", "http://upturnglobal.in/gym_app/images/banner1.jpg");
        HashMapForLocalRes.put("3", "http://upturnglobal.in/gym_app/images/banner1.jpg");
        HashMapForLocalRes.put("4", "http://upturnglobal.in/gym_app/images/banner1.jpg");*/

        for(int i=0;i<listitem.size();i++){
         //   HashMapForLocalRes.put("5", R.drawable.banner5);
            HashMapForLocalRes.put(listitem.get(i).getId(), listitem.get(i).getImage_url());
            Log.e("ddddd",listitem.get(i).getImage_url());
        }
    }
    public void getBannerImagelist(){
        HashMapForLocalRes = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_Banner_Images,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getPackage(result);
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



                Log.e("select_duration",select_duration);
                params.put("duration",select_duration);
                return params;
            }
        };*/
        //  RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);
    }

    private void getPackage(JSONArray j){

        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                listitem.add(new Banner_model(json.getString("id"),
                        json.getString("image_url")
                ) );


               /* package_array= pack_list.toArray(new String[pack_list.size()]);
                String ssd= pack_list.get(i).toString();*/
                Log.e("jhgj",listitem.get(i).getImage_url());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

                      /*  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_textcolor,sub_list);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textcolor);
                        subject_spinner.setAdapter(spinnerArrayAdapter);
                        // spin_temp.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItems));
                        subject_spinner.setSelection(0);*/
        HashMapForLocalRes = new HashMap<String, String>();
        for(int i=0;i<listitem.size();i++){
            //   HashMapForLocalRes.put("5", R.drawable.banner5);
            HashMapForLocalRes.put(listitem.get(i).getId(),listitem.get(i).getImage_url());
            Log.e("ddddd",listitem.get(i).getImage_url());
        }
        for(String name : HashMapForLocalRes.keySet()){
            Log.e("sss11",name);
            TextSliderView textSliderView = new TextSliderView(MainActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType( BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(MainActivity.this);


    }
}
