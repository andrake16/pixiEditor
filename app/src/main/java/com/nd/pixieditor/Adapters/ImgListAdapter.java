package com.nd.pixieditor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImgListAdapter extends RecyclerView.Adapter<ImgListAdapter.ImgListItemHolder> {

    static final String TAG = ImgListAdapter.class.toString();
    Context context;
    private List<File> dataForList;

    public ImgListAdapter(List<File> dataForList, Context context) {
        this.dataForList = dataForList;
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
        Uri imageUri = Uri.parse("file://" + dataForList.get(position).toString());
        Log.i(TAG, context.getString(R.string.load_image_from_storage) + imageUri.toString());

        holder.imageView.setImageURI(imageUri);
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

}
