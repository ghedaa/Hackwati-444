package sa.ksu.swe444.hackwati.Draft;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ListenToStoryDraft extends AppCompatActivity implements View.OnClickListener {


    private Button approve;
    private Button reject;
    private ImageView close;
    private SeekBar seekBar;
    private ImageView pause;
    private ImageView backward;
    private ImageView forward;

    private MediaPlayer mediaPlayer;
    private TextView remainingTime;
    private TextView currentTime , story_name,user_name, timer;
    private String storyId;
    private String userStoryId;
    private TextView upload;
    private RelativeLayout RL;
    File localFile = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String storyTitle;



    Uri audio_url;
    Uri uri;
    Uri img;

    ImageView storyCover, backwardCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_to_story_draft);

        init();
        pause.setOnClickListener(this);
        close.setOnClickListener(this);
        backward.setOnClickListener(this);
        forward.setOnClickListener(this);
        approve.setOnClickListener(this);
        reject.setOnClickListener(this);
        seekBar = findViewById(R.id.seekBar);


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
            }// run

        };

        seekBar.postDelayed(mUpdateSeekbar, 100);

        Glide.with(ListenToStoryDraft.this)
                .load(img + "")
                .into(storyCover);

        Glide.with(ListenToStoryDraft.this)
                .load(img + "")
                .into(backwardCover);

        retriveUserData();
        retriveStoryName();
    }// end onCreate





    private void init() {
        close = findViewById(R.id.close);
        seekBar = findViewById(R.id.seekBar);
        pause = findViewById(R.id.pause);
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        backward = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        remainingTime = findViewById(R.id.remaining_time);
        currentTime = findViewById(R.id.currentTime);
        storyCover = findViewById(R.id.story_cover);
        backwardCover = findViewById(R.id.story_cover2);
        //  myService = new MyService();
        RL = findViewById(R.id.Dialog);
        storage = FirebaseStorage.getInstance();
        user_name = findViewById(R.id.userName);
        story_name = findViewById(R.id.story_name);
        //  timer = findViewById(R.id.timer);


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


            case R.id.forward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 15000);
                break;
            case R.id.back:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 15000);
                break;



            case R.id.approve:

                AlertDialog.Builder builder = new AlertDialog.Builder(ListenToStoryDraft.this);
                builder.setMessage("هل أنت متأكد من نشر القصة")
                        .setCancelable(false)
                        .setPositiveButton("أنا متأكد", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                approveStory();
                                startActivity(new Intent(ListenToStoryDraft.this, ViewDraft.class));


                            }

                        });
                builder.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });

                AlertDialog alert = builder.create();
                alert.show();


                break;

            case R.id.reject:


                AlertDialog.Builder builder2 = new AlertDialog.Builder(ListenToStoryDraft.this);
                builder2.setMessage("هل أنت متأكد من حذف القصة")
                        .setCancelable(false)
                        .setPositiveButton("أنا متأكد", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                rejectStory();
                                startActivity(new Intent(ListenToStoryDraft.this, ViewDraft.class));


                            }

                        });
                builder2.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });

                AlertDialog alert1 = builder2.create();
                alert1.show();




                break;
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


    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            uri = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_AUDIO));
            img = Uri.parse(intent.getExtras().getString(Constants.Keys.STORY_COVER));
            storyId = intent.getExtras().getString(Constants.Keys.STORY_ID);
            userStoryId = intent.getExtras().getString(Constants.Keys.STORY_USER_ID);
            storyTitle = intent.getExtras().getString(Constants.Keys.STORY_TITLE);
            story_name.setText(storyTitle);


        }
    }


    public void approveStory(){

        DocumentReference docRef = firebaseFirestore.collection("draft").document(storyId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // get data
                        document.getData();
                        String description = (String) document.get("description");
                        String pic = (String) document.get("pic");
                        String rate = (String) document.get("rate");
                        String sound = (String) document.get("sound");
                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");

                        // new doc
                        Map<String, Object> story = new HashMap<>();
                        story.put("description", description);
                        story.put("rate", rate);
                        story.put("title", title);
                        story.put("pic", pic);
                        story.put("userId", userId);
                        story.put("sound", sound);

                        firebaseFirestore.collection("stories").document().set(story)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        firebaseFirestore.collection("draft").document(storyId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });

                                        Toast.makeText(ListenToStoryDraft.this, "story publish", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ListenToStoryDraft.this, ViewDraft.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ListenToStoryDraft.this, "Error_add_story", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, e.toString());
                                    }
                                });


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


    public void rejectStory(){

        firebaseFirestore.collection("draft").document(storyId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

    public void retriveUserData() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(userStoryId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userName = document.get("username").toString();

                        if (userName != null ) {
                            user_name.setText(userName);
                        }
                    }
                }
            }
        });


    }

    public void retriveStoryName() {

        DocumentReference docRef = firebaseFirestore.collection("stories").document(storyId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String title = document.get("title").toString();
                        // String time = document.get("timestamp").toString();

                        if (title != null ) {
                            story_name.setText(title);
                            // timer.setText(time);
                        }
                    }
                }
            }
        });


    }


}


