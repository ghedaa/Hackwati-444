package sa.ksu.swe444.hackwati.user_profile_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.MySharedPreference;
import sa.ksu.swe444.hackwati.R;

public class EditProfile extends AppCompatActivity {

    private ImageView imageView;
    private TextView editImg;
    private EditText name;
    private EditText bio;
    private TextView emailText;
    private Button save;
    private static int INTENT_GALLERY = 301;
    private boolean isSelectImage;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseAuth mAuth;
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String userUid;
    private String imgPath;
    Uri contentURI;
    private File imgFile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //init
        imageView = findViewById(R.id.userImg);
        editImg = findViewById(R.id.plus);
        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraChooser();
                editImg();
            }
        });
        bio = findViewById(R.id.infotext);

        name = findViewById(R.id.nameSignUpHin);

        emailText = findViewById(R.id.emailSignUpHin);
        save = findViewById(R.id.edit_profile);
        mAuth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save changes
                String n = name.getText().toString();
                String b = bio.getText().toString();
                editName(n);
                editBio(b);
                uploadImageWithUri();
                Toast.makeText(EditProfile.this, "تم حفظ التعديلات بنجاح", Toast.LENGTH_SHORT).show();

            }
        });

        retriveUserData();
    }// onCreate


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
                            name.setText(userName);
                            emailText.setText(email);

                            Glide.with(getBaseContext())
                                    .load(thumbnail + "")
                                    .into(imageView);
                            bio.setText(infoU);


                        }

                    }
                }
            }
        });


    }

    private void editImg(){

    }
    private void editBio(String b){

        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef
                .update("info", b)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "تم تعديل النبذة");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
    }



    private void editName(String n){
        DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
        washingtonRef
                .update("username", n + "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "تم تعديل الاسم");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
    }


    private void openCameraChooser() {
        if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 100);
        }// end if

        showPhotoOptionsDialog();
    }//end of openCameraChooser()
    private void showPhotoOptionsDialog() {
        final CharSequence[] items = {"الصور"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (items[item].equals("الصور")) {
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
        Log.d("TAG", "aa2");

        if (imgPath != null) {

            final StorageReference filepath = storageRef.child(userUid).child("thumbnail.jpeg");

            //uploading the image
            final UploadTask uploadTask = filepath.putFile(contentURI);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        MySharedPreference.putString(EditProfile.this, Constants.Keys.USER_IMG, downloadURL);


                        DocumentReference updateRef = firebaseFirestore.collection("users").document(userUid);
                        Toast.makeText(EditProfile.this, "تم رفع الصورة", Toast.LENGTH_SHORT).show();

                        // reset the thumbnail" field
                        updateRef
                                .update("thumbnail", downloadUri + "")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error updating document", e);
                                    }
                                });


                        //Log.d(TAG,downloadURL+" Ya2" );

                    }
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   // Toast.makeText(getBaseContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    Log.d("","Upload successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                 //   Toast.makeText(getBaseContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    Log.d("","Upload Failed -> ");

                }
            });
        } else {
            Toast.makeText(getBaseContext(), "اختر صوره", Toast.LENGTH_SHORT).show();
        }
    }

    private void persistImage(Bitmap bitmap) {
        File fileDir = getBaseContext().getFilesDir();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EditProfile.RESULT_CANCELED) {
            return;
        }//end if statement
        if (requestCode == INTENT_GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    isSelectImage = true;
                    persistImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "خطأ", Toast.LENGTH_SHORT).show();
                }// end catch
            }//end if statement

        }
    }//end onActivityResult()
}
