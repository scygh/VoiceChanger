package com.scy.voicechanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1000;

    List<String> permissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS);
        } else {
            toSaveWav();
        }

    }

    private void toSaveWav() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream =  getResources().openRawResource(R.raw.jaguar);
                    File file = new File(Environment.getExternalStorageDirectory().getPath(),"jaguar.wav");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[10];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len=inputStream.read(buffer))!=-1){
                        outputStream.write(buffer,0,len);
                    }
                    byte[] bs = outputStream.toByteArray();
                    fileOutputStream.write(bs);
                    outputStream.close();
                    inputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("getPath",e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1000:
                if (grantResults.length > 0) {
                    for(int result: grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "你拒绝了权限，功能不可用", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        } else {
                            toSaveWav();
                        }
                    }
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public void startChange(View view) {
        String path = Environment.getExternalStorageDirectory().getPath()+ File.separator+"jaguar.wav";
        Log.i("getPath",path);
        File file = new File(path);
        if (!file.exists()) {
            Log.e("nofile", "没有文件");
            return;
        }
        switch (view.getId()) {
            // 普通
            case R.id.btn_normal:
                VoiceTools.changeVoice(path, 0);
                break;
            // 萝莉
            case R.id.btn_luoli:
                VoiceTools.changeVoice(path, 1);
                break;// 萝莉
            // 大叔
            case R.id.btn_dashu:
                VoiceTools.changeVoice(path, 2);
                break;
            // 惊悚
            case R.id.btn_jingsong:
                VoiceTools.changeVoice(path, 3);
                break;
            // 搞怪
            case R.id.btn_gaoguai:
                VoiceTools.changeVoice(path, 4);
                break;
            // 空灵
            case R.id.btn_kongling:
                VoiceTools.changeVoice(path, 5);
                break;

        }

    }


}
