package com.example.ansan.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String TAG;
    EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public void onClick(View v) {
        if (isStoragePermissionGranted() == false) {

            Toast.makeText(this, "사용 불가능", Toast.LENGTH_SHORT).show();
            return;
        }
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = strPath + "/taewon/" + e1.getText().toString() + ".txt";
        File mydir = new File(strPath + "/taewon");

        switch (v.getId()) {
            case R.id.button:
                mydir.mkdir();

                Toast.makeText(this, "폴더생성", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                mydir.delete();

                Toast.makeText(getApplicationContext(), "폴더삭제", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                try {
                    FileOutputStream fos = new FileOutputStream(filename);

                    fos.write(e1.getText().toString().getBytes());
                    fos.close();

                    Toast.makeText(getApplicationContext(), "파일생성완료", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "파일생성오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.button4:

                try {
                    FileInputStream fis = new FileInputStream(filename);

                    byte arr[] = new byte[fis.available()];

                    fis.read(arr);
                    fis.close();

                    String str = new String(arr);
                    Toast.makeText(getApplicationContext(), "파일내용:" + str, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "파일내용없음!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button5:
                String str = "";
                File[] filelist = new File(strPath).listFiles();
                for(int i = 0; i < filelist.length; i++){
                    if(filelist[i].isDirectory()){
                        str += "<폴더>"+ filelist[i].toString() +"\n";

                    }
                    else{
                        str += "<파일>" + filelist[i].toString()+"\n";
                    }
                }
                e2.setText(str);

                break;
        }
    }
}
