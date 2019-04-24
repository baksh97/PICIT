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

import java.util.ArrayList;



public class album_rv_adapter extends RecyclerView.Adapter<album_rv_adapter.ViewHolder>{


    private static final String TAG = "main_rv_adapter";
    //    private ArrayList<String> chatNames;
    private ArrayList<Bitmap> galleryImages;
    private Context mContext;


    public album_rv_adapter(ArrayList<Bitmap> galleryImages, Context mContext) {
        this.galleryImages = galleryImages;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public album_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_gallery,parent,false);
        album_rv_adapter.ViewHolder holder = new album_rv_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull album_rv_adapter.ViewHolder holder, int position) {
//        holder.chatName_tv.setText(chatNames.get(position));
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));

//        holder.
    }

    @Override
    public int getItemCount() {

        return galleryImages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ConstraintLayout completeLayout;
        ImageView galleryImage_iv_1,galleryImage_iv_2;

        public ViewHolder(View itemView) {
            super(itemView);

            galleryImage_iv_1 = (ImageView) itemView.findViewById(R.id.imageView_row_gallery_1);
            galleryImage_iv_2 = (ImageView) itemView.findViewById(R.id.imageView_row_gallery_1);

            galleryImage_iv_1.setOnClickListener(this);
            galleryImage_iv_1.setOnLongClickListener(this);

            galleryImage_iv_2.setOnClickListener(this);
            galleryImage_iv_2.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(mContext, fullImage.class);
            intent.putExtra("image",position);
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupGroup = new PopupMenu(mContext, v);
            popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.add_filters:
                            mContext.startActivity(new Intent(mContext,Filters.class));
                            break;
                    }
                    return false;
                }
            });
            popupGroup.inflate(R.menu.gallery_action);
            popupGroup.show();
            return false;
        }
    }

}
