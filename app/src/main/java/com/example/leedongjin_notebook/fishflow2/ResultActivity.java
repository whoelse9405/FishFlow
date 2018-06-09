package com.example.leedongjin_notebook.fishflow2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    final private int REQ_RESULT_CODE=1;

    private WebView webView;
    private WebSettings webSettings;

    ImageButton helpButton;
    ImageButton backButton;
    ImageView totalImageView;

    Button button;
    TextView textView;

    final String urlStr = "http://13.125.229.163:9000/api/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#ff7828"));
        }

        textView=(TextView)findViewById(R.id.textView);
        button=(Button)findViewById(R.id.rebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequst();
            }
        });

        if(AppHelper.requestQueue==null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        webView = (WebView)findViewById(R.id.webView);


        //helpButton
        helpButton=(ImageButton) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
                startActivity(intent);
            }
        });

        //backButton
        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //totalImageView
        //Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        totalImageView = (ImageView)findViewById(R.id.totalImageView);

    }

    public void sendRequst(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //String jsonData = response.substring(1,response.length()-1);
                        //textView.setText(jsonData+"\n");
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> "+error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add((request));
    }


    public void processResponse(String response){
            Gson gson = new Gson();
            UserList[] userList = gson.fromJson(response,UserList[].class);

            if(userList!=null){
                println("url : "+userList[0].url);
                println("username : "+userList[0].username);
                println("email : "+userList[0].email);
                println("groups : "+userList[0].groups.isEmpty()+"");
            }
    }


    public void println(String data){
        textView.append(data+"\n");
    }

}
