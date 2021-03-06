package com.nd.pixieditor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.pixieditor.ImagesActivity;
import com.nd.pixieditor.ImgEditorActivity;
import com.nd.pixieditor.R;
import com.nd.pixieditor.Utils.BitmapTransformer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImgListAdapter extends RecyclerView.Adapter<ImgListAdapter.ImgListItemHolder> {

    static final String TAG = ImgListAdapter.class.toString();
    Context context;
    private List<File> dataForList;
    private List<Bitmap> thumbNBitmapList;

    public ImgListAdapter(Context context) {
        this.dataForList = new ArrayList<File>();
        this.thumbNBitmapList = new ArrayList<Bitmap>();
        this.context = context;

    }


    public static class ImgListItemHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView imgNumOf;
        private TextView origImgName;
        private TextView imgDimensions;
        private ImageButton delImgBtn;

        public ImgListItemHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imgListItem_imageView);
            imgNumOf = (TextView) itemView.findViewById(R.id.imgListItem_imgNumOf);
            origImgName = (TextView) itemView.findViewById(R.id.imgListItem_OrigImgName);
            imgDimensions = (TextView) itemView.findViewById(R.id.textimgListItem_ImgDimensions);
            delImgBtn = (ImageButton) itemView.findViewById(R.id.imgListItem_delImgBtn);

        }
    }


    @Override
    public ImgListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_list_item,parent,false);
        return new ImgListItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImgListItemHolder holder, final int position) {

        //next string fix issue when 1) add img 2)change and save 3) del img 4) add the same img
        //in this case changes will be shown until change imageview Uri
        holder.imageView.setImageURI(null);
        holder.imageView.setImageBitmap(thumbNBitmapList.get(position));

        holder.imgNumOf.setText("Image " + (position+1) + " of " + getItemCount());
        //holder.origImgName.setText(dataForList.get(position));
        holder.imgDimensions.setText("655x963");

        holder.delImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileUtils.forceDelete(dataForList.get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataForList.remove(position);
                thumbNBitmapList.remove(position);
                ImgListAdapter.this.notifyItemRemoved(position);
                //need from 0 because we should recalculate imgNumOf
                ImgListAdapter.this.notifyItemRangeChanged(0,dataForList.size());
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() ,ImgEditorActivity.class);
                intent.putExtra(ImagesActivity.EXTRA_IMG_PATH,dataForList.get(position).getAbsolutePath());
                intent.putExtra(ImagesActivity.EXTRA_IMG_POSITION,position);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataForList.size();
    }


    public void addImage(File file) {
        dataForList.add(file);
        this.notifyItemInserted(dataForList.size());
        this.notifyItemRangeChanged(0,dataForList.size());
        createThumbNAddToList(file);
    }

    public void clearList() {
        dataForList.clear();
        thumbNBitmapList.clear();
    }

    private void createThumbNAddToList(File file) {
        Log.i(TAG,"create Thumbnail for: " + file.toString());
        Bitmap thumbN = BitmapFactory.decodeFile(file.toString());
        thumbN = BitmapTransformer.getScaledDownBitmap(
                thumbN,
                context.getResources().getInteger(R.integer.galleryListThumbNailSize),
                false);
        thumbNBitmapList.add(thumbN);
    }

    public void onThumbnailChanged(int position) {
        Log.i(TAG,"udate Thumbnail for: " + dataForList.get(position).toString());
        Bitmap thumbN = BitmapFactory.decodeFile(dataForList.get(position).toString());
        thumbN = BitmapTransformer.getScaledDownBitmap(
                thumbN,
                context.getResources().getInteger(R.integer.galleryListThumbNailSize),
                false);
        thumbNBitmapList.set(position,thumbN);



    }
}
