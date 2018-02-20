package com.dou.packer.DataManager.items;

/**
 * Created by Administrator on 2018-2-17.
 */

public class CardInfo {
    private String title;
    private String content;

    public CardInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public CardInfo(String title) {
        this.title = title;
        this.content = "null";
    }

    public CardInfo() {
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
}
