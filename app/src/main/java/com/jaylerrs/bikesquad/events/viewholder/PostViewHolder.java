package com.jaylerrs.bikesquad.events.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.events.models.Post;


public class PostViewHolder extends RecyclerView.ViewHolder {

    public ImageView authorImage;
    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView routeName;

    public PostViewHolder(View itemView) {
        super(itemView);
        authorImage = (ImageView) itemView.findViewById(R.id.post_author_photo);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.route_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        routeName = (TextView) itemView.findViewById(R.id.post_route);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {

        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);
        routeName.setText(post.name);

        starView.setOnClickListener(starClickListener);
    }
}
