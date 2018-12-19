package com.maxrescuerinc.myandroidapplication.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.Activites.WebActivity;
import com.maxrescuerinc.myandroidapplication.Models.NewsItem;
import com.maxrescuerinc.myandroidapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RSSListAdapter extends RecyclerView.Adapter<RSSListAdapter.FeedModelViewHolder>  {

    private List<NewsItem> mRssFeedModels;
    private ImageView imageView = null;
    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;
        FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RSSListAdapter(List<NewsItem> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @NonNull
    @Override
    public FeedModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        imageView = v.findViewById(R.id.imageViewRss);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedModelViewHolder holder, int position) {
        final NewsItem rssFeedModel = mRssFeedModels.get(position);
        if(rssFeedModel.ImageUrl != null) {
            Picasso.with(holder.rssFeedView.getContext()).load(rssFeedModel.ImageUrl).into(imageView);
        }
        else{

           imageView.setImageResource(R.drawable.default_photo);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(),WebActivity.class);
                intent.setData(Uri.parse(rssFeedModel.Link));
                v.getContext().startActivities(new Intent[]{intent});
            }
        });
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.Title);
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.Description);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}