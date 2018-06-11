package com.example.leedongjin_notebook.fishflow2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class VolleySingleton {
    private static VolleySingleton instance = null;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return this.requestQueue;
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void uploadImage(final Context context,String url,final Bitmap bitmap){
        VolleySingleton.getInstance(context);

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(context, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(context, ""+volleyError, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //get bitmap
                //BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                //Bitmap bitmap = bitmapDrawable.getBitmap();
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                params.put("image", image);
                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        VolleySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }

    public void getImage(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                //성공했을때 실행할 동작
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response  : 받은것
                    }
                },
                //에러처리
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.toString() : 에러메세지
                    }
                });

        requestQueue.add(stringRequest);
    }
}

