package com.teemo.ppjoke2;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.teemo.ppjoke2.model.Feed;

public abstract class AbsViewModel<T> extends ViewModel {
    private DataSource dataSource;
    private LiveData<PagedList<T>> pageData;

    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();
    protected PagedList.Config config;

    public AbsViewModel() {
        config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10)

//                .setMaxSize(100)
//        .setEnablePlaceholders(true)
//                .setPrefetchDistance(10)
                .build();
        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)//加载初始化数据的时候需要传递的参数
//        .setFetchExecutor()//设置获取数据的线程池,用内置的
                .setBoundaryCallback(boundaryCallback)
                .build();//设置加载数据的监听

    }


    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }


    public abstract DataSource createDataSource();

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            dataSource = createDataSource();
            return dataSource;
        }
    };
    PagedList.BoundaryCallback boundaryCallback = new PagedList.BoundaryCallback() {
        //没有数据的回调
        @Override
        public void onZeroItemsLoaded() {
            boundaryPageData.postValue(false);
        }


        @Override
        public void onItemAtFrontLoaded(@NonNull Object itemAtFront) {
            boundaryPageData.postValue(true);
        }

        //后面没有更多的数据
        @Override
        public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
        }
    };

    //可以在这个方法里 做一些清理 的工作
    @Override
    protected void onCleared() {

    }


}
