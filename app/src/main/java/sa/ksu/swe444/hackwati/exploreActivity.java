package sa.ksu.swe444.hackwati;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class exploreActivity extends AppCompatActivity {

    private fragmentadapter adapter;
    private fragmentadapter adapter2;
    private ViewPager viewPager;
    private ViewPager viewPager2;
    private AllStories allStories;
    private PopularStories popularStories;

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
