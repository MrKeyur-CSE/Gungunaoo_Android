package com.example.gungunaoo_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

//  Initialization
    TextView notRegister_btn;
    Button login_btn;
    EditText email_etxt, pass_etxt;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Data Retrive from id
        notRegister_btn = findViewById(R.id.notRegister_btn);
        login_btn = findViewById(R.id.login_btn);
        email_etxt = findViewById(R.id.email_etxt);
        pass_etxt = findViewById(R.id.pass_etxt);

        fAuth = FirebaseAuth.getInstance();

//      Checking CurrentUser is logined In or Not
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeScreen.class));
            finish();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//              Store into string
                String email = email_etxt.getText().toString().trim();
                String pass = pass_etxt.getText().toString().trim();

//              Condition for required feild
                if (TextUtils.isEmpty(email)) {
                    email_etxt.setError("Email Address is Required.");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    pass_etxt.setError("Password is Required.");
                    return;
                }

                if (pass.length() < 8) {
                    pass_etxt.setError("Password must be >= 8 Characters.");
                    return;
                }

                if (email == "admin@gungunao.com" && pass == "admin#123") {
                    startActivity(new Intent(MainActivity.this, AdminHomeScreen.class));
                } else {
//                  Authenticate the User
                    fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Logged in Sucessfully.", Toast.LENGTH_LONG);
                                startActivity(new Intent(MainActivity.this, HomeScreen.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

        notRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationScreen.class));
            }
        });
    }
}