package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main_rv_adapter_active extends RecyclerView.Adapter<main_rv_adapter_active.ViewHolder>{


    private static final String TAG = "main_rv_adapter";
    private ArrayList<String> chatNames;
    private ArrayList<Bitmap> chatImages;
    private ArrayList<Integer> chatIds;
    private Context mContext;

    static int[] backGrounds = {R.drawable.main_circle_grp_blue_dark,R.drawable.main_circle_grp_green_dark,R.drawable.main_circle_grp_orange_dark,R.drawable.main_circle_grp_blue_light,R.drawable.main_circle_grp_green_light,R.drawable.main_circle_grp_orange_light};

    public main_rv_adapter_active(ArrayList<String> items,ArrayList<Integer> chatIds,ArrayList<Bitmap> chatImages, Context mContext) {
        this.chatImages = chatImages;
        this.chatIds = chatIds;
        this.chatNames = items;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public main_rv_adapter_active.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_group_main,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull main_rv_adapter_active.ViewHolder holder, int position) {
        holder.chatName_tv.setText(chatNames.get(position));
        holder.chatImage_tv.setText(String.valueOf(chatNames.get(position).charAt(0)).toUpperCase());
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));

//        holder.
    }

    @Override
    public int getItemCount() {

        return chatNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ConstraintLayout completeLayout;
        TextView chatName_tv;
        TextView chatImage_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            chatImage_tv = (TextView) itemView.findViewById(R.id.chat_image_tv);
            chatName_tv = (TextView) itemView.findViewById(R.id.chat_name_tv);

            int rand = new Random().nextInt(6);
            chatImage_tv.setBackgroundResource(backGrounds[rand]);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
//            Chat.currentChatName = chatNames.get(position)+chatIds.get(position);
            Intent intent = new Intent(mContext, Chat.class);
            intent.putExtra("chatName",chatNames.get(position));
            intent.putExtra("chatID",chatIds.get(position));
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupGroup = new PopupMenu(mContext, v);
//                MenuInflater inflater = popupGroup.getMenuInflater();
//                inflater.inflate(R.menu.grp_action, popupGroup.getMenu());
            popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.chat_inactivate:
//                            Toast.makeText(mContext, "profle update 1",Toast.LENGTH_SHORT).show();
                            MainActivity.active2inactive(getAdapterPosition(),mContext);
                            break;

//                        case R.id.profile_update2:
//                            Toast.makeText(mContext, "profle update 2",Toast.LENGTH_SHORT).show();
//                            break;
//
//                        case R.id.profile_update3:
//                            Toast.makeText(mContext, "profle update 3",Toast.LENGTH_SHORT).show();
//                            break;
//
//                        case R.id.profile_update4:
//                            Toast.makeText(mContext, "profle update 4",Toast.LENGTH_SHORT).show();
//                            break;
                    }
                    return false;
                }
            });
            popupGroup.inflate(R.menu.grp_action);
            popupGroup.show();
            return false;
        }
    }
}
