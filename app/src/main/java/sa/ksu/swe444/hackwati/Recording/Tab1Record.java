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
import androidx.fragment.app.Fragment;

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


@RequiresApi(api = Build.VERSION_CODES.O)
public class Tab1Record extends Fragment implements View.OnClickListener, IOnFocusListenable {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private ImageButton recordButton;
    private MediaRecorder recorder;
    private MediaPlayer playerDog ;
    private MediaPlayer playerLion ;
    private MediaPlayer playerMonkey ;
    private MediaPlayer playerBird ;



    boolean mStartPlaying;
    private TextView timer ;
    private boolean isRecording = false;
    private boolean startRecording;
    private ImageButton imageButton;
    private SecondFragmentListener listener;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
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
    // pass this att to
    private long duration;





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

        initRecorder();
        timer = view.findViewById(R.id.timer);
        visualizerView = view.findViewById(R.id.visualizer);
        nextBtn = view.findViewById(R.id.next_btn);
        playRecord = view.findViewById(R.id.listen_record_btn);
        stopPlayRecord= view.findViewById(R.id.stop_listen_record_btn);

        playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRecording) {
                    startPlaying();
                }
            }
        });

        stopPlayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying())
                    player.stop();

            }
        });

        dog= view.findViewById(R.id.record_dog);
        playerDog = MediaPlayer.create(getContext() ,R.raw.bark);
        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerDog.start();
            }
        });
        monkey = view.findViewById(R.id.record_monkey);
        playerMonkey = MediaPlayer.create(getContext() ,R.raw.rmonkeycolobus);
        monkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerMonkey.start();

            }
        });
        lion = view.findViewById(R.id.record_lion);
        playerLion = MediaPlayer.create(getContext() ,R.raw.lion4);
        lion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerLion.start();
            }
        });

        bird = view.findViewById(R.id.record_bird);
        playerBird = MediaPlayer.create(getContext(),R.raw.bird);
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerBird.start();
            }
        });



        recordButton.setOnClickListener(this);
        //playButton.setOnClickListener(this);

        //playButton.setText("start playing");


        startRecording = true;
        mStartPlaying = false;
         handler = new Handler();
         updater = new Runnable() {
             public void run() {
                    handler.postDelayed(this, 1);
                    if(isRecording){  /**/
                    int maxAmplitude = recorder.getMaxAmplitude();
                    if (maxAmplitude != 0) {
                        visualizerView.addAmplitude(maxAmplitude);
                          elapsedTime = System.currentTimeMillis() - start;
                       String t= milliSecondsToTimer(elapsedTime);
                        timer.setText(t);
                        if(t.equals("3 : 0")) {
                            recorder.stop();
                            isRecording = false;
                            recordButton.setEnabled(false);
                        }
                    }
                        }
            }// run
        };

         nextBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                // if (isRecording)
                //     recorder.stop();
                 listener.onNextButton();


                 //startActivity(new Intent(getContext(), ));


               /*  if (recorder != null)
                     showDialogWithOkButton("لم تقم بالتسجيل !");*/
/*
                else{ AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                 builder.setMessage("هل انتهيت التسجيل؟؟")
                         .setCancelable(false)
                         .setPositiveButton("نعم، التالي", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {



                                 listener.onNextButton();
                                // isRecording = false;


                             }

                         });
                 builder.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {

                     }

                 });

                 AlertDialog alert = builder.create();
                 alert.show();


             }*/


             }
         });


        return view;
    }//onCreateView()


    private void initRecorder(){
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        //if (!permissionToRecordAccepted )
        //   getActivity().finish();

    }//onRequestPermissionsResult





    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_btn:
                if(startRecording){
                    recorder.start();
                    start = System.currentTimeMillis();
                    startRecording = false;
                    isRecording = true;
                    nextBtn.setBackgroundColor(R.color.gray);
                    nextBtn.setEnabled(false);
                    Toast.makeText(getContext(),"start recording" , Toast.LENGTH_LONG).show();


                }else {
                    recorder.stop();
                    nextBtn.setBackgroundColor(R.color.colorPrimary);
                    duration = elapsedTime;
                    isRecording = false;
                    nextBtn.setEnabled(true);
                    Toast.makeText(getContext(),"btn is enabled" , Toast.LENGTH_LONG).show();

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


        finalTimerString = minutes +" : "+ seconds;

        // return timer string
        return finalTimerString;
    }// milliSecondsToTimer


    public interface SecondFragmentListener{
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

    private void showDialogWithOkButton(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
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



}// class