package sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example;



import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

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
    private static  final int Pick_song = 3;
    private Button uploadBtn;
    public BottomNavigationView navView;
    private GifImageView shining;
    private Uri uriPath;
    private ImageView recordNav;


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
        recordNav = findViewById(R.id.iv_record);
        recordNav.setColorFilter(ContextCompat.getColor(recordActivity.this, R.color.blue_hak2));

        navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_record);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                Intent intint = new Intent(recordActivity.this, recordStoryInfo.class);
                intint.putExtra("path", AUDIO_FILE_PATH);
                startActivity(intint);
                Log.d(TAG, "hellobff"+AUDIO_FILE_PATH);


            } else if (resultCode == RESULT_CANCELED) {
            }
        }
         else if(requestCode==99&&resultCode==RESULT_OK&&data!=null){

            uriPath =getIntent().getData();
            try {
                Uri uri= data.getData();
                String path = getPathFromUri(getBaseContext() ,uri);
                Intent intent = new Intent(recordActivity.this , recordStoryInfo.class);
                if(data == null){
                    Toast.makeText(getBaseContext(), "NULLLL!!!", Toast.LENGTH_LONG).show();
                }
                intent.putExtra("uploadedURI"  , path);
                Toast.makeText(getBaseContext(), "audio"+path, Toast.LENGTH_LONG).show();
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }



        }else if (requestCode==3&&resultCode==RESULT_OK&&data!=null){
            Uri muri = getIntent().getData();
            Intent intent = new Intent(recordActivity.this,recordStoryInfo.class);

            intent.putExtra("uploadedURI",muri.toString());
            startActivity(intent);

        }
        else {
            Toast.makeText(recordActivity.this, "نرجوا اختيار ملف أو تسجيل قصه", Toast.LENGTH_LONG).show();
        }
    }


    private void getStory(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        Uri story = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(story, null, null,null, null);
        int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        String t = cursor.getString(title);
        Toast.makeText(getBaseContext(), "TITLE :"+t , Toast.LENGTH_LONG).show();
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
                .setColor(ContextCompat.getColor(this, R.color.white_hak))
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





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}