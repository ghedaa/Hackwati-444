package sa.ksu.swe444.hackwati;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sa.ksu.swe444.hackwati.ui.profileActivity.ProfileActivity;

import static java.lang.String.valueOf;


public class StoryActivity extends AppCompatActivity implements View.OnClickListener  {


    private TextView channelname, timestamp, bookName, duration, discrebtionM,favoritesFlag;
    private String user_id;
    private Button listenBtn;
    ImageButton btnAddToList;
    //subscribedBtn;
    private ImageView channelimage, cover;
    private static final String TAG = StoryActivity.class.getSimpleName();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public DocumentReference documentReference;
    public FirebaseAuth mAuth;
    String userUid;
    Button subscribe;
    int storyDuration;
    private Toolbar toolbarMain;
    String storyUri;
    String storyCover;
    private String storyTitle;
    MediaPlayer mediaPlayer;
    String format;


    //s1
    private RatingBar ratingBar;

    private String storyId, userStoryId;


    @SuppressLint("DefaultLocale")
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity_main);
        getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);



        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        duration = findViewById(R.id.duration);
        bookName = findViewById(R.id.bookName);
        favoritesFlag = findViewById(R.id.flag);
        favoritesFlag.setOnClickListener(this);
        discrebtionM = findViewById(R.id.discrebtion);
        timestamp = findViewById(R.id.date);
        channelimage = findViewById(R.id.channelimage);
        cover = (ImageView) findViewById(R.id.cover);
        listenBtn = findViewById(R.id.listenBtn);
        channelname = findViewById(R.id.channelname);
        channelname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (StoryActivity.this, ProfileActivity.class);
                intent.putExtra(Constants.Keys.STORY_USER_ID, userStoryId);
                startActivity(intent);

            }
        });
        channelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (StoryActivity.this, ProfileActivity.class);
                intent.putExtra(Constants.Keys.STORY_USER_ID, userStoryId);
                startActivity(intent);

            }
        });
        subscribe = findViewById(R.id.subscribeBtn);
        subscribe.setOnClickListener(this);
        listenBtn.setOnClickListener(this);
        ratingBar = findViewById(R.id.rating);
        ratingBar.setNumStars(5);

        btnAddToList = findViewById(R.id.btnAddToList);
        btnAddToList.setOnClickListener(this);


        subscribe.setVisibility(View.VISIBLE);
        subscribe.setText("اشتراك");
        ViewCompat.setBackgroundTintList(subscribe, ContextCompat.getColorStateList(StoryActivity.this,R.color.gray_hak));

        mediaPlayer = MediaPlayer.create(StoryActivity.this, Uri.parse(storyUri));
        storyDuration = mediaPlayer.getDuration();
        mediaPlayer.release();
        /*convert millis to appropriate time*/
         format = String.format("%d دقيقة, %d ثانية",
                TimeUnit.MILLISECONDS.toMinutes(storyDuration),
                TimeUnit.MILLISECONDS.toSeconds(storyDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(storyDuration))  );


        duration.setText(format+"");

        subscribeUser();
        retriveStory();
        retriveUserData();

        showRate();


    }// end of setOnNavigationItemSelectedListener()


    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            storyId = intent.getExtras().getString(Constants.Keys.STORY_ID);
            userStoryId = intent.getExtras().getString(Constants.Keys.STORY_USER_ID);
            storyUri = intent.getExtras().getString(Constants.Keys.STORY_AUDIO);
            storyCover = intent.getExtras().getString(Constants.Keys.STORY_COVER);
        }

    }


    public void subscribeUser() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        Intent intent = getIntent();
                        if (intent.getExtras() != null) {
                            storyId = intent.getExtras().getString(Constants.Keys.STORY_ID);
                            userStoryId = intent.getExtras().getString(Constants.Keys.STORY_USER_ID);
                        }


                        List<String> list = (List<String>) document.get("subscribedUsers");
                        if (list == null) {
                            return;


                        } else for (String subscribedUsers : list) {


                            if (userStoryId.equals(userUid)) {
                                subscribe.setVisibility(View.INVISIBLE);
                                break;

                            } else if (userStoryId.equals(subscribedUsers) || userStoryId == subscribedUsers) {

                                subscribe.setText("مشترك");
                                ViewCompat.setBackgroundTintList(subscribe, ContextCompat.getColorStateList(StoryActivity.this,R.color.green_hak));

                                //subscribe.setBackgroundColor(Color.YELLOW);
                                break;


                            }


                        }// end for loop

                        List<String> favoriteList = (List<String>) document.get("favorite");
                        if(favoriteList.contains(storyId)){
                           favoritesFlag.setText("1");
                            btnAddToList.setColorFilter(ContextCompat.getColor(StoryActivity.this, R.color.pink_hak), android.graphics.PorterDuff.Mode.SRC_IN);

                        }
                        else {
                            favoritesFlag.setText("0");
                            btnAddToList.setColorFilter(ContextCompat.getColor(StoryActivity.this, R.color.gray_hak), android.graphics.PorterDuff.Mode.SRC_IN);

                        }


                    }
                }
            }
        });

    }

    public void retriveStory() {

        DocumentReference docRef = firebaseFirestore.collection("publishedStories").document(storyId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String title = (String) document.get("title");
                        String description = (String) document.get("description");
                        String pic = document.get("pic").toString();
                        storyUri = (String) document.get("sound");
                        storyCover = (String) document.get("pic");
                        storyTitle = (String) document.get("title");
                        Date date = document.getDate("timestamp");

                        timestamp.setText("بتاريخ: "+date.toString());
                        bookName.setText(title);
                        Glide.with(StoryActivity.this)
                                .load(pic + "")
                                .into(cover);
                        discrebtionM.setText(description);

                        getSupportActionBar().setTitle(title+"");


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.subscribeBtn:

                if (subscribe.getText().equals("اشتراك"))
                    subscribUser();
                else if (subscribe.getText().equals("مشترك")) {
                    unsubscribUser();
                } else
                    subscribe.setVisibility(View.INVISIBLE);
                break;
            case R.id.listenBtn:
                Intent intent = new Intent(StoryActivity.this, InnerStoryActivity.class);
                intent.putExtra(Constants.Keys.STORY_AUDIO, storyUri);
                intent.putExtra(Constants.Keys.STORY_COVER, storyCover);
                intent.putExtra(Constants.Keys.STORY_ID, storyId);
                intent.putExtra(Constants.Keys.STORY_TITLE, storyTitle);
                startActivity(intent);
                break;
            case R.id.btnAddToList:
                if(favoritesFlag.getText().equals("0"))
                addToFavoritesList();
                else if (favoritesFlag.getText().equals("1"))
                    removeFromFavoritesList();


        }
    }

    private void removeFromFavoritesList() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("favorite", FieldValue.arrayRemove(storyId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(Void aVoid) {
         favoritesFlag.setText("0");
                btnAddToList.setColorFilter(ContextCompat.getColor(StoryActivity.this, R.color.gray_hak), android.graphics.PorterDuff.Mode.SRC_IN);

            }
        });
    }

    private void addToFavoritesList() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("favorite", FieldValue.arrayUnion(storyId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(Void aVoid) {
                favoritesFlag.setText("1");
                btnAddToList.setColorFilter(ContextCompat.getColor(StoryActivity.this, R.color.pink_hak), android.graphics.PorterDuff.Mode.SRC_IN);


            }
        });

    }


    public void subscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayUnion(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                subscribe.setText("مشترك");
                ViewCompat.setBackgroundTintList(subscribe, ContextCompat.getColorStateList(StoryActivity.this,R.color.green_hak));


            }
        });


    }

    public void unsubscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayRemove(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                subscribe.setText("اشتراك");
                ViewCompat.setBackgroundTintList(subscribe, ContextCompat.getColorStateList(StoryActivity.this,R.color.gray_hak));

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
                        // String infoU = document.get("info").toString();

                        String thumbnail = (String) document.get("thumbnail");
                        Glide.with(StoryActivity.this)
                                .load(thumbnail + "")
                                .into(channelimage);



                        if (userName != null) {
                            channelname.setText("سجلت بواسطة:  "+ userName);


                        }


                    }
                }
            }
        });


    }

    public void showRate() {
        //s2
        DocumentReference storyRef = firebaseFirestore.collection("publishedStories").document(storyId);

        storyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                   //     String rate = (String) document.get("rate") ;            //if the field is Integer

                       // Float rate = document.getFloat("rate");
                     //   int rateCounter = (int) document.get("rateCounter");
                    //    Float rate1 = rate.floatValue();
                    //    Float rate1 = Float.parseFloat(valueOf(rate));
                      //  Float Rate = ((Float) rate1 / rateCounter);


                        ratingBar.setRating(3);
                        ratingBar.setEnabled(false);


                      /*  Map<String, Object> story = new HashMap<>();
                        story.put("finalrate", Rate);
                        firebaseFirestore.collection("publishedStories").document(storyId).set(story);*/


                    }

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}




