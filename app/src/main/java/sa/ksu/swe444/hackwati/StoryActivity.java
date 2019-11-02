package sa.ksu.swe444.hackwati;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.ksu.swe444.hackwati.ui.profileActivity.ProfileActivity;

import static java.lang.String.valueOf;


public class StoryActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView channelname, bookName, duration, discrebtionM;
    private String user_id;
    private Button listenBtn;
    //subscribedBtn;
    private ImageView channelimage, cover;
    private static final String TAG = StoryActivity.class.getSimpleName();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public DocumentReference documentReference;
    public FirebaseAuth mAuth;
    String userUid;
    Button subscribe;
    String storyDuration;
    String storyUri;
    String storyCover;
    private String storyTitle;


    //s1
    private RatingBar ratingBar;

    private String storyId, userStoryId;


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity_main);
        getExtras();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        duration = findViewById(R.id.duration);
        bookName = findViewById(R.id.bookName);
        discrebtionM = findViewById(R.id.discrebtion);
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
        subscribe = findViewById(R.id.subscribeBtn);
        subscribe.setOnClickListener(this);
        listenBtn.setOnClickListener(this);
        ratingBar = findViewById(R.id.rating);


        subscribe.setVisibility(View.VISIBLE);
        subscribe.setText("اشتراك");

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


                        Log.d(TAG, " 111 document exist");

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
                                subscribe.setBackgroundColor(Color.YELLOW);
                                break;


                            }


                        }// end for loop


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
                        storyDuration = (String) document.get("duration");
                        storyUri = (String) document.get("sound");
                        storyCover = (String) document.get("pic");
                        storyTitle = (String) document.get("title");


                        bookName.setText(title);
                        duration.setText(description);
                        Glide.with(StoryActivity.this)
                                .load(pic + "")
                                .into(cover);
                        discrebtionM.setText(description);


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


        }
    }


    public void subscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayUnion(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                washingtonRef.update("numSubscribers", FieldValue.increment(1));
                subscribe.setText("مشترك");
                subscribe.setBackgroundColor(Color.YELLOW);


            }
        });


    }

    public void unsubscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayRemove(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                washingtonRef.update("numSubscribers", FieldValue.increment(-1));
                subscribe.setText("اشتراك");
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
                            channelname.setText(userName);


                        }


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
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


                      /*  Map<String, Object> story = new HashMap<>();
                        story.put("finalrate", Rate);
                        firebaseFirestore.collection("publishedStories").document(storyId).set(story);*/


                    }

                }

            }
        });
    }
}




