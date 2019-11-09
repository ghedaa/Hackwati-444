package sa.ksu.swe444.hackwati.user_profile_activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.StoryActivity;
import sa.ksu.swe444.hackwati.storyAdapter;


public class Tab3Fragment extends Fragment {

    private RecyclerView recyclerView;
    private storyAdapter adapter;
    private List<Item> itemList;
    private TextView emptyStories;


    public FirebaseAuth mAuth;
    private static final String TAG = "UserProfileTab2";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userUid;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userStoryId;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.profile_fragment_three, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        emptyStories =view.findViewById(R.id.empty);

        if (getArguments() != null) {
            userUid = getArguments().getString("userUid");
        }

        initRecyclerView();

        return view;
    }


    private void initRecyclerView() {
        itemList = new ArrayList<>();
        adapter = new storyAdapter(getActivity(), itemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MainActivity.GridSpacingItemDecoration(10, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
      //1
        retrieveFavoritStories();
    }

    public void retrieveFavoritStories() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        final List<String> list = (List<String>) document.get("favorite");
                        if (list == null) {
                            emptyStories.setText("لا يوجد قصص مفضلة");
                        } else { // has SubscribedUsers

                            for(String StoryID : list)
                            retriveUserFromStoryID(StoryID);
                        }// end for loop
                    }
                }
            }
        });
    }
    //2
    public void retriveUserFromStoryID(final String StoryID) {



        DocumentReference docRef2 = firebaseFirestore.collection("publishedStories").document(StoryID);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        String userID = (String) userDocument.get("userId");
                        retriveUserData(userID,StoryID);
                    }}
            }
        });
    }
   //3
    public void retriveUserData(String userID, final String StoryID) {



        DocumentReference docRef2 = firebaseFirestore.collection("users").document(userID);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        String userName = userDocument.get("username").toString();
                        String userID = userDocument.getId();
                        String thumbnail = userDocument.get("thumbnail").toString();
                        retriveStories(userID,userName,thumbnail,StoryID);
                    }
                }
            }
        });
    }
    //4
    public void retriveStories(final String userID, final String userName, final String thumbnail,String StoryID) {



        DocumentReference docRef = firebaseFirestore.collection("publishedStories").document(StoryID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document2 = task.getResult();
                    if (document2.exists()) {

                                Log.d(TAG, " 1 => test");



                                    document2.getData();
                                    String title = (String) document2.get("title");
                                    String userId = (String) document2.get("userId");
                                    String storyId = (String) document2.getId();
                                    String pic = (String) document2.get("pic");
                                    String sound = (String) document2.get("sound");


                                    Item item = new Item(true,storyId, title, pic,sound, userId, userName, thumbnail);
                                    itemList.add(item);
                                    adapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }





    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}


