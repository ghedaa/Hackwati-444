package sa.ksu.swe444.hackwati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sa.ksu.swe444.hackwati.cafe.adriel.androidaudiorecorder.example.recordActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;

public class about_Us extends AppCompatActivity implements View.OnClickListener {
    public BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);

        Button buttonContact = findViewById(R.id.contact_button);
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(about_Us.this ,ConcatUsActivity.class);
                startActivity(intent);
            }
        });

        navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_subscription);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_record:
                        startActivity(new Intent(about_Us.this, recordActivity.class));

                        break;

                    case R.id.navigation_subscription:
                        startActivity(new Intent(about_Us.this, MainActivity.class));
                        break;

                    case R.id.navigation_explore:
                        startActivity(new Intent(about_Us.this, ExploreActivity.class));
                        break;

                }// end of switch
                return true;
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_record:
                startActivity(new Intent(about_Us.this, recordActivity.class));

        }
    }
}

