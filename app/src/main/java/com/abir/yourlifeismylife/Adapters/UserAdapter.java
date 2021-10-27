package com.abir.yourlifeismylife.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Home.Home;
import com.abir.yourlifeismylife.Utils.Common;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.myUserViewHolder> {

    Context context;
    ArrayList<UserDataModel> usersList;

    public UserAdapter(Context context, ArrayList<UserDataModel> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public myUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        myUserViewHolder viewHolder = new myUserViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myUserViewHolder holder, int position) {
        UserDataModel user = usersList.get(position);
        String image = user.getProfileImage();

        try {
            if (image.equals("none") || image.equals("") || image == null)
                Glide.with(context).load(R.drawable.user).into(holder.userImage);
            else
                Glide.with(context).load(image).into(holder.userImage);

        } catch (Exception e) {

        }

        holder.userName.setText(user.getFirstName() + user.getLastName());
        holder.dopamineLevel.setText("Dopamine Level: " + generateDopamine());
        holder.serotoninLevel.setText("Serotonin Level: " + generateSerotonin());

        holder.mUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.trackingUser = user;
                context.startActivity(new Intent(context, Home.class));
                ((Activity) context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    private String generateDopamine() {
        Random r = new Random();
        int n = 1 + r.nextInt(30);
        return String.valueOf(n);
    }

    private String generateSerotonin() {
        Random r = new Random();
        int n = 101 + r.nextInt(283);
        return String.valueOf(n);
    }

    public class myUserViewHolder extends RecyclerView.ViewHolder {

        CardView mUserView;
        CircleImageView userImage;
        TextView userName, dopamineLevel, serotoninLevel;


        public myUserViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserView = itemView.findViewById(R.id.user_item_view);
            userImage = itemView.findViewById(R.id.image_user_item);
            userName = itemView.findViewById(R.id.name_user_item);
            dopamineLevel = itemView.findViewById(R.id.dopamine_user_item);
            serotoninLevel = itemView.findViewById(R.id.serotonin_user_item);


        }
    }
}
