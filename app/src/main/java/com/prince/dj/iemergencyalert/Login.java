package com.prince.dj.iemergencyalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    private static final String TAG = "login";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference mref;

    private EditText mEmailView,mPasswordView;
    private ProgressBar progressBar;
    private Button signinBtn;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the login form.
        mEmailView = findViewById(R.id.myemail);
        mPasswordView = findViewById(R.id.mypassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        signinBtn = findViewById(R.id.signinBtn);

        setupFirebaseAuth();
        hideSoftKeyboard();

           }




    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        //check if the fields are filled out
        if(isEmpty(email)
                && isEmpty(password)){
            Log.d(TAG, "onClick: attempting to authenticate.");

            showProgressDialog();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,
                    password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            hideProgressDialog();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            });
        }else{
            Toast.makeText(Login.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(String string){
        return !string.equals("");
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private boolean validateForm() {
        String email = mEmailView.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email can't be empty.");
            mEmailView.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailView.getText().toString().trim()).matches()) {
            mEmailView.setError("Enter correct email.");
            mEmailView.requestFocus();
            return false;
        } else {
            mEmailView.setError(null);
        }


        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password can't be empty.");
            mPasswordView.requestFocus();
            return false;
        } else {
            mPasswordView.setError(null);
        }
        return true;
    }



    public void showProgressDialog() {
        if (progressBar.getVisibility()==View.GONE){
            progressBar.setVisibility(View.VISIBLE);
            signinBtn.setEnabled(false);
        }
    }

    public void hideProgressDialog() {
        if (progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            signinBtn.setEnabled(true);

        }
    }

    public void GoToSignup(View view) {
        startActivity(new Intent(Login.this,SignUp.class));
    }


    public void Signinmeton(View view) {
        if (Utils.haveNetworkConnection(this))
            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
        else{
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //check if email is verified
                   if(user.isEmailVerified()){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // Toast.makeText(Login.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();


                       if (user.getEmail().contains("princedank")){
                           Intent intent = new Intent(Login.this, Reports.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();
                       }else {
                           Intent intent = new Intent(Login.this, MainPage.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();
                       }
                    }
                    else{
                        Toast.makeText(Login.this, "Email is not Verified \nCheck your Inbox", Toast.LENGTH_SHORT).show();
                       sendVerificationEmail();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    public void resetPass(View view) {
        if (emailChk())
            sendPasswordResetEmail(mEmailView.getText().toString().trim());
    }

    private boolean emailChk() {
        String email = mEmailView.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email can't be empty.");
            mEmailView.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailView.getText().toString().trim()).matches()) {
            mEmailView.setError("Enter correct email.");
            mEmailView.requestFocus();
            return false;
        } else {
            mEmailView.setError(null);
        }
        return true;
    }


    public void sendPasswordResetEmail(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Password Reset Email sent.");
//                            Toast.makeText(mContext, "Password Reset Link Sent to Email",
//                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Password Reset Link Sent to Email", Toast.LENGTH_SHORT).show();

                        }else{
                            Log.d(TAG, "onComplete: No user associated with that email.");
//                            Toast.makeText(mContext, "No User is Associated with that Email",
//                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "No User is Associated with that Email", Toast.LENGTH_SHORT).show();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error Occurred Try Again!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            Toast.makeText(Login.this, "Verification Email sent", Toast.LENGTH_SHORT).show();
//
//                            // after email is sent just logout the user and finish this activity
//                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}
