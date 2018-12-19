package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maxrescuerinc.myandroidapplication.Adapters.RSSListAdapter;
import com.maxrescuerinc.myandroidapplication.Functions.ParseRss;
import com.maxrescuerinc.myandroidapplication.Models.NewsItem;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;


public class WelcomeFragment extends Fragment {

    private View welcomeFragmentView;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mFetchFeedButton;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;

    private List<NewsItem> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        welcomeFragmentView = inflater.inflate(R.layout.fragment_welcome, container, false);
        findAllFields();
        new FetchFeedTask().execute((Void) null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(welcomeFragmentView.getContext()));
//        mFetchFeedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new FetchFeedTask().execute((Void) null);
//            }
//        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
        return welcomeFragmentView;
    }

    private void findAllFields(){
        mRecyclerView = welcomeFragmentView.findViewById(R.id.recyclerView);
//        mFetchFeedButton = welcomeFragmentView.findViewById(R.id.fetchFeedButton);
        mSwipeLayout = welcomeFragmentView.findViewById(R.id.swipeRefreshLayout);
//        mEditText = welcomeFragmentView.findViewById(R.id.editTextWelcomeFragment);
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;
        private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
        private final String APP_PREFERENCES = "current_user_setting";
        private SharedPreferences mSettings = null;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
            Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
            User user = SugarRecord.findById(User.class, user_id);
            urlLink = user.RssLink;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = ParseRss.parseFeed(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                mRecyclerView.setAdapter(new RSSListAdapter(mFeedModelList));
            } else {
                Toast.makeText(welcomeFragmentView.getContext(),
                        getString(R.string.error_rss),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}