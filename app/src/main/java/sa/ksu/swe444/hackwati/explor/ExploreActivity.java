package sa.ksu.swe444.hackwati.explor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    }
}
