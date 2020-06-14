package com.example.voicedirectory;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import android.speech.tts.TextToSpeech;
public class MainActivity extends Activity {

    TextToSpeech t;
    public String name,ask,disp;
    public TextView enter,output;
    public Button dep;
    private final int REQ_CODE = 100;
    private ImageView speak;
    public View v1;
    public int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        t= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result =t.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speak = findViewById(R.id.speak);
        //BUTTON TO ENTER THE NAME
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText("");
                ask=null;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    v1=v;
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    public void run() {
                        singleToneClass singleToneClass = com.example.voicedirectory.singleToneClass.getInstance();
                        disp=singleToneClass.getData();
                        speaknow(disp);
                    }
                }, 10000);
            }
        });
        //BUTTON TO ENTER DEPARTMENT
        dep = findViewById(R.id.dep1);
        dep.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         ask=null;
                                         Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                 RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                                         intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                                         try {
                                             v1=v;
                                             flag=1;
                                             startActivityForResult(intent, REQ_CODE);
                                         } catch (ActivityNotFoundException a) {
                                             Toast.makeText(getApplicationContext(),
                                                     "Sorry your device not supported",
                                                     Toast.LENGTH_SHORT).show();
                                         }
                                         Handler handler1 = new Handler();
                                         handler1.postDelayed(new Runnable() {
                                             public void run() {
                                                 singleToneClass singleToneClass = com.example.voicedirectory.singleToneClass.getInstance();
                                                 disp=singleToneClass.getData();
                                                 speaknow(disp);
                                             }
                                         }, 10000);
                                     }
                                 });
        enter = (TextView) findViewById(R.id.enter1);
        output = (TextView) findViewById(R.id.output1);
    }
        @Override
        protected void onActivityResult(int requestCode,int resultCode,Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQ_CODE: {
                    if (resultCode==RESULT_OK && null!= data) {
                        ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if(flag==0) {
                            name = result.get(0).toString();
                            if (name != null) {
                                login(v1);
                            }
                        }
                        else {
                            ask = result.get(0).toString();
                            if (ask != null) {
                                flag=0;
                                login(v1);
                            }
                        }
                    }
                    break;
                }
            }

        }
        //FUNCTION TO LOGIN WITH THE CORRESPONDING CREDENTIALS
    public void login(View view) {
        String name2 = name;
        String dept = ask;
        SigninActivity a = new SigninActivity(this, output,disp);
        a.execute(name2, dept);
    }
              //FUNCTION TO CONVERT TEXT TO SPEECH
    public void speaknow(String toSpeak)
    {
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
