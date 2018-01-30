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
import io.reactivex.disposables.CompositeDisposable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CatsListActivity extends AppCompatActivity {
    private static final String TAG = CatsListActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CatsListAdapter mAdapter;
    private int offset = 0;
    private boolean mIsLoading = false, hasMorePages = true;
    private boolean isRefreshing=false;
    private RelativeLayout progressLayout;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats_list);
        getCatsData();
        mAdapter = new CatsListAdapter();
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
                        offset++;
                        getCatsData();
                    }
                }else
                    mIsLoading = false;
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(mScrollListener);
    }

    @Override protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    private void getCatsData() {
        WebClient.getInstance()
                .getCatsDataToClient(""+offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<List<Cats>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Cats> catsList) {
                        Log.d("RX2","Result Size : "+catsList.size());
                        mAdapter.setCatsList(catsList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
