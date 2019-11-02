package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.R;


public class ProfileActivity extends AppCompatActivity {
   // public static final String GOOGLE_ACCOUNT = "google_account";
    //Declare the Adapter, RecyclerView and our custom ArrayList

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  Tab1Fragment tab1Fragment;
    private  Tab2Fragment tab2Fragment;
    private Tab3Fragment tab3Fragment;

    public FirebaseAuth mAuth;
    private String   userStoryId ;
    private TextView userNameText;
    private ImageView userImage;
    private ImageView img;
    private Button sub;

    private static final String TAG = "UserProfileTabs";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userUid;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<CustomPojo> listContentArr= new ArrayList<>();
    //final String userStoryId = intent.getExtras().getString(Constants.Keys.STORY_USER_ID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        getExtras();

        userNameText = findViewById(R.id.userNameT);
        img=findViewById(R.id.userImg);
        sub=findViewById(R.id.subscribeBtn);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();


        //initCollapsingToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        tab1Fragment = new Tab1Fragment();
        adapter.addFragment(tab1Fragment, "نبذه");

        tab2Fragment = new Tab2Fragment();
        adapter.addFragment(tab2Fragment, "القصص");

        tab3Fragment = new Tab3Fragment();
        adapter.addFragment(tab3Fragment, "القوائم");

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

    private void retriveUserData() {



        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, " 111 document exist");


                            String userName = document.get("username").toString();

                            String thumbnail = document.get("thumbnail").toString();

                            if (userName != null) {
                                userNameText.setText(userName);

                                Glide.with(ProfileActivity.this)
                                        .load(thumbnail + "")
                                        .into(img);



                        }
                        /*           subscribedno.setText(document.get("numSubscribers").toString());*/


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


}



