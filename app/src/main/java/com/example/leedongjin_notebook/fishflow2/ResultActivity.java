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

    private WebSettings webSettings;

    ImageButton helpButton;
    ImageButton backButton;
    ImageView imageView;

    Button reportButton;

    final String urlStr = "http://13.125.229.163:9000/api/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#ff7828"));
        }

        Intent intent = getIntent();    //인텐트 받아오기

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
        imageView = (ImageView)findViewById(R.id.totalImageView);
        Bitmap bitmap = (Bitmap)intent.getExtras().get("image");
        imageView.setImageBitmap(bitmap);

        reportButton=(Button)findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
                startActivity(intent);
            }
        });

    }

}
