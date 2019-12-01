package sa.ksu.swe444.hackwati.user_profile_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Draft.ViewDraft;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.MySharedPreference;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.SplashActivity;
import sa.ksu.swe444.hackwati.SubscribedListActivity;
import sa.ksu.swe444.hackwati.SubscribersListActivity;
import sa.ksu.swe444.hackwati.uploaded_stories.UserUploadedStories;


public class Tab1Fragment extends Fragment {

    LinearLayout linear;
    public FirebaseAuth mAuth;
    Button log_out;
    private TextView relImg, info, storyno, subscribed, subscriber, subscriberno, stories;
    private static int INTENT_GALLERY = 301;
    private boolean isSelectImage;
    Uri contentURI;
    private File imgFile;
    private Button uploadImg;
    private String imgPath;
    private ImageView edit1, img, edit2;
    TextView userinfo;

    private static final String TAG = "Tab1Fragment";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userUid;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userStoryId;
    private Button editProfile ;



    private TextView userNameText, emailText, subscribedno;


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_vistor_row, container, false);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            userUid = getArguments().getString("userUid");

        }


        img = v.findViewById(R.id.userImg);
        userNameText = v.findViewById(R.id.nameSignUpHin);
        emailText = v.findViewById(R.id.emailSignUpHin);




        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        relImg = v.findViewById(R.id.plus);

        storyno = v.findViewById(R.id.storyno);






        info = v.findViewById(R.id.infotext);



        retriveUserData();
        countStories();
        Subscribers();

        editProfile= v.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });


        return v;
    }// onCreate()

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


                          //  subscribedno.setText(list.size() + "");
                            //todo : number of following

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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }//end if statement
        if (requestCode == INTENT_GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                   // Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(bitmap);
                    isSelectImage = true;
                    persistImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }// end catch
            }//end if statement

        }
    }//end onActivityResult()

    private void persistImage(Bitmap bitmap) {
        File fileDir = getContext().getFilesDir();
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }// end if

        showPhotoOptionsDialog();
    }//end of openCameraChooser()

    private void showPhotoOptionsDialog() {
        final CharSequence[] items = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        uploadImageWithUri();
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
                        MySharedPreference.putString(getContext(), Constants.Keys.USER_IMG, downloadURL);


                        DocumentReference updateRef = firebaseFirestore.collection("users").document(userUid);
                        Toast.makeText(getContext(), "تم رفع الصورة", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialogWithTextInput(String title) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilderUserInput = new androidx.appcompat.app
                .AlertDialog.Builder(getContext());

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

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilderUserInput = new androidx.appcompat.app
                .AlertDialog.Builder(getContext());

        alertDialogBuilderUserInput.setView(mView)
                .setTitle(title + "");

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setText(title);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

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

                .setNegativeButton("إلغاء",
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
                            final int[] sum = {0,0,0};

                            for (DocumentSnapshot document : task.getResult()) {
                                counter++;
                            }
                            sum[0] +=counter;

                            firebaseFirestore.collection("rejectedStories")
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
                                                sum[1] +=counter;

                                                firebaseFirestore.collection("stories")
                                                        .whereEqualTo("userId", userUid)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    int counter = 0;
                                                                    int storyNum=0;
                                                                    for (DocumentSnapshot document : task.getResult()) {
                                                                        counter++;
                                                                    }
                                                                    sum[2] +=counter;
                                                                    for(int i =0 ; i<sum.length ; i++){
                                                                        storyNum+=sum[i];
                                                                    }
                                                                    //storyno.setText(storyNum + " ");
                                                                    //todo : set number of stories
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });



                        }
                    }
                });
    }


    public void Subscribers() {

        firebaseFirestore.collection("users")
                .whereArrayContains("subscribedUsers", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                counter++;
                            }
                            //subscriberno.setText(counter + "");
                            //todo : set number of subscribers

                        }
                    }
                });
    }

    public void logout(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setMessage("هل أنت متأكد من أنك تريد تسجيل الخروج؟")
                .setCancelable(false)
                .setPositiveButton("أنا متأكد", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Token ID
                        String uid= mAuth.getInstance().getUid();
                        Map<String,Object> user_updateToken = new HashMap<>();
                        user_updateToken.put("TokenID","");
                        firebaseFirestore.collection("users").document(uid).update(user_updateToken);
                        // done by fatimah clearing token id

                        FirebaseAuth.getInstance().signOut();
                        MySharedPreference.clearData(getContext());

                        startActivity(new Intent(getContext(), SplashActivity.class));
                    }

                });
        builder.setNeutralButton("إلغاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();

    }//logout
}

