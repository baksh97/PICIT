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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class galleryAlbums_rv_adapter extends RecyclerView.Adapter<galleryAlbums_rv_adapter.ViewHolder>{

    private static final String TAG = "main_rv_adapter";
//    private ArrayList<String> chatNames;
    private ArrayList<File> galleryAlbums;
    private Context mContext;


    public galleryAlbums_rv_adapter(ArrayList<File> galleryAlbums, Context mContext) {
        this.galleryAlbums = galleryAlbums;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public galleryAlbums_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_gallery,parent,false);
        galleryAlbums_rv_adapter.ViewHolder holder = new galleryAlbums_rv_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull galleryAlbums_rv_adapter.ViewHolder holder, int position1) {
//        holder.chatName_tv.setText(chatNames.get(position));
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));

//        Toast.makeText(mContext, "position1: "+position1+" and galleryImages: "+galleryAlbums.size(), Toast.LENGTH_SHORT).show();

        int position = 3*position1;

        holder.galleryImage_iv_1.setEnabled(true);
        holder.galleryImage_iv_2.setEnabled(true);
        holder.galleryImage_iv_3.setEnabled(true);

        if(galleryAlbums.size()>position){
            holder.galleryImage_iv_1.setImageResource(R.drawable.ic_folder_black_24dp);
            holder.galleryImage_tv_1.setText(galleryAlbums.get(position).getName().substring(6).split("\t")[0]);
//            holder.galleryImage_iv_1.setBackground(functions.bitmap2Drawable(galleryAlbums.get(position),mContext));
//            holder.galleryImage_iv_1.setImageBitmap(galleryAlbums.get(position));
//            holder.galleryImage_iv_1.setImageBitmap(galleryAlbums.get(position));
            if(galleryAlbums.size()>position+1){
//                holder.galleryImage_iv_2.setImageBitmap(galleryAlbums.get(position+1));
//                holder.galleryImage_iv_2.setBackground(functions.bitmap2Drawable(galleryAlbums.get(position+1),mContext));
                holder.galleryImage_iv_2.setImageResource(R.drawable.ic_folder_black_24dp);
                holder.galleryImage_tv_2.setText(galleryAlbums.get(position+1).getName().substring(6).split("\t")[0]);
                if(galleryAlbums.size()>position+2){
//                    holder.galleryImage_iv_3.setImageBitmap(galleryAlbums.get(position+2));
//                    Drawable d = functions.bitmap2Drawable(galleryAlbums.get(position+2),mContext);
//                    holder.galleryImage_iv_3.setBackground(d);
//                    holder.galleryImage_iv_3.setBackground(functions.bitmap2Drawable(galleryAlbums.get(position+2),mContext));
                    holder.galleryImage_iv_3.setImageResource(R.drawable.ic_folder_black_24dp);
                    holder.galleryImage_tv_3.setText(galleryAlbums.get(position+2).getName().substring(6).split("\t")[0]);
                }
                else{
                    holder.galleryImage_iv_3.setEnabled(false);

                    holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);
                }
            }
            else{
                holder.galleryImage_iv_2.setEnabled(false);
                holder.galleryImage_iv_3.setEnabled(false);

                holder.galleryImage_tv_2.setVisibility(View.INVISIBLE);
                holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);
            }
        }
        else{
            holder.galleryImage_iv_1.setEnabled(false);
            holder.galleryImage_iv_2.setEnabled(false);
            holder.galleryImage_iv_3.setEnabled(false);

            holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);
            holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);
            holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);


        }


    }

    @Override
    public int getItemCount() {

        int s = galleryAlbums.size();
        int x = s/3;
        if(s%3>0)x++;
        return x;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ConstraintLayout completeLayout;
        ImageView galleryImage_iv_1,galleryImage_iv_2,galleryImage_iv_3;
        TextView galleryImage_tv_1,galleryImage_tv_2,galleryImage_tv_3;

        public ViewHolder(View itemView) {
            super(itemView);

            galleryImage_iv_1 = (ImageView) itemView.findViewById(R.id.imageView_row_gallery_1);
            galleryImage_iv_2 = (ImageView) itemView.findViewById(R.id.imageView_row_gallery_2);
            galleryImage_iv_3 = (ImageView) itemView.findViewById(R.id.imageView_row_gallery_3);

            galleryImage_tv_1 = (TextView) itemView.findViewById(R.id.textView_row_gallery_1);
            galleryImage_tv_2 = (TextView) itemView.findViewById(R.id.textView_row_gallery_2);
            galleryImage_tv_3 = (TextView) itemView.findViewById(R.id.textView_row_gallery_3);

            galleryImage_iv_1.setOnClickListener(this);
//            galleryImage_iv_1.setOnLongClickListener(this);

            galleryImage_iv_2.setOnClickListener(this);
//            galleryImage_iv_2.setOnLongClickListener(this);

            galleryImage_iv_3.setOnClickListener(this);
//            galleryImage_iv_3.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = 3*getAdapterPosition();
//            Intent intent = new Intent(mContext, fullImage.class);
//            intent.putExtra("")
            switch (v.getId()){
                case R.id.imageView_row_gallery_1:{
//                    position
                    break;
                }
                case R.id.imageView_row_gallery_2:{
                    position += 1;
                    break;
                }
                case R.id.imageView_row_gallery_3:{
                    position += 2;
                    break;
                }

            }

            Intent intent = new Intent(mContext,showAlbum.class);
//            intent.p
//            intent.put
            intent.putExtra("file",galleryAlbums.get(position));
            mContext.startActivity(intent);

//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            galleryAlbums.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
////            Intent in1 = new Intent(this, Activity2.class);
////            in1.putExtra("image",byteArray);
//            intent.putExtra("image",byteArray);
//            mContext.startActivity(intent);

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
