package com.zw.avshome.settings.bean;

public class SettingItem {

    private String itemName;
    private int iconResId;

    public SettingItem(String itemName, int iconResId) {
        this.itemName = itemName;
        this.iconResId = iconResId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getIconResId() {
        return iconResId;
    }
}
