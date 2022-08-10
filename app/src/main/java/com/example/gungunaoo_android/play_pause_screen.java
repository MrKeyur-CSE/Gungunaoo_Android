package com.example.gungunaoo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class play_pause_screen extends AppCompatActivity {

    TextView song_Name, song_Desc, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pause_screen);

        song_Name = findViewById(R.id.songTitleName);
        song_Desc = findViewById(R.id.songDescription);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(play_pause_screen.this, HomeScreen.class));
            }
        });

        String Name = getIntent().getStringExtra("song_Name");
        String Description = getIntent().getStringExtra("song_Desc");

        song_Name.setText(Name);
        song_Desc.setText(Description);
    }
}