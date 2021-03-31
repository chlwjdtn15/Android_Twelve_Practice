package com.example.week10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    String Tag = "MainActivity";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
    }

    public void getData(View view) {

        String url = "http://www.google.com";


        
        if (isConnected()){
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    String data = downloadData(url);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(data);

                        }
                    });

                }
            }).start();



        }
        else 
        {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
        }


    }


    private boolean isConnected() {


        boolean res = false;

        ConnectivityManager conMger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMger.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            res = true;
            


        }else {
            res = false;
        }


        return res;

    }


    private String downloadData(String url) {

        InputStream is = null;
        String data = "";

        try {
            URL myURl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myURl.openConnection();
            con.setRequestMethod("GET");

            con.connect();

            int response = con.getResponseCode();
            Log.d(Tag, "Download Data: response code = " + response);

            is = con.getInputStream();

            data = processResponse(is);


        } catch (MalformedURLException e) {
            Log.d(Tag, "download Data: " + e.getMessage());
        } catch (IOException e) {
            Log.d(Tag, "download Data: " + e.getMessage());
        }

        return data;
    }


    private String processResponse(InputStream is) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        StringBuilder sb =new StringBuilder();
        while (( line = br.readLine())!= null) {

            Log.d(Tag, "processResponse: Line = " + line);
            sb.append(line);


        }

        return sb.toString();

    }
}