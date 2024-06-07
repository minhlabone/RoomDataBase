package com.example.roomdatabaseviewmodellivedata.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabaseviewmodellivedata.R;
import com.example.roomdatabaseviewmodellivedata.User;
import com.example.roomdatabaseviewmodellivedata.Utils.Ultils;
import com.example.roomdatabaseviewmodellivedata.fragment.FavoriteFragment;
import com.example.roomdatabaseviewmodellivedata.adapter.UserAdapter;
import com.example.roomdatabaseviewmodellivedata.ViewModel5;
import com.example.roomdatabaseviewmodellivedata.fragment.UpdateFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton btnAdd,btnFavorite;
    List<User> userList;
    UserAdapter userAdapter;
    ViewModel5 viewModel5;
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intiView();
        userList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        viewModel5 = new ViewModelProvider(this).get(ViewModel5.class);
//        viewModel5.addNewUser(new User("Điệp 3","Bắc Ninh",R.drawable.bg));
        // user swipe left to delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                handleDeleteUser(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        // Add item usser
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActitity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.insert_activity_in,R.anim.insert_activity_out);
            }
        });
        // Search User
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handlerSearchUser();
                }
                return false;
            }
        });
        viewModel5.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userList.clear();
                for (User u : users) {
                    Log.d("userAllList", u.getName());
                    userList.add(u);
                }
               userAdapter.notifyDataSetChanged();
            }
        });


        userAdapter = new UserAdapter(userList, getApplicationContext(), new UserAdapter.ItemClickListener() {
            @Override
            public void onClickUpdate(int pos) {
                UpdateFragment updateFragment = new UpdateFragment();
                User user = userList.get(pos);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sendData",user);
                updateFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.update_fragment_in,R.anim.update_fragment_out);
                transaction.replace(R.id.updateFragment,updateFragment);
                transaction.addToBackStack("fragmentUpdate");
                transaction.commit();
            }
        });
        recyclerView.setAdapter(userAdapter);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavorite.setVisibility(View.GONE);
                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    openFramgent(favoriteFragment);
            }
        });
    }
    private  void openFramgent(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.update_fragment_in,R.anim.update_fragment_out);
        transaction.replace(R.id.updateFragment,fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            btnFavorite.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();


            overridePendingTransition(R.anim.insert_activity_out,R.anim.insert_activity_in);
        }
    }

    private void handlerSearchUser() {
       String key = edtSearch.getText().toString().trim();
       userList = new ArrayList<>();
       viewModel5.getSearchUser(key).observe(this, new Observer<List<User>>() {
           @Override
           public void onChanged(List<User> users) {
               for (User u: users) {
                   Log.d("userList", u.getName());
                   userList.add(u);
               }
               userAdapter.setData(userList);
           }
       });
    }

    private void handleDeleteUser(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có muốn xóa User này khỏi danh sách không");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = userList.get(pos);
                viewModel5.deleteUser(user);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onResume();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        Log.d("LifeCycle","OnResume");
        super.onResume();
        loadData();
    }

    @Override
    protected void onRestart() {
        Log.d("LifeCycle","onRestart");
        super.onRestart();
        loadData();
    }

    private void loadData() {
        viewModel5.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userList.clear();
                for (User u : users) {
                    Log.d("userList", u.getName());
                    userList.add(u);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    private void intiView() {
        btnFavorite = findViewById(R.id.floatingactionbuttonfavorite);
        edtSearch = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.recyclerViewUser);
        btnAdd = findViewById(R.id.floatingactionbutton);
    }
}