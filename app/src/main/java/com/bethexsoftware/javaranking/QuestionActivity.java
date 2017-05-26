package com.bethexsoftware.javaranking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Hardik on 6/21/2016.
 */

/*
* API check code - for Material design features
* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//material} else {//non-material}
* */
public class QuestionActivity extends AppCompatActivity{
    public static final String PREFS_NAME = "JR_PREFS";
    public static final String JSON_RESP = "JsonResp";
    public static final String CURR_Q = "CurrQ";
    public static final String ANSWER_1 = "Answer1";
    public static final String ANSWER_2 = "Answer2";
    public static final String ANSWER_3 = "Answer3";
    public static final String QUESTION_1 = "Question1";
    public static final String QUESTION_2 = "Question2";
    public static final String QUESTION_3 = "Question3";
    Integer CurrQID;
    public static final int MAX_Q = 3;
    // Progress Dialog Object
    SharedPreferences  settings;
    ProgressDialog prgDialog;
    TextView txtQues;
    RadioGroup optionsGroup;
    RadioButton optOne;
    RadioButton optTwo;
    RadioButton optThree;
    RadioButton optFour;
    Button btnNext;
    int intCurrQ=0;
    private TextView textTimer;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    private long startTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //TextView txtSignin;
        //txtSignin = (TextView) findViewById(R.id.txt_signin);
        //txtSignin.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        textTimer = (TextView) findViewById(R.id.textTimer);
        txtQues = (TextView) findViewById(R.id.txt_question);
        optionsGroup = (RadioGroup) findViewById(R.id.optionsGroup);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Selecting today's three random questions for you...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.i("JavaRanking","CurrQ: " + settings.getInt(CURR_Q,0));
        if (settings.getInt(CURR_Q,0)==0) {
            Log.i("JavaRanking","True - settings.getInt(CURR_Q,0)==0");
            try {
                invokeWS(new RequestParams());
            }catch(Exception e)
            {e.printStackTrace();}
        }
        else if (settings.getInt(CURR_Q,0)>MAX_Q){
            Log.i("JavaRanking","True - settings.getInt(CURR_Q,0)>MAX_Q");
            invokeWS(new RequestParams());
        }
        else if (settings.getInt(CURR_Q,0)>0 && settings.getInt(CURR_Q,0)<=MAX_Q)
            setCurrQ();
        //myHandler.postDelayed(updateTimerMethod, 0);
        new CountDownTimer(51000, 10000) {

            public void onTick(long millisUntilFinished) {
                textTimer.setText("Quick Response Opportunity: " + millisUntilFinished / 1000 + "+");
            }

            public void onFinish() {
                textTimer.setText("");
            }
        }.start();
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Capturing response
                Log.i("JavaRanking","Curr QID:" + CurrQID.toString());
                int intSelection;
                try {
                    intSelection = optionsGroup.findViewById(optionsGroup.getCheckedRadioButtonId()).getId();
                }catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(),"Please select an option",Toast.LENGTH_LONG);
                    return;
                }
                Log.i("JavaRanking", "Curr Selection Opt ID:" + intSelection);

                switch (settings.getInt(CURR_Q,0)) {
                    case 0:
                        break;
                    case 1:
                        settings.edit() .putInt(QUESTION_1, CurrQID)
                                        .putInt(ANSWER_1,intSelection).commit();
                        break;
                    case 2:
                        settings.edit() .putInt(QUESTION_2,CurrQID)
                                        .putInt(ANSWER_2,intSelection).commit();
                        break;
                    case 3:
                        settings.edit() .putInt(QUESTION_3,CurrQID)
                                        .putInt(ANSWER_3,intSelection).commit();
                        break;
                }

                //set CurrQ to next question
                Intent i;
                settings.edit().putInt(CURR_Q,settings.getInt(CURR_Q,0)+1).commit();
                if (settings.getInt(CURR_Q,0)>MAX_Q) {
                    i = new Intent(getApplicationContext(), ThanksActivity.class);
                    //settings.edit().putInt(CURR_Q,1).commit();
                }
                else
                    i = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(i);
            }
        });

    }

    public void setCurrQ(){
        String response = settings.getString(JSON_RESP, "");
        Log.i("JavaRanking", response);
        Log.i("JavaRanking", "CurrQ: " + settings.getInt(CURR_Q,0));
        if (response.equals("[]")){
            //Log.i("JavaRanking",response + " Length:" + qs.length + " qs[0].getqActive()" + qs[0].getqActive());

            Log.i("JavaRanking","Done for today's questions, redirecting the user to home");
            Toast.makeText(getApplicationContext(), "Done with your today's questions? Try tomorrow please...",
                    Toast.LENGTH_LONG).show();
            Intent i;
            i = new Intent(getApplicationContext(),IntroActivity.class);
            startActivity(i);
            return;
        }

        else {
            QuestionsEntity[] qs;

            Log.i("JavaRanking", response);
            Integer CurrQ = settings.getInt(CURR_Q, MAX_Q + 1);
            Log.i("JavaRanking", CurrQ.toString());

            if (CurrQ <= MAX_Q) {
                qs = new Gson().fromJson(response, QuestionsEntity[].class);
                txtQues.setText(qs[CurrQ - 1].getqText());
                CurrQID = qs[CurrQ - 1].getQid();
                Log.i("JavaRanking", qs[CurrQ - 1].getqText());
                int i = 0;
                for (QuestionsOptionsEntity s : qs[CurrQ - 1].getQuestionsOptions()) {
                    ((RadioButton) optionsGroup.getChildAt(i)).setText(s.getqOptText());
                    ((RadioButton) optionsGroup.getChildAt(i)).setId(s.getqOptId());
                    i++;
                }
            }
        }
    }
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();
        Log.i("JavaRanking","User ID:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.i("JavaRanking","About to call the web service");
        /*try {*/
            client.get("https://javaranking-bethexsoftware.rhcloud.com/JR/getQ/" + FirebaseAuth.getInstance().getCurrentUser().getUid(), new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'

                @Override
                public void onSuccess(int status, Header[] header, byte[] response) {
                    // Hide Progress Dialog
                    Log.i("JavaRanking", "In Success");
                    prgDialog.hide();
                    //ObjectMapper objMap = new ObjectMapper();

                    try {
                        String resp = new String(response);
                        Log.i("JavaRanking", resp);
                        settings.edit().putString(JSON_RESP, resp).commit();
                        if (!resp.equals("[]"))
                            settings.edit().putInt(CURR_Q, 1).commit();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    setCurrQ();


                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] x,
                                      byte[] content, Throwable error) {
                    // Hide Progress Dialog
                    Log.i("JavaRanking", "In failure, statuscode:" + statusCode);
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
                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.i("JavaRanking","In onRetry... retryNo" + retryNo);
                }
            });
        /*}
        catch (Exception e)
        {e.printStackTrace();}*/
    }
    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            startTime = SystemClock.uptimeMillis();
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            textTimer.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };

}


