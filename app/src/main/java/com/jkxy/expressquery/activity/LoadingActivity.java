package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.utils.ImageLoader;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        String str = getIntent().getStringExtra("data");
        new AsyncTask<String,Void,Uri>(){

            @Override
            protected Uri doInBackground(String... params) {
                Bitmap bitmap = ImageLoader.getBitmapFromFile(params[0]);
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                return uri;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                Intent intent = new Intent();
                intent.setData(uri);
                setResult(RESULT_OK,intent);
                finish();
                super.onPostExecute(uri);
            }
        }.execute(str);
    }
}
