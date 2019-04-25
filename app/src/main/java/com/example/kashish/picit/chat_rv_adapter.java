//package com.example.kashish.picit;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//
//
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//
//public class chat_rv_adapter extends RecyclerView.Adapter<chat_rv_adapter.ViewHolder> {
//
//    private static final String TAG = "chat_rv_adapter";
//    //    private ArrayList<String> chatNames;
//    private ArrayList<Bitmap> chatImages;
//    private Context mContext;
//
//
//    public chat_rv_adapter(ArrayList<Bitmap> chatImages, Context mContext) {
//        this.chatImages = chatImages;
//        this.mContext = mContext;
//    }
//    @NonNull
//    @Override
//    public chat_rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat,parent,false);
//        chat_rv_adapter.ViewHolder holder = new chat_rv_adapter.ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull chat_rv_adapter.ViewHolder holder, int position1) {
////        holder.chatName_tv.setText(chatNames.get(position));
////        holder.chatImage_iv.setImageBitmap(chatImages.get(position));
//
//        int position = 3*position1;
//
//        holder.chatImage_iv_1.setEnabled(true);
//        holder.chatImage_iv_1.setEnabled(true);
//        holder.chatImage_iv_3.setEnabled(true);
//
//        if(chatImages.size()>position){
////            holder.chatImage_iv_1.setImageBitmap(chatImages.get(position));
////            holder.chatImage_iv_1.setImageBitmap(chatImages.get(position));
//            holder.chatImage_iv_1.setBackground(functions.bitmap2Drawable(chatImages.get(position),mContext));
//
//            if(chatImages.size()>position+1){
////                holder.chatImage_iv_2.setImageBitmap(chatImages.get(position+1));
//                holder.chatImage_iv_2.setBackground(functions.bitmap2Drawable(chatImages.get(position+1),mContext));
//                if(chatImages.size()>position+2){
////                    holder.chatImage_iv_3.setImageBitmap(chatImages.get(position+2));
//                    holder.chatImage_iv_3.setBackground(functions.bitmap2Drawable(chatImages.get(position+2),mContext));
//                }
//                else{
//                    holder.chatImage_iv_3.setEnabled(false);
//                }
//            }
//            else{
//                holder.chatImage_iv_2.setEnabled(false);
//                holder.chatImage_iv_3.setEnabled(false);
//            }
//        }
//        else{
//            holder.chatImage_iv_1.setEnabled(false);
//            holder.chatImage_iv_2.setEnabled(false);
//            holder.chatImage_iv_3.setEnabled(false);
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return chatImages.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
//        ConstraintLayout completeLayout;
//        ImageView chatImage_iv_1,chatImage_iv_2,chatImage_iv_3;
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            chatImage_iv_1 = (ImageView) itemView.findViewById(R.id.imageView_row_chat_1);
//            chatImage_iv_2 = (ImageView) itemView.findViewById(R.id.imageView_row_chat_2);
//            chatImage_iv_3 = (ImageView) itemView.findViewById(R.id.imageView_row_chat_3);
//
//            chatImage_iv_1.setOnClickListener(this);
//            chatImage_iv_2.setOnClickListener(this);
//            chatImage_iv_3.setOnClickListener(this);
//
////            chatImage_iv_1.setOnClickListener(this);
////            chatImage_iv_1.setOnLongClickListener(this);
////
////            chatImage_iv_2.setOnClickListener(this);
////            chatImage_iv_2.setOnLongClickListener(this);
////
////            chatImage_iv_3.setOnClickListener(this);
////            chatImage_iv_3.setOnLongClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = 3*getAdapterPosition();
//            Intent intent = new Intent(mContext, fullImage.class);
////            intent.putExtra("")
//            switch (v.getId()){
//                case R.id.imageView_row_chat_1:{
////                    position
//                    break;
//                }
//                case R.id.imageView_row_chat_2:{
//                    position += 1;
//                    break;
//                }
//                case R.id.imageView_row_chat_3:{
//                    position += 2;
//                    break;
//                }
//
//            }
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            chatImages.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
////            Intent in1 = new Intent(this, Activity2.class);
////            in1.putExtra("image",byteArray);
//            intent.putExtra("image",byteArray);
//            mContext.startActivity(intent);
//
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            PopupMenu popupGroup = new PopupMenu(mContext, v);
//            popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()){
//                        case R.id.add_filters:
//                            mContext.startActivity(new Intent(mContext,Filters.class));
//                            break;
//                    }
//                    return false;
//                }
//            });
//            popupGroup.inflate(R.menu.gallery_action);
//            popupGroup.show();
//            return false;
//        }
//    }
//
//}