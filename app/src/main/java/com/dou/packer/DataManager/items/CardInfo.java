package com.dou.packer.DataManager.items;

/**
 * Created by Administrator on 2018-2-17.
 */

public class CardInfo {
    private String title;
    private String content;
    private int cnt_all,cnt_unpacked;

    public CardInfo(String title, String content, int unpacked, int all) {
        this.title = title;
        this.content = content;
        this.cnt_unpacked = unpacked;
        this.cnt_all = all;
    }

    public CardInfo(String title, String content) {
        this.title = title;
        this.content = content;
        this.cnt_unpacked = -1;
        this.cnt_all = -1;
    }

    public CardInfo(String title) {
        this.title = title;
        this.content = "null";
        this.cnt_unpacked = -1;
        this.cnt_all = -1;
    }

    public CardInfo() {
        this.title = "Title";
        this.content = "content";
        this.cnt_unpacked = -1;
        this.cnt_all = -1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getItemCount() {
        return cnt_all;
    }

    public int getUnpackedCount() {
        return cnt_unpacked;
    }

    public void setItemCount(int cnt_all) {
        this.cnt_all = cnt_all;
    }

    public void setUnpackedCount(int cnt_unpacked) {
        this.cnt_unpacked = cnt_unpacked;
    }
}
