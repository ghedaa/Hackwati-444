package sa.ksu.swe444.hackwati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    public FirebaseAuth mAuth;
    private final String TAG = "Sign up";
    private EditText register_email;
    private EditText register_password;
    private EditText register_re_password;
    private EditText register_name;
    private EditText register_user_name;

    private Button register_btn;
    private String str;
    private TextView haveAccount ;
    private  FirebaseUser user;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        init();
        register_btn.setOnClickListener(this);
        haveAccount.setOnClickListener(this);


    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.email_register);
        register_password = findViewById(R.id.pass_register);
        register_re_password = findViewById(R.id.repass_register);
        register_btn = findViewById(R.id.register_btn);
        haveAccount = findViewById(R.id.haveAccount);
        register_name = findViewById(R.id.username_login);



    }

    private boolean checkPassword(String pass, String repass) {
        if (pass.equals(repass))
            return true;
        else
            return false;
    }

    private void register() {



        if (!checkPassword(register_password.getText().toString(), register_re_password.getText().toString())){
            showErrorMsg();
             return;}
            //input
        String entered_email = register_email.getText().toString();
        String entered_password =register_password.getText().toString();

        if(register_email.getText().toString().equals("")&& register_password.getText().toString().equals("") && register_name.getText().toString().equals("") && register_re_password.getText().toString().equals("")){
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال اسم المستخدم و البريد الالكتروني وكلمة المرور");

        }

        else if (register_email.getText().toString().equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني");

        }//end if

         else if (register_name.getText().toString().equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال اسم المستخدم");

        }//end if

        else if (register_re_password.getText().toString().equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء اعادة تعين كلمة المرور");

        }//end if

        else if (register_password.getText().toString().equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال كلمة المرور");


        }
        if(!entered_email.equals("")&& !entered_password.equals("") && !register_re_password.equals("") && !register_name.equals("")){
        mAuth.createUserWithEmailAndPassword(entered_email, entered_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignUp.this, "createUserWithEmail:success",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //send email by email to verify user account

                                createUserCollection();
                       startActivity(new Intent(SignUp.this, Login.class));


                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                              /*  Toast.makeText(SignUp.this, "Authentication failed register.",
                                        Toast.LENGTH_SHORT).show();*/
                              showDialogWithOkButton("تحقق من البيانات المدخلة");
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
    }//end of register
    }

    private void showErrorMsg() {
       // Toast.makeText(SignUp.this, "password didn't match", Toast.LENGTH_LONG).show();
        showDialogWithOkButton("كلمتا المرور غير متطابقتين");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn:
                register();
                break;
            case R.id.haveAccount:
                startActivity(new Intent(SignUp.this, Login.class));
        }
    }

    private void showDialogWithOkButton(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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


    private void createUserCollection (){

        //Token ID

                String tokenID= FirebaseInstanceId.getInstance().getToken();
                String uid= mAuth.getInstance().getUid();

                Map<String,Object> user_updateToken = new HashMap<>();
                user_updateToken.put("TokenID",tokenID);
                firebaseFirestore.collection("users").document(uid).update(user_updateToken);
// end og getting token by fatimah

        Map<String,Object> user = new HashMap<>();
        user.put("username",register_name.getText().toString());
        user.put("email",register_email.getText().toString());
        user.put("info","");
        user.put("favorite", Arrays.asList());
        user.put("subscribedUsers", Arrays.asList());
        user.put("thumbnail","https://firebasestorage.googleapis.com/v0/b/hackwati444.appspot.com/o/Hakawati%2Fdefult_thumbnail.png?alt=media&token=be4ed812-e028-493c-a703-593e4a993c1f");

        MySharedPreference.clearData(this);
        MySharedPreference.putString(this, Constants.Keys.ID, mAuth.getInstance().getUid());




        firebaseFirestore.collection("users").document(mAuth.getInstance().getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, "user added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Error_add_user", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
        
        //Token ID Method:
    }


}// end of class






