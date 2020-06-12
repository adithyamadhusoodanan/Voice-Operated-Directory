package com.example.itminorproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private EditText name,ask;
    private TextView enter,output;
    private Button login;
    public Button dep;
    public int i;
    private final int REQ_CODE = 100;
    String res;
    int k=0;

    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EditText to enter the name
        //name = (EditText) findViewById(R.id.name1);
        enter = (TextView) findViewById(R.id.enter1);
        output = (TextView) findViewById(R.id.output1);
        //EditText to enter the department
        // ask=(EditText) findViewById(R.id.ask1);
        login = (Button) findViewById(R.id.login1);
        dep=(Button) findViewById(R.id.department);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        login.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }

            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {       i=1;
                    Openclass.e="";



                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi state your Name");
                            try {

                                startActivityForResult(intent, REQ_CODE);

                            } catch (ActivityNotFoundException a) {
                                Toast.makeText(getApplicationContext(),
                                        "Sorry your device not supported",
                                        Toast.LENGTH_SHORT).show();
                            }



                try {
                    Thread.sleep(2* 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }


                //speak(Openclass.r);

                Openclass.r=res;


                //login();
                try {
                    new MyAsyncTask().execute("").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (k % 2 == 1) {
                    output.setText(Openclass.link);
                    enter.setText(Openclass.s);


                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        public void run() {
                            if (Openclass.s != null)
                                speak(Openclass.r);
                        }
                    }, 3000);   //

                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        public void run() {
                            speak("Your details are being fetched");
                        }
                    }, 1000);


                    if (Openclass.r.length() > 50) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                dep.performClick();
                            }
                        }, 6000);   //
                    }

                }

                k = k + 1;

                if(k%2==1) {
                    login.performClick();

                }
            }
        });

        dep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                i=2;
                //Openclass.s = name.getText().toString();
                //Openclass.e=ask.getText().toString();

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi state your Department");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }



                //speak(Openclass.r);


                    //login();
                    try {
                        new MyAsyncTask().execute("").get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    res = result.get(0).toString();
                    if(i==1)
                        Openclass.s=res;
                    if(i==2)
                        Openclass.e=res;


                }
                break;
            }
        }
    }



    public void speak(String tospeak) {
        mTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }



}


class MyAsyncTask extends AsyncTask<String, Integer, String> {

    public MyAsyncTask() {
        // TODO Auto-generated constructor stub
    }

    protected void onPreExecute(){
    }


    @Override
    protected String doInBackground(String... arg0) {
        try{
            String name =Openclass.s;
            String dept=Openclass.e;
            String link = "https://unobtained-hatchets.000webhostapp.com/voice.php?name="+name+"&department="+dept;
            Openclass.link=link;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            Openclass.r=sb.toString();
            return sb.toString();
        } catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

}
