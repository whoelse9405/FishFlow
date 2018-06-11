package com.example.leedongjin_notebook.fishflow2;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQ_GALLERY_CODE = 1;
    static final int REQ_CAMERA_CODE = 2;
    static final int REQ_IMAGE_CROP = 3;
    static final int REQ_PERMISSION_CAMERA = 4;

    static final String UPLOAD_URL = "http://13.125.229.163:9000/api/fish/";

    String mCurrentPhotoPath;   //현재 사용중인 사진의 경로(임시 파일의 경로)
    Uri photoURI, albumURI;

    boolean isAlbum = false; // Crop시 사진을 직접 찍은 것인지, 앨범에서 가져온 것인지 확인하는 플래그

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

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#ff7828"));
        }



        //helpButton
        helpButton = (ImageButton) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        //exitButton
        exitButton = (ImageButton) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //galleryButton
        galleryButton = (ImageButton) findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);                     //갤러리 불러오기 인텐트
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);               //타입 결정
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");                                          //이미지 형태만 가져오기
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_PICK_CODE);
                startActivityForResult(intent, REQ_GALLERY_CODE);
            }
        });

        //cameraButton
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExistsCameraApplication()) {
                    // Camera Application을 실행
                    boolean isCamera = isCheck("Camera");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQ_CAMERA_CODE);
                }
            }
        });

        //identificationButton
        identificationButton = (ImageButton) findViewById(R.id.identificationButton);
        identificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

                //이미지뷰가 비어있을 때
                if (imageView.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "이미지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //이미지 전송


                    //이미지뷰에 사진이 있으면 결과 화면으로 넘기기
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    intent.putExtra("image", (Bitmap) bitmap);

                    startActivity(intent);
                }

            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQ_GALLERY_CODE: {
                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageURI(data.getData());
                break;
            }
            case REQ_CAMERA_CODE: {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(imageBitmap);
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

    //권한 체크
    public boolean isCheck(String permission) {
        switch (permission) {
            case "Camera":
                Log.i("Camera Permission", "CALL");
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 다시 보지 않기 버튼을 만드려면, else 말고 이 부분에 바로 요청을 하도록 하면 됨
                    //ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION_CAMERA);

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(this)
                                .setTitle("알림")
                                .setMessage("저장소 권한은 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:com.shuvic.alumni.andokdcapp"));
                                        startActivity(intent);
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION_CAMERA);
                    }

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        return false;
                    else
                        return true;
                } else {
                    return true;
                }
        }
        return false;
    }
/*
    //파일 생성
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/pathvalue/"+ imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
        return storageDir;
    }*/


                                                                        //넣을 데이터
/*    public String requestJsonUpload(String urlStr, Context context, AddProductActivity.UploadDatas datas) throws FileNotFoundException,SocketTimeoutException,UnsupportedEncodingException,IOException {

        ArrayList<MultipartEntityParams> paremeter = new ArrayList<MultipartEntityParams>();                //post 방식으로 집어넣을 때 변수로 들어갈 파라미터들
        paremeter.add(new MultipartEntityParams(context,"image",datas.getRefernceImageUri()));

        Uri[] imageUris = datas.getDisplayImageUri();
        for(Uri uri : imageUris){
            paremeter.add(new MultipartEntityParams(context,"displayImage", uri));
        }

        if(datas.isVideoExist()) {
            paremeter.add(new MultipartEntityParams(context, "movie", datas.getMovie()));
        }
        paremeter.add(new MultipartEntityParams(context,"alias", datas.getAlias()));
        paremeter.add(new MultipartEntityParams(context,"header", datas.getHeader()));
        paremeter.add(new MultipartEntityParams(context,"contents", datas.getContents()));

        return sendFiles(urlStr, paremeter);
    }*/

/*
    private String sendFiles(String url, ArrayList<MultipartEntityParams> hashMap){
        //선언
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (MultipartEntityParams param:hashMap) {
            param.add(reqEntity);//입력
        }

        //전송 후 결과 받아오기
        String response = multipost(url, reqEntity);
        return response;
    }
*/

 /*   private static String multipost(String urlString, MultipartEntity reqEntity) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READTIMEOUT);
            conn.setConnectTimeout(CONNECTTIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Language", "utf-8");
            conn.setRequestProperty("Accept-charset", "utf-8");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            String res = readStream(conn.getInputStream());

            conn.disconnect();
            return res;

        } catch (Exception e) {
            Log.e("tag", "multipart post error " + e + "(" + urlString + ")");
            if(conn != null)
                conn.disconnect();

        }

        return null;
    }
*/
}


