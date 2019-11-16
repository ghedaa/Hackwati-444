package sa.ksu.swe444.hackwati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sa.ksu.swe444.hackwati.Constants.Keys.USER_EMAIL;
import static sa.ksu.swe444.hackwati.Constants.Keys.USER_PASS;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText login_password;
    private EditText login_email;
    private String entered_password;
    private String entered_email;
    private Button loginBtn;
    private SignInButton loginGoogleBtn;
    private TextView createAccount;
    private TextView forgetPassword;
    private ProgressBar progressBar;
    private GoogleSignInOptions gso;
    private EditText forget_email;
    public String userID;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean verify = false;
    private MySharedPreference pref1;

    // ...
    private final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // save the user loggong by fatimah

        //end by fatimah
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        init();

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userID = mFirebaseUser.getUid();
        }


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (verify) {

                        startActivity(new Intent(Login.this, MainActivity.class));

                    }

                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loginBtn.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        loginGoogleBtn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        progressBar.setVisibility(View.GONE);
       // loginGoogleBtn.setEnabled(false);

    }// end of onCreate()

    private void init() {

        //init
        login_email = findViewById(R.id.email_login);
        login_password = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.loginbutton_login);
        loginGoogleBtn = findViewById(R.id.regBtnGoogle);
        createAccount = findViewById(R.id.createAccount);
        forgetPassword = findViewById(R.id.forgetText);
        progressBar = findViewById(R.id.progress_bar);
        // Initialize Firebase Auth
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
              // String name = task.getResult().getDisplayName();
              // String email = task.getResult().getEmail();
            }
        });

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }//end of init

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.loginbutton_login:


                signIn();
                // save user in myshared by fatimah

                // ended by fatimah
                break;
            case R.id.regBtnGoogle:
                Log.e("TAG", "google clicked");
                googleSignIn();
                break;
            case R.id.createAccount:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.forgetText:
                showDialoge();
                break;
        }//end of switch


    }//end of onClick

    private void showDialoge() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        forget_email = dialogView.findViewById(R.id.edt_comment);
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword(forget_email.getText().toString());
                Log.d(TAG, "signInWithEmail:" + forgetPassword.getText().toString());

                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void forgetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "password is sent",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "password is NOT sent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void googleSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        //Token ID
        mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String tokenID= getTokenResult.getToken();
                String uid= mAuth.getInstance().getUid();

                Map<String,Object> user_updateToken = new HashMap<>();
                user_updateToken.put("TokenID",tokenID);
                firebaseFirestore.collection("users").document(uid).update(user_updateToken)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"token is here",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });// end og getting token by fatimah
        createUserCollection();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }// end of googleSignIn

    private void createUserCollection() {

        Map<String,Object> user = new HashMap<>();
        //user.put("username",register_name.getText().toString());
        //user.put("email",register_email.getText().toString());
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
                        Toast.makeText(Login.this, "user added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error_add_user", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });

        //Token ID Method:
    }

    private void signIn() {
        //input

        entered_email = login_email.getText().toString();
        entered_password = login_password.getText().toString();


        if (entered_password.equals("") && entered_email.equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني وكلمة المرور");

        } else if (entered_email.equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني");

        }//end if

        else if (entered_password.equals("")) {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال كلمة المرور");


        }

        if (!entered_email.equals("") && !entered_password.equals("")) {
            mAuth.signInWithEmailAndPassword(entered_email, entered_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");


                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    //Token ID
                                    mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                        @Override
                                        public void onSuccess(GetTokenResult getTokenResult) {
                                            String tokenID= getTokenResult.getToken();
                                            String uid= mAuth.getInstance().getUid();

                                            Map<String,Object> user_updateToken = new HashMap<>();
                                            user_updateToken.put("TokenID",tokenID);
                                            firebaseFirestore.collection("users").document(uid).update(user_updateToken)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG,"token is here");
                                                        }
                                                    });
                                        }
                                    });// end og getting token by fatimah

                                    startActivity(new Intent(Login.this, MainActivity.class));
                                } else {
                                    showDialogWithOkButton("تحقق من الرابط المرسل على بريدك لإكمال عملية تسجيل الدخول ");
                                }



                                if (mAuth.getCurrentUser().getUid().equalsIgnoreCase("DUbp3gH497gydI7fJodUfRz9A2K3")
                                        || mAuth.getCurrentUser().getUid() == "DUbp3gH497gydI7fJodUfRz9A2K3") {
                                    Log.d(TAG, "admin"+userID);
                                    startActivity(new Intent(Login.this, AdminActivity.class));
                                }
                                else {
                                    //Token ID

                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {
                                            String newToken = instanceIdResult.getToken();
                                            Map<String,Object> user_updateToken = new HashMap<>();
                                            user_updateToken.put("TokenID",newToken);
                                            firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid()).update(user_updateToken);

                                        }
                                    });
                                    // end og getting token by fatimah

                                    startActivity(new Intent(Login.this, MainActivity.class));
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                               Toast.makeText(Login.this, "Authentication failed google.",
                                      Toast.LENGTH_SHORT).show();
                                showDialogWithOkButton("البريد الإلكتروني أو كلمة المرور غير صحيحة");
                                // updateUI(null);
                            }

                            // ...
                        }
                    });

        }

    }//end of signIn

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(Login.this, MainActivity.class);
        }
        //updateUI(currentUser);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        mAuth.addAuthStateListener(authStateListener);

    }//end of on start


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(Login.this, MainActivity.class));
                            } else {
                               /* Toast.makeText(Login.this, "email not verified",
                                        Toast.LENGTH_SHORT).show();*/
                                showDialogWithOkButton("البريد الإلكتروني غير صالح");
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                            showDialogWithOkButton("الرجاء ادخال المعلومات");
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    } // [END auth_with_google]

    private void showDialogWithOkButton(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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


}// end of class
