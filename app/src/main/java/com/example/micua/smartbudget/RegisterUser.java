package com.example.micua.smartbudget;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity {

    private static final String TAG = "123";
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        getSupportActionBar().hide();
            Button register = (Button) findViewById(R.id.btn_register_activity);
            final EditText email = (EditText) findViewById(R.id.et_email_reg_activity);
            final EditText password = (EditText) findViewById(R.id.et_password_reg_activity);

            auth = FirebaseAuth.getInstance();

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int validation = 0;
                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    if (passValidation(pass))
                        validation += 1;
                    else
                        Toast.makeText(RegisterUser.this, "Password should contain at least one uppercase, number and a special character", Toast.LENGTH_SHORT).show();
                    if (emailValidation(mail))
                        validation += 1;
                    else
                        Toast.makeText(RegisterUser.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                    if (validation == 2)
                        signUp(mail, pass);
                }
            });

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String name = user.getDisplayName();
                String e_mail = user.getEmail();
                Uri photoUrl = user.getPhotoUrl();

                String uid = user.getUid();
                System.out.println(name + " " + e_mail + " " + uid);
            }


        }

        @Override
        protected void onStart() {
            super.onStart();
            auth.addAuthStateListener(authStateListener);
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (authStateListener != null)
                auth.removeAuthStateListener(authStateListener);
        }

    private void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (! task.isSuccessful()) {
                            Toast.makeText(RegisterUser.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean emailValidation(String email) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();

    }

    private boolean passValidation (String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }
}
