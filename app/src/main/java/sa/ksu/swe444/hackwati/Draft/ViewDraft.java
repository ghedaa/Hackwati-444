package sa.ksu.swe444.hackwati.Draft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.ksu.swe444.hackwati.AdminActivity;
import sa.ksu.swe444.hackwati.ConcatUsActivity;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.MySharedPreference;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SplashActivity;
import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;
import sa.ksu.swe444.hackwati.storyAdapter;
import sa.ksu.swe444.hackwati.user_profile_activity.UserProfileActivity;

public class ViewDraft extends AppCompatActivity {

    private RecyclerView recyclerView;
    private storyAdapter adapter;
    private List<Item>   itemList = new ArrayList<>();
    private Toolbar toolbarMain;
    public BottomNavigationView navView;
    public View item;
    private TextView emptyStories;
    private String userUid;
    private Button goToStory;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "AdminActivity";
    private FirebaseAuth auth;
    public FirebaseAuth mAuth= FirebaseAuth.getInstance();



    public String userName, userTumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_draft);

        emptyStories =findViewById(R.id.emptyStories);
        navView = findViewById(R.id.nav_view);
        toolbarMain = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setTitle("المسودات");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);





        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbarMain);


        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initRecyclerView();
        retrieveUserDraft();


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(ViewDraft.this, recordActivity.class));
                        // navView.setSelectedItemId(R.id.navigation_record);
                        //  navView.getMenu().getItem(R.id.navigation_record).setChecked(true);
                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(ViewDraft.this, MainActivity.class));
                        //navView.setSelectedItemId(R.id.navigation_subscription);

                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(ViewDraft.this, ExploreActivity.class));
                        //   navView.setSelectedItemId(R.id.navigation_explore);

                        break;

                }// end of switch

                return true;
            }
        });

    }//end onCreate



    private void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new storyAdapter(this, itemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new AdminActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }//end dpToPx

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

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
        }
    }//end inner class GridSpacingItemDecoration

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), UserProfileActivity.class); // from where? and to the distanation
        startActivity(intent); // to start another activity
    }


    public void retrieveUserDraft() {


        firebaseFirestore.collection("draft")
                .whereEqualTo("userId", auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();

                            for (DocumentSnapshot document : myListOfDocuments) {


                                document.getData();
                                String description = (String) document.get("description");
                                String pic = (String) document.get("pic");
                                String rate = (String) document.get("rate");
                                String sound = (String) document.get("sound");
                                String title = (String) document.get("title");
                                String userId = (String) document.get("userId");
                                String storyId = (String) document.getId();
                                userName = "";
                                userTumbnail = "";
                                Item item = new Item(storyId, title, pic,userId, sound);
                                itemList.add(item);
                                adapter.notifyDataSetChanged();


                            }
                        }
                    }
                });

    }

    private void setListener(final AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDraft.this);
                        builder.setMessage("هل أنت متأكد من أنك تريد تسجيل الخروج؟")
                                .setCancelable(false)
                                .setPositiveButton("أنا متأكد", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        //Token ID
                                        String uid= mAuth.getInstance().getUid();
                                        Map<String,Object> user_updateToken = new HashMap<>();
                                        user_updateToken.put("TokenID","");
                                        firebaseFirestore.collection("users").document(uid).update(user_updateToken);
                                        // done by fatimah clearing token id

                                        FirebaseAuth.getInstance().signOut();
                                        MySharedPreference.clearData(ViewDraft.this);

                                        startActivity(new Intent(ViewDraft.this, SplashActivity.class));
                                    }

                                });
                        builder.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();


                        break;
                    case 2:
                        startActivity(new Intent(ViewDraft.this, UserProfileActivity.class));
                        break;
                    case 3:
                        break;
                    case 4:
                        startActivity(new Intent(ViewDraft.this, ConcatUsActivity.class));
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

}
