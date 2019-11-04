package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sa.ksu.swe444.hackwati.R;


public class Tab1Fragment extends Fragment {

LinearLayout linear;
    public FirebaseAuth mAuth;

    TextView userinfo;

    private static final String TAG = "Tab1Fragment";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userUid;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userStoryId;



    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.profile_fragment_one, container, false);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            userStoryId = getArguments().getString("userStoryId");
            Log.d(TAG,userStoryId+" gggg");

        }

        userinfo=v.findViewById(R.id.userinfo);


        retriveUserData();

       /* namesList();*/

        return  v;
    }

    public void retriveUserData() {


        DocumentReference docRef = firebaseFirestore.collection("users").document(userStoryId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userInformation = document.get("info").toString();
                            userinfo.setText(userInformation+"");


                    }
                }
            }
        });


    }




}

