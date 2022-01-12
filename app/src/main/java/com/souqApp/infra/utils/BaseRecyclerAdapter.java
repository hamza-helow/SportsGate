package com.souqApp.infra.utils;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.souqApp.infra.listener.NeedLoadMoreListener;
import com.souqApp.infra.listener.OnClickItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SuppressWarnings("unused")
public abstract class BaseRecyclerAdapter<Binding extends ViewDataBinding, Model>
        extends RecyclerView.Adapter<BaseRecyclerAdapter<Binding, Model>.Holder> {

    private int visibleItemCount, totalItemCount, pastVisibleItems;

    private int page = 0;

    private int lastSizeList = 0;

    private int idViewCLikable = -1;

    private boolean canLoading = true;


    public void setScrollView(NestedScrollView scrollView) {

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {

                    if (lastSizeList != list.size() || page == 0) {
                        lastSizeList = list.size();
                        needLoadMore(++page);
                    } else
                        needLoadMore(page);

                }
            }
        });


    }

    protected NeedLoadMoreListener needLoadMoreListener;

    public void setNeedLoadMoreListener(NeedLoadMoreListener needLoadMoreListener) {
        this.needLoadMoreListener = needLoadMoreListener;
    }


    public Set<Model> asSet() {
        return new HashSet<Model>() {{
            addAll(list);
        }};
    }

    final private String TAG = BaseRecyclerAdapter.class.getSimpleName();

    final protected List<Model> list = new ArrayList<>();

    protected OnClickItemListener<Model> onClickItemListener;

    protected OnClickItemListener<Model> onClickSubItemListener;

    public void setOnClickItemListener(OnClickItemListener<Model> onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public abstract Binding getBinding(@NonNull ViewGroup parent, int viewType);


    public abstract boolean enableAddItem();


    public void clickableSubItem(int idViewCLikable, OnClickItemListener<Model> onClickSubItemListener) {

        this.onClickSubItemListener = onClickSubItemListener;

        this.idViewCLikable = idViewCLikable;
    }


    public EndlessRecyclerViewScrollListener scrollListener;


    protected Model getItemByPosition(int position) {
        return list.get(position);
    }

    protected boolean isAddItem(int position) {
        if (list.isEmpty())
            return true;
        else return position == list.size();

    }


    public List<Model> getList() {
        return list;
    }


    public void addList(List<Model> list) {
        if (list == null)
            return;

        final int startPosition = this.list.size() + 1;

        this.list.addAll(list);

        notifyItemRangeInserted(startPosition, list.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListIfEmpty(List<Model> list) {
        if (list == null)
            return;

        if (this.list.isEmpty()) {
            this.list.addAll(list);
        }

        notifyDataSetChanged();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Model> list) {
        if (list == null)
            return;


        this.list.clear();

        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void addItem(Model model) {

        list.add(model);
        notifyItemInserted(list.size());

    }


    public void addItemIfExist(Model model) {

        list.add(model);
        notifyItemInserted(list.size());

    }


    public void addItem(Model model, int position) {
        list.add(position, model);
        notifyItemInserted(position);
    }


    public void removeItem(int position) {

        list.remove(position);
        notifyItemRemoved(position);

        notifyItemRangeChanged(position, list.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                    if (dy > 0) { //check for scroll down
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (canLoading) {
                            if (lastSizeList != list.size() || page == 0)
                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                                    lastSizeList = list.size();
                                    canLoading = false;

                                    needLoadMore(++page);

                                    canLoading = true;
                                }
                        }
                    }

                }
            });
        }

    }


    protected abstract void needLoadMore(int page);


    @Override
    public int getItemViewType(int position) {

        if (isAddItem(position))
            return -1;
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Holder(getBinding(parent, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);


        if (idViewCLikable != -1 && onClickSubItemListener != null)
            holder.itemView.findViewById(idViewCLikable)
                    .setOnClickListener(view -> onClickSubItemListener.onClickItem(getItemByPosition(holder.getAdapterPosition())));


        if (onClickItemListener != null && onClickSubItemListener == null)
            holder.getBinding().getRoot().setOnClickListener(view -> onClickItemListener.onClickItem(list.get(holder.getAdapterPosition())));

    }


    @Override
    public int getItemCount() {

        return enableAddItem() ? list.size() + 1 : list.size();
    }

    public class Holder extends BaseViewHolder<Binding> {

        public Holder(Binding binding) {
            super(binding);
        }
    }


}