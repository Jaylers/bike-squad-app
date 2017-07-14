package com.jaylerrs.bikesquad.events.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

public class MyTopPostsFragment extends PostListFragment {

    public MyTopPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child(FirebaseTag.user_post).child(myUserId)
                .orderByChild(FirebaseTag.star_count);
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }
}
