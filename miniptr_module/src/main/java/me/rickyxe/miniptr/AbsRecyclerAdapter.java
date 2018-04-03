package me.rickyxe.miniptr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据Adapter基础类。使用时需提供实体Entity类型与{@link android.support.v7.widget.RecyclerView.ViewHolder}类型。
 * 子类需要实现{@link #getViewHolder(Context, ViewGroup, int)}与
 * {@link #fillData(RecyclerView.ViewHolder, List, int)}方法。
 */
public abstract class AbsRecyclerAdapter<ENTITY, HOLDER extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<HOLDER> implements View.OnClickListener {

    Context mContext;
    List<ENTITY> dataList;

    private OnListItemClickListener onListItemClickListener;

    public AbsRecyclerAdapter(Context ctx, List<ENTITY> datas) {
        mContext = ctx;
        dataList = datas;
    }

    /**
     * 生成列表项的ViewHolder
     * @param ctx Context对象
     * @param parent parent视图
     * @param viewType 视图类型
     * @return ViewHolder对象
     */
    public abstract HOLDER getViewHolder(Context ctx, ViewGroup parent, int viewType);

    /**
     * 填充ViewHolder数据
     * @param holder 对应项的ViewHolder
     * @param datas 全部数据列表
     * @param position 填充项在列表中的位置
     */
    public abstract void fillData(HOLDER holder, List<ENTITY> datas, int position);


    @Override
    public HOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        HOLDER holder = getViewHolder(mContext, parent, viewType);
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(HOLDER holder, int position) {
        if (dataList != null && position < dataList.size()) {
            try {
                fillData(holder, dataList, position);
                holder.itemView.setTag(position);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        if (onListItemClickListener != null) {
            try {
                final int position = (int) v.getTag();
                onListItemClickListener.onItemClick(v, position);
            } catch (Exception e) {

            }

        }
    }

    public Context getAdapterContext() {
        return mContext;
    }

    public List<ENTITY> getDataList() {
        return dataList;
    }

    public void setDataList(List<ENTITY> list) {
        this.dataList = list;
    }

    /**
     * 在已有列表后端添加数据
     * @param dataListToAdd
     */
    public void addDataList(List<ENTITY> dataListToAdd) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }

        this.dataList.addAll(dataListToAdd);
    }

    /**
     * 清空数据列表
     */
    public void clearDataList() {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
            return;
        }

        this.dataList.clear();
    }

    public OnListItemClickListener getOnListItemClickListener() {
        return onListItemClickListener;
    }

    /**
     * 设定列表项点击监听器
     * @param onListItemClickListener 监听器对象
     */
    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    /**
     * 列表项点击监听器
     */
    public interface OnListItemClickListener {
        /**
         * 列表项点击回调函数
         * @param itemView 点击的列表项视图
         * @param position 对应数据列表中的位置
         */
        void onItemClick(View itemView, int position);
    }

}
