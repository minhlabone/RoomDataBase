package com.example.roomdatabaseviewmodellivedata.fragment;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.roomdatabaseviewmodellivedata.R;
import com.example.roomdatabaseviewmodellivedata.User;
import com.example.roomdatabaseviewmodellivedata.Utils.Ultils;
import com.example.roomdatabaseviewmodellivedata.adapter.FavoriteAdapter;
import java.util.List;


public class FavoriteFragment extends Fragment implements View.OnTouchListener {
    private float startTouchY;
    private float startY;
    private long startTime;
    private  View view;
    RecyclerView recyclerView;
    List<User> userList ;
    ;
    FavoriteAdapter favoriteAdapter;
    private static final float SWIPE_SPEED_THRESHOLD = 1.0f;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        view.setOnTouchListener(this);
        initView();
        Log.d("fragment","OnCreate");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        userList = Ultils.userFavorite;
            // e get nó ra đây
        favoriteAdapter = new FavoriteAdapter(userList, getContext());
        recyclerView.setAdapter(favoriteAdapter);
        return  view;
        // lúc e bỏ chọn ở main thì phải thoát ra vào lại mới cập nhật fravority còn nó ko cập nhật ngay ấy a
    }

    @Override
    public void onResume() {
        Log.d("fragment","OnResume");
        super.onResume();
//        userList.clear();
//        favoriteAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        Log.d("fragment","onDestroy");
        super.onDestroy();
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerViewFavorite);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
  // Hàm vuốt lên vuốt xuống fragment
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