package sa.ksu.swe444.hackwati;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class InnerStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_STORAG_CODE = 1000;
    private ImageView share,backwardCover;
    private ImageView close;
    private SeekBar seekBar;
    private ImageView pause;
    private ImageView backward;
    private ImageView forward;
    private ImageView nightMood;
    private TextView speed;
    private MediaPlayer mediaPlayer;
    private TextView remainingTime;
    private TextView currentTime;
    private TextView upload;
    private RelativeLayout RL;
    File localFile = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    Uri uri;
    Uri img;
    private String title;
    private String storyID;
    private String link;
    Context context = getBaseContext();

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
        nightMood.setOnClickListener(this);
        //get Audio from fire base


        // download();


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

        Glide.with(InnerStoryActivity.this)
                .load(img + "")
                .into(storyCover);

        Glide.with(InnerStoryActivity.this)
                .load(img + "")
                .into(backwardCover);



    }//end onCreate

    private void viewRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InnerStoryActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("قيم قصتي");
        View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_ratingbar, null);
        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        builder.setView(dialogLayout);
        builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "تقييمك هو " + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                float rate =ratingBar.getRating();
                DocumentReference storyRef = firebaseFirestore.collection("stories").document(storyID);
               // storyRef.update("rate", FieldValue.increment(rate));
              //  storyRef.update("rateCounter", FieldValue.increment(1));
                String Rate = String.valueOf(rate);
                updateStoryRate(Rate);


            }
        });
        builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }
    private void updateStoryRate(String rate) {

        Map<String, Object> story = new HashMap<>();
        //todo: delete rate
        story.put("rate", rate);

        story.put("rateCounter", FieldValue.increment(1));

        firebaseFirestore.collection("publishedStories").document(storyID).set(story, SetOptions.merge());
    }
    
    // end of viewRateDialog
    private void init() {
        share = findViewById(R.id.share);
        close = findViewById(R.id.close);
        seekBar = findViewById(R.id.seekBar);
        pause = findViewById(R.id.pause);
        backward = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        nightMood = findViewById(R.id.download);
        backwardCover = findViewById(R.id.story_cover2);

        speed = findViewById(R.id.speed);
        remainingTime = findViewById(R.id.remaining_time);
        currentTime = findViewById(R.id.currentTime);
        storyCover = findViewById(R.id.story_cover);
        //  myService = new MyService();
      //  RL = findViewById(R.id.Dialog);
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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion(MediaPlayer mp) {
                    viewRateDialog();

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
                } else {
                    pause.setImageResource(R.drawable.ic_pause);
                    mediaPlayer.start();

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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String [] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission , PERMISSION_STORAG_CODE);

                    }else {
                        startDownloading();
                    }
                }




        }// end switch


    }// onClick()

    private void startDownloading() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(title);
        request.setDescription("downloading story");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , "story"+System.currentTimeMillis());


        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }

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

    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            uri = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_AUDIO));
            link = intent.getExtras().getString(Constants.Keys.STORY_AUDIO);
            img = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_COVER));
            storyID = intent.getExtras().getString(Constants.Keys.STORY_ID);
            title = intent.getExtras().getString(Constants.Keys.STORY_TITLE);
            titleView.setText(title);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode){
            case PERMISSION_STORAG_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    startDownloading();

                }

        }
    }
}// end class
