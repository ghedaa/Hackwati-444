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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import sa.ksu.swe444.hackwati.storyAdapter;

public class Tab4Fragment extends Fragment {

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
        View view= inflater.inflate(R.layout.profile_fragment_two, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        emptyStories =view.findViewById(R.id.emptyStories);

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
        retrieveUserStories();
    }

    private void retrieveUserStories() {
        firebaseFirestore.collection("publishedStories")
                .whereEqualTo("userId", userUid)// <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            for (DocumentSnapshot document : task.getResult()) {


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

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
