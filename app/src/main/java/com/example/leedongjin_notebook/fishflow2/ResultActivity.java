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
import android.widget.ImageButton;
import android.widget.ImageView;

public class ResultActivity extends AppCompatActivity {

    final private int REQ_RESULT_CODE=1;

    private WebView webView;
    private WebSettings webSettings;

    ImageButton helpButton;
    ImageButton backButton;
    ImageView totalImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#ff7828"));
        }

        //웹뷰세팅
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
}
