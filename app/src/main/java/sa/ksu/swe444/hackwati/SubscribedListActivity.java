package sa.ksu.swe444.hackwati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;
import sa.ksu.swe444.hackwati.list_adabter.CustomAdapter;
import sa.ksu.swe444.hackwati.list_adabter.CustomPojo;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;

public class SubscribedListActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomPojo> namesList;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String userUid;
    private TextView emptyUsers;
    Button subscribe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_list);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("المتابعون");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        emptyUsers = findViewById(R.id.emptyUsers);
        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new CustomAdapter(SubscribedListActivity.this);

        //We set the array to the adapter
        mAdapter.setListContent(namesList);

        mLayoutManager = new LinearLayoutManager(SubscribedListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


        retrieveSubscribedUsers();



    }//onCreate



    public void retrieveSubscribedUsers() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        final List<String> list = (List<String>) document.get("subscribedUsers");
                        if (list == null) {
                            emptyUsers.setText("لا يوجد متابعين");
                        } else { // has SubscribedUsers

                            retriveUserData(list);
                        }// end for loop


                    }
                }
            }
        });


    }
    public void retriveUserData(List<String> list) {


        for(int i=0; i<list.size(); i++) {
            DocumentReference docRef = firebaseFirestore.collection("users").document(list.get(i));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            String userName = document.get("username").toString();
                            String thumbnail = document.get("thumbnail").toString();
                            String userID = document.getId().toString();
                            boolean isSubscribed = true;

                            CustomPojo user = new CustomPojo(userID, userName,thumbnail,"مشترك");

                            namesList.add(user);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                }
            });

        }//end for loop
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
