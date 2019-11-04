package sa.ksu.swe444.hackwati;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinda.alert.KAlertDialog;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Draft.ViewDraft;
import sa.ksu.swe444.hackwati.list_adabter.CustomPojo;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;


public class UserProfile extends BaseActivity {
    Button log_out;
    private TextView relImg, info, storyno, subscribed ,subscriber,subscriberno;
    private static int INTENT_GALLERY = 301;
    private boolean isSelectImage;
    Uri contentURI;
    private File imgFile;
    public FirebaseAuth mAuth;
    private Button uploadImg;
    private String imgPath;
    private ImageView edit1, img, edit2;
    private Button draft;
    private TextView stories;



    private static final String TAG = "UserProfile";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userUid;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    private TextView userNameText, emailText, subscribedno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_vistor_row);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ملفي الشخصي");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        img = findViewById(R.id.userImg);
        userNameText = findViewById(R.id.nameSignUpHin);
        emailText = findViewById(R.id.emailSignUpHin);
        draft = findViewById(R.id.draft_page);
        draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ViewDraft.class);
                intent.putExtra(Constants.Keys.DRAFT, true);
                startActivity(intent);
            }
        });
        stories = findViewById(R.id.story);
        stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UserUploadedStories.class));
            }
        });


        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        relImg = findViewById(R.id.plus);
        uploadImg = findViewById(R.id.uploadImg);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageWithUri();


            }
        });

        subscriberno = findViewById(R.id.subscriberno);
        subscribedno = findViewById(R.id.subscribedno);
        subscribed = findViewById(R.id.subscribed);
        subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this,SubscribedListActivity.class));
            }
        });
        storyno = findViewById(R.id.storyno);

        relImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCameraChooser();

            }
        });


        edit1 = findViewById(R.id.edit1);
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogWithTextInput("تعديل اسم المستخدم");
            }
        });


        subscriber = findViewById(R.id.subscriber);
        subscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this,SubscribersListActivity.class));            }
        });

        info = findViewById(R.id.infotext);

        edit2 = findViewById(R.id.edit2);
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edituserInfo("تعديل النبذة");
            }
        });

        log_out = findViewById(R.id.logout_profile);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        retriveUserData();
        countStories();
        Subscribers ();

        //////////


    }// end onCreate()

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfile.this, SplashActivity.class));
    }//end of signOut


    public void retriveUserData() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userName = document.get("username").toString();
                        String email = document.get("email").toString();
                        String thumbnail = document.get("thumbnail").toString();
                        String infoU = document.get("info").toString();
                        List<String> list = (List<String>) document.get("subscribedUsers");

                        if (userName != null && email != null) {
                            userNameText.setText(userName);
                            emailText.setText(email);

                            Glide.with(UserProfile.this)
                                    .load(thumbnail + "")
                                    .into(img);

                            subscribedno.setText(list.size()+"");

                            info.setText(infoU);


                        }

                    }
                }
            }
        });


    }

    // IMAGE

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UserProfile.this.RESULT_CANCELED) {
            return;
        }//end if statement
        if (requestCode == INTENT_GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserProfile.this.getContentResolver(), contentURI);
                    Toast.makeText(UserProfile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(bitmap);
                    isSelectImage = true;
                    persistImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }// end catch
            }//end if statement

        }
    }//end onActivityResult()
    private void persistImage(Bitmap bitmap) {
        File fileDir = UserProfile.this.getFilesDir();
        String name = "image";

        imgFile = new File(fileDir, name + ".png");
        OutputStream os;
        try {
            os = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            imgPath = imgFile.getPath();
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }//end catch
    }//end of persistImage()
    private void openCameraChooser() {
        if (ActivityCompat.checkSelfPermission(UserProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.CAMERA}, 100);
        }// end if

        showPhotoOptionsDialog();
    }//end of openCameraChooser()
    private void showPhotoOptionsDialog() {
        final CharSequence[] items = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (items[item].equals("Gallery")) {
                    galleryIntent();
                }//end if else
            }//end onClick()
        });//end setItems
        builder.show();
    }//end showPhotoOptionsDialog()
    private void galleryIntent() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, INTENT_GALLERY);
    }//END OF galleryIntent()
    private void uploadImageWithUri() {
        Log.d(TAG, "aa2");

        if (imgPath != null) {

            final StorageReference filepath = storageRef.child(userUid).child("thumbnail.jpeg");

            //uploading the image
            final UploadTask uploadTask = filepath.putFile(contentURI);

            Log.d(TAG, "aa1");


            // get Uri
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();
                        MySharedPreference.putString(UserProfile.this, Constants.Keys.USER_IMG, downloadURL);


                        DocumentReference updateRef = firebaseFirestore.collection("users").document(userUid);
                        Toast.makeText(UserProfile.this, "تم رفع الصورة", Toast.LENGTH_SHORT).show();

                        // reset the thumbnail" field
                        updateRef
                                .update("thumbnail", downloadUri + "")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });


                        //Log.d(TAG,downloadURL+" Ya2" );

                    }
                }
            });


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserProfile.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserProfile.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialogWithTextInput(String title) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(UserProfile.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilderUserInput = new androidx.appcompat.app
                .AlertDialog.Builder(UserProfile.this);

        alertDialogBuilderUserInput.setView(mView)
                .setTitle(title + "");

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


                        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
                        washingtonRef
                                .update("username", userInputDialogEditText.getText().toString() + "")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "تم تعديل الاسم");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });


                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        androidx.appcompat.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void edituserInfo(String title) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(UserProfile.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilderUserInput = new androidx.appcompat.app
                .AlertDialog.Builder(UserProfile.this);

        alertDialogBuilderUserInput.setView(mView)
                .setTitle(title + "");

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


                        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
                        washingtonRef
                                .update("info", userInputDialogEditText.getText().toString() + "")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "تم تعديل النبذة");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });


                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        androidx.appcompat.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void countStories() {
        firebaseFirestore.collection("publishedStories")
                .whereEqualTo("userId", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                counter++;
                            }
                            storyno.setText(counter + "");

                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserProfile.this, MainActivity.class); // from where? and to the distanation
        startActivity(intent); // to start another activity
    }

    public void Subscribers (){

        firebaseFirestore.collection("users")
                .whereArrayContains("subscribedUsers", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int counter=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               counter++;
                            }
                            subscriberno.setText(counter+"");

                        }
                    }
                });

    }


}


