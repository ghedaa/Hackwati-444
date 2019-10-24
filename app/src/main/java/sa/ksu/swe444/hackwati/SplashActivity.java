package sa.ksu.swe444.hackwati;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;



public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout splashLayout; //Declare View "container"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_main);//R=res , path



        splashLayout = (LinearLayout) findViewById(R.id.splashLL); // 1- splash ID : <LinearLayout -> android:id="@+id/splashLL"
        splashLayout.setOnClickListener(this); //this: implements View.OnClickListener

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, Login.class); // from where? and to the distanation
                startActivity(intent); // to start another activity
                finish();
            }
        }, 2000);


    }//end OnCreate()


    @Override
    public void onClick(View view) {//move from screen to anther

        Intent intent = new Intent(SplashActivity.this, Login.class); // from where? and to the distanation
        startActivity(intent); // to start another activity
        finish();

    } // end onClick



}//end class

