package com.zw.avshome.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zw.avshome.R;
import com.zw.avshome.settings.bean.SettingItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemAdapter.ItemViewHolder> {

    private Context context;
    private List<SettingItem> settingsList;
    private OnItemClickListener mOnItemClickListener;

    public SettingItemAdapter(Context context, List<SettingItem> list) {
        this.context = context;
        this.settingsList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settings_list, null, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        SettingItem settingItem = settingsList.get(position);
        holder.itemName.setText(settingItem.getItemName());
        holder.itemIcon.setImageResource(settingItem.getIconResId());
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout itemContainer;
        ImageView itemIcon;
        TextView itemName;


        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.item_setting_list_container);
            itemIcon = itemView.findViewById(R.id.item_setting_list_icon);
            itemName = itemView.findViewById(R.id.item_setting_list_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

}
