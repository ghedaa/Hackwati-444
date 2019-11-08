package sa.ksu.swe444.hackwati.uploaded_stories;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.concurrent.TimeUnit;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.InnerStoryActivity;
import sa.ksu.swe444.hackwati.R;

public class ListenToUserStory extends AppCompatActivity implements View.OnClickListener  {
    private ImageView share,backwardCover;
    private ImageView close;
    private SeekBar seekBar;
    private ImageView pause;
    private ImageView backward;
    private ImageView forward;
    private ImageView downloadStory;
    private TextView speed;
    private MediaPlayer mediaPlayer;
    private TextView remainingTime;
    private TextView currentTime;
    // private MyService myService;
    private TextView upload;
    private RelativeLayout RL;
    File localFile = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;


    Uri uri;
    Uri img;
    private String title;
    private String storyID;
    private String status;


    ImageView storyCover;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_story_layout);
        init();
        pause.setOnClickListener(this);
        close.setOnClickListener(this);
        share.setOnClickListener(this);
        backward.setOnClickListener(this);
        forward.setOnClickListener(this);
        speed.setOnClickListener(this);
        downloadStory.setOnClickListener(this);
        //get Audio from fire base





        getExtras();
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
        seekBar.setMax(mediaPlayer.getDuration());


        defaultTimer();
        SeekBar();

        Runnable mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.postDelayed(this, 50);
                currentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                remainingTime.setText(milliSecondsToTimer(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
                SeekBar();

                Log.e("LOG", "Current position:"+mediaPlayer.getCurrentPosition()+"Duration:"+mediaPlayer.getDuration());
            }// run

        };

        seekBar.postDelayed(mUpdateSeekbar, 100);

        Glide.with(ListenToUserStory.this)
                .load(img + "")
                .into(storyCover);

        Glide.with(ListenToUserStory.this)
                .load(img + "")
                .into(backwardCover);



    }//end onCreate



    private void download() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission");


                // String url = uri;
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDescription("Some descrition");
                request.setTitle("Some title");
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.3gp");

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);


            }

        }
    }


    // end of viewRateDialog
    private void init() {
        share = findViewById(R.id.share);
        close = findViewById(R.id.close);
        seekBar = findViewById(R.id.seekBar);
        pause = findViewById(R.id.pause);
        backward = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        downloadStory = findViewById(R.id.download);
        backwardCover = findViewById(R.id.story_cover2);

        speed = findViewById(R.id.speed);
        remainingTime = findViewById(R.id.remaining_time);
        currentTime = findViewById(R.id.currentTime);
        storyCover = findViewById(R.id.story_cover);
        //  myService = new MyService();
        //RL = findViewById(R.id.Dialog);
        storage = FirebaseStorage.getInstance();
        titleView = findViewById(R.id.story_name);


    }//end init

    private void SeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null && mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(progress);
            }// onProgressChanged
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.pause();
                defaultTimer();
                pause.setImageResource(R.drawable.ic_play_button);
            }// onCompletion

        });

    }// seekbar


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pause:
                // toggle pause/ start button

                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.ic_play_button);
                    mediaPlayer.pause();
                    // stopService(new Intent(getApplicationContext(), MyService.class));
                } else {
                    pause.setImageResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                    //startService(new Intent(getApplicationContext(), MyService.class));

                    //record();
                }// else
                break;

            case R.id.close:
                finish();

                break;
            case R.id.share:
                shareStory();
                break;

            case R.id.forward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 15000);
                break;
            case R.id.back:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 15000);
                break;

            case R.id.speed:
                if (mediaPlayer.isPlaying()) {
                    if (speed.getText().toString().equals("X2")) {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.5f));
                        speed.setText("X1");
                    } else {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1f));
                        speed.setText("X2");

                    }
                }// if
                break;
            case R.id.download:
                download();
                break;

                // startActivity(new Intent(InnerStoryActivity.this , Test.class));

        }// end switch


    }// onClick()

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }// milliSecondsToTimer


    private void defaultTimer() {
        currentTime.setText("00:00");
        int duration = mediaPlayer.getDuration();
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        remainingTime.setText(time);

    }//end default timer

    private void shareStory() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "SEND"));
    }// end shareStory()



    private void downloadFile(InnerStoryActivity context, String fileName, String fileExtention, String destinationDir, String url) {
        DownloadManager downloadManager =
                (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request =
                new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDir, fileName + fileExtention);

        downloadManager.enqueue(request);
    }


    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            uri = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_AUDIO));
            img = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_COVER));
            storyID = intent.getExtras().getString(Constants.Keys.STORY_ID);
            title = intent.getExtras().getString(Constants.Keys.STORY_TITLE);
            titleView.setText(title);

        }

    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
        {
            int grantResultsLength = grantResults.length;
            if(grantResultsLength > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
}// class
 }
