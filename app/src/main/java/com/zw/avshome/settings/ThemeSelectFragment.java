package com.zw.avshome.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.avshome.R;
import com.zw.avshome.home.base.ParentFragment;
import com.zw.avshome.settings.bean.ThemeItemAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ThemeSelectFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private String themePATH = "file:///android_asset/theme/";
    private List<String> themeImageList = new ArrayList<>();
    private ThemeItemAdapter themeItemAdapter;
    private ThemeItemAdapter.OnItemClickListener onItemClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.fragment_theme_select,null,false);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        recyclerView =findViewById(R.id.theme_select_fragment_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void initData() {
        themeImageList.clear();
        for (int i = 0; i <=8 ; i++) {
            StringBuilder builder = new StringBuilder(themePATH);
            builder.append("theme_0").append(i).append(".jpg");
            themeImageList.add(builder.toString());
        }
        themeItemAdapter = new ThemeItemAdapter(getContext(),themeImageList,0);
        recyclerView.setAdapter(themeItemAdapter);
    }

    @Override
    public void initEvent() {
        if (onItemClickListener!=null){
            themeItemAdapter.setOnItemClickListener(onItemClickListener);
        }

    }

    public void setOnItemClickListener(ThemeItemAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
