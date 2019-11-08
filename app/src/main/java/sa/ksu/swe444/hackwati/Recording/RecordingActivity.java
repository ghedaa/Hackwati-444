package sa.ksu.swe444.hackwati.Recording;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

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
       // navView.setSelectedItemId(R.id.);


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
                if(f1.goToNext())
                    return false;
                else
                    return true;
            }

        });
// to enable swapping between tabs if you have not record
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(f1.goToNext())
                        return false;
                    else
                        return true;
                }
            });
        }


    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(f1.goToNext())
            ((IOnFocusListenable) f1).onWindowFocusChanged(hasFocus);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNextButton() {
        if(f1.goToNext())
          viewPager.setCurrentItem(1);

    }
}