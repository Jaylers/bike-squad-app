package com.jaylerrs.bikesquad.route.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.route.models.Route;


public class RouteViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView dangLevel;
    public TextView diffLevel;
    public TextView mapId;

    public RouteViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.route_title);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.route_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.route_body);
        dangLevel = (TextView) itemView.findViewById(R.id.txt_route_dangerous_level);
        diffLevel = (TextView) itemView.findViewById(R.id.txt_route_difficult_level);
        mapId = (TextView) itemView.findViewById(R.id.txt_route_map_id);
    }

    public void bindToPost(Route route, View.OnClickListener starClickListener) {
        titleView.setText(route.title);
        mapId.setText(route.mapid);
        numStarsView.setText(String.valueOf(route.starCount));
        bodyView.setText(route.body);
        dangLevel.setText("Dangerous ".concat(route.dangerous));
        diffLevel.setText("Difficult ".concat(route.difficult));

        starView.setOnClickListener(starClickListener);
    }
}
