package com.dou.packer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dou.packer.R;
import com.dou.packer.DataManager.items.CardInfo;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public List<CardInfo> datas = null;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public RecyclerAdapter(List<CardInfo> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.rv_item,
                        viewGroup,
                        false);
        final ViewHolder vh = new ViewHolder(view);
        if (mClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(vh.itemView, vh.getLayoutPosition());
                }
            });
        }
        if (itemLongClickListener != null){
            vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return itemLongClickListener.onItemLongClick(vh.itemView, vh.getLayoutPosition());
                }
            });
        }
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewTitle.setText(datas.get(position).getTitle());
        viewHolder.textViewContent.setText(datas.get(position).getContent());
        viewHolder.textViewCntUnpack.setText(""+datas.get(position).getUnpackedCount());
        viewHolder.textViewCntAll.setText(""+datas.get(position).getItemCount());
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addCards(List<CardInfo> data) {
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public String getItemTitle(int pos) {
        return datas.get(pos).getTitle();
    }

    public void clearCards() {
        datas.clear();
    }

    public boolean redundant(String packingTitleString) {

        for (CardInfo card :
                datas) {
            if (card.getTitle().equals(packingTitleString))
                return true;
        }

        return false;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle, textViewContent, textViewCntAll, textViewCntUnpack;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = (TextView) view.findViewById(R.id.item_title_tv);
            textViewContent= (TextView) view.findViewById(R.id.item_content_tv);
            textViewCntAll = (TextView) view.findViewById(R.id.textView_all);
            textViewCntUnpack= (TextView) view.findViewById(R.id.textView_unpack);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        itemLongClickListener = longClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View itemView, int pos);
    }
}
