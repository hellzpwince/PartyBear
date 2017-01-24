package com.discoverfriend.partybear;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mukesh on 19/01/17.
 */

public class CakeItemModel {
    String imageurl;
    String name;
    String offer;
    Map<String, Object> products = new HashMap<String, Object>();
    String type;
    String special;

    public CakeItemModel() {
    }

    public CakeItemModel(String imageurl, String name, String offer, Map<String, Object> products, String type, String special) {
        this.imageurl = imageurl;
        this.name = name;
        this.offer = offer;
        this.products = products;
        this.type = type;
        this.special = special;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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

    public Map<String, Object> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Object> products) {
        this.products = products;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
