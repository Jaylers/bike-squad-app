package com.jaylerrs.bikesquad.route;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.main.BaseActivity;
import com.jaylerrs.bikesquad.route.models.Route;
import com.jaylerrs.bikesquad.utility.sharedstring.FirebaseTag;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewRouteActivity extends BaseActivity {

    private static final String TAG = "NewRouteActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    @BindView(R.id.field_title) EditText mTitleField;
    @BindView(R.id.field_body) EditText mBodyField;
    @BindView(R.id.field_dangerous_lvl) EditText mDangerous;
    @BindView(R.id.field_difficult_lvl) EditText mDifficult;
    @BindView(R.id.fab_submit_post) FloatingActionButton mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        ButterKnife.bind(this);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        String title = mTitleField.getText().toString();
        String body = mBodyField.getText().toString();
        String dang = mDangerous.getText().toString();
        String diff = mDifficult.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();
        showProgressDialog();

        // [START single_value_read]
        final String userId = getUid();
        writeNewPost(title, body, dang, diff);
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        mDangerous.setEnabled(enabled);
        mDifficult.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String title, String body, String dang, String diff) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child(FirebaseTag.route).push().getKey();
        Route post = new Route(key, title, body, dang, diff);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+FirebaseTag.route+"/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        hideProgressDialog();
        finish();
    }
    // [END write_fan_out]
}
