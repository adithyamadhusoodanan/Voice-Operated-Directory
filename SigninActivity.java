package com.example.voicedirectory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
public class SigninActivity extends AsyncTask<String,String,String>{
    private TextView output;
    private Context context;
    public String disp;
    public SigninActivity(Context context,TextView output,String disp) {
        this.context = context;
        this.output = output;
        this.disp=disp;
    }
    protected void onPreExecute(){
    }
    @Override
    protected String doInBackground(String... arg0) {
            try{
                String name = (String)arg0[0];
                String dept=(String)arg0[1];
                String link;
                if(dept==null)
                    link="https://unobtained-hatchets.000webhostapp.com/voice.php?name="+name;
                else
                    link="https://unobtained-hatchets.000webhostapp.com/voice.php?name="+name+"&department="+dept;
                link=link.replaceAll(" ","%20");
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
                return sb.toString();
            } catch(Exception e){
                return ("Exception: " + e.getMessage());
            }
        }
    @Override
    protected void onPostExecute(String result){
        singleToneClass s = com.example.voicedirectory.singleToneClass.getInstance();
        if(result.charAt(0)>=48&&result.charAt(0)<=58) {
            result = result.replaceAll(".", "$0 ");
            this.output.setText(result);
            s.setData(result);
        }
        else
            s.setData(result);
    }

}
