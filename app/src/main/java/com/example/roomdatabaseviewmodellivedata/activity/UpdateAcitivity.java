package com.example.roomdatabaseviewmodellivedata.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomdatabaseviewmodellivedata.R;
import com.example.roomdatabaseviewmodellivedata.User;
import com.example.roomdatabaseviewmodellivedata.ViewModel5;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateAcitivity extends AppCompatActivity {
    EditText edtname,edtaddress;
    Button btnUpdate;
    ImageView imguser,imgfolder,imgcamera;
    User user;
    ViewModel5 viewModel5;
    String path;
    private static final int REQUEST_FOLDER = 345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_acitivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel5 = new ViewModelProvider(this).get(ViewModel5.class);
        initView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            user = (User) bundle.get("senduser");
            edtname.setText(user.getName());
            edtaddress.setText(user.getAddress());
            byte[] decode = Base64.decode(user.getImage(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
            imguser.setImageBitmap(bitmap);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hanlderUpdateUser();
                }
            });
        }else {
            Toast.makeText(this, "Bundle không tồn tại", Toast.LENGTH_SHORT).show();
        }
          imgfolder.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_PICK);
                  intent.setType("image/*");
                  startActivityForResult(intent,REQUEST_FOLDER);
              }
          });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imguser.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[]  bytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    path = Base64.encodeToString(bytes, Base64.DEFAULT);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void hanlderUpdateUser() {
         Intent intent = new Intent(this, MainActivity.class);
         String name = edtname.getText().toString();
         String address = edtaddress.getText().toString();
         user.setAddress(address);
         user.setName(name);
         if(path == null){
             user.setImage(user.getImage());
         }else {
             user.setImage(path);
         }
         viewModel5.updateUser(user);
           startActivity(intent);
    }

    private void initView() {
        edtaddress = findViewById(R.id.edt_addressupdate);
        edtname = findViewById(R.id.edt_usernameupdate);
        btnUpdate = findViewById(R.id.btn_update_user);
        imguser = findViewById(R.id.img_user_update);
        imgfolder = findViewById(R.id.image_folder_update);
        imgcamera = findViewById(R.id.image_camera_update);
    }
}