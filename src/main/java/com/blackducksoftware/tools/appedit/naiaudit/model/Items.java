package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.List;

public class Items {

    private List<String> itemList;

    private String comment;

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Items [itemList=" + itemList + ", comment=" + comment + "]";
    }

}
