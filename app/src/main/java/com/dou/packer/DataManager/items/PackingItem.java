package com.dou.packer.DataManager.items;

/**
 * Created by Administrator on 2018-2-19.
 */

public class PackingItem {
    boolean packed;
    String name;

    public PackingItem(String name, boolean packed) {
        this.name = name;
        this.packed = packed;
    }

    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public boolean isPacked() {
        return packed;
    }

    public PackingItem setPackStatus(boolean packed) {
        this.packed = packed;
        return this;
    }

    @Override
    public String toString() {
        return name + ", " + (packed?"packed":"unpack");
    }
}
