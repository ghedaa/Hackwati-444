package sa.ksu.swe444.hackwati.uploaded_stories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;

public class UserUploadedStories extends AppCompatActivity {
    private String userUid;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UploadedStoriesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<story> namesList;
    private TextView emptyUsers;
    private Button status;
    public BottomNavigationView navView;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_stories);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

      //  Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("القصص المرفوعة");
  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      getSupportActionBar().setHomeButtonEnabled(false);


        status = findViewById(R.id.subscribeBtn);
        emptyUsers = findViewById(R.id.emptyUsers);
        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new UploadedStoriesAdapter(UserUploadedStories.this);
        coordinatorLayout= findViewById(R.id.coordinator_layout);

        //We set the array to the adapter
        mAdapter.setListContent(itemList);

        mLayoutManager = new LinearLayoutManager(UserUploadedStories.this);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
        retrievePublishedStories();
        retrieveRejectedStories();
        retrieveUnderProcessingStories();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(UserUploadedStories.this, recordActivity.class));
                        // navView.setSelectedItemId(R.id.navigation_record);
                        //  navView.getMenu().getItem(R.id.navigation_record).setChecked(true);
                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(UserUploadedStories.this, MainActivity.class));
                        //navView.setSelectedItemId(R.id.navigation_subscription);

                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(UserUploadedStories.this, ExploreActivity.class));
                        //   navView.setSelectedItemId(R.id.navigation_explore);

                        break;

                }// end of switch

                return true;
            }
        });

    }//end onCreate()




    public void retrievePublishedStories() {
        firebaseFirestore.collection("publishedStories")
                .whereEqualTo("userId", userUid + "")// <-- This line
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        document.getData();

                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";
                        Item item = new Item(true, storyId, title, pic, sound, userId, userName, thumbnail);
                        item.setStatus(Constants.Keys.PUBLISHED);
                        item.setColor(R.color.green_hak);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();
                    }
                }//end is successful
            }
        });

    }// end of retrievePublishedStories

    public void retrieveRejectedStories() {
        firebaseFirestore.collection("rejectedStories")
                .whereEqualTo("userId", userUid + "")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        Log.d("UPLOaDED STORY", " HERE");

                        document.getData();
                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";

                        Item item = new Item(true, storyId, title, pic, sound, userId, userName, thumbnail);
                        item.setStatus(Constants.Keys.REJECTED);
                        item.setColor(R.color.pink_hak2);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }// end of retrieveRejectedStories

    public void retrieveUnderProcessingStories() {
        firebaseFirestore.collection("stories")
                .whereEqualTo("userId", userUid + "")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {


                        document.getData();

                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";

                        Item item = new Item(true, storyId, title, pic, sound, userId, userName, thumbnail);
                        item.setStatus(Constants.Keys.PROCESSING);
                        item.setColor(R.color.orange_hak);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }//end of retrieveUnderProcessingStories


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



    }// class
