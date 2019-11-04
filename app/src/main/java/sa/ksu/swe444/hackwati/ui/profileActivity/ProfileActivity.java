package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.InnerStoryActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.StoryActivity;
import sa.ksu.swe444.hackwati.UserProfile;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Tab1Fragment tab1Fragment;
    private Tab2Fragment tab2Fragment;
    private Tab3Fragment tab3Fragment;
    private String userStoryId;
    private ImageView userImg;
    private TextView userName;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Button subscribe;
    String userUid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_main);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getExtras();

        userImg = findViewById(R.id.userImg);
        userName = findViewById(R.id.userName);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());


        Bundle bundle = new Bundle();
        bundle.putString("userStoryId", userStoryId);


        tab1Fragment = new Tab1Fragment();
        tab1Fragment.setArguments(bundle);
        adapter.addFragment(tab1Fragment, "نبذه");

        tab2Fragment = new Tab2Fragment();
        tab2Fragment.setArguments(bundle);
        adapter.addFragment(tab2Fragment, "القصص");

      tab3Fragment = new Tab3Fragment();
        adapter.addFragment(tab3Fragment, "التفضيلات");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        subscribe = findViewById(R.id.subscribe);
        subscribe.setOnClickListener(this);
        subscribe.setVisibility(View.VISIBLE);
        subscribe.setText("اشتراك");
        subscribeUser();
        retriveUserData();


    }

    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userStoryId = intent.getExtras().getString(Constants.Keys.STORY_USER_ID);

        }

    }
    public void retriveUserData() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(userStoryId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userNamec = document.get("username").toString();
                        String thumbnail = document.get("thumbnail").toString();
                        String infoU = document.get("info").toString();

                            userName.setText(userNamec);

                            Glide.with(ProfileActivity.this)
                                    .load(thumbnail + "")
                                    .into(userImg);

                            //info.setText(infoU);




                    }
                }
            }
        });


    }

    public void subscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayUnion(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                subscribe.setText("مشترك");


            }
        });


    }

    public void unsubscribUser() {
        final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef.update("subscribedUsers", FieldValue.arrayRemove(userStoryId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                subscribe.setText("اشتراك");
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
                        List<String> list = (List<String>) document.get("subscribedUsers");
                        if (list == null) {
                            return;
                        } else for (String subscribedUsers : list) {
                            if (userStoryId.equals(userUid)) {
                                subscribe.setVisibility(View.INVISIBLE);
                                break;
                            } else if (userStoryId.equals(subscribedUsers) ) {
                                subscribe.setText("مشترك");
                                //subscribe.setBackgroundColor(Color.YELLOW);
                                break;
                            }
                        }// end for loop
                    }
                }
            }
        });

    }

}

