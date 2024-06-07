package com.example.roomdatabaseviewmodellivedata.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class AddActitity extends AppCompatActivity {
    ImageView imgfolder, imgcamera, imgUser;
    EditText edtname, edtaddress;
    private static final int REQUEST_CAMERA = 123;
    private static final int REQUEST_FOLDER = 234;

    Button btnAdd;
    ViewModel5 viewModel5;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_actitity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel5 = new ViewModelProvider(this).get(ViewModel5.class);
        initView();
        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
        imgfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_FOLDER);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString();
                String address = edtaddress.getText().toString();
                if (path != null) {
                    User user = new User(name, address, path);
                    viewModel5.addNewUser(user);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(AddActitity.this, "Mời bạn nhập vào ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        imgcamera = findViewById(R.id.image_camera);
        imgfolder = findViewById(R.id.image_folder);
        imgUser = findViewById(R.id.img_user);
        btnAdd = findViewById(R.id.btn_add_user);
        edtname = findViewById(R.id.edt_username);
        edtaddress = findViewById(R.id.edt_address);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (imgUser != null) {
                imgUser.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Không thể hiển thị ảnh", Toast.LENGTH_SHORT).show();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                path = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        }
        if (requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgUser.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    path = Base64.encodeToString(bytes, Base64.DEFAULT);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}