package com.example.recorder;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
public class  MainActivity extends AppCompatActivity {
    private static final int REQUEST_AUDIO_PERMISION_CODE =0 ;
    public MediaPlayer mediaPlayer;
   public   MediaRecorder recorder;
    public Chronometer chronometer;
    public  boolean isprepapared=false;
    private final   String LOG_CAT="AudioTest";
    
    public  TextView startRecording,stopRecording;
    public  String filename=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRecording=findViewById(R.id.startrecordingtv);
        stopRecording=findViewById(R.id.stoprecordingtv);
        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                        recordAudio();


                } catch (IOException e) {
                    Log.e(LOG_CAT,"preapare() failed");
                }
            }
        });
        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });


    }
    public void stopRecording() throws IOException {
        if(recorder!=null){
            try {
                recorder.stop();
            }
            catch (RuntimeException e){

            }

        }
        // play the record

    }


    private boolean recordAudio() throws IOException {
        if (checkPermission()){
            //audioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
           // audioPath +="/my_Record.3gp";

            recorder= new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioSamplingRate(16000);

            recorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/record.3gb");
            try {
                if (prepareRecorder()){
                    recorder.start();

                }
                else{
                    Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }






        }
        else {
            requestPermission();
        }

        return  true;








    }
    public boolean prepareRecorder() throws IOException {
        recorder.prepare();
        return  true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_AUDIO_PERMISION_CODE:
                if (grantResults.length>0){
                    boolean permissinToRecord=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean permissinToStore=grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if (permissinToRecord && permissinToStore){
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }

                }
        }
    }

    public  boolean checkPermission(){
        int result= ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int result1=ContextCompat.checkSelfPermission(getApplicationContext(),RECORD_AUDIO);
        return result== PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]
                        {WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},
                REQUEST_AUDIO_PERMISION_CODE);
    }

}