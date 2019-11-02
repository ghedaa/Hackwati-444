package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import sa.ksu.swe444.hackwati.R;


public class ProfileActivity extends AppCompatActivity {
   // public static final String GOOGLE_ACCOUNT = "google_account";
    //Declare the Adapter, RecyclerView and our custom ArrayList

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  Tab1Fragment tab1Fragment;
    private  Tab2Fragment tab2Fragment;
    private Tab3Fragment tab3Fragment;

    private ArrayList<CustomPojo> listContentArr= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);




        //initCollapsingToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        tab1Fragment = new Tab1Fragment();
        adapter.addFragment(tab1Fragment, "نبذه");

        tab2Fragment = new Tab2Fragment();
        adapter.addFragment(tab2Fragment, "القوائم");

        tab3Fragment = new Tab3Fragment();
        adapter.addFragment(tab3Fragment, "القصص");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }



}

