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

public class shareAlbum_rv_adapter extends RecyclerView.Adapter<shareAlbum_rv_adapter.ViewHolder>{


    private static final String TAG = "main_rv_adapter";
    private ArrayList<String> chatNames;
    private ArrayList<Integer> chatIds;
    private Context mContext;

    ArrayList<Integer> selectedGroups;

    public shareAlbum_rv_adapter(ArrayList<Integer> selectedgroupIds,ArrayList<String> chatNames,ArrayList<Integer> chatIds,Context mContext) {
        this.chatNames = chatNames;
        this.mContext = mContext;
        this.chatIds = chatIds;
        this.selectedGroups = selectedgroupIds;
    }
    @NonNull
    @Override
    public shareAlbum_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_share_album,parent,false);
        shareAlbum_rv_adapter.ViewHolder holder = new shareAlbum_rv_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull shareAlbum_rv_adapter.ViewHolder holder, int position1) {
//        holder.chatName_tv.setText(chatNames.get(position));
//        holder.chatImage_iv.setImageBitmap(chatImages.get(position));

//        holder.
        int position = position1;
        holder.grpName.setText(chatNames.get(position));

    }

    @Override
    public int getItemCount() {
        return chatNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ConstraintLayout completeLayout;
        TextView grpName;
        CheckBox r1;

        public ViewHolder(View itemView) {
            super(itemView);

            r1 = (CheckBox) itemView.findViewById(R.id.checkBox_shareAlbum);

            grpName = (TextView) itemView.findViewById(R.id.tv_grpName_sharealbum);
            r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onClick(buttonView);
                }
            });

        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(mContext, fullImage.class);
            int position = getAdapterPosition();
            switch (v.getId()){
                case R.id.checkBox_shareAlbum:
                    if(r1.isChecked()) {
                        Toast.makeText(mContext, "adding "+chatIds.get(position)+" to list", Toast.LENGTH_SHORT).show();
                        selectedGroups.add(chatIds.get(position));
                    }
                    else{
                        selectedGroups.remove(chatIds.get(position));
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
