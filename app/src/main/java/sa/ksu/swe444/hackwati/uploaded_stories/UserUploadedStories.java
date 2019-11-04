package sa.ksu.swe444.hackwati.uploaded_stories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.ConcatUsActivity;
import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SplashActivity;
import sa.ksu.swe444.hackwati.SubscribedListActivity;
import sa.ksu.swe444.hackwati.UserProfile;
import sa.ksu.swe444.hackwati.list_adabter.CustomAdapter;
import sa.ksu.swe444.hackwati.list_adabter.CustomPojo;

public class UserUploadedStories extends AppCompatActivity {
    private String userUid;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UploadedStoriesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<story> namesList;
    private TextView emptyUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_stories);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        emptyUsers = findViewById(R.id.emptyUsers);
        recyclerView = findViewById(R.id.recycleView);
        namesList = new ArrayList<>();
        mAdapter = new UploadedStoriesAdapter(UserUploadedStories.this);

        //We set the array to the adapter
        mAdapter.setListContent(itemList);

        mLayoutManager = new LinearLayoutManager(UserUploadedStories.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        retrievePublishedStories();
        retrieveRejectedStories();
        retrieveUnderProcessingStories();


        installButton110to250();

    }//end onCreate()



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
                        item.setStatus(Constants.Keys.PUBLISHED);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }// end of retrievePublishedStories
    public void retrieveRejectedStories(){
        firebaseFirestore.collection("rejectedStories")
                .whereEqualTo("userId", userUid + "")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
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

                        Item item = new Item(true,storyId, title, pic,sound, userId, userName, thumbnail);
                        item.setStatus(Constants.Keys.REJECTED);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }// end of retrieveRejectedStories
    public void retrieveUnderProcessingStories(){
        firebaseFirestore.collection("stories")
                .whereEqualTo("userId", userUid + "")
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
                        item.setStatus(Constants.Keys.PROCESSING);
                        itemList.add(item);

                        mAdapter.notifyDataSetChanged();

                    }

                }//end is successful

            }
        });

    }//end of retrieveUnderProcessingStories

    private void installButton110to250() {


        final AllAngleExpandableButton button = (AllAngleExpandableButton) findViewById(R.id.button_expandable_110_250);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.defult_thumbnail, R.drawable.ic_power_settings_new_black_24dp, R.drawable.defult_thumbnail, R.drawable.ic_search_black_24dp, R.drawable.ic_error_outline_black_24dp};// gray is some thing else
        int[] color = {R.color.yellow_hak, R.color.gray_hak, R.color.gray_hak, R.color.gray_hak, R.color.gray_hak};
        for (int i = 0; i < 5; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 7);
            } else {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            }
            buttonData.setBackgroundColorId(this, color[i]);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);
        setListener(button);
    }

    private void setListener(final AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1:


                        AlertDialog.Builder builder = new AlertDialog.Builder(UserUploadedStories.this);
                        builder.setMessage("هل أنت متأكد من أنك تريد تسجيل الخروج؟")
                                .setCancelable(false)
                                .setPositiveButton("أنا متأكد", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(UserUploadedStories.this, SplashActivity.class));
                                    }

                                });
                        builder.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { }});

                        AlertDialog alert = builder.create();
                        alert.show();


                        break;
                    case 2:
                        startActivity(new Intent(UserUploadedStories.this, UserProfile.class));
                        break;
                    case 3:
                        break;
                    case 4:
                        startActivity(new Intent(UserUploadedStories.this, ConcatUsActivity.class));
                        break;
                }
            }

            @Override
            public void onExpand() {


            }

            @Override
            public void onCollapse() {
            }
        });
    }

}// class
