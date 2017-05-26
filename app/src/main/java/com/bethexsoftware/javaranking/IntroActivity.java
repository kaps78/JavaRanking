package com.bethexsoftware.javaranking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Hardik on 9/4/2016.
 */

public class IntroActivity extends AppCompatActivity{
    Button btnNext;
    TextView txtLastDayRankGrade;
    TextView txtLastAlltimeRankGrade;
    ProgressDialog prgDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        invokeWS(FirebaseAuth.getInstance().getCurrentUser().getUid());

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(i);
            }
        });
    }
    public void invokeWS(String userid){
        // Show Progress Dialog
        txtLastDayRankGrade = (TextView) findViewById(R.id.txtLastDayRankGrade);
        txtLastAlltimeRankGrade = (TextView) findViewById(R.id.txtLastAllTimeRankGrade);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("A sec please, getting your last results...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String getURL= "https://javaranking-bethexsoftware.rhcloud.com/JR/getL/" + userid;
        Log.i("JavaRanking", "Get URL:" + getURL);

        client.get(getURL, new RequestParams(), new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int status, Header[] header, byte[] response) {
                // Hide Progress Dialog
                prgDialog.hide();
                //ObjectMapper objMap = new ObjectMapper();

                try {
                    Log.i("JavaRanking", new String(response));
                    UserSummaryClientEntity us = new Gson().fromJson(new String(response), UserSummaryClientEntity.class);
                    if (us.getUsDayRank()>0) {
                        txtLastDayRankGrade.setText("" + (us.getUsDayRank() + "/" + us.getUsDayRankBase()) + ", " + us.getUsDayGrade());
                        txtLastAlltimeRankGrade.setText("" + (us.getUsAllTimeRank() + "/" + us.getUsAllTimeRankBase()) + ", " + us.getUsAllTimeGrade());
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] x,
                                  byte[] content, Throwable error) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
