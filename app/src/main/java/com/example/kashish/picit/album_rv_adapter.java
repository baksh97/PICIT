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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;



public class album_rv_adapter extends RecyclerView.Adapter<album_rv_adapter.ViewHolder>{


    private static final String TAG = "main_rv_adapter";
    private ArrayList<String> imageNames;
    private ArrayList<Bitmap> galleryImages;
    private Context mContext;

    ArrayList<Bitmap> selectedImages = create_album.selectedImages;
    ArrayList<String> selectedNames = create_album.selectedNames;


    public album_rv_adapter(ArrayList<String> imageNames,ArrayList<Bitmap> galleryImages, Context mContext) {
        this.galleryImages = galleryImages;
        this.mContext = mContext;
        this.imageNames = imageNames;
    }
    @NonNull
    @Override
    public album_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_create_album,parent,false);
        album_rv_adapter.ViewHolder holder = new album_rv_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull album_rv_adapter.ViewHolder holder, int position1) {
        int position = 2*position1;
        if(position < galleryImages.size()){
            holder.albumImage_iv_1.setBackground(functions.bitmap2Drawable(galleryImages.get(position),mContext));
            if(position+1 < galleryImages.size()){
                holder.albumImage_iv_2.setBackground(functions.bitmap2Drawable(galleryImages.get(position+1),mContext));
            }
            else{
                holder.albumImage_iv_2.setVisibility(View.INVISIBLE);
                holder.r2.setVisibility(View.INVISIBLE);
            }
        }
        else{
            holder.albumImage_iv_1.setVisibility(View.INVISIBLE);
            holder.r1.setVisibility(View.INVISIBLE);

            holder.albumImage_iv_2.setVisibility(View.INVISIBLE);
            holder.r2.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return (galleryImages.size()/2) + (galleryImages.size()%2);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ConstraintLayout completeLayout;
        ImageView albumImage_iv_1,albumImage_iv_2;
        CheckBox r1, r2;

        public ViewHolder(View itemView) {
            super(itemView);

            r1 = (CheckBox) itemView.findViewById(R.id.checkBox_1);
            r2 = (CheckBox) itemView.findViewById(R.id.checkBox_2);

            albumImage_iv_1 = (ImageView) itemView.findViewById(R.id.imageView_album_1);
            albumImage_iv_2 = (ImageView) itemView.findViewById(R.id.imageView_album_2);

            albumImage_iv_1.setOnClickListener(this);
            albumImage_iv_1.setOnLongClickListener(this);

            albumImage_iv_2.setOnClickListener(this);
            albumImage_iv_2.setOnLongClickListener(this);

            r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onClick(buttonView);
                }
            });

            r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onClick(buttonView);
                }
            });

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, fullImage.class);
            int position = 2*getAdapterPosition();
            switch (v.getId()){
                case R.id.imageView_album_2:
                    position++;
                case R.id.imageView_album_1:
                    intent.putExtra("image",galleryImages.get(position));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    galleryImages.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image",byteArray);
                    mContext.startActivity(intent);
                    break;
                case R.id.checkBox_2:
                    position++;
                    if(r2.isChecked()) {
                        selectedImages.add(galleryImages.get(position));
                        selectedNames.add(imageNames.get(position));
                    }
                    else{
                        selectedImages.remove(galleryImages.get(position));
                        selectedNames.remove(imageNames.get(position));
                    }
                    break;
                case R.id.checkBox_1:
                    if(r1.isChecked()) {
                        selectedImages.add(galleryImages.get(position));
                        selectedNames.add(imageNames.get(position));
                    }
                    else{
                        selectedImages.remove(galleryImages.get(position));
                        selectedNames.remove(imageNames.get(position));
                    }
                    break;
            }
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
