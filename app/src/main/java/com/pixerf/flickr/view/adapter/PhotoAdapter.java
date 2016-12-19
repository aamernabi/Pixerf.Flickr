package com.pixerf.flickr.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pixerf.flickr.R;
import com.pixerf.flickr.activities.PhotoActivity;
import com.pixerf.flickr.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * created by Aamer on 12/18/2016.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private final static String TAG = PhotoAdapter.class.getSimpleName();
    private Context context;
    private List<Photo> photoList;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photoList = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = photoList.get(position);
        // TODO: 12/18/2016 Remove Picasso and implement Lazy Loading..
        Picasso.with(context).load(photo.getUrl()).into(holder.imageViewPhoto);

        holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("photo", photo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void clear() {
        //running = false;
        photoList.clear();
        //imageCache.evictAll();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
        }
    }

}