package sa.ksu.swe444.hackwati;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PopularStories extends Fragment {


    private RecyclerView recyclerView;
    private storyAdapter adapter;
    private List<Item> itemList;
    private RecyclerView.LayoutManager mLayoutManager;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_popular_stories, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {

        itemList = new ArrayList<>();
        adapter = new storyAdapter(getActivity(),itemList);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        retrieveSubscribedUsers();

    }

    private void retrieveSubscribedUsers() {
        firebaseFirestore.collection("stories")
                .orderBy("rate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int x=0;
                            while (x!=10){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String title = (String) document.get("title");
                                String pic = (String) document.get("pic");

                                Log.d(TAG, document.getId() + "test rate" + document.get("rate"));
                                Item item = new Item( title, pic);
                                itemList.add(item);
                                adapter.notifyDataSetChanged();
                                x++;
                            }
                        } }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}
