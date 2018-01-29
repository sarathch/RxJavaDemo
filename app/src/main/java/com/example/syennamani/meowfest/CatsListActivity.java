package com.example.syennamani.meowfest;


/**
 *  Triple Byte sample App
 *
 *   - syennamani
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.Observer;
import rx.Subscription;

public class CatsListActivity extends AppCompatActivity {
    private static final String TAG = CatsListActivity.class.getSimpleName();
    private List<Cats> catsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CatsListAdapter mAdapter;
    private int offset = 0;
    private boolean mIsLoading = false, hasMorePages = true;
    private boolean isRefreshing=false;
    private RelativeLayout progressLayout;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats_list);
        loadNextDataFromApi();
        mAdapter = new CatsListAdapter(catsList);
        recyclerView = findViewById(R.id.recycler_view);
        progressLayout = findViewById(R.id.progress);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount && !mIsLoading) {
                    //End of list
                    mIsLoading = true;
                    if(hasMorePages && !isRefreshing) {
                        progressLayout.setVisibility(View.VISIBLE);
                        loadNextDataFromApi();
                    }
                }else
                    mIsLoading = false;
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void getCatsData() {
        subscription = WebClient.getInstance()
                .getCatsDataToClient(""+offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Cats>>() {
                    @Override public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override public void onNext(List<Cats> catsList) {
                        Log.d(TAG, "In onNext()");
                        mAdapter.setCatsList(catsList);
                    }
                });
    }

    // Append the next page of data into the adapter
    public void loadNextDataFromApi() {
        String url = "https://chex-triplebyte.herokuapp.com/api/cats?page="+offset;
        Log.v("CATS JSON LIST URL", url);
        isRefreshing = true;
        FetchJson fetchJson = (FetchJson) new FetchJson(new FetchJson.AsyncResponse() {
            @Override
            public void processFinish(String output) {

                int positionStart = catsList.size();
                JSONArray jsonarray = null;
                try {
                    jsonarray = new JSONArray(output);
                    if(jsonarray.length() == 0) hasMorePages =false;
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Cats cat = new Cats();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy");
                        try {
                            Date date = formatter.parse(jsonobject.getString("timestamp"));
                            cat.setTimestamp(targetFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            cat.setTimestamp("");
                        }
                        cat.setImage_url(jsonobject.getString("image_url"));
                        cat.setTitle(jsonobject.getString("title"));
                        cat.setDescription(jsonobject.getString("description"));
                        catsList.add(cat);
                    }

                    mAdapter.notifyItemRangeInserted(positionStart,jsonarray.length());
                    if(hasMorePages) {
                        offset++;
                        isRefreshing = false;
                    }
                    progressLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(url);
    }
}
