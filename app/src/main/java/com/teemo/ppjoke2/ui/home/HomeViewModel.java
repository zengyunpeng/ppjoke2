package com.teemo.ppjoke2.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.TypeReference;
import com.teemo.libnetwork.ApiResponse;
import com.teemo.libnetwork.ApiService;
import com.teemo.libnetwork.GetRequest;
import com.teemo.libnetwork.JsonCallBack;
import com.teemo.libnetwork.Request;
import com.teemo.ppjoke2.AbsViewModel;
import com.teemo.ppjoke2.model.Feed;
import com.teemo.ppjoke2.ui.MutableDataSource;
import com.teemo.ppjoke2.utils.DebugLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeViewModel extends AbsViewModel<Feed> {
    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    @Override
    public DataSource createDataSource() {
        return mDataSource;
    }

    private boolean withCache;
    //是否是加载中
    private AtomicBoolean loadAfter = new AtomicBoolean(false);


    public void setWithCache(boolean withCache) {
        this.withCache = withCache;
    }

    ItemKeyedDataSource<Integer, Feed> mDataSource = new ItemKeyedDataSource<Integer, Feed>() {
        //这个方法在子线程中执行
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //加载初始化数据
            loadData(0, callback);
            withCache = false;
        }

        //这个方法在子线程中执行
        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            loadData(params.key, callback);
        }

        //这个方法在子线程中执行
        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            DebugLog.w("**loadBefore");
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    };

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (key > 0) {
            loadAfter.set(true);
        }

        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType", null)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());

        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            request.excute(new JsonCallBack<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    List<Feed> body = response.body;
                    MutableDataSource dataSource = new MutableDataSource<Integer, Feed>();
                    dataSource.data.add(body);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    cacheLiveData.postValue(pagedList);
                }

            });
        }

        try {
            Request netRequest = request.clone();
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.excute();
            List<Feed> data = response == null ? Collections.emptyList() : response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);

            if (key > 0) {
                //通过LiveData发送数据,告诉UI层是否应该主动关闭数据的上拉加载
                getBoundaryPageData().postValue(data.size() > 0);
                loadAfter.set(false);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> feedLoadCallback) {
        if (loadAfter.get()) {
            feedLoadCallback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, feedLoadCallback);
            }
        });
    }

}