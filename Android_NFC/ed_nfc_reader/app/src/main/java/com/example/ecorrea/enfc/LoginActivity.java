package com.example.ecorrea.enfc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {


    private static final String username = "admin";
    private static final String password = "1234";
    TextView textView2;
    String[] cardata;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView2 = (TextView) findViewById(R.id.textView2);


       textView2.setText(getcurrentTime());


    }

    public void sendToView(View view) {

        EditText et = (EditText) findViewById(R.id.editText2);
        EditText et1 = (EditText) findViewById(R.id.editText);

        if(!et.getText().toString().matches("") && !et1.getText().toString().matches("")) {

            String password_input = et.getText().toString();
            String username_input = et1.getText().toString();


            if (password_input.equals("1234") && username_input.equals("admin")) {

                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
            }
        }


        else {
            Toast.makeText(LoginActivity.this, "Enter a Password", Toast.LENGTH_LONG).show();

        }

    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    void getcarData(String url){

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cardata = response.split(" ");

               // Log.i("Response:",response);

                //textView2.setText(cardata[13]);

            /*
            2 - Tag Id
            4 - Car Reg
            7 - NCT
            10 - Insurance
            13 - Road Tax
             */

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                textView2.setText("Response: " + error.toString());

            }
        });

        ExampleRequestQueue.add(ExampleStringRequest);


    }


    String getcurrentTime(){


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        Log.d("MainActivity", "Current Timestamp: " + format);

        return format;

    }

}
