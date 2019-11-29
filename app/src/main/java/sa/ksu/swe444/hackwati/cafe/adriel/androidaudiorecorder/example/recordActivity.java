package sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.droidsonroids.gif.GifImageView;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;

import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.model.AudioChannel;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.model.AudioSource;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class recordActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.3gp";

    private Button uploadBtn;
    public BottomNavigationView navView;
    private GifImageView shining;
    private Uri audioFatimah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity_asma);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //NavView
        navView = findViewById(R.id.navigation_record);
        navView.setSelectedItemId(R.id.navigation_record);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(recordActivity.this, recordActivity.class));
                        break;
                    case R.id.navigation_subscription:
                        startActivity(new Intent(recordActivity.this, MainActivity.class));
                        break;
                    case R.id.navigation_explore:
                        startActivity(new Intent(recordActivity.this, ExploreActivity.class));
                        break;

                }// end of switch
                return true;
            }
        });

        //Buttons:

        shining = findViewById(R.id.shining);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shining.setVisibility(View.VISIBLE);

                    if (ContextCompat.checkSelfPermission(recordActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        selectAudio();
                    }
                    else
                        ActivityCompat.requestPermissions(recordActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);


                }
        });
    }

    private void selectAudio() {
        Intent intent=new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "تم تسجيل الصوت بنجاح", Toast.LENGTH_SHORT).show();

                Intent intint = new Intent(recordActivity.this, recordStoryInfo.class);
                intint.putExtra("path", AUDIO_FILE_PATH);
                startActivity(intint);
                Log.d(TAG, "hellobff"+AUDIO_FILE_PATH);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "تم إلغاء التسجيل", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==99&&resultCode==RESULT_OK&&data!=null){
            audioFatimah=data.getData();


            String fatimah=audioFatimah.getPath()+"/recorded_audio.*";
            Log.d("here","click"+fatimah);
            Intent intent = new Intent(recordActivity.this,recordStoryInfo.class);
            intent.putExtra("FatimahAudio",audioFatimah);
            intent.putExtra("isFull",true);
            startActivity(intent);

        }
        else {
            Toast.makeText(recordActivity.this, "نرجوا اختيار ملف أو تسجيل قصه", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 9:{
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    selectAudio();
                } else
                    Toast.makeText(recordActivity.this, "Permissions Denied to upload audio file", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void recordAudio(View v) {
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(this, R.color.green_hak))
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}