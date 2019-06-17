package com.zw.avshome.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zw.avshome.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ThemeItemAdapter extends RecyclerView.Adapter<ThemeItemAdapter.ItemViewHolder> {

    private Context context;
    private List<String> themeImageList;
    private OnItemClickListener mOnItemClickListener;
    private int selectedPositon;

    public ThemeItemAdapter(Context context,List<String> themeImageList, int selectedPosition) {
        this.context = context;
        this.themeImageList = themeImageList;
        this.selectedPositon = selectedPosition;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_theme_select_list, null, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        String imgPath =  themeImageList.get(position);
        Glide.with(context).load(imgPath).into(holder.itemImage);

        if (selectedPositon == position){
            holder.itemChecked.setVisibility(View.VISIBLE);
        }else {
            holder.itemChecked.setVisibility(View.INVISIBLE);
        }

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPositon = position;
                notifyDataSetChanged();
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeImageList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemContainer;
        ImageView itemImage;
        ImageView itemChecked;


        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.item_theme_select_container);
            itemImage = itemView.findViewById(R.id.item_theme_select_image);
            itemChecked = itemView.findViewById(R.id.item_theme_select_checked_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

}
