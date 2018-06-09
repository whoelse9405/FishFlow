package com.example.leedongjin_notebook.fishflow2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final private int REQ_PICK_CODE=1;
    final private int REQ_CAMERA_CODE=2;


    ImageButton helpButton;
    ImageButton exitButton;
    ImageButton galleryButton;
    ImageButton identificationButton;
    ImageButton cameraButton;
    ImageView imageView;
    File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.parseColor("#ff7828"));
        }



        //helpButton
        helpButton=(ImageButton) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
                startActivity(intent);
            }
        });

        //exitButton
        exitButton=(ImageButton) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //galleryButton
        galleryButton=(ImageButton)findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);                     //갤러리 불러오기 인텐트
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);               //타입 결정
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");                                          //이미지 형태만 가져오기
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_PICK_CODE);
                startActivityForResult(intent, REQ_PICK_CODE);
            }
        });

        //cameraButton
        cameraButton=(ImageButton)findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( isExistsCameraApplication() ) {
                    // Camera Application을 실행
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQ_CAMERA_CODE);
                }
            }
        });

        //identificationButton
        identificationButton=(ImageButton)findViewById(R.id.identificationButton);
        identificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
            }
        });

        //imageView=(ImageView)findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK) return;

        switch (requestCode)
        {
            case REQ_PICK_CODE:{
                imageView=(ImageView)findViewById(R.id.imageView);
                imageView.setImageURI(data.getData());
                break;
            }
            case REQ_CAMERA_CODE:{
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(imageBitmap);
                break;
            }
        }

    }

    private boolean isExistsCameraApplication() {

        // Android의 모든 Application을 얻어온다
        PackageManager packageManager = getPackageManager();

        // Camera Application
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // MediaStore.ACTION_IMAGE_CAPTURE의 Intent를 처리할 수 있는 Application 정보 가져옴
        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);

        return cameraApps.size() > 0;
    }





}
