package com.example.gungunaoo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegistrationScreen extends AppCompatActivity {

    TextView alreadyRegister_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        alreadyRegister_btn = (TextView) findViewById(R.id.alreadyRegister_btn);

        alreadyRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationScreen.this, MainActivity.class));
            }
        });
    }
}