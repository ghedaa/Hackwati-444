package sa.ksu.swe444.hackwati.uploaded_stories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SubscribedListActivity;
import sa.ksu.swe444.hackwati.list_adabter.CustomAdapter;
import sa.ksu.swe444.hackwati.list_adabter.CustomPojo;

public class UserUploadedStories extends AppCompatActivity {
    private String userUid;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomPojo> namesList;
    private TextView emptyUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_stories);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        emptyUsers = findViewById(R.id.emptyUsers);
        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new CustomAdapter(UserUploadedStories.this);

        //We set the array to the adapter
        mAdapter.setListContent(namesList);

        mLayoutManager = new LinearLayoutManager(UserUploadedStories.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        retrievePublishedStories();
        retrieveRejectedStories();
        retrieveUnderProcessingStories();



    }
//end onCreate()



    public void retrievePublishedStories(){
        firebaseFirestore.collection("publishedStories")
                .whereEqualTo("userId", userUid + "")// <-- This line
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {



                        document.getData();

                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";

                        Item item = new Item(true,storyId, title, pic,sound, userId, userName, thumbnail);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }// end of retrievePublishedStories
    public void retrieveRejectedStories(){
        firebaseFirestore.collection("rejectedStories")
                .whereEqualTo("userId", userUid + "")// <-- This line
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {



                        document.getData();

                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";

                        Item item = new Item(true,storyId, title, pic,sound, userId, userName, thumbnail);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }// end of retrieveRejectedStories
    public void retrieveUnderProcessingStories(){
        firebaseFirestore.collection("stories")
                .whereEqualTo("userId", userUid + "")// <-- This line
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {



                        document.getData();

                        String title = (String) document.get("title");
                        String userId = (String) document.get("userId");
                        String storyId = (String) document.getId();
                        String userName = "";
                        String pic = (String) document.get("pic");
                        String sound = (String) document.get("sound");

                        String thumbnail = "";

                        Item item = new Item(true,storyId, title, pic,sound, userId, userName, thumbnail);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }//end of retrieveUnderProcessingStories



}// class
