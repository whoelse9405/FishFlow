package com.example.leedongjin_notebook.fishflow2;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class MultipartEntityParams{

    private int type; //1-스트링, 2-이미지,음원
    private String key; //키값.
    private AbstractContentBody body;   //Filebody, Stringbody의 수퍼클래스.
    private ContentBody contentBody;

    public MultipartEntityParams(Context context, String key, String text) throws UnsupportedEncodingException {//텍스트가 들어왔다
        type = 1;
        this.key = key;

        StringBody stringBody = new StringBody(text, Charset.forName("UTF-8"));//서버가 UTF-8. 그에 맞게 인코딩
        body = stringBody;
    }

    public MultipartEntityParams(Context context, String key, Uri fileuri) throws IOException {//파일이 들어왔다
        type = 2;
        this.key = key;

        ContentResolver cr = context.getContentResolver();
        InputStream is = cr.openInputStream(fileuri);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = is.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();
        ContentBody contentPart = new ByteArrayBody(bytes, key);

        contentBody = contentPart;

    }


    public void add(MultipartEntity reqEntity){
        if(type == 1){
            reqEntity.addPart(key, (StringBody)body);
        }
        else{
            reqEntity.addPart(key, contentBody);
        }
    }

    public int getType() {
        return type;
    }

    public AbstractContentBody getBody() {
        return body;
    }

}

