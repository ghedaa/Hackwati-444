package sa.ksu.swe444.hackwati.user_profile_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SubscribedListActivity;
import sa.ksu.swe444.hackwati.SubscribersListActivity;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;


public class UserProfileActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Tab1Fragment tab1Fragment;
    private Tab2Fragment tab2Fragment;
    private Tab3Fragment tab3Fragment;
    private Tab4Fragment tab4Fragment;
    private String userStoryId;
    private ImageView userImg;
    private TextView userName;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Button subscribe;
    String userUid;
    public BottomNavigationView navView;


    ///
    private TextView  storyno, subscribed, subscriber, subscriberno, stories, userNameText, subscribedno;
    private CircularImageView img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity_main);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //"0gsAM4f2eqfppuCtWFex8kmmMHB2";
        //

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
*/


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());


        Bundle bundle = new Bundle();
        bundle.putString("userUid", userUid);


        tab1Fragment = new Tab1Fragment();
        tab1Fragment.setArguments(bundle);
        adapter.addFragment(tab1Fragment, "معلوماتي");

        tab2Fragment = new Tab2Fragment();
        tab2Fragment.setArguments(bundle);
        adapter.addFragment(tab2Fragment, "قصصي");


        tab3Fragment = new Tab3Fragment();
        tab3Fragment.setArguments(bundle);
        adapter.addFragment(tab3Fragment, "مفضلتي");

        tab4Fragment = new Tab4Fragment();
        tab4Fragment.setArguments(bundle);
        adapter.addFragment(tab4Fragment, "مسوداتي");




        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        // User INFO
        img = findViewById(R.id.userImg);
        userNameText = findViewById(R.id.nameSignUpHin);
        subscriberno = findViewById(R.id.subscriberno);
        subscribedno = findViewById(R.id.subscribedno);
        subscribed = findViewById(R.id.subscribed);
        subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, SubscribedListActivity.class));
            }
        });
        storyno = findViewById(R.id.storyno);
        subscriber = findViewById(R.id.subscriber);
        subscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, SubscribersListActivity.class));
            }
        });

        retriveUserData();
        countStories();
        Subscribers();


        // this code below to disable swapping between Tabs
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
            }

        });

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(UserProfileActivity.this, recordActivity.class));
                        // navView.setSelectedItemId(R.id.navigation_record);
                        //  navView.getMenu().getItem(R.id.navigation_record).setChecked(true);
                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                        //navView.setSelectedItemId(R.id.navigation_subscription);

                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(UserProfileActivity.this, ExploreActivity.class));
                        //   navView.setSelectedItemId(R.id.navigation_explore);

                        break;

                }// end of switch

                return true;
            }
        });

    }//onCreate()

    public void retriveUserData() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userName = document.get("username").toString();
                        String email = document.get("email").toString();
                        String thumbnail = document.get("thumbnail").toString();
                        String infoU = document.get("info").toString();
                        List<String> list = (List<String>) document.get("subscribedUsers");

                        if (userName != null && email != null) {
                            userNameText.setText(userName);

                            Glide.with(UserProfileActivity.this)
                                    .load(thumbnail + "")
                                    .into(img);

                            subscribedno.setText(list.size() + "");



                        }

                    }
                }
            }
        });


    }


    public void countStories() {
        firebaseFirestore.collection("publishedStories")
                .whereEqualTo("userId", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int counter = 0;
                            final int[] sum = {0,0,0};

                            for (DocumentSnapshot document : task.getResult()) {
                                counter++;
                            }
                            sum[0] +=counter;

                            firebaseFirestore.collection("rejectedStories")
                                    .whereEqualTo("userId", userUid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int counter = 0;
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    counter++;
                                                }
                                                sum[1] +=counter;

                                                firebaseFirestore.collection("stories")
                                                        .whereEqualTo("userId", userUid)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    int counter = 0;
                                                                    int storyNum=0;
                                                                    for (DocumentSnapshot document : task.getResult()) {
                                                                        counter++;
                                                                    }
                                                                    sum[2] +=counter;
                                                                    for(int i =0 ; i<sum.length ; i++){
                                                                        storyNum+=sum[i];
                                                                    }
                                                                    storyno.setText(storyNum + " ");
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });



                        }
                    }
                });
    }

    public void Subscribers() {

        firebaseFirestore.collection("users")
                .whereArrayContains("subscribedUsers", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                counter++;
                            }
                            subscriberno.setText(counter + "");
                        }
                    }
                });
    }// Subscribers

}

