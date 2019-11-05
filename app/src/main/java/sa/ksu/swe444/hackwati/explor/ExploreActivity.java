package sa.ksu.swe444.hackwati.explor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.Recording.RecordingActivity;

public class ExploreActivity extends AppCompatActivity {

    private fragmentadapter adapter;
    private fragmentadapter adapter2;
    private ViewPager viewPager;
    private ViewPager viewPager2;
    private AllStories allStories;
    private PopularStories popularStories;
    public BottomNavigationView navView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("اكتشف");


        navView = findViewById(R.id.nav_explore_view);
        navView.setSelectedItemId(R.id.navigation_explore);

        allStories=new AllStories();
        popularStories=new PopularStories();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager2= (ViewPager) findViewById(R.id.viewPager2);
        adapter = new fragmentadapter(getSupportFragmentManager());
        adapter2=new  fragmentadapter(getSupportFragmentManager());
        adapter2.addFragment(allStories);
        adapter.addFragment(popularStories);
        viewPager.setAdapter(adapter);
        viewPager2.setAdapter(adapter2);



        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(ExploreActivity.this, RecordingActivity.class));

                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(ExploreActivity.this, MainActivity.class));
                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(ExploreActivity.this, ExploreActivity.class));
                        break;

                }// end of switch
                return true;
            }
        });
    }
}
