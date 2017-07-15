package com.jaylerrs.bikesquad.route.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

public class TopRouteFragment extends RouteListFragment {

    public TopRouteFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = getUid();
        Query myTopRoutesQuery = databaseReference.child(FirebaseTag.route).child(myUserId)
                .orderByChild(FirebaseTag.star_count);
        // [END my_top_posts_query]

        return myTopRoutesQuery;
    }
}
