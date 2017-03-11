package com.discoverfriend.partybear.category;

/**
 * Created by mukesh on 21/01/17.
 */

public class CategoryModel {
    private String name, offer, imageurl, feature,premium;
    private int price,baseprice;

    public CategoryModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(int baseprice) {
        this.baseprice = baseprice;
    }

    public CategoryModel(String name, String offer, String imageurl, String feature, String premium, int price, int baseprice) {
        this.name = name;
        this.offer = offer;
        this.imageurl = imageurl;
        this.feature = feature;
        this.premium = premium;
        this.price = price;
        this.baseprice = baseprice;
    }
}
