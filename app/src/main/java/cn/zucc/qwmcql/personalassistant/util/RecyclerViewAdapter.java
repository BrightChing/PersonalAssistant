package cn.zucc.qwmcql.personalassistant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by angelroot on 2017/5/31.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;//上下文
    private int mLayoutId;//布局id
    private List<T> mData;//数据源
    private LayoutInflater mInflater; //布局器
    private OnItemClickListener mClickListener;//点击事件监听器
    private OnItemLongClickListener mLongClickListener;//长按监听器
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public RecyclerViewAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mData = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    mClickListener.onItemClick(recyclerView, view, position);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    mLongClickListener.onItemLongClick(recyclerView, view, position);
                    return true;
                }
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    protected abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 定义一个点击事件接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView parent, View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    /**
     * Created by angelroot on 2017/6/1.
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected SparseArray<View> mViews;
        protected View mConvertView;
        protected Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        /**
         * 取得一个RecyclerHolder对象
         * @param context  上下文
         * @param itemView 子项
         * @return 返回一个RecyclerHolder对象
         */
        public static ViewHolder getViewHolder(Context context, View itemView) {
            return new ViewHolder(context, itemView);
        }

        public SparseArray<View> getViews() {
            return this.mViews;
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 设置字符串
         */
        public ViewHolder setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageResource(int viewId, int drawableId) {

            ImageView iv = getView(viewId);
            iv.setImageResource(drawableId);
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
            ImageView iv = getView(viewId);
            iv.setImageBitmap(bitmap);
            return this;
        }
    }
}