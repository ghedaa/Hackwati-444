package sa.ksu.swe444.hackwati.user_profile_activity;

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

import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.R;


public class UserProfileActivity extends AppCompatActivity  {

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
        setContentView(R.layout.user_profile_activity_main);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //"0gsAM4f2eqfppuCtWFex8kmmMHB2";
                //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ملفي الشخصي");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);




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


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    }




}

