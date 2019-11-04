package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.explor.fragmentadapter;
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
            userStoryId = getArguments().getString("userStoryId");
            Log.d(TAG,userStoryId+" gggg");

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

                            retriveStories(list);
                        }// end for loop


                    }
                }
            }
        });


    }
    public void retriveStories(List<String> list) {


        for(int i=0; i<list.size(); i++) {
            DocumentReference docRef = firebaseFirestore.collection("publishedStories").document(list.get(i));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            document.getData();
                            String title = (String) document.get("title");
                            String userId = (String) document.get("userId");
                            String storyId = (String) document.getId();
                            String userName = "";
                            String pic = (String) document.get("pic");
                            String sound = (String) document.get("sound");

                            Item item = new Item(true,storyId, title, pic,sound, userId, "", "");
                            itemList.add(item);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
            });

        }//end for loop
    }



    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}


