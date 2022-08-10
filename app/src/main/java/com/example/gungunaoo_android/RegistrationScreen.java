package com.example.gungunaoo_android;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.usage.StorageStats;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RegistrationScreen extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 107;
    public static final int CAMERA_REQUEST_CODE = 103;
    //  Initialization
    TextView alreadyRegister_btn;
    Button register_btn;
    EditText firstName_etxt, lastName_etxt, emailAddress_etxt, passWord_etxt;
    ImageView profile_eImg;
    String userID;
    Bitmap image;
    Uri imageUri;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    StorageReference storageProfileImgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

//      Data Retrive from id
        alreadyRegister_btn = findViewById(R.id.alreadyRegister_btn);
        register_btn = findViewById(R.id.register_btn);
        profile_eImg = findViewById(R.id.profile_eImg);
        firstName_etxt = findViewById(R.id.firstName_etxt);
        lastName_etxt = findViewById(R.id.lastName_etxt);
        emailAddress_etxt = findViewById(R.id.emailAddress_etxt);
        passWord_etxt = findViewById(R.id.passWord_etxt);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        storageProfileImgRef = fStorage.getReference();

//      Checking CurrentUser is logined In or Not
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationScreen.this, HomeScreen.class));
            finish();
        }

        profile_eImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//              Store into string
                String emailAddress = emailAddress_etxt.getText().toString().trim();
                String passWord = passWord_etxt.getText().toString().trim();
                String firstName = firstName_etxt.getText().toString().trim();
                String lastName = lastName_etxt.getText().toString().trim();

//              Condition for required feild
                if (TextUtils.isEmpty(emailAddress)) {
                    emailAddress_etxt.setError("Email Address is Required.");
                    return;
                }

                if (TextUtils.isEmpty(passWord)) {
                    passWord_etxt.setError("Password is Required.");
                    return;
                }

                if (passWord.length() < 8) {
                    passWord_etxt.setError("Password must be >= 8 Characters.");
                    return;
                }

//              Register the User into Firestore
                fAuth.createUserWithEmailAndPassword(emailAddress, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationScreen.this, "User is Registered Sucessfully.", Toast.LENGTH_LONG);

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("userInfo").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("emailAddress", emailAddress);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: User Profile is Created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.toString());
                                }
                            });

                            startActivity(new Intent(RegistrationScreen.this, HomeScreen.class));
                        } else {
                            Toast.makeText(RegistrationScreen.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });

        alreadyRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationScreen.this, MainActivity.class));
            }
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }else {
                Toast.makeText(this, "Need Camera Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            image = (Bitmap) data.getExtras().get("data");
//            profile_eImg.setImageBitmap(image);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), image, "val", null);
            imageUri = Uri.parse(path);

            profile_eImg.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageProfileImgRef.child("images/"+randomKey);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload Fail.", Toast.LENGTH_LONG ).show();
                    }
                });
    }
}