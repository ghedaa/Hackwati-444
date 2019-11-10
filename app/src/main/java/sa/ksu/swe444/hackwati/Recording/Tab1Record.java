package sa.ksu.swe444.hackwati.Recording;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.tyorikan.voicerecordingvisualizer.RecordingSampler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SignUp;
import sa.ksu.swe444.hackwati.SplashActivity;
import sa.ksu.swe444.hackwati.explor.ExploreActivity;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Tab1Record extends Fragment implements View.OnClickListener, IOnFocusListenable {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private ImageButton recordButton;
    private MediaRecorder recorder;
    private MediaPlayer playerDog;
    private MediaPlayer playerLion;
    private MediaPlayer playerMonkey;
    private MediaPlayer playerBird;


    boolean mStartPlaying;
    private TextView timer;
    private boolean isRecording = false;
    private boolean startRecording;
    private ImageButton imageButton;
    private SecondFragmentListener listener;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private View view;

    private VisualizerView visualizerView;
    private Handler handler;
    Runnable updater;
    private long elapsedTime;
    private long start;
    private Button nextBtn;
    private List<VisualizerView> mVisualizerViews = new ArrayList<>();
    private RecordingSampler.CalculateVolumeListener mVolumeListener;
    private int mSamplingInterval = 100;
    private Timer mTimer;
    private ImageButton dog;
    private ImageButton monkey;
    private ImageButton lion;
    private ImageButton bird;
    private ImageButton playRecord;
    private ImageButton stopPlayRecord;
    private MediaPlayer player = null;
    private Button cancelRecording;
    private WaveVisualizer mVisualizer;
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 33;
    // pass this att to
    private long duration;
    String t;
    public BottomNavigationView navView;



    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recording_fragment_one, container, false);
        // Record to the external cache directory for visibility
        fileName = getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        startRecording = true;
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        recordButton = view.findViewById(R.id.record_btn);
        cancelRecording = view.findViewById(R.id.cancel_recording);
        cancelRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        mVisualizer = view.findViewById(R.id.blast);
        //initRecorder();
        timer = view.findViewById(R.id.timer);
        visualizerView = view.findViewById(R.id.visualizer);
        nextBtn = view.findViewById(R.id.next_btn);
        playRecord = view.findViewById(R.id.listen_record_btn);
        stopPlayRecord = view.findViewById(R.id.stop_listen_record_btn);

        playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording && checkForPermission()) {
                    startPlaying();
                }
            }
        });

        stopPlayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForPermission() && player.isPlaying() )
                    player.stop();

            }
        });

        dog = view.findViewById(R.id.record_dog);
        playerDog = MediaPlayer.create(getContext(), R.raw.bark);
        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerDog.start();
            }
        });
        monkey = view.findViewById(R.id.record_monkey);
        playerMonkey = MediaPlayer.create(getContext(), R.raw.rmonkeycolobus);
        monkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerMonkey.start();

            }
        });
        lion = view.findViewById(R.id.record_lion);
        playerLion = MediaPlayer.create(getContext(), R.raw.lion4);
        lion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerLion.start();
            }
        });

        bird = view.findViewById(R.id.record_bird);
        playerBird = MediaPlayer.create(getContext(), R.raw.bird);
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerBird.start();
            }
        });


        recordButton.setOnClickListener(this);


        startRecording = true;
        mStartPlaying = false;
        handler = new Handler();
        updater = new Runnable() {
            public void run() {
               // Toast.makeText(getContext(), "run", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 1);
                if (isRecording) {  /**/
                    int maxAmplitude = recorder.getMaxAmplitude();
                    if (maxAmplitude != 0) {
//                        mVisualizer.setAudioSessionId(maxAmplitude);
                        visualizerView.addAmplitude(maxAmplitude);
                        elapsedTime = System.currentTimeMillis() - start;
                        t = milliSecondsToTimer(elapsedTime);
                        timer.setText(t);
                        timer.setTextColor(R.color.gray);
                        if (t.equals("0 : 3")) {
                            recorder.stop();
                            isRecording = false;
                            recordButton.setEnabled(false);
                        }
                    }
                }
            }// run

        };
        handler.postDelayed(updater , 1000);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (isRecording)
                //     recorder.stop();
                if (!startRecording) {
                    listener.onNextButton();
                } else if (isRecording) {
                    showDialogWithOkButton("هل تريد إلغاء التسجيل؟");

                } else {
                    showDialogWithOkButton("لم تسجل القصة بعد");
                }




            }
        });

       if(checkForPermission()) {
           initRecorder();



       }
        bottomNavigation();
        return view;
    }//onCreateView()

    public void bottomNavigation() {
        navView = view.findViewById(R.id.nav_view_rec);
        navView.setSelectedItemId(R.id.navigation_record);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.navigation_record:
                            break;

                        case R.id.navigation_subscription:
                            if(!isRecording && startRecording ) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                            else {
                                showDialogWithOkButton("هل حقاً تريد ترك القصة؟");
                            }
                            break;

                        case R.id.navigation_explore:
                            if(!isRecording && startRecording) {
                                startActivity(new Intent(getContext(), ExploreActivity.class));
                            }
                            else {
                                showDialogWithOkButton("هل حقاً تريد ترك القصة؟");
                            }
                            break;

                    }// end of switch


                return true;
            }
        });
    }
    private boolean checkForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            return false;
        } else {

            return true;
        }
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }//initRecorder()

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Toast.makeText(getContext(), "Permissions Accepted to record audio", Toast.LENGTH_LONG).show();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_btn:

                if (checkForPermission()) {
                    if (startRecording ) {
                        isRecording = true;
                        recorder.start();
                        start = System.currentTimeMillis();
                        startRecording = false;
                        nextBtn.setEnabled(false);
                        ViewCompat.setBackgroundTintList(nextBtn, ContextCompat.getColorStateList(getContext(), R.color.gray));

                    } else if(isRecording){
                        recorder.stop();
                        duration = elapsedTime;
                        isRecording = false;
                        nextBtn.setEnabled(true);
                        //timer.setText(t);
                        recordButton.setEnabled(false);
                        ViewCompat.setBackgroundTintList(nextBtn, ContextCompat.getColorStateList(getContext(), R.color.yellow_hak));


                    }
                }


          /*  case R.id.play_btn:
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    playButton.setText("Stop playing");
                } else {
                    playButton.setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
                break;*/

        }// switch

    }//onClick()

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
        }

    }//onStop()


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
            handler.post(updater);

    }//onWindowFocusChanged()

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there


        finalTimerString = seconds + " : " + minutes;

        // return timer string
        return finalTimerString;
    }// milliSecondsToTimer


    public interface SecondFragmentListener {
        // methods are the action i need
        void onNextButton();
    }


    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }//onPlay()

    private void startPlaying() {
        player = new MediaPlayer();
        int audioSessionId = player.getAudioSessionId();
        if (audioSessionId != -1)
            mVisualizer.setAudioSessionId(audioSessionId);

        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }//startPlaying()

    private void stopPlaying() {
        player.stop();
        player.release();
        player = null;
    }//stopPlaying()

    private void showDialogWithOkButton(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("حسناً", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                        if(isRecording){
                            recorder.stop();
                          //  isRecording = false;
                        }
                    }
                }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
          listener = (SecondFragmentListener) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    /**
     * stop recording
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        super.onDestroy();
        if (mVisualizer != null)
            mVisualizer.release();
    }

    public boolean goToNext(){
        if(startRecording || isRecording)
            return false;
        else
            return true;
    }

}// class