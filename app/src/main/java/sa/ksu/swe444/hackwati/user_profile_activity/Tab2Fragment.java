package sa.ksu.swe444.hackwati.user_profile_activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.storyAdapter;
import sa.ksu.swe444.hackwati.uploaded_stories.SwipeToDeleteCallback;
import sa.ksu.swe444.hackwati.uploaded_stories.UploadedStoriesAdapter;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;
import sa.ksu.swe444.hackwati.uploaded_stories.story;

public class Tab2Fragment extends Fragment {

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_user_uploaded_stories, container, false);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        status = view.findViewById(R.id.subscribeBtn);
        emptyUsers = view.findViewById(R.id.emptyUsers);
        recyclerView = view.findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new UploadedStoriesAdapter(getContext());

        //We set the array to the adapter
        mAdapter.setListContent(itemList);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        retrievePublishedStories();
        retrieveRejectedStories();
        retrieveUnderProcessingStories();


        return view;
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




}
