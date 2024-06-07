package com.example.roomdatabaseviewmodellivedata.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabaseviewmodellivedata.R;
import com.example.roomdatabaseviewmodellivedata.User;
import com.example.roomdatabaseviewmodellivedata.Utils.Ultils;
import com.example.roomdatabaseviewmodellivedata.sharePrefence.DataLocal2;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    List<User> userList;
    Context context;
    ItemClickListener itemClickListener;
    User user1;
    boolean ischeck;
    User s = null;
  // thay doi 1
  // thay doi 2
    public UserAdapter(List<User> userList, Context context, ItemClickListener itemClickListener) {
        this.userList = userList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onClickUpdate(int pos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_recyclerview);
        User user = userList.get(position);
        holder.tvAddress.setText(user.getAddress());
        holder.tvName.setText(user.getName());
        int pos = position;
        byte[] decode = Base64.decode(user.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        holder.imgUser.setImageBitmap(bitmap);
        holder.itemView.startAnimation(animation);


        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischeck = DataLocal2.getFavorite(pos);
                ischeck = !ischeck;
                if (ischeck == false) {
                    holder.imgFavorite.setImageResource(R.drawable.hearth);
                    if(Ultils.userFavorite.contains(userList.get(pos))){
                        Ultils.userFavorite.remove(userList.get(pos));
                    }
                } else {
                    holder.imgFavorite.setImageResource(R.drawable.hearthred);
                    Ultils.userFavorite.add(userList.get(pos));
                    Log.d("AAA","add " +  String.valueOf(Ultils.userFavorite.size()));
                }
                // Lưu trạng thái icon yêu thích
                DataLocal2.setSaveFavorite(ischeck,pos);
                // Lưu user đã tích yêu thích vào share
                DataLocal2.setObjectFavorite(userList.get(pos),pos,ischeck);
            }

        });
        ischeck = DataLocal2.getFavorite(position);
        User user1 = DataLocal2.getUser(position);
        if(ischeck == false){
            holder.imgFavorite.setImageResource(R.drawable.hearth);
            Ultils.userFavorite.remove(user1);

        }else {
            holder.imgFavorite.setImageResource(R.drawable.hearthred);
            if(user1 != null) {
                Log.d("usersave",user1.getName());
                if(!Ultils.userFavorite.contains(user1)) {
                    Ultils.userFavorite.add(user1);
                }
            }else {
                Log.d("userSave", "Null");
            }
        }

        //Cách 1; update với activity
//        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UpdateAcitivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("senduser", user);
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
        // Cách 2; update với fragment
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickUpdate(pos);
            }
        });

    }


    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser, imgFavorite;
        TextView tvName, tvAddress;
        Button btnUpdate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imageviewUser);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
        }
    }
}
