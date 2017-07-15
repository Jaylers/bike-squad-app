package com.jaylerrs.bikesquad.route.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

public class RecentRouteFragment extends RouteListFragment {

    public RecentRouteFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentRouteQuery = databaseReference.child(FirebaseTag.route)
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentRouteQuery;
    }
}
