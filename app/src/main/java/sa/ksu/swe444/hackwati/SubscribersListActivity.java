package sa.ksu.swe444.hackwati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_list);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        emptyUsers = findViewById(R.id.emptyUsers);

        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new CustomAdapter(SubscribersListActivity.this);

        //We set the array to the adapter
        mAdapter.setListContent(namesList);

        mLayoutManager = new LinearLayoutManager(SubscribersListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        Subscribers ();
       // retrieveSubscribedUsers();

    }




    public void Subscribers (){


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
                                CustomPojo user = new CustomPojo(userName,thumbnail);
                                namesList.add(user);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                });

    }

    public void retrieveSubscribedUsers() {

        DocumentReference docRef = firebaseFirestore.collection("users").document();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> listSubscribers = new ArrayList<>();

                        final List<String> list = (List<String>) document.get("subscribedUsers");
                        if (list == null) {
                        } else {


                         for(int i=0; i<list.size(); i++){

                               if (list.get(i).equalsIgnoreCase(userUid))
                                   listSubscribers.add(list.get(i));


                            }

                        }// end for loop


                    }
                }
            }
        });


    }


    public void retriveUserData(String name, String imge) {





    }

}
