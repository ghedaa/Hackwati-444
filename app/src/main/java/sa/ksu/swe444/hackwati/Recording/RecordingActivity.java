package sa.ksu.swe444.hackwati.Recording;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;


public class RecordingActivity extends AppCompatActivity implements Tab1Record.SecondFragmentListener{
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Tab1Record f1;
    private Toolbar toolbarRec;
    private Tab2StoryInfo f2;

    public BottomNavigationView navView;
    private static final String TAG = "Recording screen";


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main_activity);
        toolbarRec= (Toolbar) findViewById(R.id.toolbarRec);
        setSupportActionBar(toolbarRec);
        getSupportActionBar().setTitle("سجل");
        navView = findViewById(R.id.nav_view_rec);


        //initiate frags
        f1 = new Tab1Record();
        f2 = new Tab2StoryInfo();

        // fragments array list
        ArrayList<Fragment> frags = new ArrayList<>();
        frags.add(f1);
        frags.add(f2);

        //titles array list
        ArrayList<String> titles = new ArrayList<>();
        titles.add("سجل صوتك");
        titles.add("انشر قصتك");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new TabAdapter(getSupportFragmentManager() , frags , titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

     // this code below to disable swapping between Tabs
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }

        });



        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(RecordingActivity.this, RecordingActivity.class));
                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(RecordingActivity.this, MainActivity.class));
                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(RecordingActivity.this, ExploreActivity.class));
                        break;

                }// end of switch
                return true;
            }
        });

    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

            ((IOnFocusListenable) f1).onWindowFocusChanged(hasFocus);

    }

    @Override
    public void onNextButton() {
        viewPager.setCurrentItem(1);
        Tab2StoryInfo f2 = new Tab2StoryInfo();
     //   f2.getInfo(duration , timestamp , id);

    }
}