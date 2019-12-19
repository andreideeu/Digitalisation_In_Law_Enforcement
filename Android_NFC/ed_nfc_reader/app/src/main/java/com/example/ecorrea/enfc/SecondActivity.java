package com.example.ecorrea.enfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Handler;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class SecondActivity extends AppCompatActivity {
    ImageView imageValidInsurance;
    ImageView imageExpiredInsurance;
    ImageView imageWarningInsurance;
    ImageView imageValidNCT;
    ImageView imageExpiredNCT;
    ImageView imageWarningNCT;
    ImageView imageValidRoadTax;
    ImageView imageExpiredRoadTax;
    ImageView imageWarningRoadTax;

    View layout;

    TextView textView2,textView3;
    public int setCode = 0;
    RelativeLayout rr1;
    private TextView mTextView;
    public  String[] cardata;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //textView2 = (TextView) findViewById(R.id.textView);

        layout = findViewById(R.id.activity_second);

        imageValidInsurance = (ImageView) findViewById(R.id.imageValidInsurance);
        imageExpiredInsurance = (ImageView) findViewById(R.id.imageExpiredInsurance);
        imageWarningInsurance = (ImageView) findViewById(R.id.imageWarningInsurance);
        imageValidNCT = (ImageView) findViewById(R.id.imageValidNCT);
        imageExpiredNCT = (ImageView) findViewById(R.id.imageExpiredNCT);
        imageWarningNCT = (ImageView) findViewById(R.id.imageWarningNCT);
        imageValidRoadTax = (ImageView) findViewById(R.id.imageValidRoadTax);
        imageExpiredRoadTax = (ImageView) findViewById(R.id.imageExpiredRoadTax);
        imageWarningRoadTax = (ImageView) findViewById(R.id.imageWarningRoadTax);

        imageExpiredInsurance.setVisibility(View.INVISIBLE);
        imageExpiredNCT.setVisibility(View.INVISIBLE);
        imageExpiredRoadTax.setVisibility(View.INVISIBLE);
        imageValidInsurance.setVisibility(View.INVISIBLE);
        imageValidNCT.setVisibility(View.INVISIBLE);
        imageValidRoadTax.setVisibility(View.INVISIBLE);
        imageWarningInsurance.setVisibility(View.INVISIBLE);
        imageWarningNCT.setVisibility(View.INVISIBLE);
        imageWarningRoadTax.setVisibility(View.INVISIBLE);

        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText(""); // REG PLATE

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed () {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "1");

        //mTextView.setText("onResume:");
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        //enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //===================================
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("onPause", "1");

        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "1");

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Log.d("onNewIntent", "2");

            //if(getIntent().hasExtra(NfcAdapter.EXTRA_TAG)){

            Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tagN != null) {
                Log.d(TAG, "Parcelable OK");
                NdefMessage[] msgs;
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                byte[] payload = dumpTagData(tagN).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
                Log.d("this is this: "+TAG, msgs[0].toString());

            } else {
                Log.d(TAG, "Parcelable NULL");
            }


            Parcelable[] messages1 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages1 != null) {
                Log.d(TAG, "Found " + messages1.length + " NDEF messages");
            } else {
                Log.d(TAG, "Not EXTRA_NDEF_MESSAGES");
            }

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {

                Log.d("onNewIntent:", "NfcAdapter.EXTRA_TAG");

                Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (messages != null) {
                    Log.d(TAG, "Found " + messages.length + " NDEF messages");
                }
            } else {
                Log.d(TAG, "Write to an unformatted tag not implemented");
            }


            //mTextView.setText( "NFC Tag\n" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG)));
        }
    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        //sb.append("Tag ID HEX: ").append(getHex(id)).append("\n");
        sb.append("ID").append("\n").append(getDec(id)).append("\n");
        // sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        Log.i("Response:", "Im here");

        int num = 0;

        setCode = 2;

        if (getDec(id) == 2829966771L){
            num = 384348358;
        }


    
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cardata = response.split(" ");
                Log.i("Response:",response);

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

                setCode = 1;
            }
        });

        ExampleRequestQueue.add(ExampleStringRequest);

        // DELAY

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                for (int i=0;i>20;i++)
                Log.i("waiting.",".");
            }
        }, 1000);

        Log.i("Code:", Integer.toString(setCode));


        if (setCode == 2){

           layout.setBackgroundResource(R.drawable.afs1);

            TextView textView = (TextView) findViewById(R.id.textView4);

           // textView.setText(cardata[4]); // REG PLATE


            // ------------------- Check NCT --------------------------

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            Date currentDate = Calendar.getInstance().getTime();

            Log.i("Current Date:",currentDate.toString());

            Date date1= null;
            try {
                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(cardata[7]);
                Log.i("Date Stored:",date1.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (currentDate.after(date1)){

                //show X
                imageExpiredNCT.setVisibility(View.VISIBLE);
                Log.i("NCT_State:", "Expired bc "+cardata[7]);
            }

           else if (currentDate.before(date1)){

                //show valid
                imageValidNCT.setVisibility(View.VISIBLE);
                Log.i("NCT_State:", "Valid bc "+ cardata[7]);
            }

            else if (currentDate.equals(date1)){

                //Caution
                imageWarningNCT.setVisibility(View.VISIBLE);
                Log.i("NCT_State:", "Warning bc "+cardata[7]);
            }
            else {
                //ERROR
            }


            // -------------------------- Check Insurance --------------------------

            Date date2= null;
            try {
                date2 = new SimpleDateFormat("dd/MM/yyyy").parse(cardata[10]);
                Log.i("Date Stored:",date2.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (currentDate.after(date2)){

                //show X
                imageExpiredInsurance.setVisibility(View.VISIBLE);
                Log.i("Insurance_State:", "Expired bc "+cardata[10]);
            }

            else if (currentDate.before(date2)){

                //show valid
                imageValidInsurance.setVisibility(View.VISIBLE);
                Log.i("Insurance_State:", "Valid bc "+ cardata[10]);
            }

            else if (currentDate.equals(date2)){

                //Caution
                imageValidInsurance.setVisibility(View.VISIBLE);
                Log.i("Insurance_State:", "Warning bc "+cardata[10]);
            }
            else {
                //ERROR
            }


            // -------------------------- Check Road Tax --------------------------

            Date date3= null;
            try {
                date3 = new SimpleDateFormat("dd/MM/yyyy").parse(cardata[13]);
                Log.i("Date Stored:",date3.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (currentDate.after(date3)){
                //show X
                imageExpiredRoadTax.setVisibility(View.VISIBLE);
                Log.i("RoadTax_State:", "Expired bc "+cardata[13]);
            }

            else if (currentDate.before(date3)){

                //show valid
                imageValidRoadTax.setVisibility(View.VISIBLE);
                Log.i("RoadTax_State:", "Valid bc "+ cardata[13]);
            }

            else if (currentDate.equals(date3)){

                //Caution
                imageWarningRoadTax.setVisibility(View.VISIBLE);
                Log.i("RoadTax_State:", "Warning bc "+cardata[13]);
            }
            else {
                //ERROR
            }

                setCode = 0;
        }

        else {

           layout.setBackgroundResource(R.drawable.notreg);

            //view.setBackground(R.drawable.afs);

            imageExpiredInsurance.setVisibility(View.INVISIBLE);
            imageExpiredNCT.setVisibility(View.INVISIBLE);
            imageExpiredRoadTax.setVisibility(View.INVISIBLE);
            imageValidInsurance.setVisibility(View.INVISIBLE);
            imageValidNCT.setVisibility(View.INVISIBLE);
            imageValidRoadTax.setVisibility(View.INVISIBLE);
            imageWarningInsurance.setVisibility(View.INVISIBLE);
            imageWarningNCT.setVisibility(View.INVISIBLE);
            imageWarningRoadTax.setVisibility(View.INVISIBLE);
        }

        String prefix = "android.nfc.tech.";

        return sb.toString();
    }


    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    // ----------------Encyrpt string to md5----------------

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    // ----------------GET REQUEST---------------------------


    public int getcarData(String url){

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setCode = 2;
                cardata = response.split(" ");

                Log.i("Response:", "Im here");

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

                setCode = 1;
            }
        });

        ExampleRequestQueue.add(ExampleStringRequest);

        return setCode;
    }


    String getcurrentTime(){


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date
        Log.d("MainActivity", "Current Timestamp: " + currentDateTime);

        return currentDateTime;

    }
}
