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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;
import sa.ksu.swe444.hackwati.list_adabter.CustomAdapter;
import sa.ksu.swe444.hackwati.list_adabter.CustomPojo;

public class SubscribersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomPojo> namesList;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String userUid;
    private TextView emptyUsers;

    private static final String TAG = "SubscribersListActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_list);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("المتابعين");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        emptyUsers = findViewById(R.id.emptyUsers);

        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new CustomAdapter(SubscribersListActivity.this);

        //We set the array to the adapter
        mAdapter.setListContent(namesList);

        mLayoutManager = new LinearLayoutManager(SubscribersListActivity.this);
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

                            Subscribers(list);
                        }// end for loop


                    }
                }
            }
        });


    }

    public void Subscribers(final List<String> list) {


        firebaseFirestore.collection("users")
                .whereArrayContains("subscribedUsers", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userName = document.get("username").toString();
                                String thumbnail = document.get("thumbnail").toString();
                                String userID = document.getId().toString();
                                boolean isSubscribed = list.contains(userID);
                            //    Log.d(TAG,"bobo"+isSubscribed+"");
                                CustomPojo user;


                                if(isSubscribed){
                                user = new CustomPojo(userID, userName, thumbnail, "مشترك");
                                namesList.add(user);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();}
                                else{
                                    user = new CustomPojo(userID, userName, thumbnail, "اشتراك");
                                    namesList.add(user);
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                }
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
