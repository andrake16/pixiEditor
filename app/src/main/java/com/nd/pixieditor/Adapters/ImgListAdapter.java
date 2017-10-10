package com.nd.pixieditor.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.pixieditor.ImagesActivity;
import com.nd.pixieditor.ImgEditorActivity;
import com.nd.pixieditor.R;

import java.util.List;

public class ImgListAdapter extends RecyclerView.Adapter<ImgListAdapter.ImgListItemHolder> {
    private List<Integer> dataForList;

    public ImgListAdapter(List<Integer> dataForList) {
        this.dataForList = dataForList;
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
        holder.imageView.setImageResource(dataForList.get(position));
        holder.imgNumOf.setText("Image " + (position+1) + " of " + getItemCount());
        holder.origImgName.setText(dataForList.get(position));
        holder.imgDimensions.setText("655x963");

        holder.delImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                v.getContext().startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataForList.size();
    }
}
