package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.UserProfile;


public class ProfileActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_main);


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

      /*  tab3Fragment = new Tab3Fragment();
        adapter.addFragment(tab3Fragment, "القصص");*/


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

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

}

