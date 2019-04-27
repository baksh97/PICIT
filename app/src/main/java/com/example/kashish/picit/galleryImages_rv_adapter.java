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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class galleryImages_rv_adapter extends RecyclerView.Adapter<galleryImages_rv_adapter.ViewHolder>{

    private static final String TAG = "main_rv_adapter";
//    private ArrayList<String> chatNames;
    private ArrayList<Bitmap> galleryImages;
    private ArrayList<String> imageNames;
    private Context mContext;


    public galleryImages_rv_adapter(ArrayList<String> imageNames,ArrayList<Bitmap> galleryImages, Context mContext) {
        this.imageNames = imageNames;
        this.galleryImages = galleryImages;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public galleryImages_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_gallery,parent,false);
        galleryImages_rv_adapter.ViewHolder holder = new galleryImages_rv_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull galleryImages_rv_adapter.ViewHolder holder, int position1) {
//        holder.chatName_tv.setText(chatNames.get(position));
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));

        int position = 3*position1;

        holder.galleryImage_iv_1.setEnabled(true);
        holder.galleryImage_iv_2.setEnabled(true);
        holder.galleryImage_iv_3.setEnabled(true);

        if(galleryImages.size()>position){
            holder.galleryImage_iv_1.setBackground(functions.bitmap2Drawable(galleryImages.get(position),mContext));
            holder.galleryImage_tv_1.setText(imageNames.get(position));
//            holder.galleryImage_iv_1.setImageBitmap(galleryImages.get(position));
//            holder.galleryImage_iv_1.setImageBitmap(galleryImages.get(position));
            if(galleryImages.size()>position+1){
//                holder.galleryImage_iv_2.setImageBitmap(galleryImages.get(position+1));
                holder.galleryImage_iv_2.setBackground(functions.bitmap2Drawable(galleryImages.get(position+1),mContext));
                holder.galleryImage_tv_2.setText(imageNames.get(position+1));
                if(galleryImages.size()>position+2){
//                    holder.galleryImage_iv_3.setImageBitmap(galleryImages.get(position+2));
//                    Drawable d = functions.bitmap2Drawable(galleryImages.get(position+2),mContext);
//                    holder.galleryImage_iv_3.setBackground(d);
                    holder.galleryImage_iv_3.setBackground(functions.bitmap2Drawable(galleryImages.get(position+2),mContext));
                    holder.galleryImage_tv_3.setText(imageNames.get(position+2));

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
            holder.galleryImage_tv_1.setVisibility(View.INVISIBLE);
            holder.galleryImage_tv_2.setVisibility(View.INVISIBLE);
            holder.galleryImage_tv_3.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        int s = galleryImages.size();
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
            galleryImage_iv_1.setOnLongClickListener(this);

            galleryImage_iv_2.setOnClickListener(this);
            galleryImage_iv_2.setOnLongClickListener(this);

            galleryImage_iv_3.setOnClickListener(this);
            galleryImage_iv_3.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = 3*getAdapterPosition();
            Intent intent = new Intent(mContext, fullImage.class);
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

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            galleryImages.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

//            Intent in1 = new Intent(this, Activity2.class);
//            in1.putExtra("image",byteArray);
            intent.putExtra("image",byteArray);
            mContext.startActivity(intent);

        }

        @Override
        public boolean onLongClick(final View v) {

            PopupMenu popupGroup = new PopupMenu(mContext, v);
            popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.add_filters:
                            int position = 3*getAdapterPosition();
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
                            Intent intent = new Intent(mContext,Filters.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            galleryImages.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            intent.putExtra("image",byteArray);
                            mContext.startActivity(intent);
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
