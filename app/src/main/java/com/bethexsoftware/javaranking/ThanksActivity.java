package com.bethexsoftware.javaranking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by Hardik on 8/6/2016.
 */
public class ThanksActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "JR_PREFS";
    SharedPreferences settings;
    public static final String ANSWER_1 = "Answer1";
    public static final String ANSWER_2 = "Answer2";
    public static final String ANSWER_3 = "Answer3";
    public static final String QUESTION_1 = "Question1";
    public static final String QUESTION_2 = "Question2";
    public static final String QUESTION_3 = "Question3";
    public static final String JSON_RESP = "JsonResp";
    ProgressDialog prgDialog;
    TextView txtCorrectAnswers;
    TextView txtTodaysBaseScore;
    TextView txtSwiftResponseScore;
    TextView txtTotalScore;
    TextView txtDayRank;
    TextView txtAllTimeRank;
    Button btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Selecting today's three random questions for you...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        RequestParams params = new RequestParams();
        params.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());

        params.put(QUESTION_1, settings.getInt(QUESTION_1, 0));
        params.put(QUESTION_2, settings.getInt(QUESTION_2, 0));
        params.put(QUESTION_3, settings.getInt(QUESTION_3, 0));
        params.put(ANSWER_1, settings.getInt(ANSWER_1, 0));
        params.put(ANSWER_2, settings.getInt(ANSWER_2, 0));
        params.put(ANSWER_3, settings.getInt(ANSWER_3, 0));
        invokeWS(FirebaseAuth.getInstance().getCurrentUser().getUid());

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(i);
            }
        });
    }

    public void invokeWS(String userid){
        // Show Progress Dialog
        txtCorrectAnswers = (TextView) findViewById(R.id.txtCorrectAnswers);
        txtTodaysBaseScore = (TextView) findViewById(R.id.txtTodaysBaseScore);
        txtSwiftResponseScore = (TextView) findViewById(R.id.txtSwiftResponseScore);
        txtTotalScore = (TextView) findViewById(R.id.txtTotalScore);
        txtDayRank = (TextView) findViewById(R.id.txtDayRank);
        txtAllTimeRank = (TextView) findViewById(R.id.txtAllTimeRank);

        prgDialog.setMessage("Submitting answers and waiting for results...");
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String getURL= "https://javaranking-bethexsoftware.rhcloud.com/JR/setA/" + userid + "/" +
                settings.getInt(QUESTION_1, 0) + "/" +
                settings.getInt(QUESTION_2, 0) + "/" +
                settings.getInt(QUESTION_3, 0) + "/" +
                settings.getInt(ANSWER_1, 0) + "/" +
                settings.getInt(ANSWER_2, 0) + "/" +
                settings.getInt(ANSWER_3, 0);
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
                    settings.edit().putString(JSON_RESP, new String(response));
                    UserSummaryClientEntity us = new Gson().fromJson(new String(response), UserSummaryClientEntity.class);
                    txtCorrectAnswers.setText(us.getUsCorrectAnswers() + "/3");
                    txtTodaysBaseScore.setText("" + (us.getUsDayScore()-us.getUsSwiftReplyBonus()));
                    txtSwiftResponseScore.setText("" + us.getUsSwiftReplyBonus());
                    txtTotalScore.setText("" + us.getUsDayScore());
                    txtDayRank.setText("" + (us.getUsDayRank() + "/" + us.getUsDayRankBase()) + ", " +us.getUsDayGrade());
                    txtAllTimeRank.setText("" + (us.getUsAllTimeRank() + "/" + us.getUsAllTimeRankBase())+ ", " + us.getUsAllTimeGrade());

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
