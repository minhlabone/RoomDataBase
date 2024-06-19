package com.example.roomdatabaseviewmodellivedata.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.roomdatabaseviewmodellivedata.R;
import com.example.roomdatabaseviewmodellivedata.model.User;
import com.example.roomdatabaseviewmodellivedata.viewmodel.ViewModel5;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class UpdateFragment extends Fragment implements View.OnTouchListener {
   private float startTouchY;
    private float startY;
    private long startTime;
    User user;
    EditText edtname,edtaddress;
    Button btnUpdate;
    ImageView imgFolder,imgCamera,imgUser;
    String path;
    ViewModel5 viewModel5;
    ProgressBar progressBar;
    private static final int REQUEST_FOLDER = 456;

    private static final float SWIPE_SPEED_THRESHOLD = 1.0f;
   View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //lan sau ko code vào hàn mày
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_update,container,false);
        view.setOnTouchListener(this);
        viewModel5 = new ViewModelProvider(this).get(ViewModel5.class);
        initView();
        user = (User) getArguments().get("sendData");
        edtname.setText(user.getName());
        edtaddress.setText(user.getAddress());
        byte[] decode = Base64.decode(user.getImage(),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
        imgUser.setImageBitmap(bitmap);
        handlerSetImage();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString();
                String address = edtaddress.getText().toString();
                if(name.isEmpty() || address.isEmpty()){
                    Toast.makeText(getContext(), "Bạn Không Được Để Trống", Toast.LENGTH_SHORT).show();
                }
                btnUpdate.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(path == null){
                            user.setName(name);
                            user.setAddress(address);
                            user.setImage(user.getImage());
                        }else {
                            user.setName(name);
                            user.setAddress(address);
                            user.setImage(path);
                        }
                        viewModel5.updateUser(user);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                }).start();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //viết nó vaào đây
        // màn này là màn updateuser a ạ uhh


    }

    private void handlerSetImage() {
        imgFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_FOLDER);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri);
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

    private void initView() {
        progressBar = view.findViewById(R.id.progressBar);
        edtaddress = view.findViewById(R.id.edt_addressupdatefm);
        edtname = view.findViewById(R.id.edt_usernameupdatefm);
        btnUpdate = view.findViewById(R.id.btn_update_userfm);
        imgCamera = view.findViewById(R.id.image_camera_updatefm);
        imgFolder = view.findViewById(R.id.image_folder_updatefm);
        imgUser = view.findViewById(R.id.img_user_updatefm);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchY = event.getY();
                startY = v.getY();
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentTouchY = event.getY();
                float swipeDistance = currentTouchY - startTouchY;
                long timeDelta = System.currentTimeMillis() - startTime;
                float swipeSpeed = Math.abs(swipeDistance) / timeDelta;

                // Di chuyển view theo cử chỉ vuốt
                v.setTranslationY(startY + swipeDistance);
                break;
            case MotionEvent.ACTION_UP:
                float endTouchY = event.getY();
                swipeDistance = endTouchY - startTouchY;
                timeDelta = System.currentTimeMillis() - startTime;
                swipeSpeed = Math.abs(swipeDistance) / timeDelta;

                if (swipeSpeed > SWIPE_SPEED_THRESHOLD) {
                    // Vuốt nhanh - đóng fragment
                    getActivity().onBackPressed();
                } else {
                    // Kiểm tra nếu người dùng dừng vuốt và fragment chưa trở lại vị trí ban đầu
                    if (Math.abs(swipeDistance) > v.getHeight() / 4) {
                        // Hoạt hình để đưa fragment trở lại vị trí ban đầu nếu nó đã được vuốt ít hơn 1/4 chiều cao của nó
                        v.animate().translationY(startY).setDuration(300).start();
                    }
                }
                break;
        }
        return true;
    }

}