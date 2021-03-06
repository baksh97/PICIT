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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main_rv_adapter_inactive extends RecyclerView.Adapter<main_rv_adapter_inactive.ViewHolder>{

    private static final String TAG = "main_rv_adapter";
    private ArrayList<String> chatNames;
    private ArrayList<Bitmap> chatImages;
    ArrayList<Integer> chatIds;
    private Context mContext;


    public main_rv_adapter_inactive(ArrayList<String> items,ArrayList<Integer> chatIds,ArrayList<Bitmap> chatImages, Context mContext) {
        this.chatImages = chatImages;
        this.chatIds = chatIds;
        this.chatNames = items;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public main_rv_adapter_inactive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_group_main,parent,false);
        main_rv_adapter_inactive.ViewHolder holder = new main_rv_adapter_inactive.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull main_rv_adapter_inactive.ViewHolder holder, int position) {
        holder.chatName_tv.setText(chatNames.get(position));
        holder.chatImage_tv.setText(String.valueOf(chatNames.get(position).charAt(0)).toUpperCase());
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));
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
            chatImage_tv.setBackgroundResource(main_rv_adapter_active.backGrounds[rand]);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
//            Chat.currentChatName = chatNames.get(position) + chatIds.get(position);
            Intent intent = new Intent(mContext, Chat.class);
            intent.putExtra("chatName",chatNames.get(position));
            intent.putExtra("chatID",chatIds.get(position));
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupGroup = new PopupMenu(mContext, v);
            popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.chat_activate:
//                            Toast.makeText(mContext, "profle update 1",Toast.LENGTH_SHORT).show();
//                            MainActivity m = new MainActivity();
                            MainActivity.inactive2active(getAdapterPosition(),mContext);
                            break;

                    }
                    return false;
                }
            });
            popupGroup.inflate(R.menu.grp_action_inactive);
            popupGroup.show();
            return false;
        }
    }
}
