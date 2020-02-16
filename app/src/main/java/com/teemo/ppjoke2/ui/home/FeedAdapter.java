package com.teemo.ppjoke2.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.teemo.ppjoke2.databinding.LayoutFeedTypeImageBinding;
import com.teemo.ppjoke2.databinding.LayoutFeedTypeVideoBinding;
import com.teemo.ppjoke2.model.Feed;
import com.teemo.ppjoke2.ui.base.AbsPagedListAdapter;
import com.teemo.ppjoke2.utils.DebugLog;
import com.teemo.ppjoke2.view.ListPlayerView;

public class FeedAdapter extends AbsPagedListAdapter<Feed, FeedAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private Context mContext;
    private String mCategory;

    public FeedAdapter(Context context, String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });

        inflater = LayoutInflater.from(context);
        mContext = context;
        mCategory = category;
    }


    @Override
    protected int getItemViewType2(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;
        if (viewType == Feed.TYPE_IMAGE) {
            binding = LayoutFeedTypeImageBinding.inflate(inflater, parent, false);
        } else {
            binding = LayoutFeedTypeVideoBinding.inflate(inflater, parent, false);
        }
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final Feed feed = getItem(position);
        DebugLog.w("绑定数据");
        holder.bindData(feed);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "跳转到评论详情页面", Toast.LENGTH_SHORT).show();
//                FeedDetailActivity.startFeedDetailActivity(mContext, feed, mCategory);
//                onStartFeedDetailActivity(feed);
//                if (mFeedObserver == null) {
//                    mFeedObserver = new FeedObserver();
//                    LiveDataBus.get()
//                            .with(InteractionPresenter.DATA_FROM_INTERACTION)
//                            .observe((LifecycleOwner) mContext, mFeedObserver);
//                }
//                mFeedObserver.setFeed(feed);
            }
        });
    }

    public void onStartFeedDetailActivity(Feed feed) {

    }


    private class FeedObserver implements Observer<Feed> {

        private Feed mFeed;

        @Override
        public void onChanged(Feed newOne) {
            if (mFeed.id != newOne.id)
                return;
            mFeed.author = newOne.author;
            mFeed.ugc = newOne.ugc;
            mFeed.notifyChange();
        }

        public void setFeed(Feed feed) {

            mFeed = feed;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding mBinding;

        public ListPlayerView listPlayerView;
        public ImageView feedImage;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            mBinding = binding;
        }

        //这里之所以手动绑定数据的原因是 图片 和视频区域都是需要计算的
        //而dataBinding的执行默认是延迟一帧的。
        //当列表上下滑动的时候 ，会明显的看到宽高尺寸不对称的问题
        public void bindData(Feed item) {
            DebugLog.i("**feed: " + item);
            if (mBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) mBinding;
                feedImage = imageBinding.feedImage;
                imageBinding.setFeed(item);
                imageBinding.feedImage.bindData(item.width, item.height, 16, item.cover);
                imageBinding.interactionBinding.setLifeCycleOwner((LifecycleOwner) mContext);
            } else if (mBinding instanceof LayoutFeedTypeVideoBinding) {
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) mBinding;
                videoBinding.setFeed(item);//自动绑定
                videoBinding.listPlayerView.bindData(mCategory, item.width, item.height, item.cover, item.url);//手动绑定
                videoBinding.interactionBinding.setLifeCycleOwner((LifecycleOwner) mContext);

                listPlayerView = videoBinding.listPlayerView;
            }
        }

        public boolean isVideoItem() {
            return mBinding instanceof LayoutFeedTypeVideoBinding;
        }

        public ListPlayerView getListPlayerView() {
            return listPlayerView;
        }

    }

}
