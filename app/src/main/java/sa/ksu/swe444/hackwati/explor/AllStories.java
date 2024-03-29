package sa.ksu.swe444.hackwati.explor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.storyAdapter;


public class AllStories extends Fragment {

    private RecyclerView recyclerView;
    private storyAdapter adapter;
    private List<Item> itemList;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "AllStories";

    String intent;
    boolean searchb;
    String title;
    String search;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        title=null;
        initRecyclerView();
        searchb=false;
        return view;

    }

    public void setName(String name){
        Log.d("here ","onClick on AllStories"+name);

        title=name;
        retrieveSubscribedUsers();
    }
    public void setTitle(String name){
        search=name;
        searchb=true;
        if(search!=null)
        retrieveSubscribedUsers();

    }

    private void initRecyclerView() {
        itemList = new ArrayList<>();
        adapter = new storyAdapter(getActivity(), itemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        retrieveSubscribedUsers();
    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void retrieveSubscribedUsers() {
        Log.d(TAG, "All stories time" + Timestamp.now().toDate().toString());

        if ((title == null||title=="جميع القصص")&&searchb==false){
            Log.d(TAG, "title is null");

            firebaseFirestore.collection("publishedStories")
                    .orderBy("timestamp")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                itemList.clear();
                                adapter.notifyDataSetChanged();
                                List<Item> newitemList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "title is null inside");

                                    String title = (String) document.get("title");
                                    String userId = (String) document.get("userId");
                                    String storyId = (String) document.getId();
                                    String userName = "";
                                    String pic = (String) document.get("pic");
                                    String sound = (String) document.get("sound");

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Item item = new Item(true, storyId, title, pic, sound, userId, "", "");
                                    newitemList.add(item);
                                    itemList.addAll(newitemList);
                                    adapter.notifyDataSetChanged();

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }
        else if(title=="الأكثر شهرة"&&searchb==false){
            firebaseFirestore.collection("publishedStories")
                    .orderBy("rate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                itemList.clear();
                                adapter.notifyDataSetChanged();
                                List<Item> newitemList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "title is null inside");

                                    String title = (String) document.get("title");
                                    String userId = (String) document.get("userId");
                                    String storyId = (String) document.getId();
                                    String userName = "";
                                    String pic = (String) document.get("pic");
                                    String sound = (String) document.get("sound");

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Item item = new Item(true, storyId, title, pic, sound, userId, "", "");
                                    newitemList.add(item);
                                    itemList.addAll(newitemList);
                                    adapter.notifyDataSetChanged();

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }else if(search!=null && searchb==true){
            firebaseFirestore.collection("publishedStories")
                    .whereEqualTo("title",search)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                itemList.clear();
                                adapter.notifyDataSetChanged();
                                List<Item> newitemList = new ArrayList<>();
                                Log.d("","Search all");



                                try{

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        String title = (String) document.get("title");
                                        String userId = (String) document.get("userId");
                                        String storyId = (String) document.getId();
                                        String userName = "";
                                        String pic = (String) document.get("pic");
                                        String sound = (String) document.get("sound");

                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        Item item = new Item(true, storyId, title, pic, sound, userId, "", "");
                                        newitemList.add(item);
                                        itemList.addAll(newitemList);
                                        adapter.notifyDataSetChanged();

                                    }}catch (Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            searchb=false;
        }
                else {
            Log.d(TAG, "onClick title is not null");

            Log.d(TAG, "onClick title is not null"+itemList.size());

            firebaseFirestore.collection("publishedStories")
                    .whereEqualTo("story_type",title)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                    itemList.clear();
                                    adapter.notifyDataSetChanged();
                                List<Item> newitemList = new ArrayList<>();



                                try{

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String title = (String) document.get("title");
                                    String userId = (String) document.get("userId");
                                    String storyId = (String) document.getId();
                                    String userName = "";
                                    String pic = (String) document.get("pic");
                                    String sound = (String) document.get("sound");

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Item item = new Item(true, storyId, title, pic, sound, userId, "", "");
                                    newitemList.add(item);
                                    itemList.addAll(newitemList);
                                    adapter.notifyDataSetChanged();

                                }}catch (Exception e){
                                     e.printStackTrace();
                                 }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }




public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }}



}

