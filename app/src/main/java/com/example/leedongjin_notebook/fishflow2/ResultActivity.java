package com.example.leedongjin_notebook.fishflow2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ResultActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;

    ImageButton helpButton;
    ImageButton backButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //imageView
        imageView = (ImageView)findViewById(R.id.imageView);

    }
}
