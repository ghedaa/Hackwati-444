package sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;
import sa.ksu.swe444.hackwati.user_profile_activity.UserProfileActivity;

public class recordComplitaion extends AppCompatActivity {

    public BottomNavigationView navView;
    TextView done,discriprion;
    Button stories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_complitaion);

        done = findViewById(R.id.done);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              done.setVisibility(View.VISIBLE);
            }

        }, 2000);

        discriprion = findViewById(R.id.discriprion);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                discriprion.setVisibility(View.VISIBLE);
            }

        }, 2700);

        //Button
        stories = findViewById(R.id.stories);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                stories.setVisibility(View.VISIBLE);
            }

        }, 3300);




        stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = 2;
                Intent intent = new Intent(recordComplitaion.this, UserProfileActivity.class);
                intent.putExtra("One", page);// One is your argument
                startActivity(intent);
            }
        });



        //NavView
        navView = findViewById(R.id.navigation_record);
        navView.setSelectedItemId(R.id.navigation_record);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(recordComplitaion.this, recordActivity.class));
                        break;
                    case R.id.navigation_subscription:
                        startActivity(new Intent(recordComplitaion.this, MainActivity.class));
                        break;
                    case R.id.navigation_explore:
                        startActivity(new Intent(recordComplitaion.this, ExploreActivity.class));
                        break;

                }// end of switch
                return true;
            }
        });


    }


    @Override
    public void onBackPressed() {

       startActivity(new Intent(recordComplitaion.this, MainActivity.class));
       finish();


    }


}
