package com.discoverfriend.partybear;

/**
 * Created by mukesh on 19/01/17.
 */

public class CakeItemModel {
    String image;
    String title;
    String offer;

    public CakeItemModel() {

    }

    public CakeItemModel(String title, String image, String offer) {
        this.image = image;
        this.title = title;
        this.offer = offer;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryItem() {
        return title;
    }

    public void setCategoryItem(String categoryItem) {
        this.title = categoryItem;
    }

    public String getCategoryOffer() {
        return offer;
    }

    public void setCategoryOffer(String categoryOffer) {
        this.offer = categoryOffer;
    }
}
